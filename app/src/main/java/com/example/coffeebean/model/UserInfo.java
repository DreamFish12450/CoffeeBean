package com.example.coffeebean.model;

public class UserInfo {
    public static final String TABLE_NAME ="userInfo" ;

    public static final String COLUMN_USERID="id";
    public static final String COLUMN_USERNAME="username";
    public static final String COLUMN_PASSWORD="password";
    public static final String COLUMN_VIP_LEVEL="vip_level";
    public static final String COLUMN_AMOUNT ="amount" ;
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " VARCHAR(32),"
            + COLUMN_PASSWORD + " VARCHAR(32),"
            + COLUMN_VIP_LEVEL + " INTEGER,"
            + COLUMN_AMOUNT + " DOUBLE,"
            +")";

    private int id;
    private String username;
    private String password;
    private int vip_level;
    private double amount;

    public UserInfo() {
    }

    public UserInfo(int id, String username, String password, int vip_level, double amount) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.vip_level = vip_level;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", vip_level=" + vip_level +
                ", amount=" + amount +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getVip_level() {
        return vip_level;
    }

    public void setVip_level(int vip_level) {
        this.vip_level = vip_level;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
