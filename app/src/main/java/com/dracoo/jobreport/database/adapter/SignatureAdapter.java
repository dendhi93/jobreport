package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterSignature;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class SignatureAdapter extends DatabaseAdapter {
    private Dao<MasterSignature, Integer> mSign;
    public SignatureAdapter(Context context) { super(context); }

    public Dao<MasterSignature, Integer> getAdapter() throws SQLException {
        if (mSign == null) {
            mSign = getDao(MasterSignature.class);
        }
        return mSign;
    }

    public Cursor val_signCursor(int custId, String un, String userType){
        Cursor cursor;

        String sql = "SELECT id_sign, " +
                " id_site, " +
                " t_user_type " +
                "from t_signature " +
                " where id_site = " + custId + " " +
                " and t_user_type = '"+userType+"' " +
                " and un_user = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

    public ArrayList<MasterSignature> val_dataSign(int custId, String un, String userType){
        ArrayList<MasterSignature> signs = new ArrayList<>();
        Cursor cursor = null;

        cursor = val_signCursor(custId, un, userType);

        while (cursor.moveToNext()) {
            MasterSignature sign = new MasterSignature();
            sign.setId_sign(cursor.getInt(0));
            sign.setId_site(cursor.getInt(1));
            sign.setT_user_type(cursor.getString(2));

            signs.add(sign);
        }
        cursor.close();
        getReadableDatabase().close();
        return signs;
    }

    public Cursor init_signCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_sign, " +
                " id_site, " +
                " t_user_type, " +
                " t_sign_path, " +
                " progress_type, " +
                " conn_type " +
                "from t_signature " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

    public ArrayList<MasterSignature> init_dataSign(int custId, String un){
        ArrayList<MasterSignature> signs = new ArrayList<>();
        Cursor cursor = null;

        cursor = init_signCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterSignature sign = new MasterSignature();
            sign.setId_sign(cursor.getInt(0));
            sign.setId_site(cursor.getInt(1));
            sign.setT_user_type(cursor.getString(2));
            sign.setT_sign_path(cursor.getString(3));
            sign.setProgress_type(cursor.getString(4));
            sign.setConn_type(cursor.getString(5));

            signs.add(sign);
        }
        cursor.close();
        getReadableDatabase().close();
        return signs;
    }



}
