package com.example.coffeebean.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class VolleyRequestUtil {

    private VolleyRequestUtil(){}

    private static volatile  VolleyRequestUtil mVolleyRequestUtil;

    private static Context mContext;

    private RequestQueue requestQueue;
    public static VolleyRequestUtil getInstance(Context context){
        if (mVolleyRequestUtil == null) {
            synchronized (VolleyRequestUtil.class){
                if (mVolleyRequestUtil == null) {
                    mContext = context;

                    mVolleyRequestUtil = new VolleyRequestUtil();
                }
            }
        }
        return mVolleyRequestUtil;
    }
    //获取RequestQueue对象
    private void getVolley(){
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext);
        }
    }
    /**
     * 文本实现接口
     */
    public interface VolleyStringInterface{
        public Response.Listener<String> onStringResponse();

        public Response.ErrorListener onErr();
    }
    /**
     *
     * @param url   用于请求文本接口
     * @param volleyStringInterface 用于实现请求接口
     */
    public void StringRequest(String url, VolleyStringInterface volleyStringInterface){
        getVolley();
        StringRequest stringRequest = new StringRequest(url, volleyStringInterface.onStringResponse(), volleyStringInterface.onErr());
        requestQueue.add(stringRequest);
    }
    /**
     * JsonObject实现接口
     */
    public interface VolleyListenerInterface {
        /**
         *
         * @return  返回JSONObject接口对象
         */
        public abstract Response.Listener<JSONObject>  onResponse();
        public abstract Response.Listener<JSONArray>  onResponseArray();
        /**
         *
         * @return 报错提示
         */
        public abstract Response.ErrorListener  onErr();
    }
    /**
     *
     * @param url   请求GET接口
     * @param volleyListenerInterface
     */
    public void GETJsonObjectRequest(String url, VolleyListenerInterface volleyListenerInterface){

        getVolley();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                null,volleyListenerInterface.onResponse(),volleyListenerInterface.onErr());

        requestQueue.add(jsonObjectRequest);
    }
    public void GETJsonArrayRequest(String url, VolleyListenerInterface volleyListenerInterface){

        getVolley();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,volleyListenerInterface.onResponseArray(),volleyListenerInterface.onErr());

        requestQueue.add(jsonArrayRequest);

    }




}

