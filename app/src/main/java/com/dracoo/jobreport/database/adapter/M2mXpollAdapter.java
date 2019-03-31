package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterXpoll;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class M2mXpollAdapter extends DatabaseAdapter {
    private Dao<MasterXpoll, Integer> xpolL;

    public M2mXpollAdapter(Context context) {
        super(context);
    }

    public Dao<MasterXpoll, Integer> getAdapter() throws SQLException {
        if (xpolL == null) {
            xpolL = getDao(MasterXpoll.class);
        }
        return xpolL;
    }

    public ArrayList<MasterXpoll> val_xpoll(int custId, String un){
        ArrayList<MasterXpoll> xpolls = new ArrayList<>();
        Cursor cursor = null;

        cursor = val_xpollCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterXpoll datam2m = new MasterXpoll();
            datam2m.setId_xpoll(cursor.getInt(0));
            datam2m.setId_site(cursor.getInt(1));

            xpolls.add(datam2m);
        }
        cursor.close();
        getReadableDatabase().close();
        return xpolls;
    }

    public Cursor val_xpollCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_xpoll, " +
                " id_site " +
                "from xpoll " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";


        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }
}
