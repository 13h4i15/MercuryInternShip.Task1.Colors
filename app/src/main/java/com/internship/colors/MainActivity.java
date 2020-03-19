package com.internship.colors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String POSITION_INDEX = "position";
    private static final String DIALOG_ALIVE = "dialog";
    private static final int ADD_ELEMENT_REQUEST_CODE = 1;

    private ColorsListRecyclerAdapter colorsListRecyclerAdapter;
    private FloatingActionButton fab;
    private List<ColorListElement> colorList;
    private boolean isDialogAlive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.colors_list_recycler);

        int selectedPosition = -1;
        isDialogAlive = false;
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(POSITION_INDEX);
            isDialogAlive = savedInstanceState.getBoolean(DIALOG_ALIVE);
        }

        try {
            colorList = ColorListJsonLoader.readJsonFromFile(getFilesDir());
        } catch (IOException ignore) {
            colorList = new ArrayList<>();
        }
        colorsListRecyclerAdapter = new ColorsListRecyclerAdapter(colorList, selectedPosition);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(colorsListRecyclerAdapter);

        CustomBroadcastReceiver receiver = new CustomBroadcastReceiver();
        this.registerReceiver(receiver, new IntentFilter(CustomBroadcastReceiver.ACTION));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            fab.setClickable(false); //with this you can't make multy-click
            Intent createElementIntent = new Intent(this, ColorElemCreateActivity.class);
            createElementIntent.putExtra(Constants.NUMBER_INDEX_EXTRA, colorsListRecyclerAdapter.getNumberForNewElement());
            startActivityForResult(createElementIntent, ADD_ELEMENT_REQUEST_CODE);
        });


        if (isDialogAlive) {
            showDialogToDelete(selectedPosition);
        }
    }

    public class CustomBroadcastReceiver extends BroadcastReceiver {
        public static final String ACTION = "ACTION";

        @Override
        public void onReceive(Context context, Intent intent) {
            int position = intent.getIntExtra("extra", 0);
            showDialogToDelete(position);

        }
    }

    private void showDialogToDelete(int position) {
        isDialogAlive = true;
        int currentListElement = colorList.get(position).getNumber();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_delete_question, currentListElement));

        builder.setPositiveButton(getString(R.string.dialog_yes_answer), (dialog, which) -> {
            colorsListRecyclerAdapter.deleteColorElement(position, this);
            isDialogAlive = false;
        });

        builder.setNegativeButton(getString(R.string.dialog_no_answer), (dialog, which) -> {
            colorsListRecyclerAdapter.unselectElement();
            isDialogAlive = false;
        });
        builder.setOnDismissListener(dialog -> {
            colorsListRecyclerAdapter.unselectElement();
            isDialogAlive = false;
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) return;
        if (requestCode == ADD_ELEMENT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                int newListElementColor = data.getIntExtra(Constants.SELECTED_COLOR_EXTRA, 0);
                colorsListRecyclerAdapter.addColorElement(new ColorListElement(newListElementColor, colorsListRecyclerAdapter.getNumberForNewElement()), this);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(POSITION_INDEX, colorsListRecyclerAdapter.getSelectedPosition());
        outState.putBoolean(DIALOG_ALIVE, isDialogAlive);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setClickable(true);
    }
}