package com.example.coffeebean;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.example.coffeebean.model.OnlineUser;
import com.example.coffeebean.util.Requests;
import com.example.coffeebean.util.UserManage;
import com.example.coffeebean.util.VolleyRequestUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class Shake extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private static final int START_SHAKE = 0x1;
    private static final int AGAIN_SHAKE = 0x2;
    private static final int END_SHAKE = 0x3;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Vibrator mVibrator;//手机震动
    private SoundPool mSoundPool;//摇一摇音效

    //记录摇动状态
    private boolean isShake = false;


    private LinearLayout mTopLayout;
    private LinearLayout mBottomLayout;
    private ImageView mTopLine;
    private ImageView mBottomLine;
    private  ImageView centerImage;
    private MyHandler mHandler;
    private int mWeiChatAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置只竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_shake);
        //初始化View
        initView();
        mHandler = new MyHandler(this);
        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("currentName")!= null){
            Log.d("currentName",bundle.getString("currentName"));

        }

        //初始化SoundPool
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        mWeiChatAudio = mSoundPool.load(this, R.raw.weichat_audio, 1);

        //获取Vibrator震动服务
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        centerImage.setOnClickListener(v->{
            String currentName = bundle.getString("currentName");
            runOnUiThread(()->{
                String url = Requests.API_GET_ONLINE_USER_RANDOM + currentName;
                OnlineUser onlineUser = new OnlineUser();
                VolleyRequestUtil.getInstance(getApplicationContext()).GETJsonArrayRequest(url, new VolleyRequestUtil.VolleyListenerInterface() {
                    @Override
                    public Response.Listener<JSONObject> onResponse() {
                        return null;
                    }

                    @Override
                    public Response.Listener<JSONArray> onResponseArray() {
                        return response -> {
                            try {
//                                Log.d("onResponse", response.toString());
                                JSONArray jsonArr = new JSONArray();
                                jsonArr = response;
                                for (int i = 0; i < jsonArr.length(); i++) {
                                    JSONObject jsonObject = jsonArr.getJSONObject(i);
                                    Log.d("onResponse" + i, jsonArr.getJSONObject(i).toString());
                                    String name = jsonObject.getString("name");
                                    String phoneNumber = jsonObject.getString("phoneNumber");
                                    onlineUser.setName(name);
                                    onlineUser.setPhoneNumber(phoneNumber);
                                    showShakeDialog(onlineUser);
                                    break;
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        };
                    }

                    @Override
                    public Response.ErrorListener onErr() {
                        //这里需要什么就new什么
                        return error -> Log.d("onResponse", error.toString());
                    }
                });

                Log.d(TAG,onlineUser.toString());

            });

        });

    }

    private void initView() {
        centerImage = (ImageView) findViewById(R.id.center_image);
        mTopLayout = (LinearLayout) findViewById(R.id.main_linear_top);
        mBottomLayout = ((LinearLayout) findViewById(R.id.main_linear_bottom));
        mTopLine = (ImageView) findViewById(R.id.main_shake_top_line);
        mBottomLine = (ImageView) findViewById(R.id.main_shake_bottom_line);
        //默认
        mTopLine.setVisibility(View.GONE);
        mBottomLine.setVisibility(View.GONE);


    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取 SensorManager 负责管理传感器
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        if (mSensorManager != null) {
            //获取加速度传感器
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mAccelerometerSensor != null) {
                mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    @Override
    protected void onPause() {
        // 务必要在pause中注销 mSensorManager
        // 否则会造成界面退出后摇一摇依旧生效的bug
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
        super.onPause();
    }



    ///////////////////////////////////////////////////////////////////////////
    // SensorEventListener回调方法
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            //获取三个方向值
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            if ((Math.abs(x) > 17 || Math.abs(y) > 17 || Math
                    .abs(z) > 17) && !isShake) {
                isShake = true;
                Thread thread = new Thread() {
                    @Override
                    public void run() {


                        super.run();
                        try {
                            Log.d(TAG, "onSensorChanged: 摇动");

                            //开始震动 发出提示音 展示动画效果
                            mHandler.obtainMessage(START_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            //再来一次震动提示
                            mHandler.obtainMessage(AGAIN_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            mHandler.obtainMessage(END_SHAKE).sendToTarget();


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    private static class MyHandler extends Handler {
        private WeakReference<Shake> mReference;
        private Shake mActivity;
        public MyHandler(Shake activity) {
            mReference = new WeakReference<Shake>(activity);
            if (mReference != null) {
                mActivity = mReference.get();
            }
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_SHAKE:
                    //This method requires the caller to hold the permission VIBRATE.
                    mActivity.mVibrator.vibrate(300);
                    //发出提示音
                    mActivity.mSoundPool.play(mActivity.mWeiChatAudio, 1, 1, 0, 0, 1);
                    mActivity.mTopLine.setVisibility(View.VISIBLE);
                    mActivity.mBottomLine.setVisibility(View.VISIBLE);
                    mActivity.startAnimation(false);//参数含义: (不是回来) 也就是说两张图片分散开的动画
                    break;
                case AGAIN_SHAKE:
                    mActivity.mVibrator.vibrate(300);
                    break;
                case END_SHAKE:
                    //整体效果结束, 将震动设置为false
                    mActivity.isShake = false;
                    // 展示上下两种图片回来的效果
//                    mActivity.startAnimation(true);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(getClass().getName(),"OnDestory");

        Bundle bundle = getIntent().getExtras();
        if(!bundle.getBoolean("state")){
            String currentName = bundle.getString("currentName");
            runOnUiThread(()->{
                String url = Requests.API_DELETE_USER + "name="+currentName+"&su="+1;
                Log.d("userDelete",url);
                OnlineUser onlineUser = new OnlineUser();
                VolleyRequestUtil.getInstance(this).GETJsonArrayRequest(url, new VolleyRequestUtil.VolleyListenerInterface() {
                    @Override
                    public Response.Listener<JSONObject> onResponse() {
                        return null;
                    }
                    @Override
                    public Response.Listener<JSONArray> onResponseArray() {
                        return response -> {
                            try {
//                                Log.d("onResponse", response.toString());
                                JSONArray jsonArr = new JSONArray();
                                jsonArr = response;
                                for (int i = 0; i < jsonArr.length(); i++) {
                                    JSONObject jsonObject = jsonArr.getJSONObject(i);
                                    Log.d("onResponse" + i, jsonArr.getJSONObject(i).toString());
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        };
                    }

                    @Override
                    public Response.ErrorListener onErr() {
                        return error -> Log.d("onResponse", error.toString());
                    }
                });
            });
        };
        super.onDestroy();
    }

    /**
     * 开启 摇一摇动画
     *
     * @param isBack 是否是返回初识状态
     */
    private void startAnimation(boolean isBack) {
        //动画坐标移动的位置的类型是相对自己的
        int type = Animation.RELATIVE_TO_SELF;

        float topFromY;
        float topToY;
        float bottomFromY;
        float bottomToY;
        if (isBack) {
            topFromY = -0.5f;
            topToY = 0;
            bottomFromY = 0.5f;
            bottomToY = 0;
        } else {
            topFromY = 0;
            topToY = -0.5f;
            bottomFromY = 0;
            bottomToY = 0.5f;
        }

        //上面图片的动画效果
        TranslateAnimation topAnim = new TranslateAnimation(
                type, 0, type, 0, type, topFromY, type, topToY
        );
        topAnim.setDuration(200);
        //动画终止时停留在最后一帧~不然会回到没有执行之前的状态
        topAnim.setFillAfter(true);

        //底部的动画效果
        TranslateAnimation bottomAnim = new TranslateAnimation(
                type, 0, type, 0, type, bottomFromY, type, bottomToY
        );
        bottomAnim.setDuration(200);
        bottomAnim.setFillAfter(true);

        //大家一定不要忘记, 当要回来时, 我们中间的两根线需要GONE掉
        if (isBack) {
            bottomAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    //当动画结束后 , 将中间两条线GONE掉, 不让其占位
                    mTopLine.setVisibility(View.GONE);
                    mBottomLine.setVisibility(View.GONE);
                }
            });
        }
        //设置动画
        mTopLayout.startAnimation(topAnim);
        mBottomLayout.startAnimation(bottomAnim);

    }
    public void showShakeDialog(OnlineUser onlineUser) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        builder.setTitle("用户信息");
        final View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_call, null);
        dialogView.findViewById(R.id.call_button).setOnClickListener(v -> {

        });
        TextView callNameView = dialogView.findViewById(R.id.call_name);
        Log.d(TAG,onlineUser.getName()+onlineUser.getPhoneNumber());
        TextView callPhoneNumber = dialogView.findViewById(R.id.call_phone_number);
        new Thread(()->{
            callNameView.setText(onlineUser.getName());
            callPhoneNumber.setText(onlineUser.getPhoneNumber());
        }).start();

        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton("确认修改", (dialog, id) -> {
                    // sign in the user ...
//                    UserManage.getInstance().delUserInfo(getActivity());
                })
                .setNegativeButton("取消", (dialog, id) -> {
                });
        builder.show();
    }

}
