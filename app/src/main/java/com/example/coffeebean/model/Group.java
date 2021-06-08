package com.example.coffeebean.model;

public class Group {
    public static final String TABLE_NAME ="contactGroup" ;
    public static final String COLUMN_GROUPID="group_id";
    public static final String COLUMN_GROUPNAME="name";
    private int GroupID;
    private String GroupName;

    public int getGroupID() {
        return GroupID;
    }

    public void setGroupID(int groupID) {
        GroupID = groupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }
    @Override
    public String toString() {

        return GroupName;
    }
}
