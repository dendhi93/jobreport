package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterMachine;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class MashineAdapter extends DatabaseAdapter {
    private Dao<MasterMachine, Integer> mMachine;

    public MashineAdapter(Context context) {
        super(context);
    }

    public Dao<MasterMachine, Integer> getAdapter() throws SQLException {
        if (mMachine == null) {
            mMachine = getDao(MasterMachine.class);
        }
        return mMachine;
    }
}
