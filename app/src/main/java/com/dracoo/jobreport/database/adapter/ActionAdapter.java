package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterAction;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class ActionAdapter extends DatabaseAdapter {
    private Dao<MasterAction, Integer> mAction;

    public ActionAdapter(Context context) {
        super(context);
    }

    public Dao<MasterAction, Integer> getAdapter() throws SQLException {
        if (mAction == null) {
            mAction = getDao(MasterAction.class);
        }
        return mAction;
    }

    public ArrayList<MasterAction> load_dataAction(int custId, String un){
        ArrayList<MasterAction> actions = new ArrayList<>();
        Cursor cursor = null;

        cursor = load_actionCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterAction action = new MasterAction();
            action.setId_action(cursor.getInt(0));
            action.setId_site(cursor.getInt(1));
            action.setAction_date_time(cursor.getString(2));
            action.setAction_desc(cursor.getString(3));

            actions.add(action);
        }
        cursor.close();
        getReadableDatabase().close();
        return actions;
    }


    public Cursor load_actionCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_action, " +
                " id_site, " +
                " action_date_time, " +
                " action_desc " +
                "from t_action " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }
}
