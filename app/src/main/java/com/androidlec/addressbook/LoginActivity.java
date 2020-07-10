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

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText et_id, et_password;
    private Button btn_login;
    private TextView tv_register;
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
            switch (v.getId()) {
                case R.id.login_btn_login:
                    idPwChk();
                    break;
                case R.id.login_tv_register:
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    break;
            }
        }
    };

    private void idPwChk() {
        String idChk = et_id.getText().toString();
        String pwChk = et_password.getText().toString();

        // 로그인 이메일 포맷체크
        if (!Patterns.EMAIL_ADDRESS.matcher(idChk).matches()) {
            // invalid email pattern set error
            Toast.makeText(LoginActivity.this, "이메일 주소를 확인해주세요.", Toast.LENGTH_SHORT).show();
            et_id.setError("이메일 주소를 확인해주세요.");
            et_id.setFocusable(true);
        } else if (pwChk.length() < 6) {
            Toast.makeText(LoginActivity.this, "6자리 이상의 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            et_password.setError("6자리 이상의 비밀번호를 입력해주세요.");
            et_password.setFocusable(true);
        } else {
            loginChk();
        }
    }


    private void loginChk(){
        uId = et_id.getText().toString();
        uPw = et_password.getText().toString();

        urlAddr = "http://192.168.0.178:8080/Test/loginChk.jsp?";
        urlAddr = urlAddr + "id=" + uId + "&pw=" + uPw;

        connectLoginData();
    }

    private void connectLoginData(){
        try{
            LJH_LoginNetworkTask loginNetworkTask = new LJH_LoginNetworkTask(LoginActivity.this, urlAddr);
            int loginChk = loginNetworkTask.execute().get();
            if(loginChk == 0){
                Toast.makeText(LoginActivity.this, "회원님의 이메일 주소 혹은 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }else {
                // 로그인 완료.
                loginOk();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loginOk(){
        Toast.makeText(LoginActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();


        // 로그인 아이디 넘기기.
        LJH_data ljh_data = new LJH_data();
        ljh_data.setLoginId(et_id.getText().toString());

        // 로그인 스퀸스넘버 넘기기.
        getUSeqno();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
    }


    private void getUSeqno(){
        uId = et_id.getText().toString();

        urlAddr = "http://192.168.0.178:8080/Test/uSeqno.jsp?";
        urlAddr = urlAddr + "id=" + uId;

        connectUSeqnoData();
    }


    private void connectUSeqnoData(){
        try{
            LJH_LoginNetworkTask loginNetworkTask = new LJH_LoginNetworkTask(LoginActivity.this, urlAddr);
            int uSeqno = loginNetworkTask.execute().get();

            LJH_data ljh_data = new LJH_data();
            ljh_data.setLoginSeqno(uSeqno);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



}//----