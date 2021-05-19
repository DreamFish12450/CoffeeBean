package com.example.coffeebean;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coffeebean.util.RequestQueueTon;
import com.example.coffeebean.util.Requests;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    public String intToString(int duration) {
        String content = null;
        if (duration >= 60 * 60) {
            int hour = (int) Math.floor(duration / 3600);
            int minutue = (int) Math.floor((duration - hour * 3600) / 60);
            int seconds = duration - hour * 3600 - minutue * 60;
            String secondContent = minutue != 0? new String(minutue+"分") :"";
            String thirdContent = seconds !=0?new String(seconds+"秒"):"";
            content = hour + "小时" + secondContent+thirdContent;

        } else if (duration >= 60 && duration < 3600) {
            int minutue = (int) Math.floor((duration) / 60);
            int seconds = duration - minutue * 60;
            if (seconds != 0)
                content = minutue + "分" + seconds + "秒";
            else
                content = minutue + "分";
        } else {
            content = duration + "秒";
        }
        return content;
    }

    @Test
    public void testChange() {
        System.out.println(intToString(60));
        System.out.println(intToString(61));
        System.out.println(intToString(5000));
        System.out.println(intToString(3600));
    }

    @Test
    public void testDateFormat() throws ParseException {
        String date = "2021-05-06T00:00:00";
        date = date.replace("T"," ");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date1 = simpleDateFormat.parse(date);
        System.out.println(date1.toString());

    }
    @Test
    public void testApi() {
        RequestQueue queue = RequestQueueTon.getInstance(CoffeeBeanApplication.getMyApplication()).
                getRequestQueue();
        String url = Requests.API_LOGIN + "string";

        StringRequest stringRequest = new StringRequest(url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                // TODO：
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO：
            }
        });
        queue.add(stringRequest);
    }

}