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
            setup.setSn_modem(cursor.getString(2));
            setup.setSn_adaptor(cursor.getString(3));
            setup.setSn_fh(cursor.getString(4));
            setup.setSn_lnb(cursor.getString(5));
            setup.setSn_rfu(cursor.getString(6));
            setup.setSn_dip_odu(cursor.getString(7));
            setup.setSn_dip_idu(cursor.getString(8));
            setup.setAntena_size(cursor.getString(9));
            setup.setAntena_brand(cursor.getString(10));
            setup.setPedestal_type(cursor.getString(11));
            setup.setAccess_type(cursor.getString(12));
            setup.setAntena_type(cursor.getString(13));

            setups.add(setup);
        }
        cursor.close();
        getReadableDatabase().close();
        return setups;
    }

    public Cursor val_vsatSetupCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_setup, " + //0
                            " id_site, " + //1
                            " sn_modem, " + //2
                            " sn_adaptor, " + //3
                            " sn_fh, " + //4
                            " sn_lnb, " + //5
                            " sn_rfu, " + //6
                            " sn_dip_odu, " + //7
                            " sn_dip_idu, " + //8
                            " antena_size, " + //9
                            " antena_brand, " + //10
                            " pedestal_type, " + //11
                            " access_type, " + // 12
                            " antena_type " +
                "from vsat_setup " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";


        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }
}
