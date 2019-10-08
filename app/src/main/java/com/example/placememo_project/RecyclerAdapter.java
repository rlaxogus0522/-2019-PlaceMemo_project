package com.example.placememo_project;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<RecyclerItem> items = new ArrayList<>();
    private static final int VIEW_TYPE_A = 0;   // -- view 타입을 2개로 구분 (  구분선 /  아이템 )
    private static final int VIEW_TYPE_B = 1;
    int color;
    @NonNull

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getType().equals("A")) {  // -- 전달받은 값에서 Type을 구분
            color = items.get(position).getColor();
            return VIEW_TYPE_A;
        } else {
            color = items.get(position).getColor();
            return VIEW_TYPE_B;
        }
////
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_A) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_title, viewGroup, false);
            v.setBackgroundColor(color);
            return new ViewHolder1(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_memo, viewGroup, false);
            return new ViewHolder2(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder1) {
            ((ViewHolder1) viewHolder).textView.setText(items.get(position).getTitle());
            ((ViewHolder1) viewHolder).imageView.setImageResource(items.get(position).getIcon());

        } else if (viewHolder instanceof ViewHolder2) {
            ((ViewHolder2) viewHolder).textView2.setText(items.get(position).getMemo());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(RecyclerItem item) {
        items.add(item);
        notifyDataSetChanged();  // -- 업데이트
    }

    public void remove(int i){
        if(i==0){
            while(true){
                if(items.size()==0){
                    break;
                }
                items.remove(items.size()-1);
            }
            notifyDataSetChanged();  // --  업데이트
        }else{
            items.remove(i);
            notifyDataSetChanged();  // --  업데이트
        }
    }

    public String getTitle(){
        return items.get(0).toString();
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ViewHolder1(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title);
            imageView = itemView.findViewById(R.id.title_image);
        }


    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView textView2;

        public ViewHolder2(View itemView) {
            super(itemView);
            textView2 = itemView.findViewById(R.id.tv_memo);

        }

    }
}
