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
            param.setLan_parameter(cursor.getString(2));
            param.setLan_subnetmask(cursor.getString(3));
            param.setSat_parameter(cursor.getString(4));
            param.setSat_symrate(cursor.getString(5));
            param.setSat_freq(cursor.getString(6));
            param.setManagement_esnmodem(cursor.getString(7));
            param.setManagement_gateway(cursor.getString(8));
            param.setManagement_snmp(cursor.getString(9));
            param.setRanging_signal(cursor.getString(10));
            param.setRanging_data_rate(cursor.getString(11));
            param.setRanging_fec(cursor.getString(12));
            param.setRanging_power(cursor.getString(13));
            param.setRanging_esno(cursor.getString(14));
            param.setRanging_cno(cursor.getString(15));

            params.add(param);
        }
        cursor.close();
        getReadableDatabase().close();
        return params;
    }

    public Cursor val_paramCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_parameter, " + //0
                            " id_site, " + //1
                            " lan_parameter, " + //2
                            " lan_subnetmask, " + //3
                            " sat_parameter, " + //4
                            " sat_symrate, " + //5
                            " sat_freq, " + //6
                            " management_esnmodem, " + //7
                            " management_gateway, " + //8
                            " management_snmp, " + //9
                            " ranging_signal, " + //10
                            " ranging_data_rate, " + //11
                            " ranging_fec, " + //12
                            " ranging_power, " + //13
                            " ranging_esno, " + //14
                            " ranging_cno " + //15
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
