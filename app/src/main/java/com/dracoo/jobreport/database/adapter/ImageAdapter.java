package com.dracoo.jobreport.database.adapter;

import android.content.Context;

import com.dracoo.jobreport.database.master.MasterImage;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class ImageAdapter extends DatabaseAdapter {
    private Dao<MasterImage, Integer> mImage;

    public ImageAdapter(Context context) {
        super(context);
    }

    public Dao<MasterImage, Integer> getAdapter() throws SQLException {
        if (mImage == null) {
            mImage = getDao(MasterImage.class);
        }
        return mImage;
    }
}
