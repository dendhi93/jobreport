package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterSignature;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class SignatureAdapter extends DatabaseAdapter {
    private Dao<MasterSignature, Integer> mSign;
    public SignatureAdapter(Context context) { super(context); }

    public Dao<MasterSignature, Integer> getAdapter() throws SQLException {
        if (mSign == null) {
            mSign = getDao(MasterSignature.class);
        }
        return mSign;
    }

}
