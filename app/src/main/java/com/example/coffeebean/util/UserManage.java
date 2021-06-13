package com.example.coffeebean.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;


import com.example.coffeebean.model.UserInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 保存用户信息的管理类
 * Created by libin
 */

public class UserManage {

    private static UserManage instance;

    private UserManage() {
    }

    public static UserManage getInstance() {
        if (instance == null) {
            instance = new UserManage();
        }
        return instance;
    }


    /**
     * 保存自动登录的用户信息
     */
    public void saveUserInfo(Context context, String username, String password,int id) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USERNAME", username);
        editor.putString("PASSWORD", password);
        editor.putInt("ID",id);
        editor.apply();
    }

    /**
     * 清除自动登录的用户信息
     */
    public void delUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 获取用户信息model
     *
     * @param context
     * @param
     * @param
     */
    public UserInfo getUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(sp.getString("USERNAME", ""));
        userInfo.setPassword(sp.getString("PASSWORD", ""));
        userInfo.setId(sp.getInt("ID", 0));
        return userInfo;
    }

    public List<String> getUserInfoList(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfoList", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        Gson gson = new Gson();
        String jsonTextPre = sp.getString("key", null);
        String[] text = new String[10];
        if (jsonTextPre != null)
            text = gson.fromJson(jsonTextPre, String[].class);
        else text = null;
        if (text != null)
            return Arrays.asList(text);
        else return null;
    }

    public void saveUserInfoList(Context context, String username) {
        SharedPreferences sp = context.getSharedPreferences("userInfoList", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        List<String> textList =  new ArrayList<>();
        if (sp.getString("key", null) != null) {
            String jsonTextPre = sp.getString("key", null);
            String[] text = gson.fromJson(jsonTextPre, String[].class);
            Log.d("UserInfoList", String.valueOf(text));
            textList = new ArrayList<> (Arrays.asList(text));
        }
//        textList = new ArrayList<>(textList);
        textList.add(username);
        String jsonText = gson.toJson(textList);
        editor.putString("key", jsonText);
        editor.apply();

    }

    public void clearUserInfoList(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfoList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }


    /**
     * userInfo中是否有数据
     */
    public boolean hasUserInfo(Context context) {
        UserInfo userInfo = getUserInfo(context);
        if (userInfo != null) {
            if ((!TextUtils.isEmpty(userInfo.getUsername())) && (!TextUtils.isEmpty(userInfo.getPassword()))) {//有数据
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}