package com.example.placememo_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loopeer.itemtouchhelperextension.Extension;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<RecyclerItem> items = new ArrayList<>();
    private static final int VIEW_TYPE_A = 0;   // -- view 타입을 2개로 구분 (  구분선 /  아이템 )
    private static final int VIEW_TYPE_B = 1;
    int color;
    Context mcontext;

    public RecyclerAdapter(Context context){
        this.mcontext = context;
    }
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
/////
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_A) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_title, viewGroup, false);
            v.setBackgroundColor(color);
            return new ViewHolder1(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_main, viewGroup, false);
            return new ItemSwipeWithActionWidthViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder1) {
            ((ViewHolder1) viewHolder).textView.setText(items.get(position).getTitle());
            ((ViewHolder1) viewHolder).imageView.setImageResource(items.get(position).getIcon());

        } else if (viewHolder instanceof ItemSwipeWithActionWidthViewHolder) {
            ((ItemSwipeWithActionWidthViewHolder) viewHolder).mActionViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mcontext,"삭제 버튼 클릭",Toast.LENGTH_LONG).show();
                }
            });
            ((ItemSwipeWithActionWidthViewHolder) viewHolder).mActionViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mcontext,"편집 버튼 클릭",Toast.LENGTH_LONG).show();
                }
            });
            ((ItemSwipeWithActionWidthViewHolder) viewHolder).textView2.setText(items.get(position).getMemo());
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
        View mViewContent;
        View mActionContainer;

        public ViewHolder1(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title);
            imageView = itemView.findViewById(R.id.title_image);
        }


    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView textView2;
        View mViewContent;
        View mActionContainer;
        public ViewHolder2(View itemView) {
            super(itemView);
            textView2 = itemView.findViewById(R.id.text_list_main_title);
            //textView2 = itemView.findViewById(R.id.tv_memo);
            mViewContent = itemView.findViewById(R.id.view_list_main_content);
            mActionContainer = itemView.findViewById(R.id.view_list_repo_action_container);

        }

    }

    class ItemSwipeWithActionWidthViewHolder extends ViewHolder2 implements Extension {

        View mActionViewDelete;
        View mActionViewEdit;

        public ItemSwipeWithActionWidthViewHolder(View itemView) {
            super(itemView);
            mActionViewDelete = itemView.findViewById(R.id.view_list_repo_action_delete);
            mActionViewEdit = itemView.findViewById(R.id.view_list_repo_action_edit);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }
    class ItemSwipeWithActionWidthNoSpringViewHolder extends ItemSwipeWithActionWidthViewHolder implements Extension {

        public ItemSwipeWithActionWidthNoSpringViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }
}
