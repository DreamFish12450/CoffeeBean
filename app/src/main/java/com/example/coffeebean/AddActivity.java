package com.example.coffeebean;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.coffeebean.adapter.AllPhoneRecordAdapter;
import com.example.coffeebean.adapter.PersonPhoneRecordAdapter;
import com.example.coffeebean.model.ContactInfo;
import com.example.coffeebean.model.Group;
import com.example.coffeebean.model.PhoneRecord;
import com.example.coffeebean.util.BitmapUtils;
import com.example.coffeebean.util.CameraUtils;
import com.example.coffeebean.util.Requests;
import com.example.coffeebean.util.RoundAngleImageView;
import com.example.coffeebean.util.UserManage;
import com.example.coffeebean.util.VolleyRequestUtil;
import com.example.coffeebean.widget.PopWindowView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.OnClick;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddActivity extends BaseActivity implements View.OnClickListener {
    ContactInfo contactInfo;
    String TextValue;
    EditText noteNameTextView;
    EditText nameTextView;
    EditText homeAddressTextView;
    EditText workAddressTextView;
    EditText careerTextView;
    EditText phoneNumberTextView;
    Spinner spinnerGroup;
    TextView add;

    //    //????????????
//    private BottomSheetDialog bottomSheetDialog;
//    //????????????
//    private View bottomView;
//    private String imgPath= Environment.getDataDirectory().getPath()+"/data/com.example.coffeebean/avater.jpg";
    ArrayList<Group> groupInfo = null;
    long id = -1;
    private static final int FAILURE = 0;
    private static final int SUCCESS = 1;

//    //???????????????????????????
//    private File outputImagePath;
//    //??????????????????
//    public static final int TAKE_PHOTO = 1;
//    //??????????????????
//    public static final int SELECT_PHOTO = 2;
//    //??????????????????
//    private boolean hasPermissions = false;
//    //????????????
//    private RxPermissions rxPermissions;
//    //????????????
//    private RoundAngleImageView ivHead;
//    //Base64
//    private String base64Pic;
//    //??????????????????????????????Bitmap
//    private Bitmap orc_bitmap;
//
//    //Glide????????????????????????
//    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
//            .diskCacheStrategy(DiskCacheStrategy.NONE)//??????????????????
//            .skipMemoryCache(true);//??????????????????

    String regex = "^1[3-9]\\d{9}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getClass().getName(), "eneter successfully");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);
        mContext = this;
        ArrayAdapter<Group> adapterGroup = null;
        spinnerGroup = findViewById(R.id.spinnerGroup);
        groupInfo = ContactDBHelper.getInstance(getApplicationContext()).getAllGroup();
        while (groupInfo == null) {
        }
        adapterGroup = new ArrayAdapter<Group>(this,
                android.R.layout.simple_spinner_dropdown_item, groupInfo);
//?????????????????????????????????adapterGroup
        this.spinnerGroup.setAdapter(adapterGroup);

        ivHead = findViewById(R.id.add_contact_img);
        noteNameTextView = findViewById(R.id.add_contact_note_name);
        nameTextView = findViewById(R.id.add_contact_name);
        homeAddressTextView = findViewById(R.id.add_contact_home_address);
        workAddressTextView = findViewById(R.id.add_contact_workplace);
        careerTextView = findViewById(R.id.add_contact_career);
        phoneNumberTextView = findViewById(R.id.add_contact_phone_number);
        add = findViewById(R.id.add_contact);
        displayImage(imgPath);
        LinearLayout returnback = findViewById(R.id.add_returnbook);
        ImageView Info_call = findViewById(R.id.add_contact_img);
        returnback.setOnClickListener(this);
        add.setOnClickListener(this);
        phoneNumberTextView.setOnClickListener(this);
        Info_call.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_returnbook://??????
                setResult(FAILURE);
                finish();
                break;
            case R.id.add_contact://????????????
                ContactInfo contactInfo = new ContactInfo();
                contactInfo.setNoteName(noteNameTextView.getText().toString().trim());
                contactInfo.setName(nameTextView.getText().toString().trim());
                contactInfo.setHomeAddress(homeAddressTextView.getText().toString().trim());
                contactInfo.setWorkAddress(workAddressTextView.getText().toString().trim());
                contactInfo.setCareer(careerTextView.getText().toString().trim());
                contactInfo.setPhoneNumber(phoneNumberTextView.getText().toString().trim());
                contactInfo.setAvaterUri(imgPath);
                contactInfo.setGroup(((Group) spinnerGroup.getSelectedItem()).getGroupID());
                Log.d("groupid", String.valueOf(((Group) spinnerGroup.getSelectedItem()).getGroupID()));
                boolean bool = Pattern.matches(regex, phoneNumberTextView.getText().toString().trim());
                if (noteNameTextView.getText().toString().length() == 0) {
                    CharSequence cs = "??????????????????";
                    Toast.makeText(mContext, cs, Toast.LENGTH_SHORT).show();
                    break;
                } else if (phoneNumberTextView.getText().toString().trim().equals("") || !bool) {//????????????
                    CharSequence cs = "?????????????????????";
                    Toast.makeText(mContext, cs, Toast.LENGTH_SHORT).show();
                    break;
                }
                id = -1;
                new Thread() {
                    @Override
                    public void run() {
                        id = ContactDBHelper.getInstance(getApplicationContext()).insertContactInfo(contactInfo, getApplicationContext());
                    }
                }.start();
                while (id == -1) {
                }
                Intent intent = new Intent();
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("newContactInfo", contactInfo); // ????????????
                intent.putExtra("bundle", mBundle);
                Log.d("?????????", String.valueOf(id));
                if (id > 0) setResult(SUCCESS, intent);
                else setResult(FAILURE);
                this.finish();
                break;
            case R.id.add_contact_img://????????????
                changeAvatar(v);
                break;
            default:

                break;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (AddActivity.this.getCurrentFocus() != null) {
                if (AddActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(AddActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }
//    public void changeAvatar(View view) {
//        bottomSheetDialog = new BottomSheetDialog(this);
//        bottomView = getLayoutInflater().inflate(R.layout.dialog_bottom, null);
//        bottomSheetDialog.setContentView(bottomView);
//        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.WHITE);
//        TextView tvTakePictures = bottomView.findViewById(R.id.tv_take_pictures);
//        TextView tvOpenAlbum = bottomView.findViewById(R.id.tv_open_album);
//        TextView tvCancel = bottomView.findViewById(R.id.tv_cancel);
//
//        //??????
//        tvTakePictures.setOnClickListener(v -> {
//            takePhoto();
//            showMsg("??????");
//            bottomSheetDialog.cancel();
//        });
//        //????????????
//        tvOpenAlbum.setOnClickListener(v -> {
//            openAlbum();
//            showMsg("????????????");
//            bottomSheetDialog.cancel();
//        });
//        //??????
//        tvCancel.setOnClickListener(v -> {
//            bottomSheetDialog.cancel();
//        });
//        bottomSheetDialog.show();
//    }
//    /**
//     * Toast??????
//     *
//     * @param msg
//     */
//    private void showMsg(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//    }
//    /**
//     * ????????????
//     */
//    private void checkVersion() {
//        //Android6.0???????????????
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //???????????????Fragment????????????this??????getActivity()
//            rxPermissions = new RxPermissions(this);
//            //????????????
//            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    .subscribe(granted -> {
//                        if (granted) {//????????????
//                            showMsg("???????????????");
//                        } else {//????????????
//                            showMsg("???????????????");
//                        }
//                    });
//        } else {
//            //Android6.0??????
//            showMsg("????????????????????????");
//        }
//    }
//
//    /**
//     * ??????
//     */
//    private void takePhoto() {
////        if (!hasPermissions) {
////            showMsg("??????????????????");
////            checkVersion();
////            return;
////        }
//        Log.d("changeivHead","startPhoto");
//        SimpleDateFormat timeStampFormat = new SimpleDateFormat(
//                "yyyy_MM_dd_HH_mm_ss");
//        String filename = timeStampFormat.format(new Date());
//        outputImagePath = new File(getExternalCacheDir(),
//                filename + ".jpg");
//        Intent takePhotoIntent = CameraUtils.getTakePhotoIntent(this, outputImagePath);
//        // ??????????????????????????????Activity???????????????TAKE_PHOTO
//        startActivityForResult(takePhotoIntent, TAKE_PHOTO);
//    }
//    /**
//     * ????????????
//     */
//    private void openAlbum() {
////        checkVersion();
//        Log.d("changeivHead","startAlbum");
//        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO);
//    }
//    /**
//     * ?????????Activity
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            //???????????????
//            case TAKE_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    //????????????
//                    displayImage(outputImagePath.getAbsolutePath());
//                }
//                break;
//            //?????????????????????
//            case SELECT_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    String imagePath = null;
//                    //???????????????????????????
//                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//                        //4.4?????????????????????????????????????????????
//                        imagePath = CameraUtils.getImageOnKitKatPath(data, this);
//                    } else {
//                        imagePath = CameraUtils.getImageBeforeKitKatPath(data, this);
//                    }
//                    //????????????
//                    displayImage(imagePath);
//                }
//                break;
//            default:
//                break;
//        }
//    }
//    /**
//     * ??????????????????????????????
//     */
//    private void displayImage(String imagePath) {
//        if (!TextUtils.isEmpty(imagePath)) {
//            //????????????
//            Glide.with(this).load(imagePath).apply(requestOptions).into(ivHead);
//            imgPath=imagePath;
////            //????????????
//            orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath));
////            //???Base64
////            base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap);
//            SimpleDateFormat timeStampFormat = new SimpleDateFormat(
//                    "yyyy_MM_dd_HH_mm_ss");
//            String filename = timeStampFormat.format(new Date());
//            BitmapUtils.saveBitmap(filename,orc_bitmap,mContext);
//        } else {
//            showMsg("??????????????????");
//        }
//    }

}