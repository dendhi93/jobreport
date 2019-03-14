package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class TransHistoryAdapter extends DatabaseAdapter {
    private Dao<MasterTransHistory, Integer> transHist;

    public TransHistoryAdapter(Context context) {
        super(context);
    }

    public Dao<MasterTransHistory, Integer> getAdapter() throws SQLException {
        if (transHist == null) {
            transHist = getDao(MasterTransHistory.class);
        }
        return transHist;
    }
}
