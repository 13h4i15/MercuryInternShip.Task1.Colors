package com.internship.colors;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String POSITION_INDEX = "position";

    private ColorsListRecyclerAdapter colorsListRecyclerAdapter;
    private List<ColorListElem> colorsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.colors_list_recycler);
        int selectedPosition = -1;
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(POSITION_INDEX);
        }
        colorsListRecyclerAdapter = new ColorsListRecyclerAdapter(colorsList, selectedPosition);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(colorsListRecyclerAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent createElemIntent = new Intent(this, ColorElemCreateActivity.class);
            startActivityForResult(createElemIntent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null) return;
        int lastElemNumber = -1;
        if(colorsList.size() != 0) {
            lastElemNumber = colorsList.get(colorsList.size() - 1).getPosition();
        }
        int newListElemColor = data.getIntExtra("color", 0);
        colorsList.add(new ColorListElem(newListElemColor, lastElemNumber+1));
        colorsListRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int selectedPosition = colorsListRecyclerAdapter.getSelectedPosition();
        outState.putInt(POSITION_INDEX, selectedPosition);
        super.onSaveInstanceState(outState);
    }
}