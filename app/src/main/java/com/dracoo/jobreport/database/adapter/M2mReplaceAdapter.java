package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterM2mReplace;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class M2mReplaceAdapter extends DatabaseAdapter {
    private Dao<MasterM2mReplace, Integer> m2MReplace;

    public M2mReplaceAdapter(Context context) {
        super(context);
    }

    public Dao<MasterM2mReplace, Integer> getAdapter() throws SQLException {
        if (m2MReplace == null) {
            m2MReplace = getDao(MasterM2mReplace.class);
        }
        return m2MReplace;
    }
}
