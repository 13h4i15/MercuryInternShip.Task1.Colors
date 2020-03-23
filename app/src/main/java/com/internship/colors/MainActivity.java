package com.internship.colors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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
    private static final String DIALOG_VISIBLE = "dialog";
    private static final String SAVING_COLOR_LIST_FILE_ERROR_TAG = "list_saving_error";
    private static final int ADD_ELEMENT_REQUEST_CODE = 1;

    private ColorsListRecyclerAdapter colorsListRecyclerAdapter;
    private FloatingActionButton fab;
    private boolean isDialogVisible;
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
        isDialogVisible = false;
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(POSITION_INDEX);
            isDialogVisible = savedInstanceState.getBoolean(DIALOG_VISIBLE);
        }

        colorsListRecyclerAdapter = new ColorsListRecyclerAdapter(selectedPosition);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(colorsListRecyclerAdapter);

        colorsListRecyclerAdapter.setOnLongClickListener(v -> {
            showDialogToDelete();
            return true;
        });

        loadingDisposable = loadState();

        fab.setOnClickListener(v -> {
            fab.setClickable(false);  // with this you can't make multy-click
            Intent createElementIntent = new Intent(this, ColorElemCreateActivity.class);
            createElementIntent.putExtra(Constants.NUMBER_INDEX_EXTRA, colorsListRecyclerAdapter.getNumberForNewElement());
            startActivityForResult(createElementIntent, ADD_ELEMENT_REQUEST_CODE);
        });
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
    protected void onResume() {
        super.onResume();
        fab.setClickable(true);
    }

    @Override
    protected void onDestroy() {
        if (loadingDisposable != null) loadingDisposable.dispose();
        if (savingDisposable != null) savingDisposable.dispose();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(POSITION_INDEX, colorsListRecyclerAdapter.getSelectedPosition());
        outState.putBoolean(DIALOG_VISIBLE, isDialogVisible);
        super.onSaveInstanceState(outState);
    }

    private void addAndSaveState(int newListElementColor, int elementNumber) {
        final List<ColorListElement> oldVersion = new ArrayList<>(colorsListRecyclerAdapter.getColorList());
        colorsListRecyclerAdapter.addColorElement(newListElementColor, elementNumber);
        saveState(colorsListRecyclerAdapter.getColorList(), oldVersion);
    }

    private void deleteAndSaveState(int positionToDelete) {
        final List<ColorListElement> oldVersion = new ArrayList<>(colorsListRecyclerAdapter.getColorList());
        colorsListRecyclerAdapter.deleteColorElement(positionToDelete);
        saveState(colorsListRecyclerAdapter.getColorList(), oldVersion);
    }

    private void saveState(List<ColorListElement> newVersion, List<ColorListElement> oldVersion) {
        if (savingDisposable != null && !savingDisposable.isDisposed()) {
            savingDisposable.dispose();
        }
        savingDisposable = Single.fromCallable(() -> ColorListJsonLoader.writeJsonInFile(getFilesDir(), newVersion))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isSaved -> {
                            if (!isSaved) {
                                //  if IOException was occurred
                                colorsListRecyclerAdapter.setColorElements(oldVersion);
                            }
                        },
                        exception -> {
                            Toast.makeText(this, getString(R.string.saving_colored_list_error_toast), Toast.LENGTH_SHORT).show();
                            Log.e(SAVING_COLOR_LIST_FILE_ERROR_TAG, exception.toString());
                        });
    }

    private Disposable loadState() {  // fills empty list with saved elements
        return Single.fromCallable(() -> ColorListJsonLoader.readJsonFromFile(getFilesDir()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(2, TimeUnit.SECONDS)
                .subscribe(loadedColorList -> {
                            colorsListRecyclerAdapter.setColorElements(loadedColorList);
                            colorsListRecyclerAdapter.notifyDataSetChanged();
                            fab.setVisibility(View.VISIBLE);
                            if (isDialogVisible) {  // need to call dialog if it was closed by settings change
                                showDialogToDelete();
                            }
                        },
                        exception -> {
                            Toast.makeText(this, getString(R.string.loading_colored_list_error_toast), Toast.LENGTH_SHORT).show();
                            Log.e(Constants.LOADING_COLOR_LIST_FILE_ERROR_TAG, exception.toString());
                            fab.setVisibility(View.VISIBLE);
                        });
    }

    private void showDialogToDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        isDialogVisible = true;
        int selectedDialogNumber = colorsListRecyclerAdapter.getNumberByPosition(colorsListRecyclerAdapter.getSelectedPosition());
        builder.setMessage(getString(R.string.dialog_delete_question, selectedDialogNumber));

        builder.setPositiveButton(getString(R.string.dialog_yes_answer), (dialog, which) -> {
            deleteAndSaveState(colorsListRecyclerAdapter.getSelectedPosition());
            isDialogVisible = false;
        });
        builder.setNegativeButton(getString(R.string.dialog_no_answer), (dialog, which) -> {
            colorsListRecyclerAdapter.unselectElement();
            isDialogVisible = false;
        });
        builder.setOnDismissListener(dialog -> {
            colorsListRecyclerAdapter.unselectElement();
            isDialogVisible = false;
        });

        Dialog dialog = builder.create();
        dialog.show();
    }
}