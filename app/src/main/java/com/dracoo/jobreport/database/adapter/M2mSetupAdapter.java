package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.dracoo.jobreport.database.master.MasterM2mSetup;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class M2mSetupAdapter extends DatabaseAdapter {
    private Dao<MasterM2mSetup, Integer> m2mSetups;

    public M2mSetupAdapter(Context context) {
        super(context);
    }

    public Dao<MasterM2mSetup, Integer> getAdapter() throws SQLException {
        if (m2mSetups == null) {
            m2mSetups = getDao(MasterM2mSetup.class);
        }
        return m2mSetups;
    }

    public ArrayList<MasterM2mSetup> val_m2mSetup(int custId, String un){
        ArrayList<MasterM2mSetup> setups = new ArrayList<>();
        Cursor cursor = null;

        cursor = val_m2mSetupCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterM2mSetup setup = new MasterM2mSetup();
            setup.setId_setup(cursor.getInt(0));
            setup.setId_site(cursor.getInt(1));
            setup.setBrand_type_m2m(cursor.getString(2));
            setup.setSn_m2m(cursor.getString(3));
            setup.setBrand_type_adaptor(cursor.getString(4));
            setup.setSn_adaptor(cursor.getString(5));
            setup.setSim_card1_type(cursor.getString(6));
            setup.setSim_card1_sn(cursor.getString(7));
            setup.setSim_card1_puk(cursor.getString(8));
            setup.setSim_card2_type(cursor.getString(9));
            setup.setSim_card2_sn(cursor.getString(10));
            setup.setSim_card2_puk(cursor.getString(11));


            setups.add(setup);
        }
        cursor.close();
        getReadableDatabase().close();
        return setups;
    }

    public Cursor val_m2mSetupCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_setup, " + //0
                            " id_site, " + //1
                            " brand_type_m2m, " + //2
                            " sn_m2m, " + //3
                            " brand_type_adaptor, " + //4
                            " sn_adaptor, " + //5
                            " sim_card1_type, " + //6
                            " sim_card1_sn, " + //7
                            " sim_card1_puk, " + //8
                            " sim_card2_type, " + //9
                            " sim_card2_sn, " + //10
                            " sim_card2_puk " + //11
                "from m2m_setup " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";

        Log.d("sql",""+sql);
        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }
}
