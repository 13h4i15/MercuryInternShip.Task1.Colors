package com.internship.colors;

import android.content.res.ColorStateList;
import android.util.Log;
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


final class ColorsListRecyclerAdapter extends RecyclerView.Adapter<ColorsListRecyclerAdapter.RecyclerViewHolder> {
    private int selectedPosition;
    private final List<ColorListElement> colorList;
    private View.OnLongClickListener onLongClickListener;

    public ColorsListRecyclerAdapter(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        colorList = new ArrayList<>();  // creates empty page and loads list from file
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_list_element_layout, parent, false);
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
            v.setSelected(true);
            int lastSelectedPosition = getSelectedPosition();
            selectedPosition = recyclerViewHolder.getLayoutPosition();
            notifyItemChanged(lastSelectedPosition);
            onLongClickListener.onLongClick(v);
            return true;
        });
        return recyclerViewHolder;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public void addColorElement(int colorId, int number) {
        colorList.add(new ColorListElement(colorId, number));
        notifyDataSetChanged();
    }

    public void deleteColorElement(int index) {
        try {
            colorList.remove(colorList.get(index));
            unselectElement();
        } catch (IndexOutOfBoundsException e) {
            Log.e(Constants.DELETING_ELEMENT_FROM_LIST_ERROR_TAG, e.toString());
        }
    }

    public void unselectElement() {
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    public int getNumberByPosition(int position) {
        return colorList.get(position).getNumber();
    }

    public int getNumberForNewElement() {  // returns new name's number for new element
        return getItemCount() != 0 ? getNumberByPosition(colorList.size() - 1) + 1 : 0;  // returns 0 number if list is empty
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void fillElementsListWithData(List<ColorListElement> listData) {
        colorList.clear();
        colorList.addAll(listData);
    }

    public List<ColorListElement> getColorList() {
        return new ArrayList<>(colorList);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String text = holder.textView.getContext().getString(R.string.list_item_text_pattern, colorList.get(position).getNumber());
        holder.textView.setText(text);
        holder.image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.image.getContext(), colorList.get(position).getColorId())));
        holder.itemView.setSelected(position == getSelectedPosition());
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
