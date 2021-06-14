package com.example.coffeebean;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Response;
import com.example.coffeebean.adapter.PersonInfoAdapter;
import com.example.coffeebean.model.OnlineUser;
import com.example.coffeebean.model.UserInfo;
import com.example.coffeebean.util.Requests;
import com.example.coffeebean.util.UserManage;
import com.example.coffeebean.util.VolleyRequestUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PersonInfoAdapter personInfoAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LoginDBHelper loginDBHelper;
    private RecyclerView accountListView;
    private Switch onlineSwitch;
    TextView usernameView;
    LinearLayout linearLayoutShake;
    public PersonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonFragment newInstance(String param1, String param2) {
        PersonFragment fragment = new PersonFragment();
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
    AtomicBoolean s = new AtomicBoolean(false);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            loginDBHelper = LoginDBHelper.getInstance(getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_person, container, false);
        accountListView = root.findViewById(R.id.account_list);
        onlineSwitch = root.findViewById(R.id.online_switch);
        usernameView = root.findViewById(R.id.now_username);
        usernameView.setText(UserManage.getInstance().getUserInfo(getActivity()).getUsername());
        root.findViewById(R.id.change_password).setOnClickListener((view) -> {
            showChangePasswordDialog(inflater, root);
        });
        root.findViewById(R.id.account_management).setOnClickListener(v -> {
            try {
                showAccountManagementDialog(inflater, root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        onlineSwitch.setOnCheckedChangeListener((CompoundButton compoundButton, boolean b)->{
            s.set(compoundButton.isChecked());
            Log.d("Switch状态",String.valueOf(s.get()));
        });
        linearLayoutShake = root.findViewById(R.id.line7);
        root.findViewById(R.id.log_out_view).setOnClickListener(v -> {
            UserManage.getInstance().delUserInfo(getActivity());
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("status",1);
            startActivity(intent);
        });
        linearLayoutShake.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),Shake.class);
            String currentName = UserManage.getInstance().getUserInfo(requireActivity()).getUsername();
            Log.d("currentName",currentName);
            intent.putExtra("currentName",currentName);
            intent.putExtra("state",s.get());
            String phoneNumber = "19967309203";

            if(!s.get()){
                getActivity().runOnUiThread(()->{
                    String url = Requests.API_ADD_USER + "name="+currentName+"&phoneNumber="+phoneNumber;
                    Log.d(getClass().getName(),url);
                    OnlineUser onlineUser = new OnlineUser();
                    VolleyRequestUtil.getInstance(getActivity()).GETJsonArrayRequest(url, new VolleyRequestUtil.VolleyListenerInterface() {
                        @Override
                        public Response.Listener<JSONObject> onResponse() {
                            return null;
                        }
                        @Override
                        public Response.Listener<JSONArray> onResponseArray() {
                            return response -> {
                                try {
//                                Log.d("onResponse", response.toString());
                                    JSONArray jsonArr = new JSONArray();
                                    jsonArr = response;
                                    for (int i = 0; i < jsonArr.length(); i++) {
                                        JSONObject jsonObject = jsonArr.getJSONObject(i);
                                        Log.d("onResponse" + i, jsonArr.getJSONObject(i).toString());
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            };
                        }

                        @Override
                        public Response.ErrorListener onErr() {
                            //这里需要什么就new什么
                            return error -> Log.d("onResponse", error.toString());
                        }
                    });
                });
            }

            startActivity(intent);
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        usernameView.setText(UserManage.getInstance().getUserInfo(getActivity()).getUsername());
    }

    public void showChangePasswordDialog(LayoutInflater inflater, View root) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        builder.setTitle("修改密码");
        final View dialogView = LayoutInflater.from(this.getActivity())
                .inflate(R.layout.dialog_change_password, null);
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton("确认修改", (dialog, id) -> {
                    // sign in the user ...
                    EditText editPreText = dialogView.findViewById(R.id.pre_password);
                    EditText editAfterText = dialogView.findViewById(R.id.after_password);
                    String prePassword = editPreText.getText().toString();
                    Log.d("dialogViewMessage", prePassword + "input" + UserManage.getInstance().getUserInfo(getActivity()).getPassword());
                    if (prePassword.equals(UserManage.getInstance().getUserInfo(getActivity()).getPassword())) {
                        String username = UserManage.getInstance().getUserInfo(getActivity()).getUsername();
//                        Log.d("userInfoMessage", UserManage.getInstance().getUserInfo(getActivity()).getUsername().toString());
                        try {
                            loginDBHelper =  LoginDBHelper.getInstance(getActivity());
                            new Thread(() -> {
                                loginDBHelper.changePassword(username, editAfterText.getText().toString());
//                                Log.d("dialogViewMessage", loginDBHelper.getUserInfoQueryByName(UserManage.getInstance().getUserInfo(getActivity()).getUsername()).getPassword());
                                loginDBHelper.close();
                            }).start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        UserManage.getInstance().delUserInfo(getActivity());
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.putExtra("status",1);
                        intent.putExtra("isChecked", true);
                        intent.putExtra("username", username);
                        intent.putExtra("password", editAfterText.getText().toString());
                        startActivity(intent);
                        dialog.dismiss();

                    }
//                    UserManage.getInstance().delUserInfo(getActivity());
                })
                .setNegativeButton("取消", (dialog, id) -> {
                });
        final AlertDialog dialog = builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showAccountManagementDialog(LayoutInflater inflater, View root) throws IOException {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        builder.setTitle("添加用户");
        getActivity().runOnUiThread(() -> {
            final View dialogView = LayoutInflater.from(this.getActivity())
                    .inflate(R.layout.dialog_account_magament, null);
            personInfoAdapter = new PersonInfoAdapter(dialogView.findViewById(R.id.account_list));
            ArrayList<UserInfo> userInfos = new ArrayList<>();
            final UserInfo[] user = new UserInfo[1];
            String username = UserManage.getInstance().getUserInfo(getActivity()).getUsername();
            try {
                loginDBHelper = LoginDBHelper.getInstance(getActivity());
                new Thread(() -> {
                    if (UserManage.getInstance().getUserInfoList(getActivity()) != null) {
                        List<String> List = UserManage.getInstance().getUserInfoList(getActivity());
                        List<String> usernameList = new ArrayList<String>(new LinkedHashSet<String>(List));
                        usernameList.forEach(item -> {
                            UserInfo temp = loginDBHelper.getUserInfoQueryByName(item);
                            Log.d("UserInfo",temp.toString());
                            personInfoAdapter.addItem(temp);
                        });
                    }

                }
                ).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            userInfos.add(user[0]);

            builder.setView(dialogView);
            dialogView.findViewById(R.id.add_account).setOnClickListener((v -> {
                startActivityForResult(new Intent(getActivity(), AddAccount.class), 1);
            }));
        });
        builder.show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = data.getExtras().getString("username");//得到新Activity 关
        Log.d("The Result", result);
        final UserInfo[] user = new UserInfo[1];
        new Thread() {
            @Override
            public void run() {
                getActivity().runOnUiThread(() -> {
                    user[0] = loginDBHelper.getUserInfoQueryByName(result);
                    personInfoAdapter.addItem(user[0]);
                    UserManage.getInstance().saveUserInfoList(getActivity(), result);
                });
            }
        }.start();


    }

    private BroadcastReceiver RefreshUiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
}