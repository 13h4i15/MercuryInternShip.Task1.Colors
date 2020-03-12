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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String POSITION_INDEX = "position";
    private static final String COLOR_LIST = "list";

    private ColorsListRecyclerAdapter colorsListRecyclerAdapter;
    private ArrayList<Integer> colorsList = new ArrayList<>();

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
            colorsList = savedInstanceState.getIntegerArrayList(COLOR_LIST);
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
        int newListElem = data.getIntExtra("color", 0);
        colorsList.add(newListElem);
        colorsListRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int selectedPosition = colorsListRecyclerAdapter.getSelectedPosition();
        outState.putInt(POSITION_INDEX, selectedPosition);
        outState.putIntegerArrayList(COLOR_LIST, colorsList);
        super.onSaveInstanceState(outState);
    }
}