package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterUserAccess;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class UserAccessAdapter extends DatabaseAdapter {
    private Dao<MasterUserAccess, Integer> mUserAccess;

    public UserAccessAdapter(Context context) {
        super(context);
    }

    public Dao<MasterUserAccess, Integer> getAdapter() throws SQLException {
        if (mUserAccess == null) {
            mUserAccess = getDao(MasterUserAccess.class);
        }
        return mUserAccess;
    }


    public boolean valUsername(String un){
        Cursor cursor;

        String sql = "SELECT id_user_list, " +
                " ua_username, " +
                " ua_password, " +
                " ua_name " +
                "from t_user_access " +
                " where ua_username = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            getReadableDatabase().close();
            return false;
        } else {
            cursor.close();
            getReadableDatabase().close();
            return true;
        }
    }

    public boolean valPassword(String pass){
        Cursor cursor;

        String sql = "SELECT id_user_list, " +
                " ua_username, " +
                " ua_password, " +
                " ua_name " +
                "from t_user_access " +
                " where ua_password = '" +pass+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            getReadableDatabase().close();
            return false;
        } else {
            cursor.close();
            getReadableDatabase().close();
            return true;
        }
    }
}
