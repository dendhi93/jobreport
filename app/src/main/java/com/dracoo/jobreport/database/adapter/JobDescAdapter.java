package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterJobDesc;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class JobDescAdapter extends DatabaseAdapter{
    private Dao<MasterJobDesc, Integer> mJobDesc;


    public JobDescAdapter(Context context) {
        super(context);
    }

    public Dao<MasterJobDesc, Integer> getAdapter() throws SQLException {
        if (mJobDesc == null) {
            mJobDesc = getDao(MasterJobDesc.class);
        }
        return mJobDesc;
    }
}
