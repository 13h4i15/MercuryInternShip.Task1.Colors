package com.internship.colors;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ColorsListRecyclerAdapter extends RecyclerView.Adapter<ColorsListRecyclerAdapter.RecyclerViewHolder> {
    private final ArrayList<ColorListElem> colorListElems;
    private int focusedPosition;

    public ColorsListRecyclerAdapter(ArrayList<ColorListElem> colorListElems, int focusedPosition) {
        this.colorListElems = colorListElems;
        this.focusedPosition = focusedPosition;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_list_item_layout, parent, false);

        final RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);

        view.setOnClickListener(v -> {
            if (!v.isSelected() && focusedPosition != recyclerViewHolder.getLayoutPosition()) {
                // Without second condition it tries to create Snackbar again when you change app state.
                // Because of this it crashes app, it tries to create message earlier than parent context is created.
                // We can fix it with app context, but it will be a wrong behavior.
                v.setSelected(true);
                int lastFocusedPosition = getFocusedPosition();
                focusedPosition = recyclerViewHolder.getLayoutPosition();
                notifyItemChanged(lastFocusedPosition);
                String text = v.getContext().getString(R.string.fab_snackbar_message_text_pattern, focusedPosition);
                Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String text = holder.textView.getContext().getString(R.string.list_item_text_pattern, position);
        holder.textView.setText(text);
        holder.image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.image.getContext(), ColorListElem.ItemColorState.getColorByPosition(position))));
        if (position == getFocusedPosition()) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }
    }

    public int getFocusedPosition() {
        return focusedPosition;
    }

    @Override
    public int getItemCount() {
        return colorListElems.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView image;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.colors_list_item_text);
            image = itemView.findViewById(R.id.colors_list_item_circle);
        }
    }
}
