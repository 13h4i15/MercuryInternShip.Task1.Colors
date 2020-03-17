package com.internship.colors;

import android.content.Intent;
import android.os.Bundle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String POSITION_INDEX = "position";
    private static final String NUMBER_INDEX_EXTRA = "index";
    private static final String SAVE_FILE_NAME = "coloredListSave.json";
    private static final String SELECTED_COLOR_EXTRA = "color";

    private ColorsListRecyclerAdapter colorsListRecyclerAdapter;
    private List<ColorListElement> colorsList;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        File path = getFilesDir();
        File file = new File(path, SAVE_FILE_NAME);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            colorsList = objectMapper.readValue(file, new TypeReference<List<ColorListElement>>() {});
        } catch (IOException ignore) {
            colorsList = new ArrayList<>();
        }

        RecyclerView recyclerView = findViewById(R.id.colors_list_recycler);

        int selectedPosition = -1;
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(POSITION_INDEX);
        }

        colorsListRecyclerAdapter = new ColorsListRecyclerAdapter(colorsList, selectedPosition);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(colorsListRecyclerAdapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            fab.setClickable(false); //with this you can't make multy-click
            Intent createElementIntent = new Intent(this, ColorElemCreateActivity.class);
            createElementIntent.putExtra(NUMBER_INDEX_EXTRA, getLastElementNumber() + 1);
            startActivityForResult(createElementIntent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) return;
        int newListElementColor = data.getIntExtra(SELECTED_COLOR_EXTRA, 0);
        colorsList.add(new ColorListElement(newListElementColor, getLastElementNumber() + 1));
        colorsListRecyclerAdapter.notifyDataSetChanged();

        //You need to save state after any change quickly, in case app's crash
        File path = getFilesDir();
        File file = new File(path, SAVE_FILE_NAME);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(file, colorsList);
        } catch (IOException ignore) {
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private int getLastElementNumber() {
        if (colorsList.size() != 0) {
            return colorsList.get(colorsList.size() - 1).getNumber();
        }
        return -1;
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