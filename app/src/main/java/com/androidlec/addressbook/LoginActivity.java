package com.androidlec.addressbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText et_id, et_password;
    private Button btn_login;
    private TextView tv_register;
    String TAG = "Log Chk : ";
    String urlAddr;
    String uId, uPw;

    private void init() {
        et_id = findViewById(R.id.login_et_id);
        et_password = findViewById(R.id.login_et_password);
        btn_login = findViewById(R.id.login_btn_login);
        tv_register = findViewById(R.id.login_tv_register);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 키보드 화면 가림막기
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 초기화
        init();

        // 클릭 리스너
        btn_login.setOnClickListener(onClickListener);
        tv_register.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "onClick()");

            switch (v.getId()) {
                case R.id.login_btn_login:
                    Toast.makeText(LoginActivity.this, "로그인액션", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    loginChk();
                    break;
                case R.id.login_tv_register:
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    break;
            }
        }
    };

    private void loginChk(){
        Log.v(TAG, "loginChk()");

        uId = et_id.getText().toString();
        uPw = et_password.getText().toString();

        urlAddr = "http://192.168.0.178:8080/Test/loginChk.jsp?";
        urlAddr = urlAddr + "id=" + uId + "&pw=" + uPw;
        Log.v(TAG, urlAddr);

        connectLoginData();
    }

    private void connectLoginData(){
        Log.v(TAG, "connectLoginData()");

        try{
            LJH_LoginNetworkTask loginNetworkTask = new LJH_LoginNetworkTask(LoginActivity.this, urlAddr);
            int loginChk = loginNetworkTask.execute().get();
            if(loginChk == 0){
                Toast.makeText(LoginActivity.this, "회원정보를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
            }else {
                // 로그인 완료.
                loginOk();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loginOk(){
        Log.v(TAG, "loginOk()");
//        Intent intent = new Intent(LoginActivity.this, MAINActivity.class);
//        intent.putExtra("uId", et_id);
//        startActivity(intent);
    }


}//----