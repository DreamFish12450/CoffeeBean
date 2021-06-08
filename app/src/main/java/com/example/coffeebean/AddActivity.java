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

public class AddActivity extends BaseActivity implements View.OnClickListener{
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

//    //底部弹窗
//    private BottomSheetDialog bottomSheetDialog;
//    //弹窗视图
//    private View bottomView;
//    private String imgPath= Environment.getDataDirectory().getPath()+"/data/com.example.coffeebean/avater.jpg";
    ArrayList<Group> groupInfo=null;
    long id=-1;
    private static final int FAILURE = 0;
    private static final int SUCCESS = 1;

//    //存储拍完照后的图片
//    private File outputImagePath;
//    //启动相机标识
//    public static final int TAKE_PHOTO = 1;
//    //启动相册标识
//    public static final int SELECT_PHOTO = 2;
//    //是否拥有权限
//    private boolean hasPermissions = false;
//    //权限请求
//    private RxPermissions rxPermissions;
//    //图片控件
//    private RoundAngleImageView ivHead;
//    //Base64
//    private String base64Pic;
//    //拍照和相册获取图片的Bitmap
//    private Bitmap orc_bitmap;
//
//    //Glide请求图片选项配置
//    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
//            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
//            .skipMemoryCache(true);//不做内存缓存

    String regex = "0\\d{2,3}[-]?\\d{7,8}|0\\d{2,3}\\s?\\d{7,8}|13[0-9]\\d{8}|15[1089]\\d{8}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);
        mContext=this;
        ArrayAdapter<Group> adapterCity = null;
        spinnerGroup=findViewById(R.id.spinnerGroup);
        groupInfo=new ContactDBHelper(getApplicationContext()).getAllGroup();
        while (groupInfo==null){}
        adapterCity = new ArrayAdapter<Group>(this,
                android.R.layout.simple_spinner_dropdown_item, groupInfo);
//设置下拉框的数据适配器adapterCity
        this.spinnerGroup.setAdapter(adapterCity);

