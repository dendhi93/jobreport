package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterM2mData;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class M2mDataAdapter extends DatabaseAdapter {
    private Dao<MasterM2mData, Integer> m2MData;

    public M2mDataAdapter(Context context) {
        super(context);
    }

    public Dao<MasterM2mData, Integer> getAdapter() throws SQLException {
        if (m2MData == null) {
            m2MData = getDao(MasterM2mData.class);
        }
        return m2MData;
    }
}
