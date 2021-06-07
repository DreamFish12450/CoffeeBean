package com.example.coffeebean.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeebean.ContactInfoActivity;
import com.example.coffeebean.LoginActivity;
import com.example.coffeebean.LoginDBHelper;
import com.example.coffeebean.R;
import com.example.coffeebean.model.PhoneRecord;
import com.example.coffeebean.model.UserInfo;
import com.example.coffeebean.util.UserManage;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PersonInfoAdapter extends RecyclerView.Adapter<PersonInfoAdapter.RecyclerHolder> {
    List<UserInfo> items = new ArrayList<>();
    private static Context context = null;

    public PersonInfoAdapter(RecyclerView recyclerView) {
        recyclerView.setAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        context = recyclerView.getContext();
    }

    public void setItems(List<UserInfo> items) {
        this.items.clear();
        this.items = items;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public PersonInfoAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.person_info_item, parent, false);
        return new PersonInfoAdapter.RecyclerHolder(view);
    }


    @Override
    public void onBindViewHolder(PersonInfoAdapter.RecyclerHolder holder, int position) {
        String username = null;
        if (items != null) {
            holder.username.setText(items.get(position).getUsername());
//        holder.date.setText(SimpleDateFormat.getDateInstance().format(items.get(position).getDate()));
//        holder.image.setImageURI(Uri.parse(items.get(position).getAvaterUrl()));
            holder.phoneNumber.setText(items.get(position).getPhone_number());
            username = items.get(position).getUsername();
        }
        UserInfo userInfo = UserManage.getInstance().getUserInfo(context);
        if (username.equals(userInfo.getUsername())) {
            holder.loginButton.setVisibility(View.GONE);
        }
//        if (items.get(position).getStatus() == 0) {
//            holder.status.setImageResource(R.drawable.ic_no_get);
//            holder.name.setTextColor(Color.RED);
//            holder.phoneNumber.setTextColor(Color.RED);
//        } else if (items.get(position).getStatus() == 1) {
//            holder.status.setImageResource(R.drawable.ic_phone_get);
//        } else if (items.get(position).getStatus() == 2) {
//            holder.status.setImageResource(R.drawable.ic_phone_post);
//        }

//        holder.image.setImageResource(items.get(position).getImage());
    }

    public void addItem(UserInfo item) {

        items.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class RecyclerHolder extends RecyclerView.ViewHolder {
        ImageView avatarImage;
        TextView username;
        TextView phoneNumber;
        Button loginButton;

        private RecyclerHolder(View itemView) {
            super(itemView);
            avatarImage = itemView.findViewById(R.id.person_avatar_image);
            username = itemView.findViewById(R.id.username_text);
            loginButton = itemView.findViewById(R.id.person_login_button);
            phoneNumber = itemView.findViewById(R.id.person_phone_number);
            loginButton.setOnClickListener(v -> {
                final String[] password = {null};
                new Thread(()->{
                    try {
                        UserManage.getInstance().delUserInfo(context);
                        LoginDBHelper loginDBHelper = new LoginDBHelper(context);
                         password[0] = loginDBHelper.getUserInfoQueryByName(username.getText().toString()).getPassword();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("username", username.getText());
                        intent.putExtra("password", password[0]);
                        intent.putExtra("isChecked",true);
                        context.startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }).start();

            });

        }
    }
}
