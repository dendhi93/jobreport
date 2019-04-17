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
            param.setLan_subnetmask(cursor.getString(2));
            param.setLan_parameter(cursor.getString(3));
            param.setSat_symrate(cursor.getString(4));
            param.setSat_freq(cursor.getString(5));
            param.setManagement_esnmodem(cursor.getString(6));
            param.setManagement_gateway(cursor.getString(7));
            param.setManagement_snmp(cursor.getString(8));
            param.setRanging_signal(cursor.getString(9));
            param.setRanging_data_rate(cursor.getString(10));
            param.setRanging_fec(cursor.getString(11));
            param.setRanging_power(cursor.getString(12));
            param.setRanging_esno(cursor.getString(13));
            param.setRanging_cno(cursor.getString(14));

            params.add(param);
        }
        cursor.close();
        getReadableDatabase().close();
        return params;
    }

    public Cursor val_paramCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_parameter, " +
                            " id_site, " +
                            " lan_parameter, " +
                            " lan_subnetmask, " +
                            " sat_parameter, " +
                            " sat_symrate, " +
                            " sat_freq, " +
                            " management_esnmodem, " +
                            " management_gateway, " +
                            " management_snmp, " +
                            " ranging_signal, " +
                            " ranging_data_rate, " +
                            " ranging_fec, " +
                            " ranging_power, " +
                            " ranging_esno, " +
                            " ranging_cno " +
                "from network_parameter " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

    public boolean isParamEmpty(String un, int custId){
        Cursor cursor;

        String sql = "SELECT id_parameter, " +
                " id_site " +
                "from network_parameter " +
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
