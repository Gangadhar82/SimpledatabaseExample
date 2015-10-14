package com.mmadapps.simpledatabaseexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by gangadhar.g on 10/10/2015.
 */
public class Helper extends SQLiteOpenHelper {

    private static String Table_name="basi";
    private static String DB_PATH = "/data/data/com.mmadapps.simpledatabaseexample/databases/";
    private static String DB_NAME = "database2.sqlite";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private String TAG = "Helper";
    Cursor cursorGetData;

    /**
     * Helper constructor Called from Helper object getapplicationContext
     **/
    public Helper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * creating database at first time of application Setup
     * @throws IOException
     **/
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * checking the database Availability based on Availability copying database
     * to the device data
     * @return true (if Available)
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,	SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.e(TAG, "Error is" + e.toString());
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * copying database from asserts to package location in mobile data
     * @throws IOException
     */
    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * Opening database for retrieving/inserting information
     * @throws SQLException
     */
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * Closing database after operation done
     */
    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    /**
     * getting information based on SQL Query
     * @param sql
     * @return Output of Query
     */
    private Cursor getData(String sql) {
        try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cursorGetData = getReadableDatabase().rawQuery(sql, null);
        return cursorGetData;
    }

    /**
     * Inserting information based on table name and values
     * @param tableName
     * @param values
     * @return
     */
    private long insertData(String tableName, ContentValues values) {
        try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myDataBase.insert(tableName, null, values);
    }

    /**
     * Updating information based on table name and Condition
     * @param tableName
     * @param values
     * @return
     */
    private int updateData(String tableName, ContentValues values,String condition) {
        try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myDataBase.update(tableName, values, condition, null);
    }





    public boolean insertvalData(ArrayList<Bean> beanlist)
    {
         myDataBase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("id",beanlist.get(0).getId());
        contentValues.put("name",beanlist.get(0).getName());
        contentValues.put("marks", beanlist.get(0).getMarks());

      long result=myDataBase.insert(Table_name,null,contentValues);

        if(result==-1)
        {
            return  false;

        }else {
            return true;
        }

    }

    public ArrayList<Bean> getmobileinfo() {
        Cursor cursor;
        ArrayList<Bean> aMobile = new ArrayList<Bean>();
        cursor = getData("SELECT * FROM ganga");

        if (cursor.getCount() > 0) {
            aMobile = new ArrayList<Bean>();
            cursor.moveToFirst();
            for (int size = 0; size < cursor.getCount(); size++) {
                Bean oMobile = new Bean();
                oMobile.setId(cursor.getString(0));
                oMobile.setName(cursor.getString(1));
                oMobile.setMarks(cursor.getString(2));



                aMobile.add(oMobile);
                cursor.moveToNext();
            }
            cursor.close();
            cursorGetData.close();
            myDataBase.close();
        }
        return aMobile;
    }
    public void exportDatabse() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//com.mmadapps.simpledatabaseexample//databases//database2.sqlite";
                // String currentDBPath = DB_PATH + DB_NAME;
                String backupDBPath = "database2_db.sqlite";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Log.e("db", "copied");
                }
                else
                {
                    Log.e("db", "dbnotexist");
                }
            }
            else
            {
                Log.e("db", "notcopied");
            }
        } catch (Exception e) {
            Log.e("db", "error");
        }
    }





}

