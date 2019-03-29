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

            replaces.add(replace);
        }
        cursor.close();
        getReadableDatabase().close();
        return replaces;
    }

    public Cursor val_m2mReplaceCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_replace, " +
                " id_site " +
                "from m2m_setup " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";


        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }
}
