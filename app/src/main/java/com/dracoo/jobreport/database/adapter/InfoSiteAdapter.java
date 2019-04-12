package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.dracoo.jobreport.database.master.MasterInfoSite;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class InfoSiteAdapter extends DatabaseAdapter {
    private Dao<MasterInfoSite, Integer> mInfo;

    public InfoSiteAdapter(Context context) {
        super(context);
    }

    public Dao<MasterInfoSite, Integer> getAdapter() throws SQLException {
        if (mInfo == null) {
            mInfo = getDao(MasterInfoSite.class);
        }
        return mInfo;
    }

    public ArrayList<MasterInfoSite> load_site(int custId, String un){
        ArrayList<MasterInfoSite> infoSites = new ArrayList<>();
        Cursor cursor = null;

        cursor = load_infoCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterInfoSite infoSite = new MasterInfoSite();
            infoSite.setId_site(cursor.getInt(0));
            infoSite.setLocation_name(cursor.getString(1));
            infoSite.setCustomer_name(cursor.getString(2));
            infoSite.setRemote_address(cursor.getString(3));
            infoSite.setCity(cursor.getString(4));
            infoSite.setKabupaten(cursor.getString(5));
            infoSite.setProv(cursor.getString(6));
            infoSite.setLat(cursor.getString(7));
            infoSite.setLongitude(cursor.getString(8));

            infoSites.add(infoSite);
        }
        cursor.close();
        getReadableDatabase().close();
        return infoSites;
    }

    public Cursor load_infoCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT " +
                            "id_site, " + //0
                            "location_name, " + //1
                            "customer_name, " + //2
                            "remote_address, " + //3
                            "city, " + //4
                            "kabupaten, " + //5
                            "prov, " + //6
                            "lat, " + //7
                            "longitude " +  //8
                "from t_site " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

    public ArrayList<MasterInfoSite> loadMaxsite(String un){
        ArrayList<MasterInfoSite> infoSites = new ArrayList<>();
        Cursor cursor = null;

        cursor = MaxCustid_Cursor(un);

        while (cursor.moveToNext()) {
            MasterInfoSite infoSite = new MasterInfoSite();
            infoSite.setId_site(cursor.getInt(0));

            infoSites.add(infoSite);
        }
        cursor.close();
        getReadableDatabase().close();
        return infoSites;
    }


    public Cursor MaxCustid_Cursor(String un){
        Cursor cursor;

        String sql = "SELECT " +
                "max(id_site) " + //0
                "from t_site " +
                " where un_user = '" + un + "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }
}
