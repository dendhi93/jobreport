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
            problem.setModem(cursor.getString(2));
            problem.setSymptom(cursor.getString(3));
            problem.setAction(cursor.getString(4));
            problem.setBerangkat(cursor.getString(5));
            problem.setTiba(cursor.getString(6));
            problem.setFinish(cursor.getString(7));
            problem.setUpline(cursor.getString(8));
            problem.setOnline(cursor.getString(9));
            problem.setPending(cursor.getString(10));
            problem.setReason(cursor.getString(11));
            problem.setClosed(cursor.getString(12));
            problem.setClosed_by(cursor.getString(13));
            problem.setDelay_reason(cursor.getString(14));
            problem.setDelay_activity(cursor.getString(15));
            problem.setStart(cursor.getString(16));


            probs.add(problem);
        }
        cursor.close();
        getReadableDatabase().close();
        return probs;
    }

    public Cursor val_probCursor(int custId, String un){
        Cursor cursor;

        String sql = "SELECT id_problem, " + //0
                            " id_site, " + //1
                            " modem, " + //2
                            " symptom, " + //3
                            " action, " + //4
                            " berangkat, " + //5
                            " tiba, " + //6
                            " finish, " + //7
                            " upline, " + //8
                            " online, " + //9
                            " pending, " + //10
                            " reason, " + //11
                            " closed, " + //12
                            " closed_by, " + //13
                            " delay_reason, " + //14
                            " delay_activity, " + //15
                            " start " + //16
                    "from t_problem " +
                    " where id_site = " + custId + " " +
                    " and un_user = '" +un+ "' ";


        cursor = getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

    public boolean isProblemEmpty(String un, int custId){
        Cursor cursor;

        String sql = "SELECT id_problem, " +
                " id_site " +
                "from t_problem " +
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
