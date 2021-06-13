package com.example.coffeebean;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coffeebean.util.UserManage;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.Arrays;
import java.util.List;

public class DemoActivity extends AppCompatActivity {
    private static final String TAG = "DemoActivity";
    ActionBar actionBar;
    private SlidingUpPanelLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
//        setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(PanelState.COLLAPSED);
            }
        });
//        if (UserManage.getInstance().hasUserInfo(DemoActivity.this)) {
//            Intent intent = new Intent(DemoActivity.this, HomeActivity.class);
//            startActivity(intent);
//        }
        findViewById(R.id.login_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DemoActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.register_btn).setOnClickListener(v -> {
            new Thread() {
                @Override
                public void run() {
                    try {
                        // read from agconnect-services.json
                        String appId = AGConnectServicesConfig.fromContext(getApplicationContext()).getString("client/app_id");
                        String token = HmsInstanceId.getInstance(getApplicationContext()).getToken(appId, "HCM");
                        Log.i(TAG, "get token:" + token);
                        // 1. After a token is obtained by using the getToken method, null judgment must be performed.
                        // 2. In the outer code segment of the getToken method, you need to add exception capture processing.
//                        if (!TextUtils.isEmpty(token)) {
//                            sendRegTokenToServer(token);
//                        }

//                        mtokenInforView.append("\n" +"get token:" + token);
                    } catch (ApiException e) {
                        Log.e(TAG, "get token failed, " + e);
//                        mtokenInforView.append("\n" +"get token failed, " + e);
                    }
                }
            }.start();
        });


    }


}
