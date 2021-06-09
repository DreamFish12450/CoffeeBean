package com.example.coffeebean.model;

import android.content.Context;
import android.net.ParseException;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.coffeebean.util.Requests;
import com.example.coffeebean.util.VolleyRequestUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OnlineUser {
    String phoneNumber;
    String name;

    public OnlineUser(String phoneNumber, String name) {
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    public OnlineUser() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static OnlineUser GetUserInfo(Context context, String currentName) {
        String url = Requests.API_GET_ONLINE_USER_RANDOM + currentName;
        OnlineUser onlineUser = new OnlineUser();

        VolleyRequestUtil.getInstance(context).GETJsonArrayRequest(url, new VolleyRequestUtil.VolleyListenerInterface() {
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

                            String name = jsonObject.getString("name");
                            String phoneNumber = jsonObject.getString("phoneNumber");
                            onlineUser.setName(name);
                            onlineUser.setPhoneNumber(phoneNumber);
//                            break;
                            return;
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
        Log.d("onResponse3",onlineUser.toString());
        return onlineUser;
    }

    @Override
    public String toString() {
        return "OnlineUser{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
