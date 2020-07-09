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

    String TAG = "Log Chk : ";
    String urlAddr;
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
//                    onChangeTagName();
                    break;
            }
        }
    };

    private void onTagList() {
        Log.v(TAG, "onTagList()()");

        urlAddr = "http://192.168.0.178:8080/Test/tagList.jsp?";
        urlAddr = urlAddr + "id=" + ljh_data.getLoginId();

        Log.v(TAG, urlAddr);

        // 태그 리스트 불러오기.
        connectTagListData();
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


    // 태그 리스트 불러오기.
    private void connectTagListData() {
        Log.v(TAG, "connectTagListData()");

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
            Log.v(TAG, "tName 입력완료.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}//----