package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterEnvirontment;
import com.dracoo.jobreport.database.master.MasterMachine;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class MachineAdapter extends DatabaseAdapter {
    private Dao<MasterMachine, Integer> mMachine;

    public MachineAdapter(Context context) {
        super(context);
    }

    public Dao<MasterMachine, Integer> getAdapter() throws SQLException {
        if (mMachine == null) {
            mMachine = getDao(MasterMachine.class);
        }
        return mMachine;
    }


    public ArrayList<MasterMachine> val_machine(int custId, String un){
        ArrayList<MasterMachine> envs = new ArrayList<>();
        Cursor cursor = null;

        cursor = val_machineCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterMachine mEnv = new MasterMachine();
            mEnv.setId_machine(cursor.getInt(0));
            mEnv.setId_site(cursor.getInt(1));
            mEnv.setMachine_type(cursor.getString(2));
            mEnv.setMachine_qty(cursor.getInt(3));
            mEnv.setMachine_no(cursor.getString(4));
            mEnv.setAccess_type(cursor.getString(5));

            envs.add(mEnv);
        }
        cursor.close();
        getReadableDatabase().close();
        return envs;
    }

    public Cursor val_machineCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_machine, " +
                            " id_site, " +
                            " machine_type, " +
                            " machine_qty, " +
                            " machine_no, " +
                            " access_type " +
                "from t_machine " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

    public boolean isMachineEmpty(String un, int custId){
        Cursor cursor;

        String sql = "SELECT id_machine, " +
                " id_site " +
                "from t_machine " +
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
