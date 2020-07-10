package com.androidlec.addressbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class TagOptionDialog extends AppCompatActivity {

    String urlAddr;
    String tag1, tag2, tag3, tag4, tag5, tag6, tag7;
    LJH_data ljh_data; // 아이디값 불러오는 클래스.
    private Button btn_cancel, btn_submit;
    private EditText et_red, et_orange, et_yellow, et_green, et_blue, et_purple, et_gray;

    private void init() {
        btn_cancel = findViewById(R.id.dialog_tag_option_btn_cancel);
        btn_submit = findViewById(R.id.dialog_tag_option_btn_submit);
        et_red = findViewById(R.id.tag_option_et_name_red);
        et_orange = findViewById(R.id.tag_option_et_name_orange);
        et_yellow = findViewById(R.id.tag_option_et_name_yellow);
        et_green = findViewById(R.id.tag_option_et_name_green);
        et_blue = findViewById(R.id.tag_option_et_name_blue);
        et_purple = findViewById(R.id.tag_option_et_name_purple);
        et_gray = findViewById(R.id.tag_option_et_name_gray);

        // 태그 불러오기.
        onTagList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tag_option);

        // 초기화
        init();

        btn_cancel.setOnClickListener(onClickListener);
        btn_submit.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.dialog_tag_option_btn_cancel:
                    finish();
                    break;
                case R.id.dialog_tag_option_btn_submit:
                    onChangeTagName();

                    break;
            }
        }
    };

    private void onTagList() {
        urlAddr = "http://192.168.0.178:8080/Test/tagList.jsp?";
        urlAddr = urlAddr + "id=" + ljh_data.getLoginId();

        // 태그 리스트 불러오기.
        connectTagListData();
    }

    private void onChangeTagName(){
        tag1 = et_red.getText().toString();
        tag2 = et_orange.getText().toString();
        tag3 = et_yellow.getText().toString();
        tag4 = et_green.getText().toString();
        tag5 = et_blue.getText().toString();
        tag6 = et_purple.getText().toString();
        tag7 = et_gray.getText().toString();

        urlAddr = "http://192.168.0.178:8080/Test/tagChange.jsp?";
        urlAddr = urlAddr + "id=" + ljh_data.getLoginId() + "&tag1=" + tag1 + "&tag2=" + tag2 + "&tag3=" + tag3 + "&tag4=" + tag4 + "&tag5=" + tag5 + "&tag6=" + tag6 + "&tag7=" + tag7;

        // 태그명 바꾸기.
        connectTagChangeData();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


    // 태그명 불러오기.
    private void connectTagListData() {
        try {
            LJH_TagNetwork tagListNetworkTask = new LJH_TagNetwork(TagOptionDialog.this, urlAddr);
            Object obj = tagListNetworkTask.execute().get();
            ArrayList<String> tNames = (ArrayList<String>) obj; // cast.

            et_red.setText(tNames.get(0));
            et_orange.setText(tNames.get(1));
            et_yellow.setText(tNames.get(2));
            et_green.setText(tNames.get(3));
            et_blue.setText(tNames.get(4));
            et_purple.setText(tNames.get(5));
            et_gray.setText(tNames.get(6));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 태그명 바꾸기.
    private void connectTagChangeData() {
        try{
            LJH_InsertNetworkTask tagChangeNetworkTask = new LJH_InsertNetworkTask(TagOptionDialog.this, urlAddr);
            tagChangeNetworkTask.execute().get();

        }catch (Exception e){
            e.printStackTrace();
        }
        Toast.makeText(this, "태그명이 변경되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

}//----