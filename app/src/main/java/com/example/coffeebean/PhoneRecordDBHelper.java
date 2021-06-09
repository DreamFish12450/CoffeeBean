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
import com.example.coffeebean.model.PhoneRecord;
import com.example.coffeebean.model.UserInfo;
import com.example.coffeebean.util.UserManage;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PhoneRecordDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private final Context myContext;
    private SQLiteDatabase mDatabase;
    private static final String DATABASE_NAME = "identifier.sqlite";
    public PhoneRecordDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext=context;
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
    /**
     *
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


                String noteName=cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_NOTENAME));
                int status=cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_STATUS));
                String telephone = cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_PHONENUMBER));
                int recordId = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_RECORDID));
                int receiverId = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_RECEIVERID));
                String avaterUrl=cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_AVATERURL));

                SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date=sf.parse(cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_DATA)));
                int duration=cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_DURATION));

                PhoneRecord.setNoteName(noteName);
                PhoneRecord.setStatus(status);
                PhoneRecord.setDuration(duration);
                PhoneRecord.setDate(date);
                PhoneRecord.setAvaterUrl(avaterUrl);
                PhoneRecord.setPhoneNumber(telephone);

                PhoneRecordsList.add(PhoneRecord);
            }
        }
        cursor.close();
        db.close();
        return PhoneRecordsList;
    }
    /**
     *查找未接
     * @return 读取数据库，返回一个 PhoneRecord 类型的 ArrayList
     */
    public ArrayList<PhoneRecord> getMissPhoneRecords() throws ParseException {
        ArrayList<PhoneRecord> PhoneRecordsList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + PhoneRecord.TABLE_NAME
                +" WHERE "+ PhoneRecord.COLUMN_STATUS +"=0"
                + " ORDER BY " + PhoneRecord.COLUMN_DATA + " DESC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PhoneRecord PhoneRecord = new PhoneRecord();


                String noteName=cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_NOTENAME));
                int status=cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_STATUS));
                String telephone = cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_PHONENUMBER));
                int recordId = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_RECORDID));
                int receiverId = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_RECEIVERID));
                String avaterUrl=cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_AVATERURL));

                SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date=sf.parse(cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_DATA)));
                int duration=cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_DURATION));

                PhoneRecord.setNoteName(noteName);
                PhoneRecord.setStatus(status);
                PhoneRecord.setDuration(duration);
                PhoneRecord.setDate(date);
                PhoneRecord.setAvaterUrl(avaterUrl);
                PhoneRecord.setPhoneNumber(telephone);

                PhoneRecordsList.add(PhoneRecord);
            }
        }
        cursor.close();
        db.close();
        return PhoneRecordsList;
    }
    /**
     *根据名字查找
     * @return 读取数据库，返回一个 PhoneRecord 类型的 ArrayList
     */
    public ArrayList<PhoneRecord> getPhoneRecordsByName(String searchname) throws ParseException {
        ArrayList<PhoneRecord> PhoneRecordsList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + PhoneRecord.TABLE_NAME
               +" WHERE "+ PhoneRecord.COLUMN_NOTENAME +"='"+searchname + "' ORDER BY " + PhoneRecord.COLUMN_RECORDID + " ASC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PhoneRecord PhoneRecord = new PhoneRecord();


                String noteName=cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_NOTENAME));
                int status=cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_STATUS));
                String telephone = cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_PHONENUMBER));
                int recordId = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_RECORDID));
                int receiverId = cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_RECEIVERID));
                String avaterUrl=cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_AVATERURL));

                SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date=sf.parse(cursor.getString(cursor.getColumnIndex(PhoneRecord.COLUMN_DATA)));
                int duration=cursor.getInt(cursor.getColumnIndex(PhoneRecord.COLUMN_DURATION));

                PhoneRecord.setNoteName(noteName);
                PhoneRecord.setStatus(status);
                PhoneRecord.setDuration(duration);
                PhoneRecord.setDate(date);
                PhoneRecord.setAvaterUrl(avaterUrl);
                PhoneRecord.setPhoneNumber(telephone);

                PhoneRecordsList.add(PhoneRecord);
            }
        }
        cursor.close();
        db.close();
        return PhoneRecordsList;
    }
    /**
     *
     * @return 初始化通话记录
     */
    public static class SelectALLPhoneRecordAsyncTask extends AsyncTask<Void, Void, ArrayList<PhoneRecord>> {

        private WeakReference<Context> contextWeakReference;
        AllPhoneRecordAdapter allPhoneRecordAdapter;
        SelectALLPhoneRecordAsyncTask(Context context, AllPhoneRecordAdapter allPhoneRecordAdapter) {
            contextWeakReference = new WeakReference<>(context);
            this.allPhoneRecordAdapter=allPhoneRecordAdapter;
        }

        @Override
        protected ArrayList<PhoneRecord> doInBackground(Void... voids) {
            Context context = contextWeakReference.get();
            if (context != null) {
                try {
                   return new PhoneRecordDBHelper(context).getAllPhoneRecords();
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
     *
     * @return 初始化未接通话记录
     */
    public static class SelectMissPhoneRecordAsyncTask extends AsyncTask<Void, Void, ArrayList<PhoneRecord>> {

        private WeakReference<Context> contextWeakReference;
        AllPhoneRecordAdapter missPhoneRecordAdapter;
        SelectMissPhoneRecordAsyncTask(Context context, AllPhoneRecordAdapter missPhoneRecordAdapter) {
            contextWeakReference = new WeakReference<>(context);
            this.missPhoneRecordAdapter=missPhoneRecordAdapter;
        }

        @Override
        protected ArrayList<PhoneRecord> doInBackground(Void... voids) {
            Context context = contextWeakReference.get();
            if (context != null) {
                try {
                    return new PhoneRecordDBHelper(context).getMissPhoneRecords();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<PhoneRecord> phoneRecordlish) {
            super.onPostExecute(phoneRecordlish);
            Log.d("missPhone",String.valueOf(phoneRecordlish.size()));
            Context context = contextWeakReference.get();
            missPhoneRecordAdapter.setItems(phoneRecordlish);

        }
    }
    /**
     *
     * 初始化个人信息界面的来电列表
     */
    public static class SelectByNameAsyncTask extends AsyncTask<Void, Void, ArrayList<PhoneRecord>> {

        private WeakReference<Context> contextWeakReference;
        String noteName;
        PersonPhoneRecordAdapter personPhoneRecordAdapter;
        SelectByNameAsyncTask(Context context, PersonPhoneRecordAdapter personPhoneRecordAdapter,String notename) {
            contextWeakReference = new WeakReference<>(context);
            this.personPhoneRecordAdapter=personPhoneRecordAdapter;
            noteName=notename;
        }

        @Override
        protected ArrayList<PhoneRecord> doInBackground(Void... voids) {
            Context context = contextWeakReference.get();
            if (context != null) {
                try {
                    return new PhoneRecordDBHelper(context).getPhoneRecordsByName(noteName);
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
