package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterM2mSetup;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class M2mSetupAdapter extends DatabaseAdapter {
    private Dao<MasterM2mSetup, Integer> m2mSetups;

    public M2mSetupAdapter(Context context) {
        super(context);
    }

    public Dao<MasterM2mSetup, Integer> getAdapter() throws SQLException {
        if (m2mSetups == null) {
            m2mSetups = getDao(MasterM2mSetup.class);
        }
        return m2mSetups;
    }
}
