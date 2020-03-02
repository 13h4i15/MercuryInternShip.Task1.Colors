package com.example.colors;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ColorsListRecyclerAdapter extends RecyclerView.Adapter<ColorsListRecyclerAdapter.RecyclerViewHolder> {
    private ArrayList<ColorListElem> colorListElems;
    private int focusedPosition;
    private ViewGroup parent;

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView image;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.colors_list_item_text);
            image = itemView.findViewById(R.id.colors_list_item_circle);
        }
    }

    public int getFocusedPosition() {
        return focusedPosition;
    }

    public ColorsListRecyclerAdapter(ArrayList<ColorListElem> colorListElems, int focusedPosition) {
        this.colorListElems = colorListElems;
        this.focusedPosition = focusedPosition;
    }

    @Override
    public int getItemCount() {
        return colorListElems.size();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_list_item_layout, parent, false);

        final RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);

        view.setOnClickListener(v -> {  // need any action for focusability
        });

        view.setOnFocusChangeListener((v, isFocused) -> {
            if (isFocused) {
                focusedPosition = recyclerViewHolder.getLayoutPosition();
            }
        });
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String text = parent.getContext().getText(R.string.list_item_text_pattern).toString() + " " + position;
        holder.textView.setText(text);
        holder.image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(parent.getContext(), ColorListElem.ItemColorState.getColorByPosition(position))));
        if (position == getFocusedPosition()) {
            holder.itemView.requestFocus();
        }
    }
}
