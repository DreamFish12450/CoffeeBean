package com.example.coffeebean;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.coffeebean.adapter.PersonInfoAdapter;
import com.example.coffeebean.model.UserInfo;
import com.example.coffeebean.util.UserManage;

import java.io.IOException;
import java.util.ArrayList;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LoginDBHelper loginDBHelper;
    private RecyclerView accountListView;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_person, container, false);
        accountListView =root.findViewById(R.id.account_list);
        root.findViewById(R.id.change_password).setOnClickListener((view) -> {
            showChangePasswordDialog(inflater, root);
        });
        root.findViewById(R.id.account_management).setOnClickListener(v->{
            showAccountManagementDialog(inflater,root);
        });
        return root;
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
                    Log.d("dialogViewMessage", prePassword + UserManage.getInstance().getUserInfo(getActivity()).getPassword());
                    if (prePassword.equals(UserManage.getInstance().getUserInfo(getActivity()).getPassword())) {
//                        Log.d("userInfoMessage", UserManage.getInstance().getUserInfo(getActivity()).getUsername().toString());
                        try {
                            loginDBHelper = new LoginDBHelper(getActivity());
                            new Thread(() -> {
                                loginDBHelper.changePassword(UserManage.getInstance().getUserInfo(getActivity()).getUsername(), editAfterText.getText().toString());
                                Log.d("dialogViewMessage", loginDBHelper.getUserInfoQueryByName(UserManage.getInstance().getUserInfo(getActivity()).getUsername()).getPassword());
                            }).start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        UserManage.getInstance().delUserInfo(getActivity());
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
//
                    }
//                    UserManage.getInstance().delUserInfo(getActivity());
                })
                .setNegativeButton("取消", (dialog, id) -> {

                });
        builder.show();
    }

    public void showAccountManagementDialog(LayoutInflater inflater, View root) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        builder.setTitle("添加用户");
        final View dialogView = LayoutInflater.from(this.getActivity())
                .inflate(R.layout.dialog_account_magament, null);
        PersonInfoAdapter personInfoAdapter = new PersonInfoAdapter(dialogView.findViewById(R.id.account_list));
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        userInfos.add(new UserInfo(1, "1001", "123", 1, 123,"1899",null));
        userInfos.add(new UserInfo(1, "1002", "123", 1, 123,"1899",null));
        userInfos.add(new UserInfo(1, "1003", "123", 1, 123,"1899",null));
        personInfoAdapter.setItems(userInfos);
        builder.setView(dialogView);
        dialogView.findViewById(R.id.add_account).setOnClickListener((v -> {
            startActivityForResult(new Intent(getActivity(), AddAccount.class), 1);
        }));
        builder.show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = data.getExtras().getString("username");//得到新Activity 关
        Log.i("The Result", result);
    }
}