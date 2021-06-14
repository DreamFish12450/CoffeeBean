package com.example.coffeebean;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffeebean.adapter.AllPhoneRecordAdapter;
import com.example.coffeebean.model.PhoneRecord;
import com.example.coffeebean.util.Requests;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MissCallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MissCallFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    AllPhoneRecordAdapter missPhoneRecordAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View root;
    public MissCallFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MissCallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MissCallFragment newInstance(String param1, String param2) {
        MissCallFragment fragment = new MissCallFragment();
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
        getActivity().registerReceiver(receiver2, new IntentFilter("actionRefreshPL"));
        getActivity().registerReceiver(receiver, new IntentFilter("refreshPhonelist"));//更新
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_miss_call, container, false);
        missPhoneRecordAdapter = new AllPhoneRecordAdapter(root.findViewById(R.id.miss_phone_record));
        ArrayList<PhoneRecord> phoneRecords = new ArrayList<>();
        String url = Requests.API_GET_ALL_PHONE + "0";
        PhoneRecordDBHelper.SelectMissPhoneRecordAsyncTask selectMissContactAsyncTask=new PhoneRecordDBHelper.SelectMissPhoneRecordAsyncTask(getContext(),missPhoneRecordAdapter);
        selectMissContactAsyncTask.execute();
        return root;
    }
    private BroadcastReceiver receiver2 = new BroadcastReceiver() {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(getClass().getName(), "获取回调");
            PhoneRecord phoneRecord1 = (PhoneRecord) intent.getSerializableExtra("Info2");
            if(phoneRecord1.getStatus()==0){
                List<PhoneRecord> items = missPhoneRecordAdapter.getItems();
                items.add(0,phoneRecord1);
                missPhoneRecordAdapter.setItems(items);
                missPhoneRecordAdapter.notifyDataSetChanged();
            }

        }
    };
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {

            PhoneRecordDBHelper.SelectMissPhoneRecordAsyncTask selectMissContactAsyncTask=new PhoneRecordDBHelper.SelectMissPhoneRecordAsyncTask(getContext(),missPhoneRecordAdapter);
            selectMissContactAsyncTask.execute();
            missPhoneRecordAdapter.notifyDataSetChanged();
        }
    };
}