package com.internship.colors;

import android.content.Context;
import android.content.Intent;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

final class ColorsListRecyclerAdapter extends RecyclerView.Adapter<ColorsListRecyclerAdapter.RecyclerViewHolder> {
    private int selectedPosition;
    private final List<ColorListElement> colorList;
    private final File filesDir;

    public ColorsListRecyclerAdapter(Context context, File filesDir, int selectedPosition) {
        this.filesDir = filesDir;
        this.selectedPosition = selectedPosition;
        colorList = new ArrayList<>();  // creates empty page and loads list from file
        loadState(context);  // loads list, if its empty, it stars empty list adepter
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
            view.setSelected(true);
            int lastSelectedPosition = getSelectedPosition();
            selectedPosition = recyclerViewHolder.getLayoutPosition();
            notifyItemChanged(lastSelectedPosition);

            Intent intent = new Intent();  //call dialog creation in main activity
            intent.setAction(Constants.DIALOG_ACTION);
            parent.getContext().sendBroadcast(intent);

            return true;
        });

        return recyclerViewHolder;
    }

    public void addColorElement(int colorId, int number) {
        colorList.add(new ColorListElement(colorId, number));
        saveState();
    }

    public void deleteColorElement(int index) {
        colorList.remove(index);
        saveState();
    }

    public void unselectElement() {
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    public int getNumberByPosition(int position) {
        return colorList.get(position).getNumber();
    }

    public int getNumberForNewElement() {  // returns new name's number for new element
        if (getItemCount() != 0) {
            return getNumberByPosition(colorList.size() - 1) + 1;
        }
        return 0; // returns 0 number if list is empty
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    private void saveState() {
        Disposable.fromRunnable(() -> ColorListJsonLoader.writeJsonInFile(filesDir, colorList));
        notifyDataSetChanged();
    }

    private void loadState(Context context) {  // fills empty list with saved elements
        Single.fromCallable(() -> ColorListJsonLoader.readJsonFromFile(filesDir))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> {
                            fillElementsListWithData(v);
                            notifyDataSetChanged();
                            showMainFabAction(context);
                        },
                        v -> {
                            Log.e(Constants.LOADING_COLOR_LIST_FILE_ERROR_TAG, v.toString());
                            showMainFabAction(context);
                        });
    }

    private void fillElementsListWithData(List<ColorListElement> listData) {
        colorList.addAll(listData);
    }

    private void showMainFabAction(Context context) {  // invokes main activity's fab to make it visible
        // we need block fab before data will be prepared
        Intent intent = new Intent();
        intent.setAction(Constants.SHOW_FAB_ACTION);
        context.sendBroadcast(intent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String text = holder.textView.getContext().getString(R.string.list_item_text_pattern, colorList.get(position).getNumber());
        holder.textView.setText(text);
        holder.image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.image.getContext(), colorList.get(position).getColor())));
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
