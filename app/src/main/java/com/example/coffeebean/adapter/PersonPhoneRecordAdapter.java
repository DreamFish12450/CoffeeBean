package com.example.coffeebean.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeebean.R;
import com.example.coffeebean.model.PhoneRecord;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PersonPhoneRecordAdapter  extends RecyclerView.Adapter<PersonPhoneRecordAdapter.RecyclerHolder> {
    List<PhoneRecord> items = new ArrayList<>();
    private final Context context;

    public PersonPhoneRecordAdapter(RecyclerView recyclerView) {
        recyclerView.setAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        this.context = recyclerView.getContext();
    }

    public void setItems(List<PhoneRecord> items) {
        this.items.clear();
        this.items = items;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.phone_record_cell, parent, false);
        return new RecyclerHolder(view);
    }

    public String intToString(int duration) {
        String content = null;
        if (duration >= 60 * 60) {
            int hour = (int) Math.floor(duration / 3600);
            int minutue = (int) Math.floor((duration - hour * 3600) / 60);
            int seconds = duration - hour * 3600 - minutue * 60;
            String secondContent = minutue != 0? new String(minutue+"分") :"";
            String thirdContent = seconds !=0?new String(seconds+"秒"):"";
            content = hour + "小时" + secondContent+thirdContent;

        } else if (duration >= 60 && duration < 3600) {
            int minutue = (int) Math.floor((duration) / 60);
            int seconds = duration - minutue * 60;
            if (seconds != 0)
                content = minutue + "分" + seconds + "秒";
            else
                content = minutue + "分";
        } else {
            content = duration + "秒";
        }
        return content;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
//        holder.name.setText(items.get(position).getNoteName());
        int duration = items.get(position).getDuration();
        holder.duration.setText(intToString(duration));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm");
        holder.date.setText(simpleDateFormat.format(items.get(position).getDate()));
//        holder.image.setImageURI(Uri.parse(items.get(position).getAvaterUrl()));
        holder.phoneNumber.setText(items.get(position).getPhoneNumber());
        if (items.get(position).getStatus() == 0) {
            holder.status.setImageResource(R.drawable.ic_no_get);
//            holder.date.setTextColor(Color.RED);
//            holder.name.setTextColor(Color.RED);
            holder.phoneNumber.setTextColor(Color.RED);
        } else if (items.get(position).getStatus() == 1) {
            holder.status.setImageResource(R.drawable.ic_phone_get);
        } else if (items.get(position).getStatus() == 2) {
            holder.status.setImageResource(R.drawable.ic_phone_post);
        }

//        holder.image.setImageResource(items.get(position).getImage());
    }

    public void addItem(PhoneRecord item) {
        items.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class RecyclerHolder extends RecyclerView.ViewHolder {

        TextView duration;
        TextView date;
        TextView phoneNumber;
        ImageView status;

        private RecyclerHolder(View itemView) {
            super(itemView);

            duration = itemView.findViewById(R.id.record_duration);
            date = itemView.findViewById(R.id.record_time);
            phoneNumber = itemView.findViewById(R.id.record_phone_content);
            status = itemView.findViewById(R.id.record_get_status);

        }
    }
}

