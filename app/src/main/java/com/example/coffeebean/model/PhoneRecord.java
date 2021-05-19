package com.example.coffeebean.model;

import java.util.Date;

public class PhoneRecord {

    public static final String TABLE_NAME ="phoneRecord" ;

    public static final String COLUMN_NOTENAME="noteName";
    public static final String COLUMN_AVATERURL="avaterUrl";
    public static final String COLUMN_PHONENUMBER="phoneNumber";
    public static final String COLUMN_STATUS ="status" ;
    public static final String COLUMN_RECORDID="recordId";
    public static final String COLUMN_RECEIVERID="receiverId";
    public static final String COLUMN_DATA="date";
    public static final String COLUMN_DURATION="duration";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_NOTENAME + " VARCHAR(32),"
            + COLUMN_AVATERURL + " VARCHAR(100),"
            + COLUMN_PHONENUMBER + " VARCHAR(32),"
            + COLUMN_STATUS + " INTEGER,"
            + COLUMN_RECORDID + " INTEGER PRIMARY KEY ,"
            + COLUMN_RECEIVERID + " INTEGER ,"
            + COLUMN_DATA + " DATE ,"
            + COLUMN_DURATION + " INTEGER ,"
            + ")";

    String NoteName;
    String PhoneNumber;
    int status;
    Date date;
    String AvaterUrl;
    int duration;

    public PhoneRecord(String noteName, String phoneNumber, int status, Date date,String avaterUrl,int duration) {
        NoteName = noteName;
        PhoneNumber = phoneNumber;
        this.status = status;
        this.date = date;
        this.AvaterUrl = avaterUrl;
        this.duration = duration;

    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAvaterUrl() {
        return AvaterUrl;
    }

    public void setAvaterUrl(String avaterUrl) {
        AvaterUrl = avaterUrl;
    }

    public PhoneRecord(){};

    public String getNoteName() {
        return NoteName;
    }

    public void setNoteName(String noteName) {
        NoteName = noteName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
