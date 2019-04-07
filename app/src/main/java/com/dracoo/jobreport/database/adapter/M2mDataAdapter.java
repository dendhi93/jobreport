package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterM2mData;
import com.dracoo.jobreport.database.master.MasterVsatReplace;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class M2mDataAdapter extends DatabaseAdapter {
    private Dao<MasterM2mData, Integer> m2MData;

    public M2mDataAdapter(Context context) {
        super(context);
    }

    public Dao<MasterM2mData, Integer> getAdapter() throws SQLException {
        if (m2MData == null) {
            m2MData = getDao(MasterM2mData.class);
        }
        return m2MData;
    }

    public ArrayList<MasterM2mData> val_dataM2m(int custId, String un){
        ArrayList<MasterM2mData> datam2ms = new ArrayList<>();
        Cursor cursor = null;

        cursor = val_m2mDataCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterM2mData datam2m = new MasterM2mData();
            datam2m.setId_data(cursor.getInt(0));
            datam2m.setId_site(cursor.getInt(1));

            datam2ms.add(datam2m);
        }
        cursor.close();
        getReadableDatabase().close();
        return datam2ms;
    }

    public Cursor val_m2mDataCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_data, " +
                " id_site " +
                "from m2m_data " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

    public boolean isM2mDataEmpty(String un, int custId){
        Cursor cursor;

        String sql = "SELECT id_data, " +
                " id_site " +
                "from m2m_data " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            getReadableDatabase().close();
            return false;
        } else {
            cursor.close();
            getReadableDatabase().close();
            return true;
        }
    }
}
