package com.example.coffeebean;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class ContactInfoActivity extends BaseActivity implements View.OnClickListener{
    ArrayList<PhoneRecord> phoneRecords;
    ContactInfo contactInfo;
    String TextValue;
    EditText noteNameTextView;
    EditText nameTextView;
    EditText homeAddressTextView;
    EditText workAddressTextView;
    EditText careerTextView;
    EditText phoneNumberTextView;
    TextView edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSoft();
        setContentView(R.layout.activity_contact_info);
        PersonPhoneRecordAdapter personPhoneRecordAdapter = new PersonPhoneRecordAdapter(findViewById(R.id.phone_record_view));
        mContext=this;
         phoneRecords = null;
        Intent myIntent = getIntent();
        //读取Intent的值
        TextValue = myIntent.getStringExtra("NoteName");
         contactInfo = null;
        new Thread(){
            public void run(){
                Log.d("个人信息初始化" , TextValue);
                contactInfo =new ContactDBHelper(getApplicationContext()).getContactInfoQueryByName(TextValue);
                Log.d("个人信息初始化" , contactInfo.getNoteName());
                try {
                    phoneRecords=new PhoneRecordDBHelper(getApplicationContext()).getPhoneRecordsByName(TextValue);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        while(phoneRecords==null){}
        while (contactInfo==null){}
        personPhoneRecordAdapter.setItems(phoneRecords);
        noteNameTextView = findViewById(R.id.contact_note_name);
        noteNameTextView.setText(contactInfo.getNoteName());
        nameTextView = findViewById(R.id.contact_name);
        nameTextView.setText(contactInfo.getName());
        homeAddressTextView = findViewById(R.id.contact_home_address);
        homeAddressTextView.setText(contactInfo.getHomeAddress());
        workAddressTextView = findViewById(R.id.contact_workplace);
        workAddressTextView.setText(contactInfo.getWorkAddress());
        careerTextView = findViewById(R.id.contact_career);
        careerTextView.setText(contactInfo.getCareer());
        phoneNumberTextView = findViewById(R.id.contact_phone_number);
        phoneNumberTextView.setText(contactInfo.getPhoneNumber());
        edit=findViewById(R.id.edit);
        LinearLayout returnback=findViewById(R.id.returnbook);
        ImageView Info_call=findViewById(R.id.Info_call);
        returnback.setOnClickListener(this);
        edit.setOnClickListener(this);
        phoneNumberTextView.setOnClickListener(this);
        Info_call.setOnClickListener(this);
//        String url = Requests.API_GET_ALL_PHONE + "0,soso";
//        String url2 = Requests.API_GET_CONTACT_INFO+"soso";
//        new Thread() {
//            @Override
//            public void run() {
//                VolleyRequestUtil.getInstance(getApplicationContext()).GETJsonArrayRequest(url, new VolleyRequestUtil.VolleyListenerInterface() {
//                    @Override
//                    public Response.Listener<JSONObject> onResponse() {
//                        return null;
//                    }
//
//                    @Override
//                    public Response.Listener<JSONArray> onResponseArray() {
//                        return response -> {
//                            try {
////                                Log.d("onResponse", response.toString());
//                                JSONArray jsonArr = new JSONArray();
//                                jsonArr = response;
//                                for (int i = 0; i < jsonArr.length(); i++) {
//                                    JSONObject jsonObject = jsonArr.getJSONObject(i);
//                                    Log.d("onResponse" + i, jsonArr.getJSONObject(i).toString());
//
//                                    String date = jsonObject.getString("date");
//                                    date = date.replace("T"," ");
//                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                                    Date date1 = simpleDateFormat.parse(date);
//                                    PhoneRecord phoneRecord = new PhoneRecord(
//                                            jsonObject.getString("noteName"), jsonObject.getString("phoneNumber"), jsonObject.getInt("status"), date1, null,jsonObject.getInt("duration"));
//                                    phoneRecords.add(phoneRecord);
//                                }
//                                personPhoneRecordAdapter.setItems(phoneRecords);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        };
//                    }
//
//                    @Override
//                    public Response.ErrorListener onErr() {
//                        //这里需要什么就new什么
//                        return new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.d("onResponse", error.toString());
//                            }
//                        };
//                    }
//                });
//            }
//        }.start();
//
//
//        new Thread() {
//            @Override
//            public void run() {
//                VolleyRequestUtil.getInstance(getApplicationContext()).GETJsonArrayRequest(url2, new VolleyRequestUtil.VolleyListenerInterface() {
//                    @Override
//                    public Response.Listener<JSONObject> onResponse() {
//                        //这里需要什么就new什么
//                        return null;
//                    }
//
//                    @Override
//                    public Response.Listener<JSONArray> onResponseArray() {
//                        return response -> {
//                            try {
////                                Log.d("onResponse", response.toString());
//                                JSONArray jsonArr = new JSONArray();
//                                jsonArr = response;
//                                for (int i = 0; i < jsonArr.length(); i++) {
//                                    JSONObject jsonObject = jsonArr.getJSONObject(i);
//                                    Log.d("sosoResponse" + i, jsonArr.getJSONObject(i).toString());
//                                    TextView noteNameTextView = findViewById(R.id.contact_note_name);
//                                    noteNameTextView.setText(jsonObject.getString("noteName"));
//                                    TextView nameTextView = findViewById(R.id.contact_name);
//                                    nameTextView.setText(jsonObject.getString("name"));
//                                    TextView homeAddressTextView = findViewById(R.id.contact_home_address);
//                                    homeAddressTextView.setText(jsonObject.getString("homeAddress"));
//                                    TextView workAddressTextView = findViewById(R.id.contact_workplace);
//                                    workAddressTextView.setText(jsonObject.getString("workAddress"));
//                                    TextView careerTextView = findViewById(R.id.contact_career);
//                                    careerTextView.setText(jsonObject.getString("career"));
//                                    TextView phoneNumberTextView = findViewById(R.id.contact_phone_number);
//                                    phoneNumberTextView.setText(jsonObject.getString("phoneNumber"));
//
//                                }
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        };
//                    }
//
//                    @Override
//                    public Response.ErrorListener onErr() {
//                        //这里需要什么就new什么
//                        return new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.d("sosoError", error.toString());
//
//                            }
//                        };
//                    }
//                });
//            }
//        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.returnbook:
                finish();
                break;
            case R.id.edit:
                if(edit.getText().equals("编辑")) {
                    edit.setText("完成");
                    noteNameTextView.setEnabled(true);
                    nameTextView.setEnabled(true);
                    homeAddressTextView.setEnabled(true);
                    workAddressTextView.setEnabled(true);
                    careerTextView.setEnabled(true);
                    phoneNumberTextView.setEnabled(true);
                }
                else if(edit.getText().equals("完成")){
                    edit.setText("编辑");
                    noteNameTextView.setEnabled(false);
                    nameTextView.setEnabled(false);
                    homeAddressTextView.setEnabled(false);
                    workAddressTextView.setEnabled(false);
                    careerTextView.setEnabled(false);
                    phoneNumberTextView.setEnabled(false);
                }
                break;
            case R.id.Info_call:
                call(phoneNumberTextView.getText().toString());
                break;
            default:
                hideSoft();
                break;
        }
    }

    private void hideSoft(){
        try{
            InputMethodManager imm=(InputMethodManager)ContactInfoActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ContactInfoActivity.this.getWindow().getDecorView().getWindowToken(),0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (ContactInfoActivity.this.getCurrentFocus() != null) {
                if (ContactInfoActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(ContactInfoActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }
    @Override
    public void call(String telPhone) {
        phonenum=telPhone;
        if (checkReadPermission(Manifest.permission.CALL_PHONE, REQUEST_CALL_PERMISSION)) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(telPhone));
            ContactInfoActivity.this.startActivity(intent);
        }
    }
}