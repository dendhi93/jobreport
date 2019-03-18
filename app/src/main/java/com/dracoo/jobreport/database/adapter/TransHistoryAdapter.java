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


    public ArrayList<MasterTransHistory> load_trans(String custId, String un){
        ArrayList<MasterTransHistory> transHists = new ArrayList<>();
        Cursor cursor = null;

        cursor = load_transCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterTransHistory mTransHist = new MasterTransHistory();
            mTransHist.setId_trans(cursor.getInt(0));
            mTransHist.setTrans_step(cursor.getString(1));
            mTransHist.setIs_submited(cursor.getString(2));

            transHists.add(mTransHist);
        }
        cursor.close();
        getReadableDatabase().close();
        return transHists;
    }

    public Cursor load_transCursor(String custId, String un){
        Cursor cursor;

        String sql = "SELECT " +
                                "id_trans, " + //0
                                "trans_step " + //1
                                "is_submited " + //1
                    "from t_trans_history " +
                    " where id_site like " + custId + "' " +
                    " and un_user = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }
}
