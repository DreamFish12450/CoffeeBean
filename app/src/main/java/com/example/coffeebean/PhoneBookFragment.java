
package com.example.coffeebean;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Button;
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
import com.example.coffeebean.util.PinyinComparator;
import com.example.coffeebean.util.UserManage;
import com.example.coffeebean.widget.PopWindowView;
import com.example.coffeebean.widget.SideBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
    private RecyclerViewAdapter mAdapter;
    private EditText editText;
    private TextView cancelView;
    private TextView addView;
    private ListView listView;
    private static final int REQUESTCODE = 1;
    private static final int REQUESTCODE_Info = 2;
    private static final int SUCCESS = 1;
    private static final int FAILURE = 0;
    ArrayList<ContactInfo> contactInfos;
    //phonelist
    SimpleAdapter adapter;
    ArrayList<ContactInfo> ContactInfosList_searched;
    LinearLayout outLayout;

    public void showAddUserDiagLog(String user) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("好友请求");
        normalDialog.setMessage("用户名为" + user + "的用户向您发送了一则好友请求");
        normalDialog.setPositiveButton("接受",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContactInfo contactInfo = new ContactDBHelper(getActivity()).getContactInfoQueryByName(user);
                        contactInfos.add(contactInfo);
                        contactInfo.setContactId(UserManage.getInstance().getUserInfo(getActivity()).getId());
//                        new ContactDBHelper(getActivity()).insertContactInfo(contactInfo);
                        Log.d("preInfo", contactInfo.toString());
                        contactInfos = filledData(contactInfos);
                        Collections.sort(contactInfos, mComparator);

                        mAdapter.notifyDataSetChanged();
                        Log.d("add", contactInfo.getNoteName());
                    }
                });
        normalDialog.setNegativeButton("忽略",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

    private BroadcastReceiver Addreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String username = (String) intent.getSerializableExtra("user");
            if (username != null) {
                Log.d("广播信息", username);
                showAddUserDiagLog(username);
            }
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(getClass().getName(), "获取回调");
            ContactInfo contactInfo1 = (ContactInfo) intent.getSerializableExtra("Info");
//            ContactInfo preInfo = (ContactInfo) intent.getSerializableExtra("PreInfo");
            String preInfoName = (String) intent.getSerializableExtra("PreInfoName");
            if (contactInfo1 != null) {
                Log.d("信息", contactInfo1.toString());
//                Log.d("Preinfo", preInfo.toString());
                Iterator<ContactInfo> it = contactInfos.iterator();
                while (it.hasNext()) {
                    ContactInfo x = it.next();
                    if (preInfoName.equals(x.getNoteName())) {
                        it.remove();
                    }
                }
                contactInfos.add(contactInfo1);
                Log.d("preInfo", contactInfos.toString());
                contactInfos = filledData(contactInfos);
                Collections.sort(contactInfos, mComparator);
                Log.d("add", contactInfo1.getNoteName());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                Intent broadIntent = new Intent("refreshPhonelist");
                getContext().sendBroadcast(broadIntent);
            }
        }
    };
    private PinyinComparator mComparator = new PinyinComparator();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PhoneBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
