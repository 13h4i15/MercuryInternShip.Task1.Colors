package com.internship.colors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String POSITION_INDEX = "position";
    private static final String DIALOG_ELEMENT_NUMBER = "dialog";
    private static final int ADD_ELEMENT_REQUEST_CODE = 1;

    private ColorsListRecyclerAdapter colorsListRecyclerAdapter;
    private FloatingActionButton fab;
    private int dialogSelectedNumber;
    private Disposable loadingDisposable, savingDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.colors_list_recycler);
        fab = findViewById(R.id.fab);

        int selectedPosition = -1;
        dialogSelectedNumber = -1;
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(POSITION_INDEX);
            dialogSelectedNumber = savedInstanceState.getInt(DIALOG_ELEMENT_NUMBER);
        }

        colorsListRecyclerAdapter = new ColorsListRecyclerAdapter(selectedPosition);
        CustomBroadcastReceiver receiver = new CustomBroadcastReceiver();
        this.registerReceiver(receiver, new IntentFilter(Constants.DIALOG_ACTION));  // catches dialog's invokes
        this.registerReceiver(receiver, new IntentFilter(Constants.SHOW_FAB_ACTION));  // catches invoke to show fab

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(colorsListRecyclerAdapter);

        loadingDisposable = loadState();

        fab.setOnClickListener(v -> {
            fab.setClickable(false);  // with this you can't make multy-click
            Intent createElementIntent = new Intent(this, ColorElemCreateActivity.class);
            createElementIntent.putExtra(Constants.NUMBER_INDEX_EXTRA, colorsListRecyclerAdapter.getNumberForNewElement());
            startActivityForResult(createElementIntent, ADD_ELEMENT_REQUEST_CODE);
        });

        if (dialogSelectedNumber != -1) {  // need to call dialog if it was closed by settings change
            showDialogToDelete();
        }
    }

    public class CustomBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constants.DIALOG_ACTION:
                    showDialogToDelete();
                    break;
            }
        }
    }

    private void addAndSaveState(int newListElementColor, int elementNumber) {
        final List<ColorListElement> oldVersion = new ArrayList<>(colorsListRecyclerAdapter.getColorList());
        colorsListRecyclerAdapter.addColorElement(newListElementColor, elementNumber);
        saveState(oldVersion);
    }

    private void deleteAndSaveState(int positionToDelete) {
        final List<ColorListElement> oldVersion = new ArrayList<>(colorsListRecyclerAdapter.getColorList());
        colorsListRecyclerAdapter.deleteColorElement(positionToDelete);
        saveState(oldVersion);
    }

    private void saveState(List<ColorListElement> oldVersion) {
        if (savingDisposable != null && !savingDisposable.isDisposed()) {
            savingDisposable.dispose();
        }

        savingDisposable = Single.fromCallable(() -> ColorListJsonLoader.writeJsonInFile(getFilesDir(), colorsListRecyclerAdapter.getColorList()))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .timeout(2, TimeUnit.SECONDS)
                .subscribe(v -> {
                            if (!v) {
                                colorsListRecyclerAdapter.fillElementsListWithData(oldVersion);
                            }
                        },
                        v -> {
                            colorsListRecyclerAdapter.fillElementsListWithData(oldVersion);
                            Toast.makeText(this, v.toString(), Toast.LENGTH_LONG).show();//todo
                            Log.e(Constants.LOADING_COLOR_LIST_FILE_ERROR_TAG, v.toString());
                        });
    }

    private Disposable loadState() {  // fills empty list with saved elements
        return Single.fromCallable(() -> ColorListJsonLoader.readJsonFromFile(getFilesDir()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(2, TimeUnit.SECONDS)
                .subscribe(v -> {
                            colorsListRecyclerAdapter.fillElementsListWithData(v);
                            colorsListRecyclerAdapter.notifyDataSetChanged();
                            fab.setVisibility(View.VISIBLE);
                        },
                        v -> {
                            Toast.makeText(this, v.toString(), Toast.LENGTH_LONG).show();//todo
                            Log.e(Constants.LOADING_COLOR_LIST_FILE_ERROR_TAG, v.toString());
                            fab.setVisibility(View.VISIBLE);
                        });
    }

    private void showDialogToDelete() {
        if (dialogSelectedNumber == -1) {  // check if it is invoked by broadcastReceiver (-1 is start position)
            // it is need to save name of element to show it after settings changes
            // we can't take name from adapter by position if we call this method from onCreate
            // reason: adapter data can be not ready
            dialogSelectedNumber = colorsListRecyclerAdapter.getNumberByPosition(colorsListRecyclerAdapter.getSelectedPosition());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_delete_question, dialogSelectedNumber));

        builder.setPositiveButton(getString(R.string.dialog_yes_answer), (dialog, which) -> {
            int selectedPosition = colorsListRecyclerAdapter.getSelectedPosition();
            deleteAndSaveState(selectedPosition);
            dialogSelectedNumber = -1;
        });
        builder.setNegativeButton(getString(R.string.dialog_no_answer), (dialog, which) -> {
            colorsListRecyclerAdapter.unselectElement();
            dialogSelectedNumber = -1;
        });
        builder.setOnDismissListener(dialog -> {
            colorsListRecyclerAdapter.unselectElement();
            dialogSelectedNumber = -1;
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) return;
        if (requestCode == ADD_ELEMENT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                int newListElementColor = data.getIntExtra(Constants.SELECTED_COLOR_EXTRA, ColorListElement.ElementColorState.values().length - 1);
                int elementNumber = data.getIntExtra(Constants.CREATED_ELEMENT_NUMBER_EXTRA, colorsListRecyclerAdapter.getNumberForNewElement());
                addAndSaveState(newListElementColor, elementNumber);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(POSITION_INDEX, colorsListRecyclerAdapter.getSelectedPosition());
        outState.putInt(DIALOG_ELEMENT_NUMBER, dialogSelectedNumber);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        loadingDisposable.dispose();

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        fab.setClickable(true);
    }
}