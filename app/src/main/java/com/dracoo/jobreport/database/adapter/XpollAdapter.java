package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterXpoll;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class XpollAdapter extends DatabaseAdapter {
    private Dao<MasterXpoll, Integer> xpolL;

    public XpollAdapter(Context context) {
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
            MasterXpoll xpoll = new MasterXpoll();
            xpoll.setId_xpoll(cursor.getInt(0));
            xpoll.setId_site(cursor.getInt(1));
            xpoll.setSat(cursor.getString(2));
            xpoll.setTransponder(cursor.getString(3));
            xpoll.setLft(cursor.getString(4));
            xpoll.setCn(cursor.getString(5));
            xpoll.setCpi(cursor.getString(6));
            xpoll.setAsi(cursor.getString(7));
            xpoll.setInsert_time(cursor.getString(8));
            xpoll.setOp(cursor.getString(9));

            xpolls.add(xpoll);
        }
        cursor.close();
        getReadableDatabase().close();
        return xpolls;
    }

    public Cursor val_xpollCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_xpoll, " + //0
                            " id_site, " + //1
                            " sat, " + //2
                            " transponder, " + //3
                            " lft, " + //4
                            " cn, " + //5
                            " cpi, " + //6
                            " asi, " + //7
                            " insert_time, " + //8
                            " op " + //9
                "from xpoll " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";


        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

    public boolean isVsatXpollEmpty(String un, int custId){
        Cursor cursor;

        String sql = "SELECT id_xpoll, " +
                " id_site " +
                "from xpoll " +
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
