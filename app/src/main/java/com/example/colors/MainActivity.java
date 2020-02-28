package com.example.colors;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ColorsListRecyclerAdapter colorsListRecyclerAdapter;
    private ArrayList<ColorListElem> colorListElems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        colorListElems = new ArrayList<>();
        for(int i = 0; i < 10; ++i){
            colorListElems.add(new ColorListElem("ggg", i, getResources().getColor(R.color.color_red)));
        }
        recyclerView = findViewById(R.id.colors_list_recycler);
        colorsListRecyclerAdapter = new ColorsListRecyclerAdapter(colorListElems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(colorsListRecyclerAdapter);
    }

}
