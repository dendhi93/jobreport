package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterM2mReplace;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class M2mReplaceAdapter extends DatabaseAdapter {
    private Dao<MasterM2mReplace, Integer> m2MReplace;

    public M2mReplaceAdapter(Context context) {
        super(context);
    }

    public Dao<MasterM2mReplace, Integer> getAdapter() throws SQLException {
        if (m2MReplace == null) {
            m2MReplace = getDao(MasterM2mReplace.class);
        }
        return m2MReplace;
    }

    public ArrayList<MasterM2mReplace> val_m2mReplace(int custId, String un){
        ArrayList<MasterM2mReplace> replaces = new ArrayList<>();
        Cursor cursor = null;

        cursor = val_m2mReplaceCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterM2mReplace replace = new MasterM2mReplace();
            replace.setId_replace(cursor.getInt(0));
            replace.setId_site(cursor.getInt(1));
            replace.setBrand_type_replace(cursor.getString(2));
            replace.setSn_replace(cursor.getString(3));
            replace.setBrand_type_adaptor(cursor.getString(4));
            replace.setSn_adaptor(cursor.getString(5));
            replace.setSim_card1_type(cursor.getString(6));
            replace.setSim_card1_sn(cursor.getString(7));
            replace.setSim_card1_puk(cursor.getString(8));
            replace.setSim_card2_type(cursor.getString(9));
            replace.setSim_card2_sn(cursor.getString(10));
            replace.setSim_card2_puk(cursor.getString(11));

            replaces.add(replace);
        }
        cursor.close();
        getReadableDatabase().close();
        return replaces;
    }

    public Cursor val_m2mReplaceCursor(int custId, String un){
        Cursor cursor;

        String sql = " SELECT id_replace, " + //0
                            " id_site, " + //1
                            " brand_type_replace, " + //2
                            " sn_replace, " + //3
                            " brand_type_adaptor, " + //4
                            " sn_adaptor, " + //5
                            " sim_card1_type, " + //6
                            " sim_card1_sn, " + //7
                            " sim_card1_puk, " + //8
                            " sim_card2_type, " + //9
                            " sim_card2_sn, " + //10
                            " sim_card2_puk " + //11
                    " from m2m_replace " +
                    " where id_site = " + custId + " " +
                    " and un_user = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

    public boolean isM2mReplaceEmpty(String un, int custId){
        Cursor cursor;

        String sql = " SELECT id_replace, " +
                " id_site " +
                " from m2m_replace " +
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
