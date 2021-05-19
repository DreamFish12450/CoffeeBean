package com.example.coffeebean.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.example.coffeebean.R;
import com.example.coffeebean.model.PhoneRecord;

import org.jetbrains.annotations.NotNull;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AllPhoneRecordAdapter extends RecyclerView.Adapter<AllPhoneRecordAdapter.RecyclerHolder> {
    List<PhoneRecord> items = new ArrayList<>();
    private static Context context = null;

    public AllPhoneRecordAdapter(RecyclerView recyclerView) {
        recyclerView.setAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        context = recyclerView.getContext();
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
        holder.name.setText(items.get(position).getNoteName());
        holder.date.setText(SimpleDateFormat.getDateInstance().format(items.get(position).getDate()));
//        holder.image.setImageURI(Uri.parse(items.get(position).getAvaterUrl()));
        holder.phoneNumber.setText(items.get(position).getPhoneNumber());
        if (items.get(position).getStatus() == 0) {
            holder.status.setImageResource(R.drawable.ic_no_get);
            holder.name.setTextColor(Color.RED);
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
        ImageView image;
        TextView name;
        TextView date;
        TextView phoneNumber;
        ImageView status;
        ImageView more;

        private RecyclerHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.tag_cell_icon);
            name = itemView.findViewById(R.id.tag_cell_name);
            date = itemView.findViewById(R.id.tag_cell_date);
            phoneNumber = itemView.findViewById(R.id.tag_cell_phone_content);
            status = itemView.findViewById(R.id.phone_get_status);
            more = itemView.findViewById(R.id.more);
            more.setOnClickListener(v -> {
                Intent intent = new Intent(context, ContactInfoActivity.class);
                context.startActivity(intent);
            });
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
