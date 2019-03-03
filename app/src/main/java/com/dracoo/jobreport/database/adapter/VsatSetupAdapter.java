package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterVsatSetup;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class VsatSetupAdapter extends DatabaseAdapter {
    private Dao<MasterVsatSetup, Integer> vsatSetups;

    public VsatSetupAdapter(Context context) {
        super(context);
    }

    public Dao<MasterVsatSetup, Integer> getAdapter() throws SQLException {
        if (vsatSetups == null) {
            vsatSetups = getDao(MasterVsatSetup.class);
        }
        return vsatSetups;
    }
}
