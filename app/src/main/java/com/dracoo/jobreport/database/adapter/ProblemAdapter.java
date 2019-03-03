package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterProblem;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class ProblemAdapter extends DatabaseAdapter {
    private Dao<MasterProblem, Integer> mProblem;

    public ProblemAdapter(Context context) {
        super(context);
    }

    public Dao<MasterProblem, Integer> getAdapter() throws SQLException {
        if (mProblem == null) {
            mProblem = getDao(MasterProblem.class);
        }
        return mProblem;
    }

}
