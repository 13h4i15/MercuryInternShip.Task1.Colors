package com.example.colors;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ColorsListRecyclerAdapter colorsListRecyclerAdapter;
    private ArrayList<ColorListElem> colorListElems;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        colorListElems = new ArrayList<>();
        for(int i = 0; i < 50; ++i){
            colorListElems.add(new ColorListElem());
        }

        recyclerView = findViewById(R.id.colors_list_recycler);
        colorsListRecyclerAdapter = new ColorsListRecyclerAdapter(colorListElems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(colorsListRecyclerAdapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            int position = colorsListRecyclerAdapter.getFocusedPosition();
            String text;
            if(position == -1){
                text = this.getText(R.string.fab_snackbar_message_text_unclicked).toString();
            }else {
                text = this.getText(R.string.fab_snackbar_message_text_pattern).toString() + " " + position;
            }
            Snackbar.make(v, text, Snackbar.LENGTH_SHORT).show();
        });

    }

}
