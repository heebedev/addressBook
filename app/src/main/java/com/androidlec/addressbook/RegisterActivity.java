package com.androidlec.addressbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    String urlAddr;
    String urlAddrTag;
    EditText et_regId, et_regPw, et_regPwOk;
    Button regBtn;
    String regId, regPw, regPwOk;
    TextView backLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_regId = findViewById(R.id.register_et_id);
        et_regPw = findViewById(R.id.register_et_password);
        et_regPwOk = findViewById(R.id.register_et_passwordOK);

        regBtn = findViewById(R.id.register_btn_register);
        regBtn.setOnClickListener(onClickListener);

        backLogin = findViewById(R.id.register_tv_login);
        backLogin.setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.register_tv_login:
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.register_btn_register:
                    blankChk();
                    break;
            }

        }
    };


    private void blankChk() {

        String idChk = et_regId.getText().toString();
        String pwChk = et_regPw.getText().toString();

        // 회원가입 이메일 포맷체크, 비밀번호 6자리체크
        if (!Patterns.EMAIL_ADDRESS.matcher(idChk).matches()) {
            // set error and focus to email edittext
            // --------------- 대화상자 띄우기 ---------------------------------------------------------
            new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("올바른 형식의 이메일 주소를 설정해주세요.")
                    .setCancelable(false)
                    .setNegativeButton("확인", null)
                    .show();
            // -------------------------------------------------------------------------------------
            et_regId.setError("Invalid Email");
            et_regId.setFocusable(true);
        } else if (pwChk.length() < 6) {
            // --------------- 대화상자 띄우기 ---------------------------------------------------------
            new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("비밀번호를 6자 이상으로 설정해주세요.")
                    .setCancelable(false)
                    .setNegativeButton("확인", null)
                    .show();
            // -------------------------------------------------------------------------------------
            et_regPw.setError("Password length at least 6 characters");
            et_regPw.setFocusable(true);
        } else {
            pwChk(); // 패스워드 확인.
        }
    }


    private void pwChk(){
        regPw = et_regPw.getText().toString();
        regPwOk = et_regPwOk.getText().toString();

        if(regPw.equals(regPwOk)){

            idDoubleChk(); // 아이디 중복 체크.
            // connectRegData(); // 회원가입 DB 연결.
        }else {
            // --------------- 대화상자 띄우기 ---------------------------------------------------------
            new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("비밀번호가 일치하지 않습니다.")
                    .setCancelable(false)
                    .setNegativeButton("확인", null)
                    .show();
            // -------------------------------------------------------------------------------------
        }
    }


    private void idDoubleChk(){
        regId = et_regId.getText().toString();

        urlAddr = "http://192.168.0.178:8080/Test/idDoubleChk.jsp?";
        urlAddr = urlAddr + "id=" + regId;

        connectIdDoubleChkData();
    }


    private void connectIdDoubleChkData(){
        try{
            LJH_LoginNetworkTask idDoubleChkNetworkTask = new LJH_LoginNetworkTask(RegisterActivity.this, urlAddr);
            int idDoubleChk = idDoubleChkNetworkTask.execute().get();

            if(idDoubleChk == 1){
                Toast.makeText(RegisterActivity.this, "이미 존재하는 이메일 입니다.", Toast.LENGTH_SHORT).show();
            }else {
                // 아이디 중복체크 완료.
                register(); // 회원가입.
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void register(){
        regId = et_regId.getText().toString();
        regPw = et_regPw.getText().toString();

        urlAddr = "http://192.168.0.178:8080/Test/register.jsp?";
        urlAddr = urlAddr + "id=" + regId + "&pw=" + regPw;

        urlAddrTag = "";
        urlAddrTag = "http://192.168.0.178:8080/Test/tagInsert.jsp?id=" + regId;

        connectRegisterData();
        connectTagInsertData();

        Toast.makeText(RegisterActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();

        et_regId.setText("");
        et_regPw.setText("");
        et_regPwOk.setText("");

        finish();
    }


    private void connectRegisterData(){ // NetworkTask Async 방식. == permission 줘야 하는구나!
        try {
            LJH_InsertNetworkTask insertNetworkTask = new LJH_InsertNetworkTask(RegisterActivity.this, urlAddr);
            insertNetworkTask.execute().get();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void connectTagInsertData(){
        try {
            LJH_InsertNetworkTask tagInsertNetworkTask = new LJH_InsertNetworkTask(RegisterActivity.this, urlAddrTag);
            tagInsertNetworkTask.execute().get();

        }catch (Exception e){
            e.printStackTrace();
        }
    }



}//---