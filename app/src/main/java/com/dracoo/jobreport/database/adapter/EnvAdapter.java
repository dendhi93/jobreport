package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterEnvirontment;
import com.dracoo.jobreport.database.master.MasterProblem;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class EnvAdapter extends DatabaseAdapter {
    private Dao<MasterEnvirontment, Integer> mEnv;

    public EnvAdapter(Context context) {
        super(context);
    }

    public Dao<MasterEnvirontment, Integer> getAdapter() throws SQLException {
        if (mEnv == null) {
            mEnv = getDao(MasterEnvirontment.class);
        }
        return mEnv;
    }

    public ArrayList<MasterEnvirontment> val_env(int custId, String un){
        ArrayList<MasterEnvirontment> envs = new ArrayList<>();
        Cursor cursor = null;

        cursor = val_envCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterEnvirontment mEnv = new MasterEnvirontment();
            mEnv.setId_env(cursor.getInt(0));
            mEnv.setId_site(cursor.getInt(1));

            envs.add(mEnv);
        }
        cursor.close();
        getReadableDatabase().close();
        return envs;
    }

    public Cursor val_envCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_env, " +
                " id_site " +
                "from t_env " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

    public boolean isEnvEmpty(String un, int custId){
        Cursor cursor;

        String sql = "SELECT id_env, " +
                " id_site " +
                "from t_env " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";

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
