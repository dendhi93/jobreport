package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterVsatReplace;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class VsatReplaceAdapter extends DatabaseAdapter {
    private Dao<MasterVsatReplace, Integer> vsatReplace;

    public VsatReplaceAdapter(Context context) {
        super(context);
    }

    public Dao<MasterVsatReplace, Integer> getAdapter() throws SQLException {
        if (vsatReplace == null) {
            vsatReplace = getDao(MasterVsatReplace.class);
        }
        return vsatReplace;
    }
}
