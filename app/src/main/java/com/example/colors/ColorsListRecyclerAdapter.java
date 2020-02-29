package com.example.colors;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ColorsListRecyclerAdapter extends RecyclerView.Adapter<ColorsListRecyclerAdapter.RecyclerViewHolder>{
    private ArrayList<ColorListElem> colorListElems;
    private Context context;

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public ImageView image;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.colors_list_item_text);
            image = itemView.findViewById(R.id.colors_list_item_circle);
        }

    }

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
        context = parent.getContext();
        final RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        view.setOnClickListener( v -> { });
        //view.setOnFocusChangeListener( (v, isFocused) ->{ });
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        colorListElems.get(position).setPosition(position);
        holder.textView.setText("Item " + position);
        holder.image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, ColorListElem.ItemColorState.getColorByPosition(position))));
    }


}
