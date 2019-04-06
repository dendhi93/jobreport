package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterAction;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class AdapterAction extends DatabaseAdapter {
    private Dao<MasterAction, Integer> mAction;

    public AdapterAction(Context context) {
        super(context);
    }

    public Dao<MasterAction, Integer> getAdapter() throws SQLException {
        if (mAction == null) {
            mAction = getDao(MasterAction.class);
        }
        return mAction;
    }
}
