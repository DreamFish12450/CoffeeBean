package com.example.coffeebean;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.coffeebean.model.UserInfo;
import com.example.coffeebean.util.RequestQueueTon;
import com.example.coffeebean.util.Requests;
import com.example.coffeebean.util.UserManage;
import com.example.coffeebean.util.VolleyRequestUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import static java.sql.Types.NULL;

/**
 * 登录页面

 */
public class LoginActivity extends BaseActivity {
    /**
     * 用户名
     */
    private EditText edt_username;

    /**
     * 密码
     */
    private EditText edt_password;
    private CheckBox whether_save_auto;


    private LoginDBHelper loginDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
//        UserManage.getInstance().delUserInfo(LoginActivity.this);
        Log.d("UserManage", String.valueOf(UserManage.getInstance().hasUserInfo(LoginActivity.this)));
        if (UserManage.getInstance().hasUserInfo(LoginActivity.this)) {

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);//跳转到主页
            startActivity(intent);
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("username") != null) {
                edt_username.setText(bundle.getString("username"));
            }
            if (bundle.getString("password") != null) {
                edt_password.setText(bundle.getString("password"));
            }
            if (bundle.getBoolean("isChecked")) {
                whether_save_auto.setChecked(true);
            }
            if(bundle.getInt("status")==1){
                Intent intent = new Intent("RefreshUiBroadcast");
                sendBroadcast(intent);
            }
        }

    }

    private void initViews() {
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
        whether_save_auto = (CheckBox) findViewById(R.id.save_check);
        findViewById(R.id.btn_login).setOnClickListener(mOnClickListener);
    }

    @Override
    protected void onDestroy() {
        Log.d("loginDestory","onDestory");
        super.onDestroy();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {


        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_login://登录
                    String username = edt_username.getText().toString();
                    String userpwd = edt_password.getText().toString();
                    boolean ischecked = false;
                    if (whether_save_auto.isChecked()) ischecked = true;
                    Log.d("登陆判定", "start");
                    UserInfo userInfo = new UserInfo();
                    LoginAsyncTask loginAsyncTask = new LoginAsyncTask(LoginActivity.this, username, userpwd, ischecked);
                    loginAsyncTask.execute();
////
//                    String url = Requests.API_LOGIN+username;
//
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            VolleyRequestUtil.getInstance(getApplicationContext()).GETJsonObjectRequest(url, new VolleyRequestUtil.VolleyListenerInterface() {
//                                @Override
//                                public Response.Listener<JSONObject> onResponse() {
//                                    //这里需要什么就new什么
//
//                                    return response ->{
//                                        try {
//
//                                            if(response.get("password").equals(userpwd)){
//                                                if(whether_save_auto.isChecked()){
//                                                    UserManage.getInstance().saveUserInfo(LoginActivity.this,null,null);
//                                                    UserManage.getInstance().saveUserInfo(LoginActivity.this, username, userpwd);
//                                                }
//                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);//跳转到主页
//                                                startActivity(intent);
//                                                finish();
//                                            }else {
//                                                Toast.makeText(LoginActivity.this, "登录失败，密码错误", Toast.LENGTH_SHORT).show();
//                                            }
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        Log.d("onResponse", response.toString());
//                                    };
//                                }
//
//                                @Override
//                                public Response.Listener<JSONArray> onResponseArray() {
//                                    return null;
//                                }
//
//                                @Override
//                                public Response.ErrorListener onErr() {
//                                    //这里需要什么就new什么
//                                    return new Response.ErrorListener() {
//                                        @Override
//                                        public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(LoginActivity.this, "登录失败，用户名不存在", Toast.LENGTH_SHORT).show();
//
//                                        }
//                                    };
//                                }
//                            });
//                        }
//                    }.start();


                    break;
                default:
                    break;
            }

        }
    };

    public class LoginAsyncTask extends AsyncTask<String, Void, UserInfo> {
        private String username;
        private String password;
        private int contactId;
        boolean isChecked;
        private WeakReference<Context> contextWeakReference;
        private Context mContext;

        LoginAsyncTask(Context context, String username, String password, boolean ischecked) {
            contextWeakReference = new WeakReference<>(context);
            this.username = username;
            this.password = password;
            this.contactId = contactId;
            isChecked = ischecked;
            mContext = context;
        }

        @Override
        protected UserInfo doInBackground(String... strings) {
            Context context = contextWeakReference.get();
            if (context != null) {
                try {
                    loginDBHelper =  LoginDBHelper.getInstance(context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                UserInfo userInfo = loginDBHelper.getUserInfoQueryByName(username);
                return userInfo;

            }
            return null;
        }

        @Override
        protected void onPostExecute(UserInfo userInfo) {
            super.onPostExecute(userInfo);
            Context context = contextWeakReference.get();
            if (ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED) {//已有权限
            } else {//申请权限
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 1);
            }

            if (userInfo != null)
                if (userInfo.getPassword().equals(password)) {
                    if (isChecked) {
                        Log.d("loginChecked",userInfo.toString());
                        UserManage.getInstance().saveUserInfo(context, username, password,userInfo.getId());
                        UserManage.getInstance().saveUserInfoList(context, username);
                    }
                    Intent intent = new Intent(mContext, HomeActivity.class);//跳转到主页
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(context, "登录失败，密码错误", Toast.LENGTH_SHORT).show();
                }
            else {
                Toast.makeText(context, "登录失败，账户不存在", Toast.LENGTH_SHORT).show();
            }
        }
    }
}