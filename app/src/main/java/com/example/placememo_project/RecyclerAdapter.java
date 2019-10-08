package com.example.placememo_project;

import android.content.Context;
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

import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.ContentValues.TAG;
import static com.example.placememo_project.MainActivity.mContext;
import static com.example.placememo_project.MainActivity.titlename;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<RecyclerItem> items = new ArrayList<>();
    private static final int VIEW_TYPE_A = 0;   // -- view 타입을 2개로 구분 (  구분선 /  아이템 )
    private static final int VIEW_TYPE_B = 1;
    int color;
    Context mcontext;
    Realm myRealm;
    String deletMessage;

    public RecyclerAdapter(Context context){
        this.mcontext = context;
        Realm.init(context);
        try {
            myRealm = Realm.getDefaultInstance();

        } catch (Exception e) {
            Log.d(TAG, "myRealm = null");
        }
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
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_title, viewGroup, false);
            View v2 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_list_main_title_content, viewGroup, false);
            v.setBackgroundColor(color);
            v2.setBackgroundColor(color);
            return new ItemSwipeWithActionWidthViewHolder1(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_main, viewGroup, false);
            return new ItemSwipeWithActionWidthViewHolder2(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ItemSwipeWithActionWidthViewHolder1) {
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mActionViewDelete1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mcontext,"삭제 버튼 클릭",Toast.LENGTH_LONG).show();
                }
            });
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mActionViewEdit1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mcontext,"편집 버튼 클릭",Toast.LENGTH_LONG).show();
                }
            });
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).textView.setText(items.get(position).getTitle());
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).imageView.setImageResource(items.get(position).getIcon());

        } else if (viewHolder instanceof ItemSwipeWithActionWidthViewHolder2) {
            ((ItemSwipeWithActionWidthViewHolder2) viewHolder).mActionViewDelete2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mcontext,"삭제 버튼 클릭",Toast.LENGTH_LONG).show();
                    deletMessage = ((ItemSwipeWithActionWidthViewHolder2) viewHolder).textView2.getText().toString();
                    Log.d("//**//**",deletMessage);
                    remove(position);
                    ((ItemSwipeWithActionWidthViewHolder2) viewHolder).mViewContent2.setTranslationX(0f);

                }
            });
            ((ItemSwipeWithActionWidthViewHolder2) viewHolder).mActionViewEdit2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mcontext,"편집 버튼 클릭",Toast.LENGTH_LONG).show();
                }
            });
            ((ItemSwipeWithActionWidthViewHolder2) viewHolder).textView2.setText(items.get(position).getMemo());
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
                titlename.remove(getTitle());
                items.remove(items.size()-1);
            }
            notifyDataSetChanged();  // --  업데이트
        }else{
            items.remove(i);
            RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("memo",deletMessage).findAll();
            myRealm.beginTransaction();
            data_alams.deleteAllFromRealm();
            myRealm.commitTransaction();
            if(items.size()==1){
                Log.d("이건과연",getTitle());
                RealmResults<Data_alam> data_alam = myRealm.where(Data_alam.class).equalTo("name",getTitle()).findAll();
                myRealm.beginTransaction();
                data_alam.deleteAllFromRealm();
                myRealm.commitTransaction();
                titlename.remove(getTitle());
                items.remove(0);
            }
            notifyDataSetChanged();  // --  업데이트
        }
        ((MainActivity) mContext).checkNoImage();
    }

    public String getTitle(){
        return items.get(0).getTitle();    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        View mViewContent1;
        View mActionContainer1;


        public ViewHolder1(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title);
            imageView = itemView.findViewById(R.id.title_image);
            mViewContent1 = itemView.findViewById(R.id.view_list_title_content);
            mActionContainer1 = itemView.findViewById(R.id.view_list_title_action_container);
            mViewContent1.setBackgroundColor(color);

        }


    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView textView2;
        View mViewContent2;
        View mActionContainer2;
        public ViewHolder2(View itemView) {
            super(itemView);
            textView2 = itemView.findViewById(R.id.text_list_main_title);
            mViewContent2 = itemView.findViewById(R.id.view_list_main_content);
            mActionContainer2 = itemView.findViewById(R.id.view_list_repo_action_container);

        }

    }
    /*---------------------------------------------------------------------------------------------------------------------------*/
    class ItemSwipeWithActionWidthViewHolder1 extends ViewHolder1 implements Extension {

        View mActionViewDelete1;
        View mActionViewEdit1;


        public ItemSwipeWithActionWidthViewHolder1(View itemView) {
            super(itemView);
            mActionViewDelete1 = itemView.findViewById(R.id.view_list_title_action_delete);
            mActionViewEdit1 = itemView.findViewById(R.id.view_list_title_action_edit);
        }

        @Override
        public float getActionWidth() { return mActionContainer1.getWidth(); }
    }
    class ItemSwipeWithActionWidthNoSpringViewHolder1 extends ItemSwipeWithActionWidthViewHolder1 implements Extension {

        public ItemSwipeWithActionWidthNoSpringViewHolder1(View itemView) { super(itemView); }

        @Override
        public float getActionWidth() {
            return mActionContainer1.getWidth();
        }
    }

    /*------------------------------------------------------------------------------------------------------------------*/
    class ItemSwipeWithActionWidthViewHolder2 extends ViewHolder2 implements Extension {

        View mActionViewDelete2;
        View mActionViewEdit2;

        public ItemSwipeWithActionWidthViewHolder2(View itemView) {
            super(itemView);
            mActionViewDelete2 = itemView.findViewById(R.id.view_list_repo_action_delete);
            mActionViewEdit2 = itemView.findViewById(R.id.view_list_repo_action_edit);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer2.getWidth();
        }
    }
    class ItemSwipeWithActionWidthNoSpringViewHolder2 extends ItemSwipeWithActionWidthViewHolder2 implements Extension {

        public ItemSwipeWithActionWidthNoSpringViewHolder2(View itemView) {
            super(itemView);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer2.getWidth();
        }
    }
}
