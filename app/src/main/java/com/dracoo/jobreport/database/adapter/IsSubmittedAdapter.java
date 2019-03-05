package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterIsSubmited;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class IsSubmittedAdapter extends DatabaseAdapter {
    private Dao<MasterIsSubmited, Integer> masterIsSubmiteds;

    public IsSubmittedAdapter(Context context) {
        super(context);
    }

    public Dao<MasterIsSubmited, Integer> getAdapter() throws SQLException {
        if (masterIsSubmiteds == null) {
            masterIsSubmiteds = getDao(MasterIsSubmited.class);
        }
        return masterIsSubmiteds;
    }
}
