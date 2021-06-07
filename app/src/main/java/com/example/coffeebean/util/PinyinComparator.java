package com.example.coffeebean.util;
//拼音比较用于排序


import com.example.coffeebean.model.ContactInfo;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PinyinComparator implements Comparator<ContactInfo> {
    private static PinyinComparator Instance;
    Map<String, Integer> map;
    public  PinyinComparator(){
        map=new HashMap<String, Integer>();
        map.put("分组1",1);
        map.put("分组2",2);
        map.put("分组3",3);
        map.put("分组4",4);

    }
    public static PinyinComparator getInstance(){
        synchronized (PinyinComparator.class) {
            if (Instance == null) {
                Instance = new PinyinComparator();
            }
        }
        return Instance;
    }
    @Override
    public int compare(ContactInfo arg0, ContactInfo arg1) {
        // TODO Auto-generated method stub
        //分组优先权最高  排序按照指定顺序
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
}