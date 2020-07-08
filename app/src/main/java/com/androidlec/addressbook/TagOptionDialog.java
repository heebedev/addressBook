package com.androidlec.addressbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class TagOptionDialog extends AppCompatActivity {

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

    private void onChangeTagName() {
        // 추가하기
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


}