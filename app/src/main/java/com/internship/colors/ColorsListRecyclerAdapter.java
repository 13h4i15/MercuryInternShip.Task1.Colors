package com.internship.colors;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.List;

class ColorsListRecyclerAdapter extends RecyclerView.Adapter<ColorsListRecyclerAdapter.RecyclerViewHolder> {
    private int selectedPosition;
    private List<ColorListElem> colorsList;


    public ColorsListRecyclerAdapter(List<ColorListElem> colorsList, int selectedPosition) {
        this.colorsList = colorsList;
        this.selectedPosition = selectedPosition;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_list_item_layout, parent, false);

        final RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);

        view.setOnClickListener(v -> {
            if (!v.isSelected()) {
                v.setSelected(true);
                int lastSelectedPosition = getSelectedPosition();
                selectedPosition = recyclerViewHolder.getLayoutPosition();
                notifyItemChanged(lastSelectedPosition);
                String text = v.getContext().getString(R.string.fab_snackbar_message_text_pattern, selectedPosition);
                Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

        view.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
            builder.setMessage(parent.getContext().getString(R.string.dialog_delete_question, recyclerViewHolder.getLayoutPosition()));
            builder.setPositiveButton(parent.getContext().getString(R.string.dialog_yes_answer), (dialog, which) -> {
                colorsList.remove(recyclerViewHolder.getLayoutPosition());
                notifyDataSetChanged();
            });
            builder.setNegativeButton(parent.getContext().getString(R.string.dialog_no_answer),(dialog, which) -> {});
            builder.create().show();
            return true;
        });
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String text = holder.textView.getContext().getString(R.string.list_item_text_pattern, colorsList.get(position).getPosition());
        holder.textView.setText(text);
        holder.image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.image.getContext(), colorsList.get(position).getColor())));
        holder.itemView.setSelected(position == getSelectedPosition());
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @Override
    public int getItemCount() {
        return colorsList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView image;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.colors_list_item_text);
            image = itemView.findViewById(R.id.colors_list_item_circle);
        }
    }
}
