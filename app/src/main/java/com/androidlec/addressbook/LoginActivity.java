package com.androidlec.addressbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText et_id, et_password;
    Button btn_login;
    TextView tv_register;

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

        init();

        btn_login.setOnClickListener(onClickListener);
        tv_register.setOnClickListener(onClickListener);

        Log.v("LoginActivity", "login");

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_btn_login:
                    Toast.makeText(LoginActivity.this, "로그인액션", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.login_tv_register:
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    break;
            }
        }
    };

}