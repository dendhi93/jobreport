package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterUserAccess;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

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


    public boolean valLogin(String un, String pass){
        Cursor cursor;

        String sql = "SELECT id_user_list, " +
                " ua_username, " +
                " ua_password, " +
                " ua_name " +
                "from t_user_access " +
                " where ua_username = '" +un+ "' "+
                " and ua_password = '" +pass+ "' ";

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

    public ArrayList<MasterUserAccess> userList(String un, String pass){
        ArrayList<MasterUserAccess> users = new ArrayList<>();
        Cursor cursor = null;

        cursor = userCursor(un, pass);

        while (cursor.moveToNext()) {
            MasterUserAccess mUser = new MasterUserAccess();
            mUser.setId_user_list(cursor.getInt(0));
            mUser.setUa_username(cursor.getString(1));
            mUser.setUa_password(cursor.getString(2));
            mUser.setUa_name(cursor.getString(3));

            users.add(mUser);
        }
        cursor.close();
        getReadableDatabase().close();
        return users;
    }

    public Cursor userCursor(String un, String pass){
        Cursor cursor;

        String sql = "SELECT " +
                                " id_user_list, " +
                                " ua_username, " +
                                " ua_password, " +
                                " ua_name " +
                    "from t_user_access " +
                    " where ua_username = '" + un + "' " +
                    " and ua_password = '" +pass+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }


    public Cursor valUserCursor(String un){
        Cursor cursor;

        String sql = "SELECT " +
                " id_user_list, " +
                " ua_username, " +
                " ua_password, " +
                " ua_name " +
                "from t_user_access " +
                " where ua_username = '" + un + "' " ;

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

    public ArrayList<MasterUserAccess> valUserAccess(String un){
        ArrayList<MasterUserAccess> users = new ArrayList<>();
        Cursor cursor = null;

        cursor = valUserCursor(un);

        while (cursor.moveToNext()) {
            MasterUserAccess mUser = new MasterUserAccess();
            mUser.setId_user_list(cursor.getInt(0));
            mUser.setUa_username(cursor.getString(1));
            mUser.setUa_password(cursor.getString(2));
            mUser.setUa_name(cursor.getString(3));

            users.add(mUser);
        }
        cursor.close();
        getReadableDatabase().close();
        return users;
    }

}
