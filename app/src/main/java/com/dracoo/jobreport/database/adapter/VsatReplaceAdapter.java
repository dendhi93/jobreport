package com.dracoo.jobreport.database.adapter;
import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterVsatReplace;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class VsatReplaceAdapter extends DatabaseAdapter {
    private Dao<MasterVsatReplace, Integer> vsatReplace;

    public VsatReplaceAdapter(Context context) {
        super(context);
    }

    public Dao<MasterVsatReplace, Integer> getAdapter() throws SQLException {
        if (vsatReplace == null) {
            vsatReplace = getDao(MasterVsatReplace.class);
        }
        return vsatReplace;
    }


    public ArrayList<MasterVsatReplace> val_vsatReplace(int custId, String un){
        ArrayList<MasterVsatReplace> replaces = new ArrayList<>();
        Cursor cursor = null;

        cursor = val_vsatReplaceCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterVsatReplace replace = new MasterVsatReplace();
            replace.setId_replace(cursor.getInt(0));
            replace.setId_site(cursor.getInt(1));
            replace.setSn_modem(cursor.getString(2));
            replace.setSn_adaptor(cursor.getString(3));
            replace.setSn_fh(cursor.getString(4));
            replace.setSn_lnb(cursor.getString(5));
            replace.setSn_rfu(cursor.getString(6));
            replace.setSn_dip_odu(cursor.getString(7));
            replace.setSn_dip_idu(cursor.getString(8));

            replaces.add(replace);
        }
        cursor.close();
        getReadableDatabase().close();
        return replaces;
    }

    public Cursor val_vsatReplaceCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_replace, " + //0
                            " id_site, " + //1
                            " sn_modem, " + //2
                            " sn_adaptor, " + //3
                            " sn_fh, " + //4
                            " sn_lnb, " + //5
                            " sn_rfu, " + //6
                            " sn_dip_odu, " + //7
                            " sn_dip_idu " + //8
                " from vsat_replace " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

    public boolean isVsatReplaceEmpty(String un, int custId){
        Cursor cursor;

        String sql = "SELECT id_replace, " +
                " id_site " +
                "from vsat_replace " +
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
