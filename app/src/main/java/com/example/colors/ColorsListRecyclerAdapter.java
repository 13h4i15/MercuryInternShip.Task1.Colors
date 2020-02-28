package com.example.colors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ColorsListRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{
    private ArrayList<ColorListElem> colorListElems;


    public ColorsListRecyclerAdapter(ArrayList<ColorListElem> colorListElems) {
        this.colorListElems = colorListElems;
    }

    @Override
    public int getItemCount() {
        return colorListElems.size();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_list_item_layout, parent, false);
        final RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.textView.setText(colorListElems.get(position).getText());
        //holder.image.setBackgroundColor(colorListElems.get(position).getColor());
    }


}
