package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterImageConnType;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class ImageConnTypeAdapter extends DatabaseAdapter {
    private Dao<MasterImageConnType, Integer> mImage;

    public ImageConnTypeAdapter(Context context) {
        super(context);
    }

    public Dao<MasterImageConnType, Integer> getAdapter() throws SQLException {
        if (mImage == null) {
            mImage = getDao(MasterImageConnType.class);
        }
        return mImage;
    }
}
