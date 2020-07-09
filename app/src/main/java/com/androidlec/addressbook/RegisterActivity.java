package com.androidlec.addressbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    String TAG = "Log Chk : ";
    String urlAddr;
    EditText et_regId, et_regPw, et_regPwOk;
    Button regBtn;
    String regId, regPw, regPwOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_regId = findViewById(R.id.register_et_id);
        et_regPw = findViewById(R.id.register_et_password);
        et_regPwOk = findViewById(R.id.register_et_passwordOK);

        regBtn = findViewById(R.id.register_btn_register);
        regBtn.setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.v(TAG, "onClick()");

            blankChk();
        }
    };



    private void blankChk(){

        String idChk = et_regId.getText().toString();
        String pwChk = et_regPw.getText().toString();
        String pwOkChk = et_regPwOk.getText().toString();

        if(idChk.length() == 0){
            // --------------- 대화상자 띄우기 -------------------------------------------------------------
            new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("이메일 주소를 기입해주세요.")
                    .setCancelable(false)
                    .setNegativeButton("확인", null)
                    .show();
            // ---------------------------------------------------------------------------------------
        }else if(pwChk.length() == 0){
            // --------------- 대화상자 띄우기 -------------------------------------------------------------
            new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("비밀번호를 기입해주세요.")
                    .setCancelable(false)
                    .setNegativeButton("확인", null)
                    .show();
            // ---------------------------------------------------------------------------------------
        }else if(pwOkChk.length() == 0){
            // --------------- 대화상자 띄우기 -------------------------------------------------------------
            new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("확인용 비밀번호를 기입해주세요.")
                    .setCancelable(false)
                    .setNegativeButton("확인", null)
                    .show();
            // ---------------------------------------------------------------------------------------
        }else{
            pwChk(); // 패스워드 확인.
        }
    }


    private void pwChk(){
        Log.v(TAG, "pwChk()");

        regPw = et_regPw.getText().toString();
        regPwOk = et_regPwOk.getText().toString();

        if(regPw.equals(regPwOk)){
            Log.v(TAG, "패스워드 일치.");

            idDoubleChk(); // 아이디 중복 체크.
            // connectRegData(); // 회원가입 DB 연결.
        }else {
            Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    private void idDoubleChk(){
        Log.v(TAG, "idDoubleChk()");

        regId = et_regId.getText().toString();

        urlAddr = "http://192.168.0.178:8080/Test/idDoubleChk.jsp?";
        urlAddr = urlAddr + "id=" + regId;
        Log.v(TAG, urlAddr);

        connectIdDoubleChkData();
        Log.v(TAG, "connectIdDoubleChkData()");
    }


    private void connectIdDoubleChkData(){
        Log.v(TAG, "connectLoginData()");

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
        Log.v(TAG, "register()");

        regId = et_regId.getText().toString();
        regPw = et_regPw.getText().toString();

        urlAddr = "http://192.168.0.178:8080/Test/register.jsp?";
        urlAddr = urlAddr + "id=" + regId + "&pw=" + regPw;
        Log.v(TAG, urlAddr);

        connectRegisterData();
        Toast.makeText(RegisterActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();

        et_regId.setText("");
        et_regPw.setText("");
        et_regPwOk.setText("");

        Log.v(TAG, "connectRegisterData()");
    }


    private void connectRegisterData(){ // NetworkTask Async 방식. == permission 줘야 하는구나!
        try {
            LJH_InsertNetworkTask ljhinsertNetworkTask = new LJH_InsertNetworkTask(RegisterActivity.this, urlAddr);
            ljhinsertNetworkTask.execute().get();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}//---