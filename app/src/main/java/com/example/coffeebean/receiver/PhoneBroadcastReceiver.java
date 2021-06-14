package com.example.coffeebean.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.coffeebean.ContactDBHelper;
import com.example.coffeebean.HomeActivity;
import com.example.coffeebean.PhoneRecordDBHelper;
import com.example.coffeebean.model.ContactInfo;
import com.example.coffeebean.model.PhoneRecord;
import com.example.coffeebean.util.PhoneUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class PhoneBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "message";
    private static boolean mIncomingFlag = false;
    private static String mIncomingNumber = null;
    private static Date start_out=null ;//拨出时刻
    private static Date start_in=null ;//来电时刻
    private static Date hang_on=null ;//接通时刻
    private static Date end=null;//挂断时刻
    private static String phoneNumber;
    private static Context context;
    private static int cnt=0;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
// 如果是拨打电话
        Log.d("拦截",intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            start_in = null;
            start_out = new Date(System.currentTimeMillis());
            mIncomingFlag = false;
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.i(TAG, "call OUT:" + phoneNumber);
            TelephonyManager tManager = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            tManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        } else {
            SharedPreferences sp = context.getSharedPreferences("CoffeeBean", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            Set<String> blackPhone=new HashSet<String>();
            sp.getStringSet("blackbook",blackPhone);
            Log.d("开始拦截", String.valueOf(blackPhone.size()));
            for(String i:blackPhone){
                if(i==phoneNumber){
                    PhoneUtils.getInstance().endCall(phoneNumber);
                    Log.d("拦截号码",phoneNumber);
                    break;
                }
            }
// 如果是来电
            TelephonyManager tManager = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            tManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
//            switch (tManager.getCallState()) {
//                case TelephonyManager.CALL_STATE_RINGING:
//                    if(start_out==null) {//来电
//                        start_in = new Date(System.currentTimeMillis());
//                        mIncomingNumber = intent.getStringExtra("incoming_number");
//                    }
//                    Log.i(TAG, "RINGING :" + mIncomingNumber);
//                    break;
//                case TelephonyManager.CALL_STATE_OFFHOOK:
//                    //接听来电
//
//                        Log.i(TAG, "in/outcoming ACCEPT :" + mIncomingNumber);
//
//                    break;
//                case TelephonyManager.CALL_STATE_IDLE:
//                    int status;
//                    int duration;
//                    String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//                    Date date;
//                    if(hang_on==null){//电话未接通
//                        status=0;
//                        duration=0;
//                        if (start_in==null)
//                            date=start_out;
//                        else
//                            date=start_in;
//                        Log.d("未接通","1");
//                    }
//                    else{
//                         duration= (int) (hang_on.getTime()-end.getTime());
//                        if (start_in!=null) {//挂断
//                            Log.i(TAG, "incoming IDLE1");
//                            status=1;
//                            date=start_in;
//
//                            //执行更新保存
//                            start_in=null;
//                        }
//                        else if(start_out!=null) {
//                            Log.i(TAG, "outcoming IDLE2");
//                            status=2;
//                            date=start_out;
//
//                            //执行更新保存
//                            start_out = null;
//                    }
//                        hang_on=null;
//                   }
//                    break;
//            }
        }
    }
    PhoneStateListener listener=new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
