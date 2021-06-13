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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.coffeebean.adapter.AllPhoneRecordAdapter;
import com.example.coffeebean.adapter.PersonPhoneRecordAdapter;
import com.example.coffeebean.model.ContactInfo;
import com.example.coffeebean.model.Group;
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
import java.util.regex.Pattern;

import butterknife.OnClick;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ContactInfoActivity extends BaseActivity implements View.OnClickListener {
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
    Intent intent;
    Intent broadIntent;
    Spinner spinnerGroup;

    boolean isEdit = false;
    private static final int SUCCESS = 1;
    private static final int FAILURE = 0;
    int id;//用于修改
    int group;
    ArrayList<Group> groupInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        PersonPhoneRecordAdapter personPhoneRecordAdapter = new PersonPhoneRecordAdapter(findViewById(R.id.phone_record_view));
        mContext = this;
        phoneRecords = null;
        Intent myIntent = getIntent();
        //读取Intent的值
        TextValue = myIntent.getStringExtra("NoteName");
        contactInfo = null;
        broadIntent = new Intent("action1");

        nameTextView = findViewById(R.id.contact_name);

        homeAddressTextView = findViewById(R.id.contact_home_address);

        workAddressTextView = findViewById(R.id.contact_workplace);

        careerTextView = findViewById(R.id.contact_career);

        phoneNumberTextView = findViewById(R.id.contact_phone_number);

        edit = findViewById(R.id.edit);
        ivHead = findViewById(R.id.tag_cell_icon);
        groupInfo =  ContactDBHelper.getInstance(getApplicationContext()).getAllGroup();
        Log.d("个人信息初始化", TextValue);
        contactInfo = new ContactDBHelper(getApplicationContext()).getContactInfoBynoteName(TextValue);

        Log.d("个人信息初始化", contactInfo.toString());
        broadIntent.putExtra("PreInfoName", contactInfo.getNoteName());
        try {
            phoneRecords =  PhoneRecordDBHelper.getInstance(getApplicationContext()).getPhoneRecordsByName(TextValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        ArrayAdapter<Group> adapterGroup = null;
        spinnerGroup = findViewById(R.id.spinnerGroup_info);

        adapterGroup = new ArrayAdapter<Group>(this,
                android.R.layout.simple_spinner_dropdown_item, groupInfo);
//设置下拉框的数据适配器adapterGroup
        this.spinnerGroup.setAdapter(adapterGroup);

        personPhoneRecordAdapter.setItems(phoneRecords);
        id = contactInfo.getId();
        noteNameTextView = findViewById(R.id.contact_note_name);
        runOnUiThread(()->{
            noteNameTextView.setText(contactInfo.getNoteName());
            nameTextView.setText(contactInfo.getName());
            homeAddressTextView.setText(contactInfo.getHomeAddress());
            workAddressTextView.setText(contactInfo.getWorkAddress());
            careerTextView.setText(contactInfo.getCareer());
            phoneNumberTextView.setText(contactInfo.getPhoneNumber());
        });

        imgPath = contactInfo.getAvaterUri();
        group = contactInfo.getGroup();
        if (contactInfo.getAvaterUri() != null) {
            ivHead.setImageURI(Uri.parse(contactInfo.getAvaterUri()));
        }
        LinearLayout returnback = findViewById(R.id.returnbook);
        ImageView Info_call = findViewById(R.id.Info_call);
//        returnback.setOnClickListener(this);
//        edit.setOnClickListener(this);
//        phoneNumberTextView.setOnClickListener(this);
//        Info_call.setOnClickListener(this);
//        ivHead.setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.returnbook)).setOnClickListener(v -> {
            if (isEdit) {
//                    Intent i1 = new Intent();// 不能进行页面的跳转，只能实例化成这样
//                    intent.setAction("action1");// 设置Intent对象的action属性，以便于在主界面做匹配

                sendBroadcast(broadIntent);// 发送广播
                ContactInfoActivity.this.setResult(SUCCESS, intent);
                Log.d("设置", "Success");
//                    startActivity(intent);
            }
            ContactInfoActivity.this.finish();
        });

        ((TextView)findViewById(R.id.edit)).setOnClickListener((v)->{
            if (edit.getText().equals("编辑")) {
                isEdit = false;
                runOnUiThread(()-> edit.setText("完成"));
                noteNameTextView.setEnabled(true);
                nameTextView.setEnabled(true);
                homeAddressTextView.setEnabled(true);
                workAddressTextView.setEnabled(true);
                careerTextView.setEnabled(true);
                phoneNumberTextView.setEnabled(true);
                isEdit = false;
            } else if (edit.getText().equals("完成")) {
                String regex = "^1[3-9]\\d{9}$";
                boolean bool = Pattern.matches(regex, phoneNumberTextView.getText().toString().trim());
                if (noteNameTextView.getText().toString().length() == 0) {
                    CharSequence cs = "昵称不得为空";
                    Toast.makeText(mContext, cs, Toast.LENGTH_SHORT).show();
                } else if (phoneNumberTextView.getText().toString().trim().equals("") || !bool) {//正则判断
                    CharSequence cs = "电话格式不规范";
                    Toast.makeText(mContext, cs, Toast.LENGTH_SHORT).show();
                }
                runOnUiThread(()->edit.setText("编辑"));
                new Thread() {
                    @Override
                    public void run() {
                        //缺头像修改头像访问相册？
                        contactInfo.setNoteName(noteNameTextView.getText().toString());
                        contactInfo.setName(nameTextView.getText().toString());
                        contactInfo.setHomeAddress(homeAddressTextView.getText().toString());
                        contactInfo.setWorkAddress(workAddressTextView.getText().toString());
                        contactInfo.setCareer(careerTextView.getText().toString());
                        contactInfo.setPhoneNumber(phoneNumberTextView.getText().toString());
                        contactInfo.setAvaterUri(imgPath);
                        contactInfo.setGroup(spinnerGroup.getSelectedItemPosition() + 1);
                         ContactDBHelper.getInstance(getApplicationContext()).updateContactInfo(id, contactInfo);
                        isEdit = true;
                    }
                }.start();
//                while (isEdit == false) {
//                }
                intent = new Intent(this, HomeActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("refreshContactInfo", contactInfo); // 传递对象
                intent.putExtra("bundle", mBundle);
                broadIntent.putExtra("Info", contactInfo);
                noteNameTextView.setEnabled(false);
                nameTextView.setEnabled(false);
                homeAddressTextView.setEnabled(false);
                workAddressTextView.setEnabled(false);
                careerTextView.setEnabled(false);
                phoneNumberTextView.setEnabled(false);
            }
        });
        ((ImageView)findViewById(R.id.Info_call)).setOnClickListener(v -> {
            call(phoneNumberTextView.getText().toString());
        });
        (findViewById(R.id.tag_cell_icon)).setOnClickListener(v->{
            if (edit.getText().equals("完成"))
                changeAvatar(v);
        });
        spinnerGroup.setSelection(contactInfo.getGroup() - 1);
        spinnerGroup.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if ((arg2 + 1) != contactInfo.getGroup()) {
                    isEdit = false;
                    edit.setText("完成");
                    noteNameTextView.setEnabled(true);
                    nameTextView.setEnabled(true);
                    homeAddressTextView.setEnabled(true);
                    workAddressTextView.setEnabled(true);
                    careerTextView.setEnabled(true);
                    phoneNumberTextView.setEnabled(true);
                    isEdit = false;
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        setResult(FAILURE);
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
        switch (v.getId()) {
            case R.id.returnbook:

                break;
            case R.id.edit:


                break;
            case R.id.Info_call:

                break;
            case R.id.tag_cell_icon:

                break;
            default:

                break;
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

}