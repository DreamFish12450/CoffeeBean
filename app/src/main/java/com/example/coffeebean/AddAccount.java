package com.example.coffeebean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeebean.model.UserInfo;
import com.example.coffeebean.util.UserManage;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class AddAccount extends AppCompatActivity {
    TextView username;
    TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        username = findViewById(R.id.edt_acc_username);
        password = findViewById(R.id.edt_acc_password);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        findViewById(R.id.btn_add_account).setOnClickListener(v -> {
            String resultUsername =username.getText().toString();
            String resultUserpassword = password.getText().toString();
            boolean ischecked = false;

            Log.d("登陆判定", "start");
            UserInfo userInfo = new UserInfo();
            LoginVerifyAsyncTask loginVerifyAsyncTask = new LoginVerifyAsyncTask(this,resultUsername,resultUserpassword);
            loginVerifyAsyncTask.execute();
        });
    }
    @Override

    public void onBackPressed() {
        //数据是使用Intent返回

        Intent intent = new Intent();


        //设置返回数据

        this.setResult(RESULT_CANCELED, intent);

        //关闭Activity

        this.finish();

    }
    public class LoginVerifyAsyncTask extends AsyncTask<String, Void, UserInfo> {
        private String username;
        private String password;
        private LoginDBHelper loginDBHelper;

        private WeakReference<Context> contextWeakReference;

        LoginVerifyAsyncTask(Context context,String username,String password) {
            contextWeakReference = new WeakReference<>(context);
            this.username=username;
            this.password=password;

        }

        @Override
        protected UserInfo doInBackground(String... strings) {
            Context context = contextWeakReference.get();
            if (context != null) {
                try {
                    loginDBHelper=new LoginDBHelper(context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                UserInfo userInfo=loginDBHelper.getUserInfoQueryByName(username);
                return userInfo;

            }
            return null;
        }
        @Override
        protected void onPostExecute(UserInfo userInfo) {
            super.onPostExecute(userInfo);
            Context context = contextWeakReference.get();
            if (userInfo!=null)
                if(userInfo.getPassword().equals(password)){
                    Intent intent = new Intent();
                    //把返回数据存入Intent
                    intent.putExtra("username",userInfo.getUsername() );
                    intent.putExtra("phone",userInfo.getPhone_number());
                    //设置返回数据
                    AddAccount.this.setResult(RESULT_OK, intent);
                    //关闭Activity
                    AddAccount.this.finish();
//                    startActivity(intent);
//                    finish();
                }else {
                    Toast.makeText(context, "登录失败，密码错误", Toast.LENGTH_SHORT).show();
                }
            else {
                Toast.makeText(context, "登录失败，账户不存在", Toast.LENGTH_SHORT).show();
            }
        }
    }
}