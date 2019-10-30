package com.example.placememo_project;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loopeer.itemtouchhelperextension.Extension;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.MainActivity.mainContext;
import static com.example.placememo_project.MainActivity.sort;

public class LocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<LocationMemo_item> items = new ArrayList<>();
    Realm myRealm;
    private static final int VIEW_TYPE_D = -1;
    private static final int VIEW_TYPE_A = 0;   // -- view 타입을 2개로 구분 (  메뉴 /  메모 )
    private static final int VIEW_TYPE_B = 1;
    private static final int VIEW_TYPE_C = 2;
    Context mcontext;


    public LocationAdapter(Context context){
        this.mcontext = context;
        Realm.init(context);
        myRealm = Realm.getDefaultInstance();
    }
    @NonNull
    @Override
    public int getItemViewType(int position) {
        if(items.get(position).getType().equals("Pin")){
            return VIEW_TYPE_D;
        } else if (items.get(position).getType().equals("Title")) {  // -- 전달받은 값에서 Type을 구분
            return VIEW_TYPE_A;
        } else if(items.get(position).getType().equals("Memo")){
            return VIEW_TYPE_B;
        }else{
            return VIEW_TYPE_C;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
       if(viewType == VIEW_TYPE_D){
           Log.d("==onCreateViewHolder",items.size()+"");
           View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pin, viewGroup, false);
           return  new PinHolder(v);
       }
        else if(viewType == VIEW_TYPE_A) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_title, viewGroup, false);


            return new ItemSwipeWithActionWidthViewHolder1(v);
        }else if(viewType == VIEW_TYPE_B){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_main, viewGroup, false);
            return new ItemSwipeWithActionWidthViewHolder2(v);
        }else{
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_to_item, viewGroup, false);
            return new ViewHolder3(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        Data_alam data_alams = myRealm.where(Data_alam.class).equalTo("name",items.get(position).getTitle()).findFirst();
        if (viewHolder instanceof LocationAdapter.ItemSwipeWithActionWidthViewHolder1) {
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).background.setBackgroundColor(data_alams.getColor());
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mViewContent1.setBackgroundColor(data_alams.getColor());
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mActionContainer1.setBackgroundColor(data_alams.getColor());
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mActionViewDelete1.setEnabled(false);
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mActionViewEdit1.setEnabled(false);
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mActionViewDelete1.setOnClickListener(new View.OnClickListener() {  //-- 메뉴에 삭제버튼을 클릭한다면
                @Override
                public void onClick(View view) {
                    if (((LocationAdapter.ItemSwipeWithActionWidthViewHolder1) viewHolder).mViewContent1.getTranslationX() <=  -(((LocationAdapter.ItemSwipeWithActionWidthViewHolder1) viewHolder).mActionContainer1.getWidth())) {
                        remove(position,"Title");  //-- 해당 메뉴에 해당하는 메모 전부 삭제
//                    titlename.remove(getTitle());
                        ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mViewContent1.setTranslationX(0f);
                    }
                }
            });
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mActionViewEdit1.setOnClickListener(new View.OnClickListener() { //-- 메뉴에 편집버튼을 클릭한다면
                @Override
                public void onClick(View view) {
                    Toast.makeText(mcontext,"편집 버튼 클릭",Toast.LENGTH_LONG).show();
                }
            });
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).textView.setText(items.get(position).getTitle());
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).imageView.setImageResource(items.get(position).getIcon());


        }else if ( viewHolder instanceof  LocationAdapter.ItemSwipeWithActionWidthViewHolder2){
            ((ItemSwipeWithActionWidthViewHolder2) viewHolder).mActionViewDelete2.setEnabled(false);
            ((ItemSwipeWithActionWidthViewHolder2) viewHolder).mActionViewDelete2.setOnClickListener(new View.OnClickListener() {  //-- 메모에 삭제버튼을 클릭한다면
                @Override
                public void onClick(View view) {
                    if(  ((ItemSwipeWithActionWidthViewHolder2) viewHolder).mViewContent2.getWidth() != 0f) {
                        remove(position,"Memo");
                        ((ItemSwipeWithActionWidthViewHolder2) viewHolder).mViewContent2.setTranslationX(0f);
                    }
                }
            });
            ((ItemSwipeWithActionWidthViewHolder2) viewHolder).mActionViewEdit2.setOnClickListener(new View.OnClickListener() { //-- 메모에 편집버튼을 클릭한다면
                @Override
                public void onClick(View view) {
                    Toast.makeText(mcontext,"편집 버튼 클릭",Toast.LENGTH_LONG).show();
                }
            });
            ((ItemSwipeWithActionWidthViewHolder2) viewHolder).textView2.setText(items.get(position).getMemo());
        }else if( viewHolder instanceof LocationAdapter.PinHolder){
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadii(new float[ ]{ 200, 200, 200, 200, 0, 0, 0, 0 });
            gradientDrawable.setColor(data_alams.getColor());
            ((PinHolder) viewHolder).view.setBackgroundDrawable(gradientDrawable);
        }
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(LocationMemo_item item) {

        items.add(item);
        notifyDataSetChanged();  // -- 업데이트
        ((MainActivity)mainContext).checkNoImage_nomal();
    }

    public void remove(int position,String type){
        if(type.equals("Title")){
            RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("name", items.get(position).getTitle()).findAll();
            myRealm.beginTransaction();
            data_alams.deleteAllFromRealm();
            myRealm.commitTransaction();
        }else if(type.equals("Memo")){
            Data_alam data_alams = myRealm.where(Data_alam.class).equalTo("name",items.get(position).getTitle()).equalTo("memo",items.get(position).getMemo()).findFirst();
            myRealm.beginTransaction();
            data_alams.deleteFromRealm();
            myRealm.commitTransaction();
        }
        items.clear();
        notifyDataSetChanged();  // -- 업데이트
        ((MainActivity)mainContext).ShowAlamUi(sort);
    }


    public void clear(){
        items.clear();
        notifyDataSetChanged();  // -- 업데이트
    }




    /*---------------------------------------------------------------------------------------------------------------------------*/
    class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        View mViewContent1;
        View mActionContainer1;
        View background;
        int color;

        public ViewHolder1(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title);
            imageView = itemView.findViewById(R.id.title_image);
            mViewContent1 = itemView.findViewById(R.id.view_list_title_content);
            mActionContainer1 = itemView.findViewById(R.id.view_list_title_action_container);
            background = itemView.findViewById(R.id.title_background);

//            background.setBackgroundColor(color);
//            mViewContent1.setBackgroundColor(color);
//            mActionContainer1.setBackgroundColor(color);

        }
    }
    class ItemSwipeWithActionWidthViewHolder1 extends LocationAdapter.ViewHolder1 implements Extension {
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

    /*------------------------------------------------------------------------------------------------------------------*/
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
    class ItemSwipeWithActionWidthViewHolder2 extends LocationAdapter.ViewHolder2 implements Extension {

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


    /*------------------------------------------------------------------------------------------------------------------*/

    class ViewHolder3 extends RecyclerView.ViewHolder {
        View view;
        public ViewHolder3(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.between);
        }

    }


    class PinHolder extends RecyclerView.ViewHolder {
        View view;
        View padding;

        public PinHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.pin_layout);
            padding = itemView.findViewById(R.id.padding_layout);
            padding.setPadding(0,50,0,0);



        }

    }
}
