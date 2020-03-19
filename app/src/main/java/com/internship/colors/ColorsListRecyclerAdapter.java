package com.internship.colors;

import android.content.Context;
import android.content.Intent;
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

            Intent intent = new Intent();
            intent.putExtra("extra", recyclerViewHolder.getLayoutPosition());
            intent.setAction(MainActivity.CustomBroadcastReceiver.ACTION);
            parent.getContext().sendBroadcast(intent);


            return true;
        });

        return recyclerViewHolder;
    }

    public void addColorElement(ColorListElement element, Context context) {
        colorList.add(element);
        notifyDataSetChanged();
        try {
            ColorListJsonLoader.writeJsonInFile(context.getFilesDir(), colorList);
        } catch (IOException ignore) {
        }
    }

    public void deleteColorElement(int index, Context context) {
        colorList.remove(index);
        notifyDataSetChanged();
        try {
            ColorListJsonLoader.writeJsonInFile(context.getFilesDir(), colorList);
        } catch (IOException ignore) {
        }
    }

    public void unselectElement() {
        setSelectedPosition(-1);
        notifyDataSetChanged();
    }

    public int getNumberForNewElement() {
        if (getItemCount() != 0) {
            return colorList.get(colorList.size() - 1).getNumber() + 1;
        }
        return 0;
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

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
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
