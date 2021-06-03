package com.example.coffeebean.model;

public class ContactInfo {

    public static final String TABLE_NAME ="contactInfo" ;
    public static final String COLUMN_NOTENAME="noteName";
    public static final String COLUMN_NAME="name";
    public static final String COLUMN_HOMEADDRESS="homeAddress";
    public static final String COLUMN_WORKADDRESS="workAddress";
    public static final String COLUMN_CAREER="career";
    public static final String COLUMN_ID="contactId";
    public static final String COLUMN_PHONENUMBER="phoneNumber";
    public static final String COLUMN_AVATERURL="avaterUrl";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_NOTENAME + " VARCHAR(32),"
            + COLUMN_NAME + " VARCHAR(32),"
            + COLUMN_HOMEADDRESS + " VARCHAR(64),"
            + COLUMN_WORKADDRESS + " VARCHAR(64),"
            + COLUMN_CAREER + " VARCHAR(32),"
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PHONENUMBER + " VARCHAR(32),"
            + COLUMN_AVATERURL + " VARCHAR(100),"
            + ")";

    private String noteName;
    private String name;
    private String homeAddress;
    private String workAddress;
    private String career;
    private String phoneNumber;
    private String avaterUri;

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
        return noteName.substring(0,1);
    }
}
