package com.example.placememo_project.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placememo_project.dbData.Data_alam;
import com.example.placememo_project.adapter.item.LocationMemo_item;
import com.example.placememo_project.R;
import com.example.placememo_project.activity.MainActivity;
import com.loopeer.itemtouchhelperextension.Extension;

import java.util.ArrayList;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.activity.MainActivity.mainContext;
import static com.example.placememo_project.activity.MainActivity.sort;

public class LocationMemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<LocationMemo_item> items = new ArrayList<>();
    public Realm myRealm;
    private static final int VIEW_TYPE_A = 0;
    private static final int VIEW_TYPE_B = 1;
    private static final int VIEW_TYPE_C = 2;
    private static final int VIEW_TYPE_D = -1; // -- view 타입을 4개로 구분 (핀 / 메뉴 / 메모 / 위치별 메모 단락을 짓기위한 구분용 투명 View )
    Context mcontext;


    public LocationMemoAdapter(Context context) {
        this.mcontext = context;
        Realm.init(context);
        myRealm = Realm.getDefaultInstance();
    }

    @NonNull
    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getType().equals("Pin")) {
            return VIEW_TYPE_D;
        } else if (items.get(position).getType().equals("Title")) {  // -- 전달받은 값에서 Type을 구분
            return VIEW_TYPE_A;
        } else if (items.get(position).getType().equals("Memo")) {
            return VIEW_TYPE_B;
        } else {
            return VIEW_TYPE_C;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_D) {
            Log.d("==onCreateViewHolder", items.size() + "");
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pin, viewGroup, false);
            return new PinHolder(v);
        } else if (viewType == VIEW_TYPE_A) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.title_background, viewGroup, false);


            return new ItemSwipeWithActionWidthTitleHolder(v);
        } else if (viewType == VIEW_TYPE_B) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.memo_background, viewGroup, false);
            return new ItemSwipeWithActionWidthMemoHolder(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_to_item, viewGroup, false);
            return new TransParentHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        Data_alam data_alams = myRealm.where(Data_alam.class).equalTo("name", items.get(position).getTitle()).findFirst();
        if (viewHolder instanceof ItemSwipeWithActionWidthTitleHolder) { // 타이틀에 대한 설정
            ((ItemSwipeWithActionWidthTitleHolder) viewHolder).background.setBackgroundColor(data_alams.getColor());
            ((ItemSwipeWithActionWidthTitleHolder) viewHolder).mViewContent1.setBackgroundColor(data_alams.getColor());
            ((ItemSwipeWithActionWidthTitleHolder) viewHolder).mActionContainer1.setBackgroundColor(data_alams.getColor());
            ((ItemSwipeWithActionWidthTitleHolder) viewHolder).mActionViewDelete1.setOnClickListener(new View.OnClickListener() {  //-- 메뉴에 삭제버튼을 클릭한다면
                @Override
                public void onClick(View view) {
                    if (((ItemSwipeWithActionWidthTitleHolder) viewHolder).mViewContent1.getTranslationX() <= -((((ItemSwipeWithActionWidthTitleHolder) viewHolder).mActionContainer1.getWidth()) / 2f)) {
                        remove(position, "Title");  //-- 해당 메뉴에 해당하는 메모 전부 삭제
//                    titlename.remove(getTitle());
                        ((ItemSwipeWithActionWidthTitleHolder) viewHolder).mViewContent1.setTranslationX(0f);
                    }
                }
            });
            ((ItemSwipeWithActionWidthTitleHolder) viewHolder).imageButton.setOnClickListener(new View.OnClickListener() {  //-- 메뉴에 메모추가버튼을 클릭한다면
                @Override
                public void onClick(View view) { //-- 타이틀에 + 버튼을 눌렀을때
                    if (((ItemSwipeWithActionWidthTitleHolder) viewHolder).mViewContent1.getTranslationX() == 0f) {
                        ((MainActivity) mainContext).startTitleAddItem(items.get(position).getTitle());
                    }
                }
            });

            ((ItemSwipeWithActionWidthTitleHolder) viewHolder).mActionViewEdit1.setOnClickListener(new View.OnClickListener() { //-- 메뉴에 편집버튼을 클릭한다면
                @Override
                public void onClick(View view) {
                    if (((ItemSwipeWithActionWidthTitleHolder) viewHolder).mViewContent1.getTranslationX() <= -((((ItemSwipeWithActionWidthTitleHolder) viewHolder).mActionContainer1.getWidth()) / 2f)) {
                        ((MainActivity) mainContext).startLocationEdit(items.get(position).getTitle(), ((ItemSwipeWithActionWidthTitleHolder) viewHolder).mViewContent1, "title");
                    }
                }
            });
            ((ItemSwipeWithActionWidthTitleHolder) viewHolder).textView.setText(items.get(position).getTitle());
            ((ItemSwipeWithActionWidthTitleHolder) viewHolder).imageView.setImageResource(items.get(position).getIcon());


        } else if (viewHolder instanceof ItemSwipeWithActionWidthMemoHolder) { // 메모에 대한 설정
            Data_alam data_alam = myRealm.where(Data_alam.class).equalTo("memo", items.get(position).getMemo()).equalTo("name", items.get(position).getTitle()).findFirst();
            if (data_alam.getisAlamOn()) {
                ((ItemSwipeWithActionWidthMemoHolder) viewHolder).imageButton.setImageResource(R.drawable.baseline_notifications_active_white_48dp);
            } else {
                ((ItemSwipeWithActionWidthMemoHolder) viewHolder).imageButton.setImageResource(R.drawable.baseline_notifications_off_white_48dp);
            }
            ((ItemSwipeWithActionWidthMemoHolder) viewHolder).mActionViewDelete2.setEnabled(false);
            ((ItemSwipeWithActionWidthMemoHolder) viewHolder).mActionViewDelete2.setOnClickListener(new View.OnClickListener() {  //-- 메모에 삭제버튼을 클릭한다면
                @Override
                public void onClick(View view) {
                    if (((ItemSwipeWithActionWidthMemoHolder) viewHolder).mViewContent2.getTranslationX() <= -((((ItemSwipeWithActionWidthMemoHolder) viewHolder).mActionContainer2.getWidth()) / 2f)) {
                        remove(position, "Memo");
                        ((ItemSwipeWithActionWidthMemoHolder) viewHolder).mViewContent2.setTranslationX(0f);
                    }
                }
            });
            ((ItemSwipeWithActionWidthMemoHolder) viewHolder).mActionViewEdit2.setOnClickListener(new View.OnClickListener() { //-- 메모에 편집버튼을 클릭한다면
                @Override
                public void onClick(View view) {
                    if (((ItemSwipeWithActionWidthMemoHolder) viewHolder).mViewContent2.getTranslationX() <= -((((ItemSwipeWithActionWidthMemoHolder) viewHolder).mActionContainer2.getWidth()) / 2f)) {
                        ((MainActivity) mainContext).startLocationEdit(items.get(position).getMemo(), ((ItemSwipeWithActionWidthMemoHolder) viewHolder).mViewContent2, "memo");
                    }
                }
            });
            ((ItemSwipeWithActionWidthMemoHolder) viewHolder).imageButton.setOnClickListener(new View.OnClickListener() {   //-- 메모에 알람 종 모양을 클릭하면
                @Override
                public void onClick(View view) {
                    if (((ItemSwipeWithActionWidthMemoHolder) viewHolder).mViewContent2.getTranslationX() == 0f) {
                        RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("memo", items.get(position).getMemo()).equalTo("name", items.get(position).getTitle()).findAll();
                        if (Objects.requireNonNull(data_alams.first()).getisAlamOn()) {
                            ((ItemSwipeWithActionWidthMemoHolder) viewHolder).imageButton.setImageResource(R.drawable.baseline_notifications_off_white_48dp);
                            myRealm.beginTransaction();
                            Objects.requireNonNull(data_alams.first()).setAlamOn(false);
                            myRealm.commitTransaction();
                        } else {
                            ((ItemSwipeWithActionWidthMemoHolder) viewHolder).imageButton.setImageResource(R.drawable.baseline_notifications_active_white_48dp);
                            myRealm.beginTransaction();
                            Objects.requireNonNull(data_alams.first()).setAlamOn(true);
                            myRealm.commitTransaction();
                            ((MainActivity) mainContext).locationSerch(mcontext);
                        }
                    }
                }
            });

            ((ItemSwipeWithActionWidthMemoHolder) viewHolder).textView2.setText(items.get(position).getMemo());
        } else if (viewHolder instanceof LocationMemoAdapter.PinHolder) { // 핀에 디자인
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadii(new float[]{200, 200, 200, 200, 0, 0, 0, 0});
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
        ((MainActivity) mainContext).checkNomalNoImage();
    }

    public void remove(int position, String type) {
        if (type.equals("Title")) {
            RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("name", items.get(position).getTitle()).findAll();
            myRealm.beginTransaction();
            data_alams.deleteAllFromRealm();
            myRealm.commitTransaction();
        } else if (type.equals("Memo")) {
            Data_alam data_alams = myRealm.where(Data_alam.class).equalTo("name", items.get(position).getTitle()).equalTo("memo", items.get(position).getMemo()).findFirst();
            myRealm.beginTransaction();
            data_alams.deleteFromRealm();
            myRealm.commitTransaction();
        }
        items.clear();
        notifyDataSetChanged();  // -- 업데이트
        ((MainActivity) mainContext).ShowAlamUi(sort);
    }


    public void clear() {
        items.clear();
        notifyDataSetChanged();  // -- 업데이트
    }


    /*---------------------------------------------------------------------------------------------------------------------------*/
    public class TitleHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;
        public ImageButton imageButton;
        public View mViewContent1;
        public View mActionContainer1;
        public View background;


        public TitleHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title);
            imageView = itemView.findViewById(R.id.title_image);
            imageButton = itemView.findViewById(R.id.title_add_item);
            mViewContent1 = itemView.findViewById(R.id.view_list_title_content);
            mActionContainer1 = itemView.findViewById(R.id.view_list_title_action_container);
            background = itemView.findViewById(R.id.title_background);

        }
    }

    public class ItemSwipeWithActionWidthTitleHolder extends TitleHolder implements Extension {
        public View mActionViewDelete1;
        public View mActionViewEdit1;


        public ItemSwipeWithActionWidthTitleHolder(View itemView) {
            super(itemView);
            mActionViewDelete1 = itemView.findViewById(R.id.view_list_title_action_delete);
            mActionViewEdit1 = itemView.findViewById(R.id.view_list_title_action_edit);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer1.getWidth();
        }
    }

    /*------------------------------------------------------------------------------------------------------------------*/
    public class MemoHolder extends RecyclerView.ViewHolder {
        public TextView textView2;
        public ImageView imageButton;
        public View mViewContent2;
        public View mActionContainer2;

        public MemoHolder(View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.alam);
            textView2 = itemView.findViewById(R.id.text_list_main_title);
            mViewContent2 = itemView.findViewById(R.id.view_list_main_content);
            mActionContainer2 = itemView.findViewById(R.id.view_list_repo_action_container);
        }

    }

    public class ItemSwipeWithActionWidthMemoHolder extends MemoHolder implements Extension {

        public View mActionViewDelete2;
        public View mActionViewEdit2;

        public ItemSwipeWithActionWidthMemoHolder(View itemView) {
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




    public class PinHolder extends RecyclerView.ViewHolder {
        public View view;
        public View padding;

        public PinHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.pin_layout);
            padding = itemView.findViewById(R.id.padding_layout);
            padding.setPadding(0, 50, 0, 0);


        }

    }


    public class TransParentHolder extends RecyclerView.ViewHolder {
        public View view;

        public TransParentHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.between);
        }

    }
}
