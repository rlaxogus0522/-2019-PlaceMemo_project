package com.example.placememo_project.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placememo_project.R;

import java.util.ArrayList;

public class AlamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<String> items = new ArrayList<>();
//    ArrayList<Integer> color = new ArrayList<>();



    @NonNull
    @Override
    public int getItemViewType(int position) {
            return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alam_item, viewGroup, false);
            return new ViewHolder1(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ViewHolder1) {
            ((ViewHolder1) viewHolder).textView.setText(items.get(position));
        }
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(String item) {
        items.add(item);
//        this.color.add(color);
        notifyDataSetChanged();  // -- 업데이트
    }



    public class ViewHolder1 extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder1(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.alam_text);
        }
    }
}
