package com.example.coffeebean.util;
//拼音比较用于排序


import android.content.Context;

import com.example.coffeebean.ContactDBHelper;
import com.example.coffeebean.model.ContactInfo;
import com.example.coffeebean.model.Group;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PinyinComparator implements Comparator<ContactInfo> {
    private static PinyinComparator Instance;
    public  PinyinComparator(){

    }
    public static PinyinComparator getInstance(){
        synchronized (PinyinComparator.class) {
            if (Instance == null) {
                Instance = new PinyinComparator();
            }
        }
        return Instance;
    }

    public static void delete(){
        Instance=null;
    }
    @Override
    public int compare(ContactInfo arg0, ContactInfo arg1) {
        // TODO Auto-generated method stub
        //分组优先权最高  排序按照指定顺序
        if(arg0.getGroup()==1&&arg1.getGroup()==1)
        {
        if (arg0.getLetter().equals("@") || arg1.getLetter().equals("#")) {
            return -1;
        } else if (arg0.getLetter().equals("#") || arg1.getLetter().equals("@")) {
            return 1;
        } else {
            // 升序
            return arg0.getLetter().compareTo(arg1.getLetter());
            //    return arg1.getLetter().compareTo(arg0.getLetter()); // 降序
        }
        }
        else {
            //对有分组的情况进行排序
            return (arg0.getGroup()-arg1.getGroup())<0?-1:1;
        }

    }
}