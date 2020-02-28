package com.example.colors;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class RecyclerViewHolder extends RecyclerView.ViewHolder{
    public TextView textView;
    public View image;
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.colors_list_item_text);
        //image = itemView.findViewById(R.id.colors_list_item_image);
    }
}
