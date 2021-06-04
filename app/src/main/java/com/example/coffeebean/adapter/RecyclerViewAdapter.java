package com.example.coffeebean.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.coffeebean.ContactInfoActivity;
import com.example.coffeebean.R;
import com.example.coffeebean.model.ContactInfo;

import java.io.IOException;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerSwipeAdapter<RecyclerViewAdapter.SimpleViewHolder> {

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {

        SwipeLayout swipeLayout;
        TextView textViewPos;
        ImageView trashView;
        Button buttonDelete;
        TextView tv_item_tag;
        TextView contactInfoName;
        LinearLayout clickView;
        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            textViewPos = (TextView) itemView.findViewById(R.id.position);
            trashView = itemView.findViewById(R.id.trash);
            tv_item_tag = (TextView) itemView.findViewById(R.id.tv_item_tag);
            contactInfoName = itemView.findViewById(R.id.contact_info_item_name);
            clickView=itemView.findViewById(R.id.clickView);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.d(getClass().getSimpleName(), "onItemSelected: " + contactInfoName.getText().toString());
//                    Intent intent = new Intent();
//
//                    intent.setClass(mContext, ContactInfoActivity.class);
//                    //把一个值写入到Intent中
//                    intent.putExtra("NoteName", viewHolder.contactInfoName.getText());
//                    //启动另一个activity
//                    mContext.startActivity(intent);
//                    Toast.makeText(view.getContext(), "onItemSelected: " + textViewData.getText().toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }

    private Context mContext;
    private ArrayList<ContactInfo> mDataset;
    public MyItemOnClickListener mListener;
    public MyItemOnLongClickListener mLongListener;

    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    public RecyclerViewAdapter(Context context, ArrayList<ContactInfo> objects) {
        this.mContext = context;
        this.mDataset = objects;


    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {

        ContactInfo item = mDataset.get(position);
        String letter = String.valueOf(item.getLetter());
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        if (!letterCompareSection(position)) {
            viewHolder.tv_item_tag.setText(letter);
            viewHolder.tv_item_tag.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_item_tag.setVisibility(View.GONE);
        }
        viewHolder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(getClass().getSimpleName(), "onItemSelected: " + viewHolder.contactInfoName.getText().toString());
                    Intent intent = new Intent();

                    intent.setClass(mContext, ContactInfoActivity.class);
                    //把一个值写入到Intent中
                    intent.putExtra("NoteName", viewHolder.contactInfoName.getText());
                    //启动另一个activity
                    mContext.startActivity(intent);
            }
        });
        viewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
                Log.d("itemdoubleclicked","search");
//                Intent intent = new Intent();
//
//                intent.setClass(mContext, ContactInfoActivity.class);
//                //把一个值写入到Intent中
//                intent.putExtra("NoteName", viewHolder.contactInfoName.getText());
//                //启动另一个activity
//                mContext.startActivity(intent);
            }
        });
//        if(mListener != null){
//            viewHolder.swipeLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        mListener.onItemOnClick(v, mDataset.get(position));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
        //长按
//        if(mLongListener != null){
//            viewHolder.swipeLayout.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    try {
//                        mLongListener.onItemLongClick(v, mDataset.get(position));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return true;
//                }
//            });
//        }
        viewHolder.trashView.setOnClickListener(v->{
            showNormalDialog(mDataset.get(position).getNoteName());
            mItemManger.removeShownLayouts(viewHolder.swipeLayout);
            mDataset.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mDataset.size());
            mItemManger.closeAllItems();
        });
//        viewHolder.textViewPos.setText((position + 1) + ".");
        viewHolder.contactInfoName.setText(item.getNoteName());
        mItemManger.bindView(viewHolder.itemView, position);
    }
    private Boolean letterCompareSection(int position) {
        if (position == 0) {
            return false;
        }
        String letter1 = mDataset.get(position).getLetter();
        String letter2 = mDataset.get(position - 1 ).getLetter();
        Boolean result = letter1.equals(letter2);
        return result;
    }
    private void showNormalDialog(String noteName){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(mContext);

        normalDialog.setTitle("提示");
        normalDialog.setMessage("你确认要删除"+noteName+"嘛?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CharSequence cs="您已成功删除"+noteName+"联系人嘛";
                        Toast.makeText(mContext,cs,Toast.LENGTH_SHORT).show();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
    public interface MyItemOnClickListener {
        void onItemOnClick(View view, ContactInfo contact) throws IOException;
    }

    public interface MyItemOnLongClickListener {
        void onItemLongClick(View view, ContactInfo contact) throws IOException;
    }
}