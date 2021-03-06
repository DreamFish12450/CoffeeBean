package com.example.coffeebean;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;


import com.example.coffeebean.model.ContactInfo;
import com.example.coffeebean.model.PhoneRecord;
import com.example.coffeebean.model.UserInfo;
import com.example.coffeebean.util.UserManage;

import java.util.ArrayList;

public class LoginDBHelper extends SQLiteOpenHelper {
    private static LoginDBHelper instance=null;
    private static final int DATABASE_VERSION = 1;
    //    private static final String
    private final Context myContext;
    private SQLiteDatabase mDatabase;
    private static final String DATABASE_NAME = "identifier.sqlite";
    @SuppressLint("SdCardPath")
    private final String DATABASE_PATH = "/data/data/com/example/coffeebean/databases";//获取sd卡路径
    private final String DATABASE_FILENAME = "identifier.sqlite";//数据库文件名称


    public LoginDBHelper(@Nullable Context context) throws IOException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
//        createDataBase();
    }
    public static LoginDBHelper getInstance(@Nullable Context context) throws IOException {
        if(instance==null)
            synchronized (LoginDBHelper.class){
                if (instance==null){
                    instance=new LoginDBHelper(context);
                }
            }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(ContactInfo.CREATE_TABLE);
//        sqLiteDatabase.execSQL(UserInfo.CREATE_TABLE);
//        sqLiteDatabase.execSQL(PhoneRecord.CREATE_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //drop old table if is existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserInfo.TABLE_NAME);
        //create table again
        onCreate(sqLiteDatabase);
    }

    //    private SQLiteDatabase openDatabase()
//    {
//        try
//        {
//            // 获得dictionary.db文件的绝对路径
//            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
//            //配置文件或者文件夹路径参数
//            File dir = new File(DATABASE_PATH);//sd卡的路径，通过Environment获得的
//            // 如果/sdcard/dictionary目录不存在，创建这个目录
//            if (!dir.exists())
//                dir.mkdir();
//            // 如果在/sdcard/dictionary目录中不存在
//            // dictionary.db文件，则从res\raw目录中复制这个文件到
//            // SD卡的目录（/sdcard/dictionary）
//            /**
//             * 通过输入流和输出流来实现文件的复制（这是最常用的复制文件的方法）
//             */
//            Log.d("复制dictionary.db文件count", "out");
//            if (!(new File(databaseFilename)).exists())
//            {
//                // 获得封装dictionary.db文件的InputStream对象
//                InputStream is = myContext.getResources().openRawResource(
//                        R.raw.identifier);
//                FileOutputStream fos = new FileOutputStream(databaseFilename);
//                byte[] buffer = new byte[8192];
//                int count = 0;
//                // 开始复制dictionary.db文件
//                Log.d("复制dictionary.db文件count", "in");
//                while ((count = is.read(buffer)) > 0)
//                {   Log.d("复制dictionary.db文件count", String.valueOf(count));
//                    fos.write(buffer, 0, count);
//                }
//
//                fos.close();
//                is.close();
//            }
//
//
//            // 打开/sdcard/dictionary目录中的dictionary.db文件
//            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
//                    databaseFilename, null);
//            return database;
//        }
//        catch (Exception e)
//        {
//        }
//        return null;
//    }
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DATABASE_PATH + "/" + DATABASE_FILENAME;
            ;
//            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (Exception e) {

            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getResources().openRawResource(
                R.raw.identifier);
//         如果/sdcard/dictionary目录不存在，创建这个目录
        File dir = new File(DATABASE_PATH);//sd卡的路径，
        if (!dir.exists())
            dir.mkdir();
        // Path to the just created empty db
        String outFileName = DATABASE_PATH + "/" + DATABASE_FILENAME;
        ;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);


        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public SQLiteDatabase openDataBase() throws SQLException {

        //Open the database
        String myPath = DATABASE_PATH + "/" + DATABASE_FILENAME;
        ;
        mDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        return mDatabase;
    }

    @Override
    public synchronized void close() {

        if (mDatabase != null)
            mDatabase.close();

        super.close();

    }

    /**
     * @param searchName query database by name
     * @return UserInfo
     */
    public UserInfo getUserInfoQueryByName(String searchName) {
        UserInfo UserInfo = new UserInfo();
        mDatabase = this.getReadableDatabase();
        String[] columnArray = new String[]{
                UserInfo.COLUMN_USERID,
                UserInfo.COLUMN_USERNAME,
                UserInfo.COLUMN_PASSWORD,
                UserInfo.COLUMN_VIP_LEVEL,
                UserInfo.COLUMN_AMOUNT,
                UserInfo.COLUMN_PHONE_NUMBER,
                UserInfo.COLUMN_AVATAR_URL};
        Cursor cursor = mDatabase.query(UserInfo.TABLE_NAME,
                columnArray,
                UserInfo.COLUMN_USERNAME + "=? ",
                new String[]{searchName},
                null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(UserInfo.COLUMN_USERID));
            String username = cursor.getString(cursor.getColumnIndex(UserInfo.COLUMN_USERNAME));
            String password = cursor.getString(cursor.getColumnIndex(UserInfo.COLUMN_PASSWORD));
            int vip_level = cursor.getInt(cursor.getColumnIndex(UserInfo.COLUMN_VIP_LEVEL));
            double amount = cursor.getDouble(cursor.getColumnIndex(UserInfo.COLUMN_AMOUNT));
            String phone_number = cursor.getString(cursor.getColumnIndex(UserInfo.COLUMN_PHONE_NUMBER));
            String avatar_url = cursor.getString(cursor.getColumnIndex(UserInfo.COLUMN_AVATAR_URL));
            UserInfo.setId(id);
            UserInfo.setUsername(username);
            UserInfo.setPassword(password);
            UserInfo.setVip_level(vip_level);
            UserInfo.setAmount(amount);
            UserInfo.setPhone_number(phone_number);
            UserInfo.setAvatar_url(avatar_url);
            cursor.close();
            close();
            return UserInfo;
        }
        return null;
    }

    public void changePassword(String username, String password) {
        mDatabase = this.getWritableDatabase();
        Log.d("database",mDatabase.getPath());
        ContentValues values = new ContentValues();
        values.put("password", password);
        mDatabase.update(UserInfo.TABLE_NAME, values, "username=?", new String[]{username});
        close();
    }


}
