package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterImage;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

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

    public ArrayList<MasterImage> val_dataImage(int custId, String un, String connType, int imgPos){
        ArrayList<MasterImage> images = new ArrayList<>();
        Cursor cursor = null;

        cursor = val_imageCursor(custId, un, connType, imgPos);

        while (cursor.moveToNext()) {
            MasterImage image = new MasterImage();
            image.setId_image(cursor.getInt(0));
            image.setId_site(cursor.getInt(1));
            image.setImage_name(cursor.getString(2));

            images.add(image);
        }
        cursor.close();
        getReadableDatabase().close();
        return images;
    }


    public Cursor val_imageCursor(int custId, String un, String connType, int imgPos){
        Cursor cursor;

        String sql = "SELECT id_image, " +
                " id_site, " +
                " image_name " +
                "from t_image " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' "+
                " and image_position = " +imgPos+ " "+
                " and conn_type = '" +connType+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }


    public ArrayList<MasterImage> load_dataImage(int custId, String un){
        ArrayList<MasterImage> images = new ArrayList<>();
        Cursor cursor = null;

        cursor = load_imageCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterImage image = new MasterImage();
            image.setId_image(cursor.getInt(0));
            image.setId_site(cursor.getInt(1));
            image.setImage_name(cursor.getString(2));
            image.setImage_url(cursor.getString(3));

            images.add(image);
        }
        cursor.close();
        getReadableDatabase().close();
        return images;
    }


    public Cursor load_imageCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_image, " +
                " id_site, " +
                " image_name, " +
                " image_url " +
                "from t_image " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }


}
