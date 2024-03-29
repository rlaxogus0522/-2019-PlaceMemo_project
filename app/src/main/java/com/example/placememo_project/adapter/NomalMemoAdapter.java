package com.example.placememo_project.adapter;

import android.content.Context;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placememo_project.dbData.Data_nomal;
import com.example.placememo_project.R;
import com.example.placememo_project.activity.MainActivity;
import com.loopeer.itemtouchhelperextension.Extension;

import java.util.ArrayList;


import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.activity.MainActivity.mainContext;

public class NomalMemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<String> items = new ArrayList<>();
    public ArrayList<Integer> color = new ArrayList<>();
    public Realm myRealm;
    public Context mcontext;


    public NomalMemoAdapter(Context context){
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nomal_background, viewGroup, false);
        return new ItemSwipeWithActionWidthNomalHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ItemSwipeWithActionWidthNomalHolder) {
            Log.d("==",items.get(position));
            Data_nomal data_nomals = myRealm.where(Data_nomal.class).equalTo("memo", items.get(position)).findFirst();
            ((ItemSwipeWithActionWidthNomalHolder) viewHolder).textView.setText(items.get(position));
            ((ItemSwipeWithActionWidthNomalHolder) viewHolder).colorView.setBackgroundColor(color.get(position));
            if(data_nomals.getFrag()){
                ((ItemSwipeWithActionWidthNomalHolder) viewHolder).textView.setPaintFlags(((ItemSwipeWithActionWidthNomalHolder) viewHolder).textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                ((ItemSwipeWithActionWidthNomalHolder) viewHolder).textView.setPaintFlags(((ItemSwipeWithActionWidthNomalHolder) viewHolder).textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
            ((ItemSwipeWithActionWidthNomalHolder) viewHolder).mViewContent1.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onSingleClick(View v) {
//                    Toast.makeText(mcontext,data_nomals.get(position).getOrder()+"",Toast.LENGTH_LONG).show();

                }

                @Override
                public void onDoubleClick(View v) {
                    if(((ItemSwipeWithActionWidthNomalHolder) viewHolder).textView.getPaintFlags() != (((ItemSwipeWithActionWidthNomalHolder) viewHolder).textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG) ) {
                        ((ItemSwipeWithActionWidthNomalHolder) viewHolder).textView.setPaintFlags(((ItemSwipeWithActionWidthNomalHolder) viewHolder).textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        RealmResults<Data_nomal> data_nomals = myRealm.where(Data_nomal.class).equalTo("memo", items.get(position)).findAll();
                        myRealm.beginTransaction();
                        data_nomals.first().setFrag(true);
                        myRealm.commitTransaction();
                    }else{
                        ((ItemSwipeWithActionWidthNomalHolder) viewHolder).textView.setPaintFlags(((ItemSwipeWithActionWidthNomalHolder) viewHolder).textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        RealmResults<Data_nomal> data_nomals = myRealm.where(Data_nomal.class).equalTo("memo", items.get(position)).findAll();
                        myRealm.beginTransaction();
                        data_nomals.first().setFrag(false);
                        myRealm.commitTransaction();
                    }
                }
            });
            ((ItemSwipeWithActionWidthNomalHolder) viewHolder).mActionViewDelete1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((ItemSwipeWithActionWidthNomalHolder) viewHolder).mViewContent1.getTranslationX() <= -((((ItemSwipeWithActionWidthNomalHolder) viewHolder).mActionContainer1.getWidth()) / 2f)){
                        remove(position);
                        ((ItemSwipeWithActionWidthNomalHolder) viewHolder).mViewContent1.setTranslationX(0f);

                    }
                }
            });
            ((ItemSwipeWithActionWidthNomalHolder) viewHolder).mActionViewEdit1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(((ItemSwipeWithActionWidthNomalHolder) viewHolder).mViewContent1.getTranslationX() <=  -(((ItemSwipeWithActionWidthNomalHolder) viewHolder).mActionContainer1.getWidth()) / 2f) {
//                        Data_nomal data_nomal = myRealm.where(Data_nomal.class).equalTo("memo",items.get(position)).findFirst();
////                        data_nomal
                        ((MainActivity)mainContext).startNomalEdit(items.get(position),((ItemSwipeWithActionWidthNomalHolder) viewHolder).mViewContent1,position);
                        view.setTranslationX(0f);
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
        ((MainActivity)mainContext).checkNomalNoImage();
    }

    public void remove(int position){
        RealmResults<Data_nomal> data_nomals = myRealm.where(Data_nomal.class).equalTo("memo", items.get(position)).findAll();
        myRealm.beginTransaction();
        data_nomals.deleteAllFromRealm();
        myRealm.commitTransaction();
        items.remove(position);
        color.remove(position);
        notifyDataSetChanged();  // -- 업데이트
        ((MainActivity)mainContext).checkNomalNoImage();
    }


    public void clear(){
        items.clear();
        color.clear();
        notifyDataSetChanged();  // -- 업데이트
    }

    public class NomalHolder extends RecyclerView.ViewHolder {
        public  TextView textView;
        public  View mViewContent1;
        public  View mActionContainer1;
        public View colorView;


        public NomalHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_list_item_memo);
            mViewContent1 = itemView.findViewById(R.id.view_list_nomal_content);
            mActionContainer1 = itemView.findViewById(R.id.view_list_memo_container);
            colorView = itemView.findViewById(R.id.liner);
        }


    }

    /*---------------------------------------------------------------------------------------------------------------------------*/
    public class ItemSwipeWithActionWidthNomalHolder extends NomalHolder implements Extension {
        public View mActionViewDelete1;
        public View mActionViewEdit1;



        public ItemSwipeWithActionWidthNomalHolder(View itemView) {
            super(itemView);
            mActionViewDelete1 = itemView.findViewById(R.id.view_list_memo_delete);
            mActionViewEdit1 = itemView.findViewById(R.id.view_list_memo_edit);
        }

        @Override
        public float getActionWidth() { return mActionContainer1.getWidth(); }
    }

    /*------------------------------------------------------------------------------------------------------------------*/

}