        ivHead=findViewById(R.id.add_contact_img);
        noteNameTextView = findViewById(R.id.add_contact_note_name);
        nameTextView = findViewById(R.id.add_contact_name);
        homeAddressTextView = findViewById(R.id.add_contact_home_address);
        workAddressTextView = findViewById(R.id.add_contact_workplace);
        careerTextView = findViewById(R.id.add_contact_career);
        phoneNumberTextView = findViewById(R.id.add_contact_phone_number);
        add=findViewById(R.id.add_contact);
        displayImage(imgPath);
        LinearLayout returnback=findViewById(R.id.add_returnbook);
        ImageView Info_call=findViewById(R.id.add_contact_img);
        returnback.setOnClickListener(this);
        add.setOnClickListener(this);
        phoneNumberTextView.setOnClickListener(this);
        Info_call.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_returnbook://返回
                setResult(FAILURE);
                finish();
                break;
            case R.id.add_contact://新增用户
                ContactInfo contactInfo=new ContactInfo();
                contactInfo.setNoteName(noteNameTextView.getText().toString().trim());
                contactInfo.setName(nameTextView.getText().toString().trim());
                contactInfo.setHomeAddress(homeAddressTextView.getText().toString().trim());
                contactInfo.setWorkAddress(workAddressTextView.getText().toString().trim());
                contactInfo.setCareer(careerTextView.getText().toString().trim());
                contactInfo.setPhoneNumber(phoneNumberTextView.getText().toString().trim());
                contactInfo.setAvaterUri(imgPath);
                contactInfo.setGroup( ((Group)spinnerGroup.getSelectedItem()).getGroupID());
                Log.d("groupid",String.valueOf(((Group)spinnerGroup.getSelectedItem()).getGroupID()));
                Pattern pattern = Pattern.compile(regex);    // 编译正则表达式
                Matcher matcher = pattern.matcher(phoneNumberTextView.getText().toString().trim());    // 创建给定输入模式的匹配器
                boolean bool = matcher.matches();
                if(noteNameTextView.getText().toString().length()==0){
                    CharSequence cs="昵称不得为空";
                    Toast.makeText(mContext,cs,Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(phoneNumberTextView.getText().toString().trim().equals("")||!bool){//正则判断
                    CharSequence cs="电话格式不规范";
                    Toast.makeText(mContext,cs,Toast.LENGTH_SHORT).show();
                    break;
                }
                id=-1;
                new Thread(){
                    @Override
                    public void run() {
                        id =new ContactDBHelper(getApplicationContext()).insertContactInfo(contactInfo);
                    }
                }.start();
                while(id==-1){}
                Intent intent = new Intent();
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("newContactInfo", contactInfo); // 传递对象
                intent.putExtra("bundle",mBundle);
                Log.d("返回值",String.valueOf(id));
                if(id>0)setResult(SUCCESS,intent);
                else setResult(FAILURE);
                this.finish();
                break;
            case R.id.add_contact_img://修改头像
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
//        //拍照
//        tvTakePictures.setOnClickListener(v -> {
//            takePhoto();
//            showMsg("拍照");
//            bottomSheetDialog.cancel();
//        });
//        //打开相册
//        tvOpenAlbum.setOnClickListener(v -> {
//            openAlbum();
//            showMsg("打开相册");
//            bottomSheetDialog.cancel();
//        });
//        //取消
//        tvCancel.setOnClickListener(v -> {
//            bottomSheetDialog.cancel();
//        });
//        bottomSheetDialog.show();
//    }
//    /**
//     * Toast提示
//     *
//     * @param msg
//     */
//    private void showMsg(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//    }
//    /**
//     * 检查版本
//     */
//    private void checkVersion() {
//        //Android6.0及以上版本
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //如果你是在Fragment中，则把this换成getActivity()
//            rxPermissions = new RxPermissions(this);
//            //权限请求
//            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    .subscribe(granted -> {
//                        if (granted) {//申请成功
//                            showMsg("已获取权限");
//                        } else {//申请失败
//                            showMsg("权限未开启");
//                        }
//                    });
//        } else {
//            //Android6.0以下
//            showMsg("无需请求动态权限");
//        }
//    }
//
//    /**
//     * 拍照
//     */
//    private void takePhoto() {
////        if (!hasPermissions) {
////            showMsg("未获取到权限");
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
//        // 开启一个带有返回值的Activity，请求码为TAKE_PHOTO
//        startActivityForResult(takePhotoIntent, TAKE_PHOTO);
//    }
//    /**
//     * 打开相册
//     */
//    private void openAlbum() {
////        checkVersion();
//        Log.d("changeivHead","startAlbum");
//        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO);
//    }
//    /**
//     * 返回到Activity
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            //拍照后返回
//            case TAKE_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    //显示图片
//                    displayImage(outputImagePath.getAbsolutePath());
//                }
//                break;
//            //打开相册后返回
//            case SELECT_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    String imagePath = null;
//                    //判断手机系统版本号
//                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//                        //4.4及以上系统使用这个方法处理图片
//                        imagePath = CameraUtils.getImageOnKitKatPath(data, this);
//                    } else {
//                        imagePath = CameraUtils.getImageBeforeKitKatPath(data, this);
//                    }
//                    //显示图片
//                    displayImage(imagePath);
//                }
//                break;
//            default:
//                break;
//        }
//    }
//    /**
//     * 通过图片路径显示图片
//     */
//    private void displayImage(String imagePath) {
//        if (!TextUtils.isEmpty(imagePath)) {
//            //显示图片
//            Glide.with(this).load(imagePath).apply(requestOptions).into(ivHead);
//            imgPath=imagePath;
////            //压缩图片
//            orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath));
////            //转Base64
////            base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap);
//            SimpleDateFormat timeStampFormat = new SimpleDateFormat(
//                    "yyyy_MM_dd_HH_mm_ss");
//            String filename = timeStampFormat.format(new Date());
//            BitmapUtils.saveBitmap(filename,orc_bitmap,mContext);
//        } else {
//            showMsg("图片获取失败");
//        }
//    }

}