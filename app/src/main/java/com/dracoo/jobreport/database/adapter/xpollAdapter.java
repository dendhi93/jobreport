package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterXpoll;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class xpollAdapter  extends DatabaseAdapter {
    private Dao<MasterXpoll, Integer> xpolL;

    public xpollAdapter(Context context) {
        super(context);
    }

    public Dao<MasterXpoll, Integer> getAdapter() throws SQLException {
        if (xpolL == null) {
            xpolL = getDao(MasterXpoll.class);
        }
        return xpolL;
    }
}
