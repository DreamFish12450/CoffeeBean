package com.example.coffeebean.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeebean.ContactInfoActivity;
import com.example.coffeebean.R;
import com.example.coffeebean.model.ContactInfo;
import com.example.coffeebean.model.PhoneRecord;
import com.example.coffeebean.util.RoundAngleImageView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ContactInfoAdapter extends RecyclerView.Adapter<ContactInfoAdapter.RecyclerHolder> {
    List<ContactInfo> items = new ArrayList<>();
    private final Context context;
//    private final RecyclerView recyclerView = null;
    public ContactInfoAdapter(RecyclerView recyclerView) {
        recyclerView.setAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        this.context = recyclerView.getContext();
    }

//    public ContactInfoAdapter(Context context) {
//        recyclerView.setAdapter(this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
//        this.context = recyclerView.getContext();
//    }


    public void setItems(List<ContactInfo> items) {
        this.items.clear();
        this.items = items;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public ContactInfoAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_info_cell, parent, false);
        return new ContactInfoAdapter.RecyclerHolder(view);
    }



    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(ContactInfoAdapter.RecyclerHolder holder, int position) {

        holder.name.setText(items.get(position).getNoteName());
//        holder.name.setText(items.get(position).getNoteName());
//        int duration = items.get(position).getDuration();
////        holder.duration.setText(intToString(duration));
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm");
//        holder.date.setText(simpleDateFormat.format(items.get(position).getDate()));
////        holder.image.setImageURI(Uri.parse(items.get(position).getAvaterUrl()));
//        holder.phoneNumber.setText(items.get(position).getPhoneNumber());
//        if (items.get(position).getStatus() == 0) {
//            holder.status.setImageResource(R.drawable.ic_no_get);
////            holder.date.setTextColor(Color.RED);
////            holder.name.setTextColor(Color.RED);
//            holder.phoneNumber.setTextColor(Color.RED);
//        } else if (items.get(position).getStatus() == 1) {
//            holder.status.setImageResource(R.drawable.ic_phone_get);
//        } else if (items.get(position).getStatus() == 2) {
//            holder.status.setImageResource(R.drawable.ic_phone_post);
//        }

//        holder.image.setImageResource(items.get(position).getImage());
    }

    public void addItem(ContactInfo item) {
        items.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void removeData(int position) {
        items.remove(items.get(position));
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    public static class RecyclerHolder extends RecyclerView.ViewHolder {

        TextView duration;
        TextView date;
        TextView phoneNumber;
        ImageView status;
        RoundAngleImageView avater;
        TextView name;
        private RecyclerHolder(View itemView) {
            super(itemView);
            avater = itemView.findViewById(R.id.contact_info__item_avater);
            name = itemView.findViewById(R.id.contact_info_item_name);
            avater.setOnClickListener(v->{
                Intent intent = new Intent(itemView.getContext(), ContactInfoActivity.class);
                itemView.getContext().startActivity(intent);
            });
        }


    }
}

