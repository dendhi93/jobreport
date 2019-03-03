package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterInfoSite;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class InfoSiteAdapter extends DatabaseAdapter {
    private Dao<MasterInfoSite, Integer> mInfo;

    public InfoSiteAdapter(Context context) {
        super(context);
    }

    public Dao<MasterInfoSite, Integer> getAdapter() throws SQLException {
        if (mInfo == null) {
            mInfo = getDao(MasterInfoSite.class);
        }
        return mInfo;
    }
}
