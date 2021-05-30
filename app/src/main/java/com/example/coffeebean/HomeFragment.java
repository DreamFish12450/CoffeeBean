package com.example.coffeebean;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.transition.Visibility;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coffeebean.adapter.MainBarAdapter;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Unbinder unbinder;
    private View root;

    private TextView tv_text;
    private TextView del;
    private String all = "";

    FloatingActionButton action_call;
    LinearLayout phoneCard;
    LinearLayout main_phoneCard;
    LinearLayout phoneCard_out;
    ImageButton btn_call;
    FloatingActionsMenu floatingActionsMenu;
    FloatingActionButton button_call;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private void initView() {
        unbinder = ButterKnife.bind(this, root);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        TabLayout tabLayout = root.findViewById(R.id.menu_detail_tab);
        ViewPager2 viewPager2 = root.findViewById(R.id.menu_detail_pager);
        viewPager2.setAdapter(new MainBarAdapter(this));
        viewPager2.setOffscreenPageLimit(2);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("全部通话");
                    break;
                case 1:
                    tab.setText("未接来电");
                    break;
            }
        }).attach();
        action_call=root.findViewById(R.id.action_call);
        phoneCard=root.findViewById(R.id.phonecard);
        phoneCard_out=root.findViewById(R.id.phonecard_out);
        main_phoneCard=root.findViewById(R.id.main_phonecard);
        btn_call=root.findViewById(R.id.bt_call);
        floatingActionsMenu=root.findViewById(R.id.processing_fab_add);
        del=root.findViewById(R.id.bt_delete);
        tv_text=root.findViewById(R.id.tv_text2);
//        phoneCard_out.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                if(main_phoneCard.getVisibility()==View.VISIBLE) {
//                    main_phoneCard.setVisibility(View.GONE);
//                    floatingActionsMenu.setVisibility(View.VISIBLE);
//                }
//            }
//                                         });
//        action_call.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(main_phoneCard.getVisibility()==View.GONE) {
//                    main_phoneCard.setVisibility(View.VISIBLE);
//                    floatingActionsMenu.setVisibility(View.INVISIBLE);
//                }
//            }
//        });

        return root;
    }
    @OnClick({R.id.bt_one,R.id.bt_two,R.id.bt_three,R.id.bt_four,R.id.bt_five,R.id.bt_six,
            R.id.bt_seven,R.id.bt_eight,R.id.bt_nine,R.id.bt_zero,R.id.bt_star,R.id.bt_bottom,R.id.bt_delete,R.id.bt_call,
            R.id.phonecard_out,R.id.action_call})
    public void onClick(View v) {
        if(tv_text.length()>0)del.setVisibility(View.VISIBLE);
        switch (v.getId()) {
            case R.id.action_call:
                if(main_phoneCard.getVisibility()==View.GONE) {
                    main_phoneCard.setVisibility(View.VISIBLE);
                    floatingActionsMenu.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.phonecard_out:
                if(main_phoneCard.getVisibility()==View.VISIBLE) {
                    main_phoneCard.setVisibility(View.GONE);
                    floatingActionsMenu.setVisibility(View.VISIBLE);
                    all="";
                    tv_text.setText(all);
                    del.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.bt_one:
                all += "1";
                tv_text.setText(all);
                if(tv_text.length()>0)del.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_two:
                all += "2";
                tv_text.setText(all);
                if(tv_text.length()>0)del.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_three:
                all += "3";
                tv_text.setText(all);
                if(tv_text.length()>0)del.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_four:
                all += "4";
                tv_text.setText(all);
                if(tv_text.length()>0)del.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_five:
                all += "5";
                tv_text.setText(all);
                if(tv_text.length()>0)del.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_six:
                all += "6";
                tv_text.setText(all);
                if(tv_text.length()>0)del.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_seven:
                all += "7";
                tv_text.setText(all);
                if(tv_text.length()>0)del.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_eight:
                all += "8";
                tv_text.setText(all);
                if(tv_text.length()>0)del.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_nine:
                all += "9";
                tv_text.setText(all);
                if(tv_text.length()>0)del.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_zero:
                all += "0";
                tv_text.setText(all);
                if(tv_text.length()>0)del.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_star:
                all += "*";
                tv_text.setText(all);
                if(tv_text.length()>0)del.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_bottom:
                all += "#";
                tv_text.setText(all);
                if(tv_text.length()>0)del.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_delete:
                all = all.substring(0, all.length() - 1);
                tv_text.setText(all);
                if(tv_text.length()==0)del.setVisibility(View.INVISIBLE);
                break;
            case R.id.bt_call:
                String phonenum="tel:"+tv_text.getText().toString();
                call(phonenum);
                break;
            default:
                break;
        }
        Log.d("拨号",all);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}