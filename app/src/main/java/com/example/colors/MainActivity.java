package com.example.colors;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String POSITION_INDEX = "position";

    private RecyclerView recyclerView;
    private ColorsListRecyclerAdapter colorsListRecyclerAdapter;
    private ArrayList<ColorListElem> colorListElems;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private int focusedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        colorListElems = new ArrayList<>();
        for (int i = 0; i < 50; ++i) {
            colorListElems.add(new ColorListElem());
        }

        recyclerView = findViewById(R.id.colors_list_recycler);
        if (savedInstanceState != null) {
            focusedPosition = savedInstanceState.getInt(POSITION_INDEX);
        }
        colorsListRecyclerAdapter = new ColorsListRecyclerAdapter(colorListElems, focusedPosition);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(colorsListRecyclerAdapter);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            focusedPosition = colorsListRecyclerAdapter.getFocusedPosition();
            String text;
            if (focusedPosition == -1) {
                text = this.getText(R.string.fab_snackbar_message_text_unclicked).toString();
            } else {
                text = this.getText(R.string.fab_snackbar_message_text_pattern).toString() + " " + focusedPosition;
            }
            Snackbar.make(v, text, Snackbar.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        focusedPosition = colorsListRecyclerAdapter.getFocusedPosition();
        outState.putInt(POSITION_INDEX, focusedPosition);
    }
}