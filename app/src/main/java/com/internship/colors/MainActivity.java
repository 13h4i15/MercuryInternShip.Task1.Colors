package com.internship.colors;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private static final String POSITION_INDEX = "position";

    private ColorsListRecyclerAdapter colorsListRecyclerAdapter;

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
        colorsListRecyclerAdapter = new ColorsListRecyclerAdapter(50, selectedPosition);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(colorsListRecyclerAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent createElemIntent = new Intent(this, ColorElemCreateActivity.class);
            startActivity(createElemIntent);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int selectedPosition = colorsListRecyclerAdapter.getSelectedPosition();
        outState.putInt(POSITION_INDEX, selectedPosition);

        super.onSaveInstanceState(outState);
    }
}