package com.example.coffeebean.util;

import com.example.coffeebean.adapter.ContactInfoAdapter;
import com.example.coffeebean.adapter.RecyclerViewAdapter;

public interface OnBindCallback {
    void onViewBound(RecyclerViewAdapter.SimpleViewHolder viewHolder, int position);
}