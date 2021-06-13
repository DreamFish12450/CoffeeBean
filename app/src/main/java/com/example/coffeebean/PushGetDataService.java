package com.example.coffeebean;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

import java.util.Arrays;
import java.util.Map;

public class PushGetDataService extends HmsMessageService {
    private static final String TAG = "PushGetDataService";

    // This method callback must be completed in 10 seconds. Otherwise, you need to start a new Job for callback processing.
    // extends HmsMessageService super class
    @Override
    public void onNewToken(String token) {
        Log.i(TAG, "received refresh token:" + token);
        // send the token to your app server.
        if (!TextUtils.isEmpty(token)) {
            refreshedTokenToServer(token);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Log.i(TAG, "onMessageReceived is called");
        if (message == null) {
            Log.e(TAG, "Received message entity is null!");
            return;
        }

        Log.i(TAG, "getCollapseKey: " + message.getCollapseKey()
                + "\n getData: " + message.getData()
                + "\n getFrom: " + message.getFrom()
                + "\n getTo: " + message.getTo()
                + "\n getMessageId: " + message.getMessageId()
                + "\n getSendTime: " + message.getSentTime()
                + "\n getDataMap: " + message.getDataOfMap()
                + "\n getMessageType: " + message.getMessageType()
                + "\n getTtl: " + message.getTtl()
                + "\n getToken: " + message.getToken());
        Map<String,String> map= message.getDataOfMap();


        RemoteMessage.Notification notification = message.getNotification();
        if (notification != null) {
            Log.i(TAG, "\n getImageUrl: " + notification.getImageUrl()
                    + "\n getTitle: " + notification.getTitle()
                    + "\n getTitleLocalizationKey: " + notification.getTitleLocalizationKey()
                    + "\n getTitleLocalizationArgs: " + Arrays.toString(notification.getTitleLocalizationArgs())
                    + "\n getBody: " + notification.getBody()
                    + "\n getBodyLocalizationKey: " + notification.getBodyLocalizationKey()
                    + "\n getBodyLocalizationArgs: " + Arrays.toString(notification.getBodyLocalizationArgs())
                    + "\n getIcon: " + notification.getIcon()
                    + "\n getSound: " + notification.getSound()
                    + "\n getTag: " + notification.getTag()
                    + "\n getColor: " + notification.getColor()
                    + "\n getClickAction: " + notification.getClickAction()
                    + "\n getChannelId: " + notification.getChannelId()
                    + "\n getLink: " + notification.getLink()
                    + "\n getNotifyId: " + notification.getNotifyId());
        }
        Boolean judgeWhetherIn10s = false;
        // If the messages are not processed in 10 seconds, the app needs to use WorkManager for processing.
        if (judgeWhetherIn10s) {
            Log.d(getClass().getName(),map.get("user"));
            if(map.get("user")!=null){
                Intent BroadIntent = new Intent("addUser");
                BroadIntent.putExtra("user",map.get("user"));
                sendBroadcast(BroadIntent);
            }
            startWorkManagerJob(message);
        } else {
            Log.d(getClass().getName(),map.get("user"));
            if(map.get("user")!=null){
                Intent BroadIntent = new Intent("addUser");
                BroadIntent.putExtra("user",map.get("user"));
                sendBroadcast(BroadIntent);
            }
            // Process message within 10s
            processWithin10s(message);
        }
    }
    private void startWorkManagerJob(RemoteMessage message) {
        Log.d(TAG, "Start new job processing.");
    }
    private void processWithin10s(RemoteMessage message) {
        Log.d(TAG, "Processing now.");
    }

    private void refreshedTokenToServer(String token) {
        Log.i(TAG, "sending token to server. token:" + token);
    }
}
