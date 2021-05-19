package com.example.coffeebean;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.coffeebean.util.handleSSLHandShake;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class CoffeeBeanApplication extends Application {
    private static CoffeeBeanApplication instance;

    @Override
    public void onCreate() {
//        handleSSLHandShake h = new handleSSLHandShake();
        handleSSLHandShake.handleSSLHandshake();
        Log.d(getClass().getName(), "init ca");
        super.onCreate();
        instance = this;
    }
    // 获取Application
    public static Context getMyApplication() {
        return instance;
    }
}