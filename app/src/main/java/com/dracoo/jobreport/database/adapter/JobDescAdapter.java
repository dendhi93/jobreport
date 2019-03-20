package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterJobDesc;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class JobDescAdapter extends DatabaseAdapter{
    private Dao<MasterJobDesc, Integer> mJobDesc;


    public JobDescAdapter(Context context) {
        super(context);
    }

    public Dao<MasterJobDesc, Integer> getAdapter() throws SQLException {
        if (mJobDesc == null) {
            mJobDesc = getDao(MasterJobDesc.class);
        }
        return mJobDesc;
    }

    public ArrayList<MasterJobDesc> load_trans(int custId, String un){
        ArrayList<MasterJobDesc> jobDescs = new ArrayList<>();
        Cursor cursor = null;

        cursor = load_JobCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterJobDesc mJobDesc = new MasterJobDesc();
            mJobDesc.setId_jobdesc(cursor.getInt(0));
            mJobDesc.setName_user(cursor.getString(1));
            mJobDesc.setPic_phone(cursor.getString(2));

            jobDescs.add(mJobDesc);
        }
        cursor.close();
        getReadableDatabase().close();
        return jobDescs;
    }


    public Cursor load_JobCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT " +
                    "id_jobdesc, " + //0
                    "name_user, " + //1
                    "pic_phone " + //1
                "from t_jobdesc " +
                " where id_site = " + custId + " " +
                " and name_user = '" +un+ "' ";

        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }
}
