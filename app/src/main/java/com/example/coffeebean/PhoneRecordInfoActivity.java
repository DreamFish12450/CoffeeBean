package com.example.coffeebean;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;


import com.example.coffeebean.model.ContactInfo;

import com.example.coffeebean.model.PhoneRecord;
import com.example.coffeebean.receiver.PhoneBroadcastReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;


public class PhoneRecordInfoActivity extends BaseActivity {

    int TextValue;
    PhoneRecord phoneRecord;
    ContactInfo contactInfo;
    TextView noteNameTextView;
    TextView timeTextView;
    TextView dateTextView;
    TextView phoneView;
    LinearLayout returnhome;
    TextView call;
    TextView removeBlackbook;
    TextView blackbook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_record_info);

        mContext = this;

        Intent myIntent = getIntent();
        //读取Intent的值
        TextValue = myIntent.getIntExtra("PhoneRecord",0);
        phoneRecord = null;
        ivHead = findViewById(R.id.phonerecord_info_icon);
        noteNameTextView = findViewById(R.id.phonerecord_info_name_or_phone);
        returnhome = findViewById(R.id.returnhome);
        timeTextView=findViewById(R.id.phonerecord_info_stauts);
        blackbook=findViewById(R.id.phonerecord_add_blackbook);
        removeBlackbook=findViewById(R.id.phonerecord_remove_blackbook);
//        sendMsg.findViewById(R.id.sendsms);
        call=findViewById(R.id.phonerecord_info_call);
        dateTextView=findViewById(R.id.phonerecord_info_time);
        phoneView=findViewById(R.id.phonerecord_info_phonenum);
        Log.d("通话信息初始化", String.valueOf(TextValue));

        try {
            phoneRecord =  PhoneRecordDBHelper.getInstance(getApplicationContext()).getPhoneRecordsById(TextValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(phoneRecord.getNoteName()!=null&&phoneRecord.getNoteName().length()>0){
            contactInfo = new ContactDBHelper(getApplicationContext()).getContactInfoBynoteName(phoneRecord.getNoteName());

                noteNameTextView.setText(phoneRecord.getNoteName());
                if (contactInfo.getAvaterUri() != null&&contactInfo.getAvaterUri().length()>0) {
                    ivHead.setImageURI(Uri.parse(contactInfo.getAvaterUri()));
                }
            Log.d("个人信息查询", contactInfo.getNoteName());
        }
        phonenum=phoneRecord.getPhoneNumber();

            if (phoneRecord.getStatus()==0) {
                timeTextView.setText("未接");
                timeTextView.setTextColor(Color.RED);
            }
            if (phoneRecord.getStatus()==1){
                if(phoneRecord.getDuration()/60>60)
                timeTextView.setText("接通  "+phoneRecord.getDuration()/3600+"小时"+phoneRecord.getDuration()%3600/60+"分钟"+phoneRecord.getDuration()%60 + "秒");
                else if(phoneRecord.getDuration()>60)
                 timeTextView.setText("接通  "+phoneRecord.getDuration()/60+"分钟"+phoneRecord.getDuration() %60 + "秒");
                else
                    timeTextView.setText("接通  "+phoneRecord.getDuration() + "秒");
            }
            if (phoneRecord.getStatus()==2) {
                if(phoneRecord.getDuration()/60>60)
                    timeTextView.setText("呼出  "+phoneRecord.getDuration()/3600+"小时"+phoneRecord.getDuration()%3600/60+"分钟"+phoneRecord.getDuration()%60 + "秒");
                else if(phoneRecord.getDuration()>60)
                    timeTextView.setText("呼出  "+phoneRecord.getDuration()/60+"分钟"+phoneRecord.getDuration() %60 + "秒");
                else
                    timeTextView.setText("呼出  "+phoneRecord.getDuration()  + "秒");
            }
            String PATTEN_DEFAULT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sf_all = new SimpleDateFormat(PATTEN_DEFAULT_YMDHMS);

            dateTextView.setText(sf_all.format(phoneRecord.getDate()));
            phoneView.setText(phonenum);


        call.setOnClickListener(v -> {
            call(phonenum);
        });

        removeBlackbook.setOnClickListener(v -> {
            PhoneBroadcastReceiver.applicationContext=getApplicationContext();
            boolean flag=true;
            SharedPreferences sp = getApplicationContext().getSharedPreferences("BlackBook", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            Set<String> blackPhone=sp.getStringSet("blackbook",new HashSet<String>());
            Set<String> set=new HashSet<String>();
            set.addAll(blackPhone);
            for(String i:set){
                int cnt=0;
                while(i.charAt(0)==' ') cnt++;
                if(i.substring(cnt).trim().equals(phonenum)){
                    flag=false;
                    set.remove(phonenum);
                    edit.putStringSet("blackbook",set).commit();

                    Toast.makeText(this, "已移除黑名单", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if(flag){
                Toast.makeText(this, "不存在于黑名单", Toast.LENGTH_SHORT).show();
            }

            Log.d("黑名单", String.valueOf(set.size()));
        });
        blackbook.setOnClickListener(v -> {
            PhoneBroadcastReceiver.applicationContext=getApplicationContext();
            boolean flag=true;

            SharedPreferences sp = getApplicationContext().getSharedPreferences("BlackBook", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            Set<String> blackPhone=sp.getStringSet("blackbook",new HashSet<String>());
            Set<String> set=new HashSet<String>();
            set.addAll(blackPhone);
            for(String i:set){
                if(i.equals(phonenum)){
                    flag=false;
                    Toast.makeText(this, "已存在于黑名单", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if(flag){
                Toast.makeText(this, "加入黑名单", Toast.LENGTH_SHORT).show();
                set.add(phonenum);
                edit.putStringSet("blackbook",set).commit();

            }
            Log.d("黑名单", String.valueOf(set.size()));

        });
        returnhome.setOnClickListener(v -> {
            finish();
        });
    }

}

