package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class TransHistoryAdapter extends DatabaseAdapter {
    private Dao<MasterTransHistory, Integer> transHist;

    public TransHistoryAdapter(Context context) {
        super(context);
    }

    public Dao<MasterTransHistory, Integer> getAdapter() throws SQLException {
        if (transHist == null) {
            transHist = getDao(MasterTransHistory.class);
        }
        return transHist;
    }


    public ArrayList<MasterTransHistory> load_trans(int custId, String un){
        ArrayList<MasterTransHistory> transHists = new ArrayList<>();
        Cursor cursor = null;

        cursor = load_transCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterTransHistory mTransHist = new MasterTransHistory();
            mTransHist.setId_trans(cursor.getInt(0));
            mTransHist.setTrans_step(cursor.getString(1));
            mTransHist.setIs_submited(cursor.getInt(2));

            transHists.add(mTransHist);
        }
        cursor.close();
        getReadableDatabase().close();
        return transHists;
    }

    public Cursor load_transCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT " +
                                "id_trans, " + //0
                                "trans_step, " + //1
                                "is_submited " + //2
                    "from t_trans_history " +
                    " where id_site = " + custId + " " +
                    " and un_user = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }


    public ArrayList<MasterTransHistory> val_trans(int custId, String un, String transStep){
        ArrayList<MasterTransHistory> transHists = new ArrayList<>();
        Cursor cursor = null;

        cursor = val_transCursor(custId, un, transStep);

        while (cursor.moveToNext()) {
            MasterTransHistory mTransHist = new MasterTransHistory();
            mTransHist.setId_trans(cursor.getInt(0));

            transHists.add(mTransHist);
        }
        cursor.close();
        getReadableDatabase().close();
        return transHists;
    }

    public Cursor val_transCursor(int custId, String un, String transStep){
        Cursor cursor;

        String sql = "SELECT id_trans " +
                "from t_trans_history " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' " +
                " and trans_step = '"+transStep+"' " +
                " and is_submited = 0 ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

}
