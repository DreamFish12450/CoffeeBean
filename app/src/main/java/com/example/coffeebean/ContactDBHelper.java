package com.example.coffeebean;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;



import com.example.coffeebean.adapter.ContactInfoAdapter;
import com.example.coffeebean.adapter.PersonPhoneRecordAdapter;
import com.example.coffeebean.model.ContactInfo;
import com.example.coffeebean.model.Group;


import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.sql.Types.NULL;

public class ContactDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "identifier.sqlite";

    public ContactDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(ContactInfo.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //drop old table if is existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContactInfo.TABLE_NAME);
        //create table again
        onCreate(sqLiteDatabase);
    }

    /**
     *
     * @param ContactInfo
     * @return 返回新插入的行的ID，发生错误，插入不成功，则返回-1
     */
    public long insertContactInfo(ContactInfo ContactInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContactInfo.COLUMN_NAME, ContactInfo.getName());
        values.put(ContactInfo.COLUMN_NOTENAME, ContactInfo.getNoteName());
        values.put(ContactInfo.COLUMN_HOMEADDRESS, ContactInfo.getHomeAddress());
        values.put(ContactInfo.COLUMN_WORKADDRESS, ContactInfo.getWorkAddress());
        values.put(ContactInfo.COLUMN_CAREER, ContactInfo.getCareer());
        values.put(ContactInfo.COLUMN_PHONENUMBER, ContactInfo.getPhoneNumber());
        values.put(ContactInfo.COLUMN_AVATERURL, ContactInfo.getAvaterUri());
        values.put(ContactInfo.COLUMN_GROUP,ContactInfo.getGroup());
        long id = db.insert(ContactInfo.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    /**
     *
     * @param
     * @return ALLGroup
     */
    public ArrayList<Group> getAllGroup() {
        ArrayList<Group> GroupList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Group.TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Group group = new Group();

                String name = cursor.getString(cursor.getColumnIndex(Group.COLUMN_GROUPNAME));
                int id=cursor.getInt(cursor.getColumnIndex(Group.COLUMN_GROUPID));

                group.setGroupID(id);
                group.setGroupName(name);
                GroupList.add(group);
            }
        }
        cursor.close();
        db.close();
        return GroupList;
    }
    /**
     *
     * @param searchName query database by notename
     * @return ContactInfo
     */
    public ContactInfo getContactInfoQueryByName(String searchName) {
        ContactInfo ContactInfo = new ContactInfo();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnArray = new String[]{
                ContactInfo.COLUMN_ID,
                ContactInfo.COLUMN_NAME,
                ContactInfo.COLUMN_NOTENAME,
                ContactInfo.COLUMN_HOMEADDRESS,
                ContactInfo.COLUMN_WORKADDRESS,
                ContactInfo.COLUMN_CAREER,
                ContactInfo.COLUMN_PHONENUMBER,
                ContactInfo.COLUMN_AVATERURL,
                ContactInfo.COLUMN_GROUP};
        Cursor cursor = db.query(ContactInfo.TABLE_NAME,
                columnArray,
                ContactInfo.COLUMN_NOTENAME + "=? ",
                new String[]{searchName},
                null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(ContactInfo.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactInfo.COLUMN_NAME));
            String noteName=cursor.getString(cursor.getColumnIndex(ContactInfo.COLUMN_NOTENAME));
            String workAddress=cursor.getString(cursor.getColumnIndex(ContactInfo.COLUMN_WORKADDRESS));
            String telephone = cursor.getString(cursor.getColumnIndex(ContactInfo.COLUMN_PHONENUMBER));
            String homeAddress = cursor.getString(cursor.getColumnIndex(ContactInfo.COLUMN_HOMEADDRESS));
            String avaterUrl=cursor.getString(cursor.getColumnIndex(ContactInfo.COLUMN_AVATERURL));
            String career=cursor.getString(cursor.getColumnIndex(ContactInfo.COLUMN_CAREER));
            int group=cursor.getInt(cursor.getColumnIndex(ContactInfo.COLUMN_GROUP));
            ContactInfo.setId(id);
            ContactInfo.setName(name);
            ContactInfo.setNoteName(noteName);
            ContactInfo.setHomeAddress(homeAddress);
            ContactInfo.setWorkAddress(workAddress);
            ContactInfo.setCareer(career);
            ContactInfo.setAvaterUri(avaterUrl);
            ContactInfo.setPhoneNumber(telephone);
            ContactInfo.setGroup(group);
            cursor.close();
            return ContactInfo;
        }
        return null;
    }

    /**
     *
     * @return 读取数据库，返回一个 ContactInfo 类型的 ArrayList
     */
    public ArrayList<ContactInfo> getAllContactInfos() {
        ArrayList<ContactInfo> ContactInfosList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + ContactInfo.TABLE_NAME
                + " ORDER BY " + ContactInfo.COLUMN_ID + " ASC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ContactInfo ContactInfo = new ContactInfo();

                String name = cursor.getString(cursor.getColumnIndex(ContactInfo.COLUMN_NAME));
                String noteName=cursor.getString(cursor.getColumnIndex(ContactInfo.COLUMN_NOTENAME));
                String workAddress=cursor.getString(cursor.getColumnIndex(ContactInfo.COLUMN_WORKADDRESS));
                String telephone = cursor.getString(cursor.getColumnIndex(ContactInfo.COLUMN_PHONENUMBER));
                String homeAddress = cursor.getString(cursor.getColumnIndex(ContactInfo.COLUMN_HOMEADDRESS));
                String avaterUrl=cursor.getString(cursor.getColumnIndex(ContactInfo.COLUMN_AVATERURL));
                String career=cursor.getString(cursor.getColumnIndex(ContactInfo.COLUMN_CAREER));
                int group=cursor.getInt(cursor.getColumnIndex(ContactInfo.COLUMN_GROUP));
                int id=cursor.getInt(cursor.getColumnIndex(ContactInfo.COLUMN_ID));


                ContactInfo.setName(name);
                ContactInfo.setNoteName(noteName);
                ContactInfo.setHomeAddress(homeAddress);
                ContactInfo.setWorkAddress(workAddress);
                ContactInfo.setCareer(career);
                ContactInfo.setAvaterUri(avaterUrl);
                ContactInfo.setPhoneNumber(telephone);
                ContactInfo.setGroup(group);

                ContactInfosList.add(ContactInfo);
            }
        }
        cursor.close();
        db.close();
        return ContactInfosList;
    }

    /**
     *
     * @return 返回数据库行数
     */
    public int getContactInfoCount() {
        String countQuery = "SELECT * FROM " + ContactInfo.TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    /**
     *模糊查找
     * @return 返回对象list
     */
    public List<Map<String,Object>> getLikeContactInfos(String str){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String selectQuery = "SELECT ContactInfo.COLUMN_NOTENAME,ContactInfo.COLUMN_PHONENUMBER FROM " + ContactInfo.TABLE_NAME
                +"where "+ContactInfo.COLUMN_NOTENAME+"like%"+str+ "% ORDER BY " + ContactInfo.COLUMN_NOTENAME + " ASC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ContactInfo ContactInfo = new ContactInfo();


                String noteName=cursor.getString(cursor.getColumnIndex(ContactInfo.COLUMN_NOTENAME));
                String telephone = cursor.getString(cursor.getColumnIndex(ContactInfo.COLUMN_PHONENUMBER));


                Map<String, Object> map = new HashMap<String, Object>();
                map.put("title", noteName);
                map.put("info", telephone);
                list.add(map);



            }
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     *根据id修改，根据实际情况更改如根据其他 目前model里没有id值
     * @param id update row id （需要更新的ID）
     * @param ContactInfo update value （去更新数据库的内容）
     * @return the number of rows affected (影响到的行数，如果没更新成功，返回0。所以当return 0时，需要告诉用户更新不成功)
     */
    public int updateContactInfo(int id, ContactInfo ContactInfo) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ContactInfo.COLUMN_NAME, ContactInfo.getName());
        values.put(ContactInfo.COLUMN_NOTENAME, ContactInfo.getNoteName());
        values.put(ContactInfo.COLUMN_HOMEADDRESS, ContactInfo.getHomeAddress());
        values.put(ContactInfo.COLUMN_WORKADDRESS, ContactInfo.getWorkAddress());
        values.put(ContactInfo.COLUMN_CAREER, ContactInfo.getCareer());
        values.put(ContactInfo.COLUMN_ID, NULL);//已设置自增长
        values.put(ContactInfo.COLUMN_PHONENUMBER, ContactInfo.getPhoneNumber());
        values.put(ContactInfo.COLUMN_AVATERURL, ContactInfo.getAvaterUri());
        values.put(ContactInfo.COLUMN_GROUP,ContactInfo.getGroup());
        values.put(ContactInfo.COLUMN_ID,id);
        int idReturnByUpdate = db.update(ContactInfo.TABLE_NAME, values, ContactInfo.COLUMN_ID + " =? ", new String[]{String.valueOf(id)});
        db.close();
        return idReturnByUpdate;
    }

    /**
     *根据id删除，根据实际情况更改如根据其他 目前model里没有id值
     * @param id the database table row id need to delete(需要删除的数据库表中行的ID)
     * @return 返回影响到的行数，如果在 whereClause 有传入条件，返回该条件下影响到的行数，否则返回0。
     * 想要删除所有行，只要在 whereClause 传入 String "1"，并返回删除掉的行数总数（比如：删除了四行就返回4）
     */
    public int deleteContactInfo(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int idReturnByDelete = db.delete(ContactInfo.TABLE_NAME, ContactInfo.COLUMN_ID + "=? ", new String[]{String.valueOf(id)});
        db.close();
        return idReturnByDelete;
    }

    /**
     * 删除所有行，whereClause 传入 String "1"
     * @return 返回删除掉的行数总数（比如：删除了四行就返回4）
     */
    public int deleteAllContactInfo() {
        SQLiteDatabase db = getWritableDatabase();
        int idReturnByDelete = db.delete(ContactInfo.TABLE_NAME, String.valueOf(1), null);
        db.close();
        return idReturnByDelete;
    }

    /**
     * 初始化电话簿PhoneBook界面
     * doinBackground为查询数据库操作
     * doPost根据响应返回值对界面进行更改
     * */
    public static class SelectALLContactAsyncTask extends AsyncTask<Void, Void, ArrayList<ContactInfo>> {

        private WeakReference<Context> contextWeakReference;
        ContactInfoAdapter contactInfoAdapter;
        SelectALLContactAsyncTask(Context context, ContactInfoAdapter contactInfoAdapter) {
            contextWeakReference = new WeakReference<>(context);
            this.contactInfoAdapter=contactInfoAdapter;
        }

        @Override
        protected ArrayList<ContactInfo> doInBackground(Void... voids) {
            Context context = contextWeakReference.get();
            if (context != null) {
                return new ContactDBHelper(context).getAllContactInfos();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ContactInfo> Contactlist) {
            super.onPostExecute(Contactlist);
            Context context = contextWeakReference.get();
            contactInfoAdapter.setItems(Contactlist);

        }
    }
    /**
     * 联系人INFO界面初始化代码暂时未写
     * */

}
