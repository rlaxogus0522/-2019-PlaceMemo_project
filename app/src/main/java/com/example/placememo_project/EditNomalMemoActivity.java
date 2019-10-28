package com.example.placememo_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.placememo_project.databinding.ActivityEditMemoBinding;
import com.example.placememo_project.databinding.ActivityNomalEditMemoBinding;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.MainActivity.mainContext;
import static com.example.placememo_project.MainActivity.sort;

public class EditNomalMemoActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityNomalEditMemoBinding editMemoBinding;
    int color[] = new int[]{0xFFDF8A84, 0xFF8E65D8, 0xFF6CB8DF, 0xFFCBD654, 0xFFE76E97};  //-- 저장된 메모 메뉴에 표시할 색깔 등록해두기
    Button colorButton[] = new Button[5];
    Realm myRealm;
    Data_nomal data_nomals;
    int position;
    int selectColor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        myRealm =  Realm.getDefaultInstance();
        editMemoBinding = DataBindingUtil.setContentView(this, R.layout.activity_nomal_edit_memo);
        Intent intent  = getIntent();
        editMemoBinding.EditMemo.setText(intent.getExtras().getString("memo"));
        data_nomals = myRealm.where(Data_nomal.class).equalTo("memo",intent.getExtras().getString("memo")).findFirst();
        position = intent.getIntExtra("position",-1);
        selectColor = data_nomals.getColor();
        editMemoBinding.btnSave.setOnClickListener(this);
        editMemoBinding.btnBack.setOnClickListener(this);
        editMemoBinding.color1.setOnClickListener(this);
        editMemoBinding.color2.setOnClickListener(this);
        editMemoBinding.color3.setOnClickListener(this);
        editMemoBinding.color4.setOnClickListener(this);
        editMemoBinding.color5.setOnClickListener(this);
        colorButton[0] = editMemoBinding.color1;
        colorButton[1] = editMemoBinding.color2;
        colorButton[2] = editMemoBinding.color3;
        colorButton[3] = editMemoBinding.color4;
        colorButton[4] = editMemoBinding.color5;


    }

    @Override
    public void onClick(View view) {
        if(view == editMemoBinding.btnBack){
            finish();
        }else if(view == editMemoBinding.btnSave){
            myRealm.beginTransaction();
            data_nomals.setMemo(editMemoBinding.EditMemo.getText().toString());
            data_nomals.setColor(selectColor);
            myRealm.commitTransaction();
            ((MainActivity)mainContext).nomaladapters.items.set(position,editMemoBinding.EditMemo.getText().toString());
            ((MainActivity)mainContext).nomaladapters.color.set(position,selectColor);
            ((MainActivity)mainContext).nomaladapters.notifyDataSetChanged();
            finish();
        }else if(view == editMemoBinding.color1){
            selectColor = color[0];
            for (int i = 0; i < colorButton.length ; i++) {
                colorButton[i].setAlpha(0.4f);
            }
            colorButton[0].setAlpha(1.0f);
        }else if(view == editMemoBinding.color2){
            selectColor = color[1];
            for (int i = 0; i < colorButton.length ; i++) {
                colorButton[i].setAlpha(0.4f);
            }
            colorButton[1].setAlpha(1.0f);
        }else if(view == editMemoBinding.color3){
            selectColor = color[2];
            for (int i = 0; i < colorButton.length ; i++) {
                colorButton[i].setAlpha(0.4f);
            }
            colorButton[2].setAlpha(1.0f);
        }else if(view == editMemoBinding.color4){
            selectColor = color[3];
            for (int i = 0; i < colorButton.length ; i++) {
                colorButton[i].setAlpha(0.4f);
            }
            colorButton[3].setAlpha(1.0f);
        }else if(view == editMemoBinding.color5){
            selectColor = color[4];
            for (int i = 0; i < colorButton.length ; i++) {
                colorButton[i].setAlpha(0.4f);
            }
            colorButton[4].setAlpha(1.0f);
        }
    }
}