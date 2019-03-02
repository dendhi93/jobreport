package com.dracoo.jobreport.database.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

public class DatabaseAdapter extends OrmLiteSqliteOpenHelper {
    private static String DB_PATH = "/data/data/com.dracoo.jobreport/databases/";
    private static final String DB_NAME = "db_report.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_EMP = "t_jobdesc";
    private Context mContext;


    public DatabaseAdapter(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    public void CreateDatabase() throws IOException {
        // Create Database
        if (!DatabaseExist()) {
            this.getReadableDatabase();
            try {
                copyDatabase();
                Log.d("err", "#####DEBUG##### :" + "Copy Database success");
            } catch (Exception e) {
                Log.d("err", "#####DEBUG##### :" + "Copy Database failed");
                throw new Error("Error, Copying database");
            }
        } else {
            forceUpdate();
        }
    }

    private boolean DatabaseExist() {
        SQLiteDatabase checkDb = null;
        try {
            String mPath = DB_PATH + DB_NAME;
            checkDb = SQLiteDatabase.openDatabase(mPath, null,SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        } catch (SQLException e) {
            Log.e("Error", e.toString());
        }
        if (checkDb != null) {
            checkDb.close();
            return true;
        } else {
            return false;
        }
    }

    private void copyDatabase() throws IOException {
        // copy databasenya
        InputStream mInput;
        try {
            mInput = new FileInputStream("/sdcard/data/JobReport/databases/db_report.db");
            Log.d(null, "####DEBUG#### : " + "Copying form sdcard");
            mInput.close();
        } catch (Exception e) {
            mInput = mContext.getAssets().open(DB_NAME);
            Log.d(null, "####DEBUG#### : " + "Copying form assest");
        }

        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = mInput.read(buffer)) > 0) {
            mOutput.write(buffer, 0, length);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();

        // hapus database yg terdapat pada sdcard
        try {
            File mFile = new File("/sdcard/data/JobReport/databases/db_report.db");
            if (!mFile.exists()) {
                Log.d(null, "####DEBUG#### : " + "No Database Update");
            } else {
                mFile.delete();
                Log.d(null, "####DEBUG#### : " + "Delete Database success");
            }
        } catch (Exception e) {
            Log.d(null, "####DEBUG#### : " + "Delete Database failed");
        }
    }

    // metode update database
    private void forceUpdate() {
        InputStream mInput;
        try {
            File mFile = new File("/sdcard/data/JobReport/databases/db_report.db");
            if (!mFile.exists()) {
                Log.d(null, "####DEBUG####" + "Tidak ada Update database");
            } else {
                mInput = new FileInputStream("/sdcard/data/JobReport/databases/db_report.db");
                String outFileName = DB_PATH + DB_NAME;
                OutputStream mOutput = new FileOutputStream(outFileName);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = mInput.read(buffer)) > 0) {
                    mOutput.write(buffer, 0, length);
                }
                mOutput.flush();
                mOutput.close();
                mInput.close();

                mFile.delete();
                Log.d(null, "####DEBUG#### : "+ "Hapus Database, Update Berhasil");
            }
        } catch (Exception e) {
            Log.d(null, "####DEBUG#### : " + "Force Update Gagal");
        }
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {}

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {}
}
