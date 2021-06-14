package com.example.coffeebean.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeebean.ContactInfoActivity;
import com.example.coffeebean.PhoneRecordInfoActivity;
import com.example.coffeebean.R;
import com.example.coffeebean.model.PhoneRecord;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;


import org.jetbrains.annotations.NotNull;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AllPhoneRecordAdapter extends RecyclerView.Adapter<AllPhoneRecordAdapter.RecyclerHolder> {
    List<PhoneRecord> items = new ArrayList<>();
    private  PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    private  PhoneNumberOfflineGeocoder phoneNumberOfflineGeocoder = PhoneNumberOfflineGeocoder.getInstance();
    String language ="CN";
    Phonenumber.PhoneNumber referencePhonenumber = null;
    private static Context context = null;
    // 默认的年月日的格式. yyyy-MM-dd
    public static final String PATTEN_DEFAULT_YMD = "yyyy-MM-dd";
    public static final String PATTEN_DEFAULT_HMS = "HH:mm:ss";
    public static final String PATTEN_DEFAULT_YMDHMS = "yyyy-MM-dd HH:mm:ss";

    public AllPhoneRecordAdapter(RecyclerView recyclerView) {
        recyclerView.setAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        context = recyclerView.getContext();
//        phoneNumberOfflineGeocoder = PhoneNumberOfflineGeocoder.getInstance();
    }

    public void setItems(List<PhoneRecord> items) {
        this.items.clear();
        this.items = items;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tag_cell, parent, false);
        return new RecyclerHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        Log.d("name",items.get(position).getNoteName()+"123");
        if(items.get(position).getNoteName()!=null&&!items.get(position).getNoteName().equals(""))
            holder.name.setText(items.get(position).getNoteName());
        else
            holder.name.setText("未知来电");
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(PATTEN_DEFAULT_YMD);
        //获取今天的日期
        String nowDay = sf.format(now);
        //获取记录的日期
        SimpleDateFormat sf_day = new SimpleDateFormat(PATTEN_DEFAULT_YMD);
        String day=sf_day.format(items.get(position).getDate());
        //获取时间
        SimpleDateFormat sf_time = new SimpleDateFormat(PATTEN_DEFAULT_HMS);

        SimpleDateFormat sf_all = new SimpleDateFormat(PATTEN_DEFAULT_YMDHMS);
        Log.d("all",sf_all.format(items.get(position).getDate()));
        String times[]=sf_all.format(items.get(position).getDate()).split(" ");

        Log.d("now",nowDay);
        Log.d("this",day);
        if(times[1]!=null)
        Log.d("time",times[1]);
        if(day.equals(nowDay))
           holder.date.setText(times[1]);
        else
           holder.date.setText(day);
//        holder.image.setImageURI(Uri.parse(items.get(position).getAvaterUrl()));
        holder.phoneNumber.setText(items.get(position).getPhoneNumber());
        //根据号码判断归属地
        String phoneNum = items.get(position).getPhoneNumber();

        try {
            referencePhonenumber = phoneUtil.parse(phoneNum, language);
            } catch (NumberParseException e) {
            e.printStackTrace();
        }

        String referenceRegion = phoneNumberOfflineGeocoder.getDescriptionForNumber(referencePhonenumber, Locale.CHINA);
        holder.position.setText(referenceRegion);

        if (items.get(position).getStatus() == 0) {
            holder.status.setImageResource(R.drawable.ic_no_get);
            holder.name.setTextColor(Color.RED);
            holder.phoneNumber.setTextColor(Color.RED);
        } else if (items.get(position).getStatus() == 1) {
            holder.status.setImageResource(R.drawable.ic_phone_get);
        } else if (items.get(position).getStatus() == 2) {
            holder.status.setImageResource(R.drawable.ic_phone_post);
        }
        holder.more.setOnClickListener(v -> {
            Intent intent = new Intent(context, PhoneRecordInfoActivity.class);
            intent.putExtra("PhoneRecord",items.get(position).getRecordId());
            context.startActivity(intent);
        });
        holder.itself.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+items.get(position).getPhoneNumber()));
            context.startActivity(intent);
        });
//        holder.image.setImageResource(items.get(position).getImage());
    }

    public void addItem(PhoneRecord item) {
        items.add(0,item);
        notifyDataSetChanged();
    }
    public List<PhoneRecord> getItems() {
        return items;
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    static class RecyclerHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView date;
        TextView phoneNumber;
        ImageView status;
        ImageView more;
        TextView position;//归属地
        View itself;
        private RecyclerHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tag_cell_name);
            date = itemView.findViewById(R.id.tag_cell_date);
            phoneNumber = itemView.findViewById(R.id.tag_cell_phone_content);
            status = itemView.findViewById(R.id.phone_get_status);
            more = itemView.findViewById(R.id.more);
            position = itemView.findViewById(R.id.tag_cell_phone_position);
            itself=itemView;
//            image.setOnClickListener(v -> {
//                new AlertDialog.Builder(itemView.getContext())
//                        .setTitle("温馨小提示")
//                        .setMessage("主人，将在30分钟后解冻完成哦～")
//                        .create().show();
//                heatIcon.setVisibility(View.GONE);
//            });
        }
    }
}
