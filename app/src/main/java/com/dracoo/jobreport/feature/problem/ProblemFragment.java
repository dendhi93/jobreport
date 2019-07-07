package com.dracoo.jobreport.feature.problem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.ProblemAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.master.MasterProblem;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.feature.MenuActivity;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.DateTimeUtils;
import com.dracoo.jobreport.util.JobReportUtils;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProblemFragment extends Fragment {

    private MessageUtils messageUtils;
    @BindView(R.id.sp_prob_closedBy)
    Spinner sp_prob_closedBy;
    @BindView(R.id.txt_problem_berangkat)
    EditText txt_problem_berangkat;
    @BindView(R.id.txt_problem_tiba)
    EditText txt_problem_tiba;
    @BindView(R.id.txt_problem_start)
    EditText txt_problem_start;
    @BindView(R.id.txt_problem_finish)
    EditText txt_problem_finish;
//    @BindView(R.id.txt_problem_upline)
//    EditText txt_problem_upline;
    @BindView(R.id.txt_problem_online)
    EditText txt_problem_online;
    @BindView(R.id.txt_prob_delay)
    EditText txt_prob_delay;
    @BindView(R.id.txt_prob_actPending)
    EditText txt_prob_actPending;
    @BindView(R.id.txt_problem_restart)
    EditText txt_problem_restart;

    @BindView(R.id.txt_prob_modemDisplay)
    EditText txt_prob_modemDisplay;
    @BindView(R.id.txt_problem_symptom)
    EditText txt_problem_symptom;
    @BindView(R.id.txt_prob_action_text)
    EditText txt_prob_action_text;
//    @BindView(R.id.txt_prob_pending)
//    EditText txt_prob_pending;
    @BindView(R.id.txt_prob_reasonPending)
    EditText txt_prob_reasonPending;
    @BindView(R.id.txt_problem_closedBy)
    EditText txt_problem_closedBy;
    @BindView(R.id.imgB_problem_submit)
    ImageButton imgB_problem_submit;
    @BindView(R.id.imgB_problem_cancel)
    ImageButton imgB_problem_cancel;

    private String tempDate = "";
    private String selectedClosedBy  = "";
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;
    private Dao<MasterProblem, Integer> problemAdapter;
    private Dao<MasterTransHistory, Integer> transAdapter;
    private Preference preference;
    private String[] arrClosedBy;
    private String intentProbEdit;
    private ArrayList<MasterProblem> al_problem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_problem, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        messageUtils = new MessageUtils(getActivity());
        preference = new Preference(getActivity());
        displayClosedSpinner();
        try{
            problemAdapter = new ProblemAdapter(getActivity()).getAdapter();
            transAdapter = new TransHistoryAdapter(getActivity()).getAdapter();
        }catch (Exception e){ Log.d("Err Problem adapter ","" +e.toString());}
        probEditValidation();
    }

    private void probEditValidation(){
        try{
            intentProbEdit = getArguments().getString(ConfigApps.EXTRA_CALLER_VIEW);
            if (intentProbEdit.trim().equals(ConfigApps.VIEW_TYPE)){
                al_problem = new ProblemAdapter(getActivity()).val_prob(preference.getCustID(), preference.getUn());
                if (al_problem.size() > 0){
                    txt_prob_modemDisplay.setText(al_problem.get(0).getModem().trim());
                    txt_problem_symptom.setText(al_problem.get(0).getSymptom().trim());
                    txt_prob_action_text.setText(al_problem.get(0).getAction().trim());
                    txt_problem_berangkat.setText(al_problem.get(0).getBerangkat().trim());
                    txt_problem_tiba.setText(al_problem.get(0).getTiba().trim());
                    txt_problem_start.setText(al_problem.get(0).getStart().trim());
                    txt_problem_finish.setText(al_problem.get(0).getFinish().trim());
                    txt_problem_online.setText(al_problem.get(0).getOnline().trim());
                    txt_prob_reasonPending.setText(al_problem.get(0).getReason().trim());
                    txt_problem_closedBy.setText(al_problem.get(0).getClosed_by().trim());
                    txt_prob_delay.setText(al_problem.get(0).getDelay_reason().trim());
                    txt_prob_actPending.setText(al_problem.get(0).getDelay_activity().trim());
                    txt_problem_restart.setText(al_problem.get(0).getRestart().trim());
                    String selectedClosed = al_problem.get(0).getClosed().trim();
                    if (selectedClosed.trim().equals("EOS")){ sp_prob_closedBy.setSelection(1);
                    }else if (selectedClosed.trim().equals("NOC")){ sp_prob_closedBy.setSelection(2); }

                    imgB_problem_submit.setVisibility(View.GONE);
                    imgB_problem_cancel.setVisibility(View.GONE);
                }
            }
        }catch (Exception e){
            imgB_problem_submit.setVisibility(View.VISIBLE);
            imgB_problem_cancel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        try{
            if (!intentProbEdit.equals("") || intentProbEdit != null){ inflater.inflate(R.menu.menu, menu); }
        }catch (Exception e){}

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_menu :
                imgB_problem_cancel.setVisibility(View.VISIBLE);
                imgB_problem_cancel.setVisibility(View.VISIBLE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @OnClick(R.id.imgB_problem_submit)
    void submitProblem (){
        try{
            if (!emptyValidation()){ messageUtils.snackBar_message(getActivity().getString(R.string.emptyString), getActivity() ,ConfigApps.SNACKBAR_NO_BUTTON);
            }   else if (selectedClosedBy.trim().equals("")){ } else { transProblem(); }
        }catch (Exception e){ messageUtils.snackBar_message("Mohon dipilih pilihan pada kolom closed ", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON); }

    }

    @OnClick(R.id.imgB_problem_cancel)
    void probCancel(){
        emptyProblemText();
    }

    private void emptyProblemText(){
//        txt_prob_pending.setText("");
        txt_prob_reasonPending.setText("");
        txt_prob_action_text.setText("");
        txt_prob_modemDisplay.setText("");
        txt_problem_berangkat.setText("");
        txt_problem_closedBy.setText("");
        txt_problem_finish.setText("");
        txt_problem_online.setText("");
        txt_problem_start.setText("");
        txt_problem_symptom.setText("");
        txt_problem_tiba.setText("");
//        txt_problem_upline.setText("");
        txt_prob_delay.setText("");
        txt_prob_actPending.setText("");
    }
    @OnClick(R.id.imgBtn_timer1)
    void displayTime1(){
        datePicker(1);
    }
    @OnClick(R.id.imgBtn_timer2)
    void displayTime2(){
        datePicker(3);
    }
    @OnClick(R.id.imgBtn_timer3)
    void displayTime3(){
        datePicker(2);
    }
    @OnClick(R.id.imgBtn_timer4)
    void displayTime4(){
        datePicker(4);
    }
    @OnClick(R.id.imgBtn_timer5)
    void displayTime5(){
        datePicker(5);
    }
    @OnClick(R.id.imgBtn_timer6)
    void displayTime6(){
        datePicker(7);
    }
    @OnClick(R.id.imgBtn_timer7)
    void displayTime7(){datePicker(9);}
    @OnClick(R.id.imgBtn_timer8)
    void displayTime8(){datePicker(11);}

    private void displayClosedSpinner(){
        arrClosedBy  = new String[]{"Closed By","EOS", "NOC"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrClosedBy);
        sp_prob_closedBy.setAdapter(adapter);
        sp_prob_closedBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){ selectedClosedBy = adapter.getItem(position); }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void transProblem(){
        if (preference.getCustID() == 0){
            messageUtils.snackBar_message(getActivity().getString(R.string.customer_validation), getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
        }else{
            String stRestart;
            if (txt_problem_restart.getText().toString().trim().equals("")){
                stRestart = "-";
            }else{stRestart = txt_problem_restart.getText().toString().trim();}
            ArrayList<MasterProblem> al_valProb = new ProblemAdapter(getActivity()).val_prob(preference.getCustID(), preference.getUn());
            if (al_valProb.size() > 0){
                try{
                    MasterProblem mProb = problemAdapter.queryForId(al_valProb.get(0).getId_problem());
                    mProb.setModem(txt_prob_modemDisplay.getText().toString().trim());
                    mProb.setSymptom(txt_problem_symptom.getText().toString().trim());
                    mProb.setAction(txt_prob_action_text.getText().toString().trim());
                    mProb.setBerangkat(txt_problem_berangkat.getText().toString().trim());
                    mProb.setTiba(txt_problem_tiba.getText().toString().trim());
                    mProb.setStart(txt_problem_start.getText().toString().trim());
                    mProb.setFinish(txt_problem_finish.getText().toString().trim());
//                    mProb.setUpline(txt_problem_upline.getText().toString().trim());
                    mProb.setOnline(txt_problem_online.getText().toString().trim());
//                    mProb.setPending(txt_prob_pending.getText().toString().trim());
                    mProb.setReason(txt_prob_reasonPending.getText().toString().trim());
                    mProb.setClosed(selectedClosedBy.trim());
                    mProb.setClosed_by(txt_problem_closedBy.getText().toString().trim());
                    mProb.setProgress_type(preference.getProgress().trim());
                    mProb.setUpdate_date(DateTimeUtils.getCurrentTime());
                    mProb.setUn_user(preference.getUn().trim());
                    mProb.setDelay_reason(txt_prob_delay.getText().toString().trim());
                    mProb.setDelay_activity(txt_prob_actPending.getText().toString().trim());
                    mProb.setRestart(stRestart.trim());

                    problemAdapter.update(mProb);
                    transHistProb(ConfigApps.TRANS_HIST_UPDATE);
                }catch (Exception e){ messageUtils.toastMessage("### " +e.toString(), ConfigApps.T_ERROR); }
            } else {
                try{
                    MasterProblem mProb = new MasterProblem();
                    mProb.setModem(txt_prob_modemDisplay.getText().toString().trim());
                    mProb.setSymptom(txt_problem_symptom.getText().toString().trim());
                    mProb.setAction(txt_prob_action_text.getText().toString().trim());
                    mProb.setBerangkat(txt_problem_berangkat.getText().toString().trim());
                    mProb.setTiba(txt_problem_tiba.getText().toString().trim());
                    mProb.setStart(txt_problem_start.getText().toString().trim());
                    mProb.setFinish(txt_problem_finish.getText().toString().trim());
//                    mProb.setUpline(txt_problem_upline.getText().toString().trim());
                    mProb.setOnline(txt_problem_online.getText().toString().trim());
//                    mProb.setPending(txt_prob_pending.getText().toString().trim());
                    mProb.setReason(txt_prob_reasonPending.getText().toString().trim());
                    mProb.setClosed(selectedClosedBy.trim());
                    mProb.setClosed_by(txt_problem_closedBy.getText().toString().trim());
                    mProb.setProgress_type(preference.getProgress().trim());
                    mProb.setInsert_date(DateTimeUtils.getCurrentTime());
                    mProb.setUn_user(preference.getUn().trim());
                    mProb.setId_site(preference.getCustID());
                    mProb.setDelay_reason(txt_prob_delay.getText().toString().trim());
                    mProb.setDelay_activity(txt_prob_actPending.getText().toString().trim());
                    mProb.setRestart(stRestart.trim());

                    problemAdapter.create(mProb);
                    transHistProb(ConfigApps.TRANS_HIST_INSERT);
                }catch (Exception e){ messageUtils.toastMessage("### " +e.toString(), ConfigApps.T_ERROR); }
            }
        }
    }

    private void transHistProb(int transType){
        ArrayList<MasterTransHistory> al_valTransHist = new TransHistoryAdapter(getActivity())
                .val_trans(preference.getCustID(), preference.getUn(), getActivity().getString(R.string.problemDesc_trans));
        if (al_valTransHist.size() > 0){
            try{
                MasterTransHistory mHist = transAdapter.queryForId(al_valTransHist.get(0).getId_trans());
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getActivity().getString(R.string.problemDesc_trans));
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setIs_submited(0);

                transAdapter.update(mHist);
                if (transType == ConfigApps.TRANS_HIST_UPDATE){ messageUtils.toastMessage(getActivity().getString(R.string.transaction_success) + " diupdate", ConfigApps.T_SUCCESS);
                }else{ messageUtils.toastMessage(getActivity().getString(R.string.transaction_success), ConfigApps.T_SUCCESS); }

                emptyProblemText();
                if (getActivity() != null){ JobReportUtils.hideKeyboard(getActivity()); }
            }catch (Exception e){ messageUtils.toastMessage("err trans Hist 1 " +e.toString(), ConfigApps.T_ERROR); }
        }else{
            try{
                MasterTransHistory mHist = new MasterTransHistory();
                mHist.setId_site(preference.getCustID());
                mHist.setUn_user(preference.getUn());
                mHist.setInsert_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getActivity().getString(R.string.problemDesc_trans));
                mHist.setIs_submited(0);

                transAdapter.create(mHist);
                if (transType == ConfigApps.TRANS_HIST_UPDATE){ messageUtils.toastMessage(getActivity().getString(R.string.transaction_success) + " diupdate", ConfigApps.T_SUCCESS);
                }else{ messageUtils.toastMessage(getActivity().getString(R.string.transaction_success), ConfigApps.T_SUCCESS); }

                emptyProblemText();
                if (getActivity() != null){ JobReportUtils.hideKeyboard(getActivity()); }
            }catch (Exception e){ messageUtils.toastMessage("err trans Hist 2 " +e.toString(), ConfigApps.T_ERROR); }
        }
    }

    private void datePicker(final int selectedColumn){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedMonth, selectedDay;
                if (month < 10) { selectedMonth = "0"+month;
                }else{ selectedMonth = String.valueOf(month); }

                if (dayOfMonth < 10) { selectedDay = "0"+dayOfMonth;
                }else { selectedDay = String.valueOf(dayOfMonth); }

                tempDate = year +"-"+ selectedMonth +"-"+selectedDay;
                timePicker(selectedColumn);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePicker(final int selectedColumn){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSecond = c.get(Calendar.SECOND);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String selectedHour, selectedMinutes, selectedSecond;
                if (hourOfDay < 10){ selectedHour = "0"+ hourOfDay;
                }else{ selectedHour = String.valueOf(hourOfDay); }

                if (minute < 10){ selectedMinutes = "0"+ minute;
                }else{ selectedMinutes = String.valueOf(minute); }

                if (mSecond < 10){ selectedSecond = "0"+ mSecond; }
                else{ selectedSecond = String.valueOf(mSecond); }

                String validTime = selectedHour +":"+selectedMinutes+":"+selectedSecond;
                String validDateTime = "";
                if (!tempDate.equals("")) validDateTime = tempDate + ", " + validTime;
                if (selectedColumn == 2){ txt_problem_start.setText(validDateTime.trim());
                }else if (selectedColumn == 4){ txt_problem_finish.setText(validDateTime.trim());
                }else if (selectedColumn == 1){ txt_problem_berangkat.setText(validDateTime.trim());
                }else if (selectedColumn == 3){ txt_problem_tiba.setText(validDateTime.trim());
//                }else if (selectedColumn == 5){ txt_problem_upline.setText(validDateTime.trim());
                }else if (selectedColumn == 7){ txt_problem_online.setText(validDateTime.trim());
                }else if (selectedColumn == 9){ txt_prob_actPending.setText(validDateTime.trim());
                }else if (selectedColumn == 11){ txt_problem_restart.setText(validDateTime.trim());}
            }
        }, mHour, mMinute, true);
        timePickerDialog.show();

    }

    private boolean emptyValidation(){
//        if (txt_prob_pending.getText().toString().trim().equals("")||
        if (txt_prob_reasonPending.getText().toString().trim().equals("") ||
            txt_prob_action_text.getText().toString().trim().equals("") ||
            txt_prob_modemDisplay.getText().toString().trim().equals("") ||
            txt_problem_berangkat.getText().toString().trim().equals("") ||
            txt_problem_closedBy.getText().toString().trim().equals("") ||
            txt_problem_finish.getText().toString().trim().equals("") ||
            txt_problem_online.getText().toString().trim().equals("") ||
            txt_problem_start.getText().toString().trim().equals("") ||
            txt_problem_symptom.getText().toString().trim().equals("") ||
            txt_problem_tiba.getText().toString().trim().equals("") ||
//            txt_problem_upline.getText().toString().trim().equals("") ||
            txt_prob_delay.getText().toString().trim().equals("") ||
            txt_prob_actPending.getText().toString().trim().equals("")){
            return false;
        }
        return true;
    }
}
