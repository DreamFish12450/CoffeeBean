package com.example.coffeebean.model;

import com.example.coffeebean.util.CharacterParser;

import java.io.Serializable;

public class ContactInfo implements Serializable {
    @Override
    public String toString() {
        return "ContactInfo{" +
                "noteName='" + noteName + '\'' +
                ", name='" + name + '\'' +
                ", homeAddress='" + homeAddress + '\'' +
                ", workAddress='" + workAddress + '\'' +
                ", career='" + career + '\'' +
                ", Letter='" + Letter + '\'' +
                ", Group=" + Group +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", avaterUri='" + avaterUri + '\'' +
                ", id=" + id +
                '}';
    }

    public static final String TABLE_NAME ="contactInfo" ;
    public static final String COLUMN_NOTENAME="noteName";
    public static final String COLUMN_NAME="name";
    public static final String COLUMN_HOMEADDRESS="homeAddress";
    public static final String COLUMN_WORKADDRESS="workAddress";
    public static final String COLUMN_CAREER="career";
    public static final String COLUMN_ID="contactId";
    public static final String COLUMN_PHONENUMBER="phoneNumber";
    public static final String COLUMN_AVATERURL="avaterUrl";
    public static final String COLUMN_GROUP="group_id";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_NOTENAME + " VARCHAR(32),"
            + COLUMN_NAME + " VARCHAR(32),"
            + COLUMN_HOMEADDRESS + " VARCHAR(64),"
            + COLUMN_WORKADDRESS + " VARCHAR(64),"
            + COLUMN_CAREER + " VARCHAR(32),"
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PHONENUMBER + " VARCHAR(32),"
            + COLUMN_AVATERURL + " VARCHAR(100),"
            + COLUMN_GROUP + " INTEGER,"
            + ")";

    private String noteName;
    private String name;
    private String homeAddress;
    private String workAddress;
    private String career;
    private String Letter;
    private int Group;
    private String phoneNumber;
    private String avaterUri;
    private int id;

    public int getGroup() {
        return Group;
    }

    public void setGroup(int group) {
        Group = group;
    }

    public void setLetter(String letter) {
        Letter = letter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public ContactInfo(){}

    public ContactInfo(String noteName){
        this.noteName = noteName;
    }
    public ContactInfo(String noteName, String name, String homeAddress, String workAddress, String career, String phoneNumber, String avaterUri) {
        this.noteName = noteName;
        this.name = name;
        this.homeAddress = homeAddress;
        this.workAddress = workAddress;
        this.career = career;
        this.phoneNumber = phoneNumber;
        this.avaterUri = avaterUri;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvaterUri() {
        return avaterUri;
    }

    public void setAvaterUri(String avaterUri) {
        this.avaterUri = avaterUri;
    }
    //获取首字母
    public String getLetter(){
        return Letter;
    }
}
