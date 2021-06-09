package com.example.coffeebean.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.coffeebean.ContactDBHelper;
import com.example.coffeebean.ContactInfoActivity;
import com.example.coffeebean.R;
import com.example.coffeebean.model.ContactInfo;
import com.example.coffeebean.model.Group;
import com.example.coffeebean.util.OnBindCallback;
import com.example.coffeebean.util.RoundAngleImageView;
import com.example.coffeebean.widget.PopWindowView;

import java.io.IOException;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerSwipeAdapter<RecyclerViewAdapter.SimpleViewHolder> {
    private static final int REQUESTCODE_Info = 2;
    //Glide请求图片选项配置
    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存
    private Context mContext;
    private ArrayList<ContactInfo> mDataset;
    public MyItemOnLongClickListener mLongListener;
    public MyItemOnClickListener mListener;
    ArrayList<Group> groupInfo;
    public OnBindCallback onBind;
    public static class SimpleViewHolder extends RecyclerView.ViewHolder {


        SwipeLayout swipeLayout;
        TextView textViewPos;
        ImageView trashView;
        Button buttonDelete;
        TextView tv_item_tag;
        TextView contactInfoName;
        LinearLayout clickView;
        RoundAngleImageView roundAngleImageView;
        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            textViewPos = (TextView) itemView.findViewById(R.id.position);
            trashView = itemView.findViewById(R.id.trash);
            tv_item_tag = (TextView) itemView.findViewById(R.id.tv_item_tag);
            contactInfoName = itemView.findViewById(R.id.contact_info_item_name);
            clickView=itemView.findViewById(R.id.clickView);
            roundAngleImageView=itemView.findViewById(R.id.contact_info__item_avater);

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


    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    public RecyclerViewAdapter(Context context, ArrayList<ContactInfo> objects) {
        this.mContext = context;
        this.mDataset = objects;
        groupInfo=null;
        new Thread(()->{ groupInfo=new ContactDBHelper(mContext).getAllGroup();}).start();
        while (groupInfo==null){}
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {

        ContactInfo item = mDataset.get(position);
        new UpdateAsyncTask(viewHolder.roundAngleImageView,item).execute();
//        if(item.getAvaterUri()!=null) {
//           viewHolder.roundAngleImageView.setImageURI(Uri.parse(item.getAvaterUri()));
//        }
        String letter = String.valueOf(item.getLetter());
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        if (!letterCompareSection(position)) {
            if(mDataset.get(position).getGroup()==1)
            viewHolder.tv_item_tag.setText(letter);
            else viewHolder.tv_item_tag.setText(groupInfo.get(mDataset.get(position).getGroup()-1).getGroupName());
            viewHolder.tv_item_tag.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_item_tag.setVisibility(View.GONE);
        }
        if (onBind != null) {
            onBind.onViewBound(viewHolder, position);
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
                Log.d("Contextname",mContext.getClass().getName());
                Log.d(getClass().getName(),"I DO");
                ((Activity)mContext).startActivityForResult(intent,REQUESTCODE_Info);

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
        if(mListener != null){
            viewHolder.swipeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mListener.onItemOnClick(v, mDataset.get(position));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        //长按

            viewHolder.clickView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    initPopWindow(v, mDataset.get(position));
                    return true;
                }
            });

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
        Boolean result;
        if(mDataset.get(position).getGroup()==1) {
            String letter1 = mDataset.get(position).getLetter();
            String letter2 = mDataset.get(position - 1).getLetter();
             result = letter1.equals(letter2);
        }
        else {
            result=(mDataset.get(position).getGroup()==mDataset.get(position-1).getGroup());
        }
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

    public void setOnItemClickListener(MyItemOnClickListener listener){
        this.mListener = listener;
    }

    public void setOnItemLongClickListener(MyItemOnLongClickListener listener) {
        this.mLongListener = listener;
    }

    public interface MyItemOnClickListener {
        void onItemOnClick(View view, ContactInfo contact) throws IOException;
    }

    public interface MyItemOnLongClickListener {
        void onItemLongClick(View view, ContactInfo contact) throws IOException;
    }
    //生成删除气泡
    private void initPopWindow(View v, final ContactInfo contact) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bubble_dialog, null, false);
        Button btn_xixi = (Button) view.findViewById(R.id.buttonDelete);
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopWindowView popWindow = new PopWindowView(mContext, view);

//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popWindow.getBackground().setAlpha(0);    //要为popWindow设置一个背景才有效

        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showUp2(v, 200, 50);

        //设置popupWindow里的按钮的事件
        btn_xixi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataset.remove(contact);
                //数据库删除
                notifyDataSetChanged();
                popWindow.dismiss();
            }
        });
    }
    //更新图片 内容过大需要异步
    public class UpdateAsyncTask extends AsyncTask<Integer, Integer, Integer> {
        private RoundAngleImageView roundAngleImageView;
        private ContactInfo item;
        public UpdateAsyncTask(RoundAngleImageView roundAngleImageView,ContactInfo item) {
            super();
            this.roundAngleImageView = roundAngleImageView;
            this.item=item;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            return 1;
        }

        protected void onPreExecute() {
            Log.d("tag", "开始执行");
        }
        protected void onPostExecute(Integer result) {
            if(item.getAvaterUri()!=null) {
                roundAngleImageView.setImageURI(Uri.parse(item.getAvaterUri()));
            }
        }
    }

}
