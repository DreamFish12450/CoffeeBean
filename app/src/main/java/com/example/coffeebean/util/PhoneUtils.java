package com.example.coffeebean.util;

import android.content.Context;
import android.os.IBinder;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class PhoneUtils {
    private static PhoneUtils instance;
    public static PhoneUtils getInstance(){
        if (instance==null)
            synchronized (PhoneUtils.class){
            if (instance==null)instance=new PhoneUtils();
            }
        return instance;
    }

    //挂断电话
    public void endCall(String incomingNumber){
        try {
            Class<?> clazz = Class.forName("android.os.ServiceManager");
            Method method = clazz.getMethod("getService", String.class);
            IBinder ibinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
            ITelephony iTelephony = ITelephony.Stub.asInterface(ibinder);
            iTelephony.endCall();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
