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
import java.io.IOException;
import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


final class ColorsListRecyclerAdapter extends RecyclerView.Adapter<ColorsListRecyclerAdapter.RecyclerViewHolder> {
    private int selectedPosition;
    private List<ColorListElement> colorList;
    private File filesDir;

    public ColorsListRecyclerAdapter(Context context, File filesDir, int selectedPosition) {
        this.filesDir = filesDir;
        this.selectedPosition = selectedPosition;
        colorList = new ArrayList<>();
        loadState(context);
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
            intent.setAction(MainActivity.CustomBroadcastReceiver.DIALOG_ACTION);
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


    private void saveState(){
        Disposable.fromRunnable(() -> {
            ColorListJsonLoader.writeJsonInFile(filesDir, colorList);
        }).dispose();
        notifyDataSetChanged();
    }

    private void loadState(Context context){
        Single.fromCallable(() -> ColorListJsonLoader.readJsonFromFile(filesDir))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> {colorList = v;
                notifyDataSetChanged();
                showMainFabAction(context);
                },
                        v -> {
                            Log.v("tag", v.toString());
                            showMainFabAction(context);
                        });
    }

    private void showMainFabAction(Context context){
        Intent intent = new Intent();
        intent.setAction(MainActivity.CustomBroadcastReceiver.SHOW_FAB_ACTION);
        context.sendBroadcast(intent);
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

    public int getNumberByPosition(int position){
        return colorList.get(position).getNumber();
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
