package com.internship.colors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
    private static final int ADD_ELEMENT_REQUEST_CODE = 1;
    private static final String NUMBER_INDEX_EXTRA = "index";
    private static final String SELECTED_COLOR_EXTRA = "color";

    private ColorsListRecyclerAdapter colorsListRecyclerAdapter;
    private FloatingActionButton fab;
    List<ColorListElement> colorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*try {
            ColorListJsonLoader.writeJsonInFile(context.getFilesDir(), colorList);
        } catch (IOException ignore) {
        }*/

        RecyclerView recyclerView = findViewById(R.id.colors_list_recycler);

        int selectedPosition = -1;
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(POSITION_INDEX);
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
            createElementIntent.putExtra(NUMBER_INDEX_EXTRA, colorsListRecyclerAdapter.getNumberForNewElement());
            startActivityForResult(createElementIntent, ADD_ELEMENT_REQUEST_CODE);
        });
    }

    public class CustomBroadcastReceiver extends BroadcastReceiver{
        public static final String ACTION = "ACTION";
        @Override
        public void onReceive(Context context, Intent intent) {
            int test = intent.getIntExtra("extra", 0);

            int currentListElement = colorList.get(test).getNumber();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(context.getString(R.string.dialog_delete_question, currentListElement));

            builder.setPositiveButton(context.getString(R.string.dialog_yes_answer), (dialog, which) -> {
                colorsListRecyclerAdapter.deleteColorElement(test, context);
            });

            builder.setNegativeButton(context.getString(R.string.dialog_no_answer), (dialog, which) -> {
                colorsListRecyclerAdapter.setSelectedPosition(-1);
                colorsListRecyclerAdapter.notifyDataSetChanged();
            });
            builder.setOnDismissListener(dialog -> {
                colorsListRecyclerAdapter.setSelectedPosition(-1);
                colorsListRecyclerAdapter.notifyDataSetChanged();
            });
            builder.create().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) return;
        if (requestCode == ADD_ELEMENT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                int newListElementColor = data.getIntExtra(SELECTED_COLOR_EXTRA, 0);
                colorsListRecyclerAdapter.addColorElement(new ColorListElement(newListElementColor, colorsListRecyclerAdapter.getNumberForNewElement()), this);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(POSITION_INDEX, colorsListRecyclerAdapter.getSelectedPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setClickable(true);
    }
}