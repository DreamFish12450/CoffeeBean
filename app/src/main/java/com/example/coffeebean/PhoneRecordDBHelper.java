package com.example.coffeebean;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.example.coffeebean.adapter.AllPhoneRecordAdapter;
import com.example.coffeebean.adapter.PersonPhoneRecordAdapter;
import com.example.coffeebean.model.ContactInfo;
import com.example.coffeebean.model.PhoneRecord;
import com.example.coffeebean.model.UserInfo;
import com.example.coffeebean.util.UserManage;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PhoneRecordDBHelper extends SQLiteOpenHelper {
    private static PhoneRecordDBHelper instance = null;
    private static final int DATABASE_VERSION = 1;
    private final Context myContext;
    private SQLiteDatabase mDatabase;
    private static final String DATABASE_NAME = "identifier.sqlite";

    public PhoneRecordDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(PhoneRecord.CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //drop old table if is existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PhoneRecord.TABLE_NAME);
        //create table again
        onCreate(sqLiteDatabase);
    }

    public static PhoneRecordDBHelper getInstance(@Nullable Context context) {
        if (instance == null)
            synchronized (PhoneRecordDBHelper.class) {
                if (instance == null) {
                    instance = new PhoneRecordDBHelper(context);
                }
            }
        return instance;
    }
    /**
     * @param
     * @return 返回新插入的行的ID，发生错误，插入不成功，则返回-1
     */
    public void insertPhoneRecord(PhoneRecord phoneRecord) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        String content=PhoneRecord.COLUMN_NOTENAME+","+PhoneRecord.COLUMN_AVATERURL+","+PhoneRecord.COLUMN_PHONENUMBER+","
//                +PhoneRecord.COLUMN_STATUS+","+PhoneRecord.COLUMN_RECEIVERID+","+PhoneRecord.COLUMN_DATA+","+PhoneRecord.COLUMN_DURATION;
//        db.execSQL("INSERT INTO "+ PhoneRecord.TABLE_NAME +" VALUES ("+content+")", new Object[]{
//                phoneRecord.getNoteName(),phoneRecord.getAvaterUrl(),phoneRecord.getPhoneNumber(),
//                phoneRecord.getStatus(),phoneRecord.getReceiverId(),
//                phoneRecord.getDate(),phoneRecord.getDuration()});

        values.put(PhoneRecord.COLUMN_AVATERURL,phoneRecord.getAvaterUrl());
//        values.put(PhoneRecord.COLUMN_DATA, phoneRecord.getDate());
        values.put(PhoneRecord.COLUMN_NOTENAME, phoneRecord.getNoteName());
        values.put(PhoneRecord.COLUMN_DURATION, phoneRecord.getDuration());
        values.put(PhoneRecord.COLUMN_PHONENUMBER, phoneRecord.getPhoneNumber());
        values.put(PhoneRecord.COLUMN_RECEIVERID, phoneRecord.getReceiverId());
        values.put(PhoneRecord.COLUMN_STATUS, phoneRecord.getStatus());
//        values.put(PhoneRecord.COLUMN_RECORDID, phoneRecord.getRecordId());
        long cnt=db.insert(PhoneRecord.TABLE_NAME,null,values);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now=new Date(System.currentTimeMillis());
        if(phoneRecord.getDate()==null)phoneRecord.setDate(now);
        db.execSQL("UPDATE phoneRecord SET "+PhoneRecord.COLUMN_DATA+" = '"+simpleDateFormat.format(phoneRecord.getDate())+"' Where RecordID ="+cnt);

        db.close();
    }
    /**
     * @return 读取数据库，返回一个 PhoneRecord 类型的 ArrayList
     */
    public ArrayList<PhoneRecord> getAllPhoneRecords() throws ParseException {
        ArrayList<PhoneRecord> PhoneRecordsList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + PhoneRecord.TABLE_NAME
                + " ORDER BY " + PhoneRecord.COLUMN_DATA + " DESC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PhoneRecord PhoneRecord = new PhoneRecord();


                String noteName = cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_NOTENAME));
                int status = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_STATUS));
                String telephone = cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_PHONENUMBER));
                int recordId = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_RECORDID));
                int receiverId = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_RECEIVERID));
                String avaterUrl = cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_AVATERURL));
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sf.parse(cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_DATA)));
                int duration = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_DURATION));
                PhoneRecord.setNoteName(noteName);
                PhoneRecord.setStatus(status);
                PhoneRecord.setDuration(duration);
                PhoneRecord.setDate(date);
                PhoneRecord.setAvaterUrl(avaterUrl);
                PhoneRecord.setPhoneNumber(telephone);
                PhoneRecord.setReceiverId(receiverId);
                PhoneRecord.setRecordId(recordId);

                PhoneRecordsList.add(PhoneRecord);
            }
        }
        cursor.close();
        db.close();
        return PhoneRecordsList;
    }

    /**
     * 查找未接
     *
     * @return 读取数据库，返回一个 PhoneRecord 类型的 ArrayList
     */
    public ArrayList<PhoneRecord> getMissPhoneRecords() throws ParseException {
        ArrayList<PhoneRecord> PhoneRecordsList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + PhoneRecord.TABLE_NAME
                + " WHERE " + PhoneRecord.COLUMN_STATUS + "=0"
                + " ORDER BY " + PhoneRecord.COLUMN_DATA + " DESC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PhoneRecord PhoneRecord = new PhoneRecord();


                String noteName = cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_NOTENAME));
                int status = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_STATUS));
                String telephone = cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_PHONENUMBER));
                int recordId = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_RECORDID));
                int receiverId = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_RECEIVERID));
                String avaterUrl = cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_AVATERURL));

                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sf.parse(cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_DATA)));
                int duration = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_DURATION));

                PhoneRecord.setNoteName(noteName);
                PhoneRecord.setStatus(status);
                PhoneRecord.setDuration(duration);
                PhoneRecord.setDate(date);
                PhoneRecord.setAvaterUrl(avaterUrl);
                PhoneRecord.setPhoneNumber(telephone);
                PhoneRecord.setReceiverId(receiverId);
                PhoneRecord.setRecordId(recordId);

                PhoneRecordsList.add(PhoneRecord);
            }
        }
        cursor.close();
        db.close();
        return PhoneRecordsList;
    }

    /**
     * 根据名字查找
     *
     * @return 读取数据库，返回一个 PhoneRecord 类型的 ArrayList
     */
    public PhoneRecord getPhoneRecordsById(int id) throws ParseException {

        PhoneRecord phoneRecord = new PhoneRecord();
        String selectQuery = "SELECT * FROM " + PhoneRecord.TABLE_NAME
                +" WHERE "+ PhoneRecord.COLUMN_RECORDID +"="+id +"" ;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String noteName=cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_NOTENAME));
                int status=cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_STATUS));
                String telephone = cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_PHONENUMBER));
                int recordId = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_RECORDID));
                int receiverId = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_RECEIVERID));
                String avaterUrl=cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_AVATERURL));

                SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date=sf.parse(cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_DATA)));
                int duration=cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_DURATION));

                phoneRecord.setNoteName(noteName);
                phoneRecord.setStatus(status);
                phoneRecord.setDuration(duration);
                phoneRecord.setDate(date);
                phoneRecord.setAvaterUrl(avaterUrl);
                phoneRecord.setPhoneNumber(telephone);
                phoneRecord.setReceiverId(receiverId);
                phoneRecord.setRecordId(recordId);
            }
        }
        cursor.close();
        db.close();
        return phoneRecord;
    }
    /**
     *根据名字查找
     * @return 读取数据库，返回一个 PhoneRecord 类型的 ArrayList
     */
    public ArrayList<PhoneRecord> getPhoneRecordsByName(String searchname) throws ParseException {
        ArrayList<PhoneRecord> PhoneRecordsList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + PhoneRecord.TABLE_NAME
                + " WHERE " + PhoneRecord.COLUMN_NOTENAME + "='" + searchname + "' ORDER BY " + PhoneRecord.COLUMN_RECORDID + " ASC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PhoneRecord PhoneRecord = new PhoneRecord();


                String noteName = cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_NOTENAME));
                int status = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_STATUS));
                String telephone = cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_PHONENUMBER));
                int recordId = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_RECORDID));
                int receiverId = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_RECEIVERID));
                String avaterUrl = cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_AVATERURL));

                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sf.parse(cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_DATA)));
                int duration = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_DURATION));

                PhoneRecord.setNoteName(noteName);
                PhoneRecord.setStatus(status);
                PhoneRecord.setDuration(duration);
                PhoneRecord.setDate(date);
                PhoneRecord.setAvaterUrl(avaterUrl);
                PhoneRecord.setPhoneNumber(telephone);
                PhoneRecord.setReceiverId(receiverId);
                PhoneRecord.setRecordId(recordId);

                PhoneRecordsList.add(PhoneRecord);
            }
        }
        cursor.close();
        db.close();
        return PhoneRecordsList;
    }

    /**
     * @return 返回数据库行数
     */
    public int getContactInfoCount() {
        String countQuery = "SELECT * FROM " + PhoneRecord.TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /**
     * @return 初始化通话记录
     */
    public static class SelectALLPhoneRecordAsyncTask extends AsyncTask<Void, Void, ArrayList<PhoneRecord>> {

        private WeakReference<Context> contextWeakReference;
        AllPhoneRecordAdapter allPhoneRecordAdapter;

        SelectALLPhoneRecordAsyncTask(Context context, AllPhoneRecordAdapter allPhoneRecordAdapter) {
            contextWeakReference = new WeakReference<>(context);
            this.allPhoneRecordAdapter = allPhoneRecordAdapter;
        }

        @Override
        protected ArrayList<PhoneRecord> doInBackground(Void... voids) {
            Context context = contextWeakReference.get();
            if (context != null) {
                try {
                    return PhoneRecordDBHelper.getInstance(context).getAllPhoneRecords();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<PhoneRecord> phoneRecordlish) {
            super.onPostExecute(phoneRecordlish);
            Context context = contextWeakReference.get();
            allPhoneRecordAdapter.setItems(phoneRecordlish);

        }
    }

    /**
     * @return 初始化未接通话记录
     */
    public static class SelectMissPhoneRecordAsyncTask extends AsyncTask<Void, Void, ArrayList<PhoneRecord>> {

        private WeakReference<Context> contextWeakReference;
        AllPhoneRecordAdapter missPhoneRecordAdapter;

        SelectMissPhoneRecordAsyncTask(Context context, AllPhoneRecordAdapter missPhoneRecordAdapter) {
            contextWeakReference = new WeakReference<>(context);
            this.missPhoneRecordAdapter = missPhoneRecordAdapter;
        }

        @Override
        protected ArrayList<PhoneRecord> doInBackground(Void... voids) {
            Context context = contextWeakReference.get();
            if (context != null) {
                try {
                    return PhoneRecordDBHelper.getInstance(context).getMissPhoneRecords();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<PhoneRecord> phoneRecordlish) {
            super.onPostExecute(phoneRecordlish);
            if (phoneRecordlish != null)
                Log.d("missPhone", String.valueOf(phoneRecordlish.size()));
            Context context = contextWeakReference.get();
            missPhoneRecordAdapter.setItems(phoneRecordlish);

        }
    }

    /**
     * 初始化个人信息界面的来电列表
     */
    public static class SelectByNameAsyncTask extends AsyncTask<Void, Void, ArrayList<PhoneRecord>> {

        private WeakReference<Context> contextWeakReference;
        String noteName;
        PersonPhoneRecordAdapter personPhoneRecordAdapter;

        SelectByNameAsyncTask(Context context, PersonPhoneRecordAdapter personPhoneRecordAdapter, String notename) {
            contextWeakReference = new WeakReference<>(context);
            this.personPhoneRecordAdapter = personPhoneRecordAdapter;
            noteName = notename;
        }

        @Override
        protected ArrayList<PhoneRecord> doInBackground(Void... voids) {
            Context context = contextWeakReference.get();
            if (context != null) {
                try {
                    return PhoneRecordDBHelper.getInstance(context).getPhoneRecordsByName(noteName);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<PhoneRecord> phoneRecordlish) {
            super.onPostExecute(phoneRecordlish);
            Context context = contextWeakReference.get();
            personPhoneRecordAdapter.setItems(phoneRecordlish);

        }
    }
}
