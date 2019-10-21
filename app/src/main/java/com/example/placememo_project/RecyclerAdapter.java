package com.example.placememo_project;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
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
import java.util.Collection;
import java.util.Collections;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.MainActivity.mainContext;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<String> items = new ArrayList<>();
    ArrayList<Integer> color = new ArrayList<>();
    Realm myRealm;

    Context mcontext;


    public RecyclerAdapter(Context context){
        this.mcontext = context;
        Realm.init(context);
        myRealm = Realm.getDefaultInstance();
    }
    @NonNull
    @Override
    public int getItemViewType(int position) {
            return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_nomal_item_main, viewGroup, false);
            return new ItemSwipeWithActionWidthViewHolder1(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ItemSwipeWithActionWidthViewHolder1) {
            final RealmResults<Data_nomal> data_nomals = myRealm.where(Data_nomal.class).equalTo("memo", items.get(position)).findAll();
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).textView.setText(items.get(position));
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).colorView.setBackgroundColor(color.get(position));
            if(data_nomals.first().getFrag()){
                ((ItemSwipeWithActionWidthViewHolder1) viewHolder).textView.setPaintFlags(((ItemSwipeWithActionWidthViewHolder1) viewHolder).textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                ((ItemSwipeWithActionWidthViewHolder1) viewHolder).textView.setPaintFlags(((ItemSwipeWithActionWidthViewHolder1) viewHolder).textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mViewContent1.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onSingleClick(View v) {
//                    Toast.makeText(mcontext,data_nomals.get(position).getOrder()+"",Toast.LENGTH_LONG).show();

                }

                @Override
                public void onDoubleClick(View v) {
                    if(((ItemSwipeWithActionWidthViewHolder1) viewHolder).textView.getPaintFlags() != (((ItemSwipeWithActionWidthViewHolder1) viewHolder).textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG) ) {
                        ((ItemSwipeWithActionWidthViewHolder1) viewHolder).textView.setPaintFlags(((ItemSwipeWithActionWidthViewHolder1) viewHolder).textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        RealmResults<Data_nomal> data_nomals = myRealm.where(Data_nomal.class).equalTo("memo", items.get(position)).findAll();
                        myRealm.beginTransaction();
                        data_nomals.first().setFrag(true);
                        myRealm.commitTransaction();
                    }else{
                        ((ItemSwipeWithActionWidthViewHolder1) viewHolder).textView.setPaintFlags(((ItemSwipeWithActionWidthViewHolder1) viewHolder).textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        RealmResults<Data_nomal> data_nomals = myRealm.where(Data_nomal.class).equalTo("memo", items.get(position)).findAll();
                        myRealm.beginTransaction();
                        data_nomals.first().setFrag(false);
                        myRealm.commitTransaction();
                    }
                }
            });
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mActionViewDelete1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(((ItemSwipeWithActionWidthViewHolder1) viewHolder).mViewContent1.getTranslationX() <=  -(((ItemSwipeWithActionWidthViewHolder1) viewHolder).mActionContainer1.getWidth())) {
                        remove(position);
                        ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mViewContent1.setTranslationX(0f);

                    }
                }
            });
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mActionViewEdit1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(((ItemSwipeWithActionWidthViewHolder1) viewHolder).mViewContent1.getTranslationX() <=  -(((ItemSwipeWithActionWidthViewHolder1) viewHolder).mActionContainer1.getWidth())) {
                        Data_nomal data_nomal = myRealm.where(Data_nomal.class).equalTo("memo",items.get(position)).findFirst();
                        data_nomal
                    }
                }
            });




        }
    }
    public abstract class DoubleClickListener implements View.OnClickListener {

        private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

        long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                onDoubleClick(v);
            } else {
                onSingleClick(v);
            }
            lastClickTime = clickTime;
        }

        public abstract void onSingleClick(View v);
        public abstract void onDoubleClick(View v);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(String item,int color) {
        items.add(item);
        this.color.add(color);
        Log.d("===",color+"");
        notifyDataSetChanged();  // -- 업데이트
        ((MainActivity)mainContext).checkNoImage_nomal();
    }

    public void remove(int position){
        RealmResults<Data_nomal> data_nomals = myRealm.where(Data_nomal.class).equalTo("memo", items.get(position)).findAll();
        myRealm.beginTransaction();
        data_nomals.deleteAllFromRealm();
        myRealm.commitTransaction();
        items.remove(position);
        color.remove(position);
        notifyDataSetChanged();  // -- 업데이트
        ((MainActivity)mainContext).checkNoImage_nomal();
    }


    public void clear(){
        items.clear();
        color.clear();
        notifyDataSetChanged();  // -- 업데이트
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView textView;
        View mViewContent1;
        View mActionContainer1;
        View colorView;


        public ViewHolder1(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_list_item_memo);
            mViewContent1 = itemView.findViewById(R.id.view_list_nomal_content);
            mActionContainer1 = itemView.findViewById(R.id.view_list_memo_container);
            colorView = itemView.findViewById(R.id.liner);
        }


    }

    /*---------------------------------------------------------------------------------------------------------------------------*/
    class ItemSwipeWithActionWidthViewHolder1 extends ViewHolder1 implements Extension {
        View mActionViewDelete1;
        View mActionViewEdit1;



        public ItemSwipeWithActionWidthViewHolder1(View itemView) {
            super(itemView);
            mActionViewDelete1 = itemView.findViewById(R.id.view_list_memo_delete);
            mActionViewEdit1 = itemView.findViewById(R.id.view_list_memo_edit);
        }

        @Override
        public float getActionWidth() { return mActionContainer1.getWidth(); }
    }

    /*------------------------------------------------------------------------------------------------------------------*/

}
