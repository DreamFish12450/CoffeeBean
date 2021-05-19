package com.example.coffeebean;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.coffeebean.adapter.AllPhoneRecordAdapter;
import com.example.coffeebean.adapter.PersonPhoneRecordAdapter;
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
import java.util.Date;

public class ContactInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        PersonPhoneRecordAdapter personPhoneRecordAdapter = new PersonPhoneRecordAdapter(findViewById(R.id.phone_record_view));

        ArrayList<PhoneRecord> phoneRecords = new ArrayList<>();
        String url = Requests.API_GET_ALL_PHONE + "0,soso";
        String url2 = Requests.API_GET_CONTACT_INFO+"soso";
        new Thread() {
            @Override
            public void run() {
                VolleyRequestUtil.getInstance(getApplicationContext()).GETJsonArrayRequest(url, new VolleyRequestUtil.VolleyListenerInterface() {
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

                                    String date = jsonObject.getString("date");
                                    date = date.replace("T"," ");
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    Date date1 = simpleDateFormat.parse(date);
                                    PhoneRecord phoneRecord = new PhoneRecord(
                                            jsonObject.getString("noteName"), jsonObject.getString("phoneNumber"), jsonObject.getInt("status"), date1, null,jsonObject.getInt("duration"));
                                    phoneRecords.add(phoneRecord);
                                }
                                personPhoneRecordAdapter.setItems(phoneRecords);
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
                        return new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("onResponse", error.toString());
                            }
                        };
                    }
                });
            }
        }.start();


        new Thread() {
            @Override
            public void run() {
                VolleyRequestUtil.getInstance(getApplicationContext()).GETJsonArrayRequest(url2, new VolleyRequestUtil.VolleyListenerInterface() {
                    @Override
                    public Response.Listener<JSONObject> onResponse() {
                        //这里需要什么就new什么
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
                                    Log.d("sosoResponse" + i, jsonArr.getJSONObject(i).toString());
                                    TextView noteNameTextView = findViewById(R.id.contact_note_name);
                                    noteNameTextView.setText(jsonObject.getString("noteName"));
                                    TextView nameTextView = findViewById(R.id.contact_name);
                                    nameTextView.setText(jsonObject.getString("name"));
                                    TextView homeAddressTextView = findViewById(R.id.contact_home_address);
                                    homeAddressTextView.setText(jsonObject.getString("homeAddress"));
                                    TextView workAddressTextView = findViewById(R.id.contact_workplace);
                                    workAddressTextView.setText(jsonObject.getString("workAddress"));
                                    TextView careerTextView = findViewById(R.id.contact_career);
                                    careerTextView.setText(jsonObject.getString("career"));
                                    TextView phoneNumberTextView = findViewById(R.id.contact_phone_number);
                                    phoneNumberTextView.setText(jsonObject.getString("phoneNumber"));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        };
                    }

                    @Override
                    public Response.ErrorListener onErr() {
                        //这里需要什么就new什么
                        return new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("sosoError", error.toString());

                            }
                        };
                    }
                });
            }
        }.start();
    }
}