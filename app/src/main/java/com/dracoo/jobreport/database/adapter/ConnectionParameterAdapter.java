package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterConnectionParameter;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

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

    public ArrayList<MasterConnectionParameter> val_param(int custId, String un){
        ArrayList<MasterConnectionParameter> params = new ArrayList<>();
        Cursor cursor = null;

        cursor = val_paramCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterConnectionParameter param = new MasterConnectionParameter();
            param.setId_parameter(cursor.getInt(0));
            param.setId_site(cursor.getInt(1));

            params.add(param);
        }
        cursor.close();
        getReadableDatabase().close();
        return params;
    }

    public Cursor val_paramCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_parameter, " +
                " id_site " +
                "from network_parameter " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }
}
