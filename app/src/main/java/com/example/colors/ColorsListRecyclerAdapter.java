package com.example.colors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ColorsListRecyclerAdapter extends RecyclerView.Adapter<ColorsListRecyclerAdapter.RecyclerViewHolder>{
    private ArrayList<ColorListElem> colorListElems;
    private Context context;

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public View image;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.colors_list_item_text);
            //image = itemView.findViewById(R.id.colors_list_item_image);
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
        holder.textView.setText("Item" + position);//todo
        //holder.image.setBackgroundColor(colorListElems.get(position).getColor());
    }


}
