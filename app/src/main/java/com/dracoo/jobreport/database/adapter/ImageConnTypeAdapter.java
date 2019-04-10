package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterImageConnType;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

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


    public ArrayList<MasterImageConnType> load_imgConn(String connType){
        ArrayList<MasterImageConnType> imgConnTypes = new ArrayList<>();
        Cursor cursor = null;

        cursor = load_imgConnCursor(connType);

        while (cursor.moveToNext()) {
            MasterImageConnType imgConnType = new MasterImageConnType();
            imgConnType.setId_image(cursor.getInt(0));
            imgConnType.setConnection_type(cursor.getString(1));
            imgConnType.setImage_title(cursor.getString(2));
            imgConnType.setImage_position(cursor.getInt(3));
            imgConnType.setImage_folder(cursor.getString(4));

            imgConnTypes.add(imgConnType);
        }
        cursor.close();
        getReadableDatabase().close();
        return imgConnTypes;
    }

    public Cursor load_imgConnCursor(String connType){
        Cursor cursor;

        String sql = "SELECT " +
                "id_image, " + //0
                "connection_type, " + //1
                "image_title, " + //2
                "image_position, " + //3
                "image_folder " + //4
                "from t_image_connection_type " +
                " where connection_type = '" + connType + "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }
}
