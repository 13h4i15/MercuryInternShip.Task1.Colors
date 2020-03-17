package com.internship.colors;

import android.app.Activity;
import android.content.Intent;
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
    private List<ColorListElement> colorList;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            colorList = ColorListJsonLoader.readJsonFromFile(getFilesDir());
        } catch (IOException ignore) {
            colorList = new ArrayList<>();
        }

        RecyclerView recyclerView = findViewById(R.id.colors_list_recycler);

        int selectedPosition = -1;
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(POSITION_INDEX);
        }

        colorsListRecyclerAdapter = new ColorsListRecyclerAdapter(colorList, selectedPosition);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(colorsListRecyclerAdapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            fab.setClickable(false); //with this you can't make multy-click
            Intent createElementIntent = new Intent(this, ColorElemCreateActivity.class);
            createElementIntent.putExtra(NUMBER_INDEX_EXTRA, getNumberForNewElement() + 1);
            startActivityForResult(createElementIntent, ADD_ELEMENT_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) return;
        if (requestCode == ADD_ELEMENT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                int newListElementColor = data.getIntExtra(SELECTED_COLOR_EXTRA, 0);
                colorsListRecyclerAdapter.addColorElement(new ColorListElement(newListElementColor, getNumberForNewElement() + 1));
                try {
                    ColorListJsonLoader.writeJsonInFile(getFilesDir(), colorList);
                } catch (IOException ignore) {
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int getNumberForNewElement() {
        if (colorList.size() != 0) {
            return colorList.get(colorList.size() - 1).getNumber() + 1;
        }
        return 0;
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