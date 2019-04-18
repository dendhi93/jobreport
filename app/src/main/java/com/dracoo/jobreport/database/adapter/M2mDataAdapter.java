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
            datam2m.setUsername(cursor.getString(2));
            datam2m.setPassword(cursor.getString(3));
            datam2m.setUser(cursor.getString(4));
            datam2m.setRemote(cursor.getString(5));
            datam2m.setTunnel_id(cursor.getString(6));
            datam2m.setIp_bonding(cursor.getString(7));
            datam2m.setIp_vlan(cursor.getString(8));
            datam2m.setIp_lan(cursor.getString(9));
            datam2m.setSubnet_mask(cursor.getString(10));
            datam2m.setAgg(cursor.getString(11));
            datam2m.setIp_machine(cursor.getString(12));

            datam2ms.add(datam2m);
        }
        cursor.close();
        getReadableDatabase().close();
        return datam2ms;
    }

    public Cursor val_m2mDataCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_data, " + //0
                            " id_site, " + //1
                            " username, " + //2
                            " password, " + //3
                            " user, " + //4
                            " remote, " + //5
                            " tunnel_id, " + //6
                            " ip_bonding, " + //7
                            " ip_vlan, " + //8
                            " ip_lan, " + //9
                            " subnet_mask, " + //10
                            " agg, " + //11
                            " ip_machine "+ //12
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
