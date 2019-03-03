package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterEnvirontment;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class EnvParameter extends DatabaseAdapter {
    private Dao<MasterEnvirontment, Integer> mEnv;

    public EnvParameter(Context context) {
        super(context);
    }

    public Dao<MasterEnvirontment, Integer> getAdapter() throws SQLException {
        if (mEnv == null) {
            mEnv = getDao(MasterEnvirontment.class);
        }
        return mEnv;
    }
}