//注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE:
                    if(hang_on==null&&start_out!=null)break;
                    end = new Date(System.currentTimeMillis());
                    System.out.println("挂断");
                    Log.d("挂断",incomingNumber);
                    int status;
                    int duration;
                    Date date;
                    PhoneRecord phoneRecord=new PhoneRecord();
                    if(hang_on==null){//电话未接通
                        status=0;
                        duration=0;
                        if (start_in==null)
                            date=start_out;
                        else
                            date=start_in;
                        phoneRecord.setDate(date);
                        phoneRecord.setReceiverId(0);
                        phoneRecord.setDuration(duration);
                        phoneRecord.setStatus(status);
                        phoneRecord.setPhoneNumber(incomingNumber);
                        ContactInfo contactInfo = new ContactDBHelper(context).getContactInfoBytel(incomingNumber);

                        if(contactInfo!=null) {
                            phoneRecord.setNoteName(contactInfo.getNoteName());
                            phoneRecord.setAvaterUrl(contactInfo.getAvaterUri());
                        }
                        else {
                            phoneRecord.setNoteName("");
                            phoneRecord.setAvaterUrl("");
                        }

                        PhoneRecordDBHelper.getInstance(context).insertPhoneRecord(phoneRecord);

                        Log.d("未接通",incomingNumber);
                    }
                    else{
                        duration= (int) (end.getTime()-hang_on.getTime())/1000;
                        if (start_in!=null) {//挂断
                            Log.i(TAG, "incoming IDLE1");
                            status=1;
                            date=start_in;

                            //执行更新保存
                            phoneRecord.setDate(date);
                            phoneRecord.setReceiverId(0);
                            phoneRecord.setDuration(duration);
                            phoneRecord.setStatus(status);
                            phoneRecord.setPhoneNumber(incomingNumber);
                            ContactInfo contactInfo = new ContactDBHelper(context).getContactInfoBytel(incomingNumber);

                            if(contactInfo!=null) {
                                phoneRecord.setNoteName(contactInfo.getNoteName());
                                phoneRecord.setAvaterUrl(contactInfo.getAvaterUri());
                            }
                            else {
                                phoneRecord.setNoteName("");
                                phoneRecord.setAvaterUrl("");
                            }

                            PhoneRecordDBHelper.getInstance(context).insertPhoneRecord(phoneRecord);
                            start_in=null;
                        }
                        else if(start_out!=null) {
                            Log.i(TAG, "outcoming IDLE2");
                            status=2;
                            date=start_out;

                            //执行更新保存

                            phoneRecord.setDate(date);
                            phoneRecord.setReceiverId(0);
                            phoneRecord.setDuration(duration);
                            phoneRecord.setStatus(status);
                            phoneRecord.setPhoneNumber(incomingNumber);
                            ContactInfo contactInfo = new ContactDBHelper(context).getContactInfoBytel(incomingNumber);
                            if(contactInfo!=null) {
                                phoneRecord.setNoteName(contactInfo.getNoteName());
                                phoneRecord.setAvaterUrl(contactInfo.getAvaterUri());
                            }
                            else {
                                phoneRecord.setNoteName("");
                                phoneRecord.setAvaterUrl("");
                            }
                            PhoneRecordDBHelper.getInstance(context).insertPhoneRecord(phoneRecord);
                            start_out = null;
                        }
                        hang_on=null;
                        cnt=0;
                    }
                    Intent broadIntent = new Intent("actionRefreshPL");
                    broadIntent.putExtra("Info2", phoneRecord);
                    context.sendBroadcast(broadIntent);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    System.out.println("接听");
                    Log.d("接听","1");
                    hang_on = new Date(System.currentTimeMillis());
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    System.out.println("响铃:来电号码"+incomingNumber);
                    Log.d("响铃:来电号码","1");
//输出来电号码
                    if(start_out==null) {//来电
                        start_in = new Date(System.currentTimeMillis());
                    }
                    break;
            }
        }
    };
    private boolean getCallLogState() {
        boolean isLink;
        ContentResolver cr = context.getContentResolver();
        final Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI,
                new String[]{CallLog.Calls.NUMBER,CallLog.Calls.TYPE,CallLog.Calls.DURATION},
                CallLog.Calls.NUMBER +"=? and "+CallLog.Calls.TYPE +"= ?",
                new String[]{phoneNumber,"CallLog.Calls.TYPE"},null);
        while(cursor.moveToNext()){
            int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);
            long durationTime = cursor.getLong(durationIndex);
            if(durationTime > 0){
                isLink = true;
            } else {
                isLink = false;
            }
        }
        return false;
    }
}