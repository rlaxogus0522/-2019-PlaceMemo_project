package com.example.placememo_project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.placememo_project.databinding.ActivityTextBinding;
import com.example.placememo_project.databinding.ItemTestItemBinding;
import com.example.placememo_project.databinding.ItemTestTitleBinding;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Section;
import com.xwray.groupie.databinding.BindableItem;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<UserItem> arrItems = new ArrayList<>();
    ActivityTextBinding binding;
    GroupAdapter<GroupieViewHolder> adapter = new GroupAdapter<>();
    Section section = new Section();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_text);
        binding.Buttoon.setOnClickListener(this);
        binding.ButtoonInsert.setOnClickListener(this);
        binding.delete.setOnClickListener(this);
        UserItem item1 = new UserItem();
        item1.setUserName("홍길동");
        arrItems.add(item1);
        UserItem item2 = new UserItem();
        item2.setUserName("이순신");
        arrItems.add(item2);
        UserItem item3 = new UserItem();
        item3.setUserName("장길산");
        arrItems.add(item3);
        UserItem item4 = new UserItem();
        item4.setUserName("각시탈");
        arrItems.add(item4);
        UserItem item5 = new UserItem();
        item5.setUserName("홍마마");
        arrItems.add(item5);

        binding.rvTest.setAdapter(adapter);
        binding.rvTest.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onClick(View view) {






        if(view == binding.Buttoon){
            Section section = new Section();
            TitleHolder title = new TitleHolder(arrItems.get(0));
            section.add(title);
            for (int i = 1; i < arrItems.size(); i++) {
                ItemHolder item = new ItemHolder(arrItems.get(i));
                section.add(item);
            }
            adapter.add(section);
            Toast.makeText(this,""+section.getGroup(0),Toast.LENGTH_LONG).show();

        }





        else if(view == binding.ButtoonInsert){
            UserItem item = new UserItem();
            item.setUserName("쿄쿄쿄쿄");
            arrItems.add(item);
        }else if(view == binding.delete){
            for (int i = 0; i < arrItems.size(); i++) {
                if(arrItems.get(i).getUserName().equals(binding.et.getText().toString())){
                    arrItems.remove(i);
                }
            }

        }
    }

    class ItemHolder extends BindableItem<ItemTestItemBinding> {
        UserItem mItem;

        ItemHolder(UserItem item) {
            mItem = item;
        }

        @Override
        public void bind(@NonNull ItemTestItemBinding viewBinding, int position) {
            viewBinding.tvUserName.setText(mItem.getUserName());
        }

        @Override
        public int getLayout() {
            return R.layout.item_test_item;
        }
    }
    class TitleHolder extends BindableItem<ItemTestTitleBinding> {
        UserItem mItem;

        TitleHolder(UserItem item) {
            mItem = item;
        }

        @Override
        public void bind(@NonNull ItemTestTitleBinding viewBinding, int position) {
            viewBinding.tvTitleName.setText(mItem.getUserName());
        }

        @Override
        public int getLayout() {
            return R.layout.item_test_title;
        }
    }


}