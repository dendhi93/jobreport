package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterConnectionParameter;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class ConnectionParameterAdapter extends DatabaseAdapter {
    private Dao<MasterConnectionParameter, Integer> connParameter;

    public ConnectionParameterAdapter(Context context) {
        super(context);
    }

    public Dao<MasterConnectionParameter, Integer> getAdapter() throws SQLException {
        if (connParameter == null) {
            connParameter = getDao(MasterConnectionParameter.class);
        }
        return connParameter;
    }
}
