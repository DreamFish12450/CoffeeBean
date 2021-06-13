package com.example.coffeebean;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coffeebean.adapter.MainViewPagerAdapter;
import com.example.coffeebean.model.UserInfo;
import com.example.coffeebean.receiver.PhoneBroadcastReceiver;
import com.example.coffeebean.util.UserManage;
import com.example.coffeebean.util.ViewPager;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    PhoneBroadcastReceiver phoneBroadcastReceiver;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new PhoneBookFragment());
        fragmentArrayList.add(new HomeFragment());
        fragmentArrayList.add(new PersonFragment());
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragmentArrayList);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view1);
        ViewPager viewPager = findViewById(R.id.view_pager);

        viewPager.setAdapter(mainViewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
//                Log.d("ViewPagerID", );
                bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(position).getItemId());
            }

            @Override
            public void onPageScrollStateChanged(int position) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Log.d("ViewPageId", (String.valueOf(menuItem.getItemId())));
            switch (menuItem.getItemId()) {
                case R.id.phone_book_button:
                    viewPager.setCurrentItem(0);
                    Log.d("ViewPageId", "0");
                    return true;
                case R.id.home_button:
                    Log.d("ViewPageId", "1");
                    viewPager.setCurrentItem(1);
                    return true;

                case R.id.person_button:
                    Log.d("ViewPageId", "2");
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return true;
        });
        viewPager.setCurrentItem(1);
        bottomNavigationView.setSelectedItemId(R.id.home_button);
        if (ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED) {//已有权限
        } else {//申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 1);
        }
        phoneBroadcastReceiver=new PhoneBroadcastReceiver();
        IntentFilter intentFilter=new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(phoneBroadcastReceiver,intentFilter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //隐藏软键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (HomeActivity.this.getCurrentFocus() != null) {
                if (HomeActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(HomeActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(getClass().getName(),"I DO");
    }

}