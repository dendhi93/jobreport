package com.dracoo.jobreport.database.adapter;

import android.content.Context;
import android.database.Cursor;

import com.dracoo.jobreport.database.master.MasterProblem;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProblemAdapter extends DatabaseAdapter {
    private Dao<MasterProblem, Integer> mProblem;

    public ProblemAdapter(Context context) {
        super(context);
    }

    public Dao<MasterProblem, Integer> getAdapter() throws SQLException {
        if (mProblem == null) {
            mProblem = getDao(MasterProblem.class);
        }
        return mProblem;
    }


    public ArrayList<MasterProblem> val_prob(int custId, String un){
        ArrayList<MasterProblem> probs = new ArrayList<>();
        Cursor cursor = null;

        cursor = val_probCursor(custId, un);

        while (cursor.moveToNext()) {
            MasterProblem problem = new MasterProblem();
            problem.setId_problem(cursor.getInt(0));
            problem.setId_site(cursor.getInt(1));

            probs.add(problem);
        }
        cursor.close();
        getReadableDatabase().close();
        return probs;
    }

    public Cursor val_probCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_problem " +
                " id_site " +
                "from t_problem " +
                " where id_site = " + custId + " " +
                " and un_user = '" +un+ "' ";


        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

}