//        mAdapter = new RecyclerViewAdapter(getActivity(), contactInfos);
//        ((RecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
//        recyclerView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onStart() {
        super.onStart();
        getActivity().runOnUiThread(()->{
            contactInfos = new ContactDBHelper(getContext()).getContactInfoContactId(UserManage.getInstance().getUserInfo(getActivity()).getId());
//                contactInfos =ContactDBHelper.getInstance(getContext()).getAllContactInfos();
            contactInfos = filledData(contactInfos);
            Collections.sort(contactInfos, mComparator);
            Log.d("startInfo", contactInfos.toString());
            recyclerView.setAdapter(mAdapter);
            mAdapter.setItems(contactInfos);
            mAdapter.notifyDataSetChanged();
        });


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
        View root = inflater.inflate(R.layout.fragment_phone_book, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        getActivity().registerReceiver(Addreceiver, new IntentFilter("addUser"));
        getActivity().registerReceiver(receiver, new IntentFilter("action1"));
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
        new Thread() {
            @Override
            public void run() {
                contactInfos = new ContactDBHelper(getContext()).getContactInfoContactId(UserManage.getInstance().getUserInfo(getActivity()).getId());
//                contactInfos =ContactDBHelper.getInstance(getContext()).getAllContactInfos();


            }
        }.start();

        while (contactInfos == null) {
        }
        // Adapter:
//        Iterator<ContactInfo> it =contactInfos.iterator();
//        Log.d(getActivity().getClass().getName(),String.valueOf(UserManage.getInstance().getUserInfo(getActivity()).getId()));
//        while(it.hasNext()){
//            ContactInfo x = it.next();
//            if(!(UserManage.getInstance().getUserInfo(getActivity()).getId() == x.getContactId())){
//                it.remove();
//            }
//        }
        contactInfos = filledData(contactInfos);
        Collections.sort(contactInfos, mComparator);


        Log.d("fragment context", getContext().toString());


        mAdapter = new RecyclerViewAdapter(getContext(), contactInfos);
//        ((RecyclerViewAdapter) mAdapter).onBind = (viewHolder, position) -> {
//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent();
////
//                    intent.setClass(getContext(),ContactInfoActivity.class);
//                    //把一个值写入到Intent中
//                    TextView textView = (TextView)viewHolder.itemView.findViewById(R.id.contact_info_item_name);
//                    intent.putExtra("NoteName", textView.getText());
//                    //启动另一个activity
//                   startActivity(intent);
////                    Toast.makeText(view.getContext(), "onItemSelected: " + textViewData.getText().toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        };
        ((RecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mAdapter);
        ContactInfosList_searched = new ArrayList<ContactInfo>();
        /* Listeners */
        recyclerView.setOnScrollListener(onScrollListener);
        //各控件
        listView = root.findViewById(R.id.listview);
        cancelView = root.findViewById(R.id.text_cancel);
        addView = root.findViewById(R.id.text_add);
        editText = root.findViewById(R.id.edittext);
        outLayout = root.findViewById(R.id.search_out);
        SideBar sideBar = root.findViewById(R.id.viewSidebar);

        //实现侧边栏快速定位功能
        sideBar.setLetterTouchListener(new SideBar.LetterTouchListener() {
            @Override
            public void setLetter(String letter) {
                for (int i = 0; i < contactInfos.size(); i++) {
                    if (letter.equals(contactInfos.get(i).getLetter())) {
                        Log.d("侧边查找", contactInfos.get(i).getNoteName());
//                        if(i+1>=contactInfos.size()){
//                        recyclerView.scrollToPosition(i);
//                        break;
//                        }
//                        else if(letter.equals(contactInfos.get(i+1).getLetter()))
//                            continue;
                        recyclerView.scrollToPosition(i);
                        break;
                    }
                }
            }
        });
        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(getClass().getName(), "ready to enter");
//                Toast.makeText(getContext(),"点击",Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(getActivity(), AddActivity.class), REQUESTCODE);
            }
        });


        editText.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }//文本改变之前执行

            @Override
            //文本改变的时候执行
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果长度为0
                if (s.length() == 0) {
                    //隐藏
                    listView.setVisibility(View.GONE);
                    cancelView.setVisibility(View.INVISIBLE);
                } else {//长度不为0
                    //更新显示ListView
                    cancelView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    String str = s.toString();
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

    public void showSearchList(String str) {
        adapter = null;
        new Thread() {
            @Override
            public void run() {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                ArrayList<ContactInfo> ContactInfosList = ContactDBHelper.getInstance(getContext()).getAllContactInfos();
                Log.d("模糊搜索开始", String.valueOf(contactInfos.size()));
                CharacterParser.search(str, contactInfos, ContactInfosList_searched);
                for (ContactInfo i : ContactInfosList_searched) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("title", i.getNoteName());
                    map.put("info", i.getPhoneNumber());
                    list.add(map);
                }
                Log.d("模糊搜索结束", String.valueOf(list.size()));
//                list=new ContactDBHelper(getContext()).getLikeContactInfos(str);
                adapter = new SimpleAdapter(getContext(), list, R.layout.search_list,
                        new String[]{"title", "info"},
                        new int[]{R.id.title, R.id.info}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final int p = position;
                        final View view = super.getView(position, convertView, parent);
                        TextView useName = (TextView) view.findViewById(R.id.title);
                        LinearLayout item = view.findViewById(R.id.searchitem);
                        item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //启动个人页面
                                Log.d("itemclicked", "search");
                                Intent intent = new Intent();

                                intent.setClass(getContext(), ContactInfoActivity.class);
                                //把一个值写入到Intent中
                                intent.putExtra("NoteName", useName.getText());
                                //启动另一个activity
                                startActivity(intent);
                            }
                        });
//                        item.setOnLongClickListener(new View.OnLongClickListener(){
//
//                            @Override
//                            public boolean onLongClick(View view) {
//                                initPopWindow(view, contactInfos.get(position));
//                                return true;
//                            }
//                        });
                        return view;
                    }
                };

            }
        }.start();
        while (adapter == null) {
        }
        listView.setAdapter(adapter);
        Log.d("模糊搜索初始化", "111");
    }

    private void hideSoft() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    填充首字母
    private ArrayList<ContactInfo> filledData(ArrayList<ContactInfo> data) {
        CharacterParser characterParser = CharacterParser.getInstance();
//        ArrayList<ContactInfo> list = new ArrayList<ContactInfo>();
        for (int i = data.size() - 1; i >= 0; i--) {
//            ContactInfo sm = new ContactInfo();
//            sm.setNoteName(data.get(i).getNoteName());
//            sm.setPhoneNumber(data.get(i).getPhoneNumber());
//            sm.setAvaterUri(data.get(i).getAvaterUri());
            String pinyin = characterParser.getSelling(data.get(i).getNoteName());
            //如果标签分组不为空 Letter为分组名
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                data.get(i).setLetter(sortString);
            } else {
                data.get(i).setLetter("#");
            }
//            list.add(sm);
        }
        return data;
    }

    @Override


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        getParentFragment().onActivityResult(requestCode,resultCode,data);
        Log.d("Request", String.valueOf(requestCode));
        Log.d("Result", String.valueOf(resultCode));
        switch (requestCode) {
            case REQUESTCODE:
                if (resultCode == SUCCESS) {
                    Log.d("Success", "123");
                    Bundle bundle = data.getBundleExtra("bundle");
                    if (bundle != null) {
                        ContactInfo contactInfo = (ContactInfo) bundle.getSerializable("newContactInfo");
                        contactInfos.add(contactInfo);
                        contactInfos = filledData(contactInfos);
                        Collections.sort(contactInfos, mComparator);
                        Log.d("add", contactInfo.getNoteName());
                        mAdapter.notifyDataSetChanged();
                        Intent broadIntent = new Intent("refreshPhonelist");
                        getActivity().sendBroadcast(broadIntent);
                    }
                } else if (requestCode == FAILURE) {

                }
                break;
            case REQUESTCODE_Info:
                if (resultCode == SUCCESS) {
                    Log.d("Success_Info", "123");
                    Bundle bundle = data.getBundleExtra("bundle");
                    if (bundle != null) {
                        ContactInfo contactInfo = (ContactInfo) bundle.getSerializable("refreshContactInfo");
                        contactInfos.remove(contactInfo);
                        contactInfos.add(contactInfo);
                        contactInfos = filledData(contactInfos);
                        Collections.sort(contactInfos, mComparator);
                        Log.d("refresh", contactInfo.getNoteName());
                        mAdapter = new RecyclerViewAdapter(getActivity(), contactInfos);
                        ((RecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        Intent broadIntent = new Intent("refreshPhonelist");
                        getActivity().sendBroadcast(broadIntent);
                    }

                }
                break;
            default:
                break;
        }
    }

}