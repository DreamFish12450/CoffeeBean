package com.example.coffeebean;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.coffeebean.model.UserInfo;

public class AddAccount extends AppCompatActivity {
    TextView username;
    TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        username = findViewById(R.id.edt_acc_username);
        password = findViewById(R.id.edt_acc_password)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        findViewById(R.id.btn_add_account).setOnClickListener(v -> {
            String username = new TextView(R.id.edt_acc_username).getText().toString();
            String userpwd = edt_acc_password.getText().toString();
            boolean ischecked = false;
            if (whether_save_auto.isChecked()) ischecked = true;
            Log.d("登陆判定", "start");
            UserInfo userInfo = new UserInfo();
            LoginActivity.LoginAsyncTask loginAsyncTask = new LoginActivity.LoginAsyncTask(LoginActivity.this, username, userpwd, ischecked);
            loginAsyncTask.execute();
        });
    }
}