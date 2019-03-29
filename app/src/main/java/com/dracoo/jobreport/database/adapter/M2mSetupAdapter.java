package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

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

            setups.add(setup);
        }
        cursor.close();
        getReadableDatabase().close();
        return setups;
    }

    public Cursor val_m2mSetupCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_setup, " +
                " id_site " +
                "from m2m_setup " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";


        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }
}
