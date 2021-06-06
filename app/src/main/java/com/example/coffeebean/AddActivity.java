package com.example.coffeebean;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.coffeebean.adapter.AllPhoneRecordAdapter;
import com.example.coffeebean.adapter.PersonPhoneRecordAdapter;
import com.example.coffeebean.model.ContactInfo;
import com.example.coffeebean.model.PhoneRecord;
import com.example.coffeebean.util.Requests;
import com.example.coffeebean.util.UserManage;
import com.example.coffeebean.util.VolleyRequestUtil;
import com.example.coffeebean.widget.PopWindowView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.OnClick;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddActivity extends BaseActivity implements View.OnClickListener{
    ContactInfo contactInfo;
    String TextValue;
    EditText noteNameTextView;
    EditText nameTextView;
    EditText homeAddressTextView;
    EditText workAddressTextView;
    EditText careerTextView;
    EditText phoneNumberTextView;
    TextView add;
    long id=-1;
    private static final int FAILURE = 0;
    private static final int SUCCESS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);
        mContext=this;

        noteNameTextView = findViewById(R.id.add_contact_note_name);
        nameTextView = findViewById(R.id.add_contact_name);
        homeAddressTextView = findViewById(R.id.add_contact_home_address);
        workAddressTextView = findViewById(R.id.add_contact_workplace);
        careerTextView = findViewById(R.id.add_contact_career);
        phoneNumberTextView = findViewById(R.id.add_contact_phone_number);
        add=findViewById(R.id.add_contact);
        LinearLayout returnback=findViewById(R.id.add_returnbook);
        ImageView Info_call=findViewById(R.id.add_contact_img);
        returnback.setOnClickListener(this);
        add.setOnClickListener(this);
        phoneNumberTextView.setOnClickListener(this);
        Info_call.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_returnbook://返回
                finish();
                break;
            case R.id.add_contact://新增用户
                ContactInfo contactInfo=new ContactInfo();
                contactInfo.setNoteName(noteNameTextView.getText().toString());
                contactInfo.setName(nameTextView.getText().toString());
                contactInfo.setHomeAddress(homeAddressTextView.getText().toString());
                contactInfo.setWorkAddress(workAddressTextView.getText().toString());
                contactInfo.setCareer(careerTextView.getText().toString());
                contactInfo.setPhoneNumber(phoneNumberTextView.getText().toString());
                id=-1;
                new Thread(){
                    @Override
                    public void run() {
                        id =new ContactDBHelper(getApplicationContext()).insertContactInfo(contactInfo);
                    }
                }.start();
                while(id==-1){}
                Intent intent = new Intent();
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("newContactInfo", contactInfo); // 传递对象
                intent.putExtra("bundle",mBundle);
                Log.d("返回值",String.valueOf(id));
                if(id>0)setResult(SUCCESS,intent);
                else setResult(FAILURE);
                this.finish();
                break;
            case R.id.change_img://修改头像

                break;
            default:

                break;
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (AddActivity.this.getCurrentFocus() != null) {
                if (AddActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(AddActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }

}