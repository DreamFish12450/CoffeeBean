package com.example.coffeebean.util;
//拼音比较用于排序


import com.example.coffeebean.model.ContactInfo;

import java.util.Comparator;

public class PinyinComparator implements Comparator<ContactInfo> {

    @Override
    public int compare(ContactInfo arg0, ContactInfo arg1) {
        // TODO Auto-generated method stub
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