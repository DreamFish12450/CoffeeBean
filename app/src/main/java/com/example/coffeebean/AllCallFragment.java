package com.example.coffeebean;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daimajia.swipe.util.Attributes;
import com.example.coffeebean.adapter.AllPhoneRecordAdapter;
import com.example.coffeebean.adapter.RecyclerViewAdapter;
import com.example.coffeebean.model.ContactInfo;
import com.example.coffeebean.model.PhoneRecord;
import com.example.coffeebean.util.Requests;
import com.example.coffeebean.util.UserManage;
import com.example.coffeebean.util.VolleyRequestUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllCallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllCallFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PhoneRecordDBHelper phoneRecordDBHelper;
    AllPhoneRecordAdapter allPhoneRecordAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View root;
    public AllCallFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllCallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllCallFragment newInstance(String param1, String param2) {
        AllCallFragment fragment = new AllCallFragment();
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
        getActivity().registerReceiver(receiver, new IntentFilter("actionRefreshPL"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_all_call, container, false);
        allPhoneRecordAdapter = new AllPhoneRecordAdapter(root.findViewById(R.id.all_phone_record));
        ArrayList<PhoneRecord> phoneRecords = new ArrayList<>();
        String url = Requests.API_GET_ALL_PHONE + "0";
        PhoneRecordDBHelper.SelectALLPhoneRecordAsyncTask selectALLContactAsyncTask=new PhoneRecordDBHelper.SelectALLPhoneRecordAsyncTask(getContext(),allPhoneRecordAdapter);
        selectALLContactAsyncTask.execute();
//        new Thread() {
//            @Override
//            public void run() {
//                VolleyRequestUtil.getInstance(getActivity().getApplicationContext()).GETJsonArrayRequest(url, new VolleyRequestUtil.VolleyListenerInterface() {
//                    @Override
//                    public Response.Listener<JSONObject> onResponse() {
//                        return null;
//                    }
//
//                    @Override
//                    public Response.Listener<JSONArray> onResponseArray() {
//                        return response -> {
//                            try {
////                                Log.d("onResponse", response.toString());
//                                JSONArray jsonArr = new JSONArray();
//                                jsonArr = response;
//                                for (int i = 0; i < jsonArr.length(); i++) {
//                                    JSONObject jsonObject = jsonArr.getJSONObject(i);
//                                    Log.d("onResponse" + i, jsonArr.getJSONObject(i).toString());
//
//                                    @SuppressLint("SimpleDateFormat") Date date = new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("date"));
//                                    PhoneRecord phoneRecord = new PhoneRecord(
//                                            jsonObject.getString("noteName"), jsonObject.getString("phoneNumber"), jsonObject.getInt("status"), date, null,0);
//                                    phoneRecords.add(phoneRecord);
//                                }
//                                allPhoneRecordAdapter.setItems(phoneRecords);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        };
//                    }
//
//                    @Override
//                    public Response.ErrorListener onErr() {
//                        //这里需要什么就new什么
//                        return new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.d("onResponse", error.toString());
//
//                            }
//                        };
//                    }
//                });
//            }
//        }.start();
//        phoneRecords.add(new PhoneRecord("校招", "123", 0, null, null));

        return root;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(getClass().getName(), "获取回调");
            PhoneRecord phoneRecord1 = (PhoneRecord) intent.getSerializableExtra("Info2");
            Log.d("内容", String.valueOf(phoneRecord1.getStatus()));
            List<PhoneRecord> items = allPhoneRecordAdapter.getItems();
            items.add(0,phoneRecord1);
            allPhoneRecordAdapter = new AllPhoneRecordAdapter(root.findViewById(R.id.all_phone_record));
            allPhoneRecordAdapter.setItems(items);
            allPhoneRecordAdapter.notifyDataSetChanged();

        }
    };
}