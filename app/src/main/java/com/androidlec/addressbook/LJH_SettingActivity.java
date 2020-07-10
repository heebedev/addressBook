package com.androidlec.addressbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class LJH_SettingActivity extends AppCompatActivity {

    private TextInputEditText et_id, et_password;
    private Button btn_change, btn_delete;
    String TAG = "Log Chk : ";
    String urlAddr;
    LJH_data ljh_data;

    private void init() {
        et_id = findViewById(R.id.setting_et_id);
        et_password = findViewById(R.id.setting_et_password);
        btn_change = findViewById(R.id.setting_btn_change);
        btn_delete = findViewById(R.id.setting_btn_delete);

        et_id.setText(ljh_data.getLoginId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // 키보드 화면 가림막기
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 초기화
        init();

        // 클릭 리스너
        btn_change.setOnClickListener(onClickListener);
        btn_delete.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "onClick()");

            switch (v.getId()) {
                case R.id.setting_btn_change:
                    changePw();
                    Toast.makeText(LJH_SettingActivity.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LJH_SettingActivity.this, MainActivity.class));
                    finish();
                    break;
                case R.id.setting_btn_delete:
                    // --------------- 대화상자 띄우기 -------------------------------------------------------------
                    new AlertDialog.Builder(LJH_SettingActivity.this)
                            .setTitle("정말로 탈퇴하시겠습니까?")
                            .setCancelable(false)
                            .setNegativeButton("취소", null)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteId();
                                    startActivity(new Intent(LJH_SettingActivity.this, RegisterActivity.class));
                                    Toast.makeText(LJH_SettingActivity.this, "그동안 이용해주셔서 감사합니다.", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            })
                            .show();
                    // ---------------------------------------------------------------------------------------

                    break;
            }
        }
    };


    private void changePw(){
        Log.v(TAG, "changePw()");

        String newPw = et_password.getText().toString();

        urlAddr = "http://192.168.0.178:8080/Test/changePw.jsp?";
        urlAddr = urlAddr + "id=" + ljh_data.getLoginId() + "&pw=" + newPw;
        Log.v(TAG, urlAddr);

        connectChangePwData();
    }

    private void connectChangePwData(){
        try{
            LJH_InsertNetworkTask changePwNetworkTask = new LJH_InsertNetworkTask(LJH_SettingActivity.this, urlAddr);
            changePwNetworkTask.execute().get();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void deleteId(){
        Log.v(TAG, "changePw()");

        urlAddr = "http://192.168.0.178:8080/Test/deleteId.jsp?";
        urlAddr = urlAddr + "id=" + ljh_data.getLoginId();
        Log.v(TAG, urlAddr);

        connectDeleteIdData();
    }

    private void connectDeleteIdData(){
        try{
            LJH_InsertNetworkTask deleteIdNetworkTask = new LJH_InsertNetworkTask(LJH_SettingActivity.this, urlAddr);
            deleteIdNetworkTask.execute().get();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}//----