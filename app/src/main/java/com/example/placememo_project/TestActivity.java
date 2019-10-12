package com.example.placememo_project;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.placememo_project.databinding.ActivityMainBinding;
import com.example.placememo_project.databinding.ActivityTextBinding;
import com.example.placememo_project.databinding.ItemTestItemBinding;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Section;
import com.xwray.groupie.databinding.BindableItem;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    ArrayList<UserItem> arrItems = new ArrayList<>();
    ActivityTextBinding binding;
    GroupAdapter<GroupieViewHolder> adapter = new GroupAdapter<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_text);
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
        item5.setUserName("홍길동");
        arrItems.add(item5);
        Section section = new Section();
        for (int i = 0; i < arrItems.size(); i++) {
            MyHolder item = new MyHolder(arrItems.get(i));
            section.add(item);
        }
        adapter.add(section);
        binding.rvTest.setAdapter(adapter);
        binding.rvTest.setLayoutManager(new LinearLayoutManager(this));

    }
    class MyHolder extends BindableItem<ItemTestItemBinding> {
        UserItem mItem;

        MyHolder(UserItem item) {
            mItem = item;
        }

        @Override
        public void bind(@NonNull ItemTestItemBinding viewBinding, int position) {
            viewBinding.tvUserName.setText(mItem.getUserName());
            viewBinding.tvUserName.setText(arrItems.get(position).getUserName());
        }

        @Override
        public int getLayout() {
            return R.layout.item_test_item;
        }
    }


}