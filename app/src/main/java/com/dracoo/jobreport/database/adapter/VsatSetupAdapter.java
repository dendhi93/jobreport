package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterVsatSetup;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class VsatSetupAdapter extends DatabaseAdapter {
    private Dao<MasterVsatSetup, Integer> vsatSetups;

    public VsatSetupAdapter(Context context) {
        super(context);
    }

    public Dao<MasterVsatSetup, Integer> getAdapter() throws SQLException {
        if (vsatSetups == null) {
            vsatSetups = getDao(MasterVsatSetup.class);
        }
        return vsatSetups;
    }

    public ArrayList<MasterVsatSetup> val_vsatSetup(int custId, String un){
        ArrayList<MasterVsatSetup> setups = new ArrayList<>();
        Cursor cursor = null;

        cursor = val_vsatSetupCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterVsatSetup setup = new MasterVsatSetup();
            setup.setId_setup(cursor.getInt(0));
            setup.setId_site(cursor.getInt(1));

            setups.add(setup);
        }
        cursor.close();
        getReadableDatabase().close();
        return setups;
    }

    public Cursor val_vsatSetupCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_setup " +
                " id_site " +
                "from vsat_setup " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";


        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }
}
