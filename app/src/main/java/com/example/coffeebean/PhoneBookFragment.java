package com.example.coffeebean;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.example.coffeebean.adapter.AllPhoneRecordAdapter;
import com.example.coffeebean.adapter.ContactInfoAdapter;
import com.example.coffeebean.adapter.RecyclerViewAdapter;
import com.example.coffeebean.model.ContactInfo;
import com.example.coffeebean.model.PhoneRecord;
import com.example.coffeebean.util.CharacterParser;
import com.example.coffeebean.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhoneBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhoneBookFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private EditText editText;
    private TextView cancelView;
    private ListView listView;
    ArrayList<ContactInfo> contactInfos;
    //phonelist
    SimpleAdapter adapter;
    ArrayList<ContactInfo> ContactInfosList_searched;
    LinearLayout outLayout;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PhoneBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhoneBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhoneBookFragment newInstance(String param1, String param2) {
        PhoneBookFragment fragment = new PhoneBookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_phone_book, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            ActionBar actionBar = getActionBar();
//            if (actionBar != null) {
//                actionBar.setTitle("RecyclerView");
//            }
//        }

        // Layout Managers:
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Item Decorator:
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
//        recyclerView.setItemAnimator(new FadeInLeftAnimator());
        contactInfos = null;
        new Thread(){
            @Override
            public void run(){

                contactInfos=new ContactDBHelper(getContext()).getAllContactInfos();

                contactInfos.add(new ContactInfo("陈桂君"));
                contactInfos.add(new ContactInfo("农宣宣"));
            }
        }.start();

        while (contactInfos==null){}
        // Adapter:
        mAdapter = new RecyclerViewAdapter(getActivity(), contactInfos);
        ((RecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mAdapter);
        ContactInfosList_searched=new ArrayList<ContactInfo>();
        /* Listeners */
        recyclerView.setOnScrollListener(onScrollListener);
        listView=root.findViewById(R.id.listview);
        cancelView=root.findViewById(R.id.text_cancel);
        editText=root.findViewById(R.id.edittext);
        outLayout=root.findViewById(R.id.search_out);
//        outLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(view.getId()!=R.id.edittext&&motionEvent.getAction()==MotionEvent.ACTION_DOWN){
//                    editText.clearFocus();
//                    hideSoft();
//                    cancelView.setVisibility(View.INVISIBLE);
//                }
//                return false;
//            }
//        });
//        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//
//            }
//        });
//        outLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                editText.clearFocus();
//                hideSoft();
//                cancelView.setVisibility(View.INVISIBLE);
//            }
//        });
        editText.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cancelView.setVisibility(View.VISIBLE);
            }//文本改变之前执行

            @Override
            //文本改变的时候执行
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果长度为0
                if (s.length() == 0) {
                    //隐藏
                   listView.setVisibility(View.GONE);
                } else {//长度不为0
                        //更新显示ListView
                    listView.setVisibility(View.VISIBLE);
                    String str=s.toString();
                    showSearchList(str);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setVisibility(View.GONE);
                cancelView.setVisibility(View.INVISIBLE);
                editText.setText("");
                hideSoft();
                editText.clearFocus();
            }
        });

        return root;
    }
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e("ListView", "onScrollStateChanged");
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };
    public void showSearchList(String str){
        adapter=null;
        new Thread(){
            @Override
            public void run(){
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                ArrayList<ContactInfo> ContactInfosList = new ContactDBHelper(getContext()).getAllContactInfos();
                CharacterParser.search(str,ContactInfosList,ContactInfosList_searched);
                Log.d("模糊搜索开始", String.valueOf(ContactInfosList.size()));
                for(ContactInfo i:ContactInfosList_searched){
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("title", i.getNoteName());
                    map.put("info", i.getPhoneNumber());
                    list.add(map);
                }
                Log.d("模糊搜索结束", String.valueOf(list.size()));
//                list=new ContactDBHelper(getContext()).getLikeContactInfos(str);
                adapter = new SimpleAdapter(getContext(),list,R.layout.search_list,
                        new String[]{"title","info"},
                        new int[]{R.id.title,R.id.info}){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final int p=position;
                        final View view=super.getView(position, convertView, parent);
                        TextView useName=(TextView)view.findViewById(R.id.title);
                        useName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //启动个人页面
                                Log.d("itemclicked","search");
                                Intent intent = new Intent();

                                intent.setClass(getContext(), ContactInfoActivity.class);
                                //把一个值写入到Intent中
                                intent.putExtra("NoteName", useName.getText());
                                //启动另一个activity
                                startActivity(intent);
                            }
                        });
                        return view;
                    }
                };

            }
        }.start();
        while(adapter==null){}
        listView.setAdapter(adapter);
        Log.d("模糊搜索初始化", "111");
    }
    private void hideSoft(){
        try{
            InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(),0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}