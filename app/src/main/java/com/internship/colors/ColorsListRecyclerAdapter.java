package com.internship.colors;

import android.app.AlertDialog;
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

import java.io.File;
import java.io.IOException;
import java.util.List;

class ColorsListRecyclerAdapter extends RecyclerView.Adapter<ColorsListRecyclerAdapter.RecyclerViewHolder> {

    private int selectedPosition;
    private final List<ColorListElement> colorList;

    public ColorsListRecyclerAdapter(List<ColorListElement> colorList, int selectedPosition) {
        this.colorList = colorList;
        this.selectedPosition = selectedPosition;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_list_element_layout, parent, false);

        final RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);

        view.setOnClickListener(v -> {
            if (!v.isSelected()) {
                v.setSelected(true);
                int lastSelectedPosition = getSelectedPosition();
                selectedPosition = recyclerViewHolder.getLayoutPosition();
                notifyItemChanged(lastSelectedPosition);
                String text = v.getContext().getString(R.string.fab_snackbar_message_text_pattern, colorList.get(selectedPosition).getNumber());
                Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

        view.setOnLongClickListener(v -> {
            view.setSelected(true);
            int lastSelectedPosition = getSelectedPosition();
            selectedPosition = recyclerViewHolder.getLayoutPosition();
            notifyItemChanged(lastSelectedPosition);

            ColorListElement currentListElement = colorList.get(recyclerViewHolder.getLayoutPosition());
            AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
            builder.setMessage(parent.getContext().getString(R.string.dialog_delete_question, currentListElement.getNumber()));

            builder.setPositiveButton(parent.getContext().getString(R.string.dialog_yes_answer), (dialog, which) -> {
                deleteColorElement(recyclerViewHolder.getLayoutPosition());

                //You need to save state after any change quickly, in case app's crash
                try {
                    File path = parent.getContext().getFilesDir();
                    ColorListJsonLoader.writeJsonInFile(path, colorList);
                } catch (IOException ignore) {
                }
            });

            builder.setNegativeButton(parent.getContext().getString(R.string.dialog_no_answer), (dialog, which) -> {
                view.setSelected(false);
                notifyItemChanged(recyclerViewHolder.getLayoutPosition());
            });

            selectedPosition = -1;
            builder.create().show();

            return true;
        });

        return recyclerViewHolder;
    }

    public void addColorElement(ColorListElement element){
        colorList.add(element);
        notifyDataSetChanged();
    }

    public void deleteColorElement(int index){
        colorList.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String text = holder.textView.getContext().getString(R.string.list_item_text_pattern, colorList.get(position).getNumber());
        holder.textView.setText(text);
        holder.image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.image.getContext(), colorList.get(position).getColor())));
        holder.itemView.setSelected(position == getSelectedPosition());
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView image;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.colors_list_element_text);
            image = itemView.findViewById(R.id.colors_list_element_circle);
        }
    }
}
