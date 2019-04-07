package com.dracoo.jobreport.feature.action;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.ActionAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.master.MasterAction;
import com.dracoo.jobreport.database.master.MasterTransHistory;
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

public class ActionFragment extends Fragment {
    private MessageUtils messageUtils;
    private Preference preference;
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;
    private String tempDate = "";
    private Dao<MasterAction, Integer> actionAdapter;
    private Dao<MasterTransHistory, Integer> transHistAdapter;

    @BindView(R.id.txt_action_time)
    EditText txt_action_time;
    @BindView(R.id.txt_action_desc)
    EditText txt_action_desc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_action, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageUtils = new MessageUtils(getActivity());
        preference = new Preference(getActivity());

        try{
            actionAdapter = new ActionAdapter(getActivity()).getAdapter();
            transHistAdapter = new TransHistoryAdapter(getActivity()).getAdapter();
        }catch (Exception e){}
    }

    @OnClick(R.id.imgB_action_save)
    void onActionSubmit(){
        if (!validateEmpty()){
            messageUtils.snackBar_message(getActivity().getString(R.string.emptyString),getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
        }else if (preference.getCustID() == 0){
            messageUtils.snackBar_message(getActivity().getString(R.string.customer_validation),getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
        } else if (preference.getConnType().equals("")){
            messageUtils.snackBar_message(getActivity().getString(R.string.connection_validation),getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
        } else{
            getTransAction();
        }
    }

    private boolean validateEmpty(){
        return !txt_action_time.getText().toString().trim().equals("") && !txt_action_desc.getText().toString().trim().equals("");
    }

    private void getTransAction(){
        try{
            MasterAction mAction = new MasterAction();
            mAction.setAction_desc(txt_action_desc.getText().toString().trim());
            mAction.setAction_date_time(txt_action_time.getText().toString().trim());
            mAction.setInsert_date(DateTimeUtils.getCurrentTime().trim());
            mAction.setConn_type(preference.getConnType().trim());
            mAction.setProgress_type(preference.getProgress().trim());
            mAction.setId_site(preference.getCustID());
            mAction.setUn_user(preference.getUn().trim());

            actionAdapter.create(mAction);
            transHist();
        }catch (Exception e){
            messageUtils.toastMessage("err trans action " +e.toString(), ConfigApps.T_ERROR);
        }
    }

    private void transHist(){
        ArrayList<MasterTransHistory> al_valTransHist = new TransHistoryAdapter(getActivity())
                .val_trans(preference.getCustID(), preference.getUn(),getString(R.string.dataM2m_trans));
        if (al_valTransHist.size() > 0){
            try{
                MasterTransHistory mHist = transHistAdapter.queryForId(al_valTransHist.get(0).getId_trans());
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getString(R.string.action_trans));
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setIs_submited(0);

                transHistAdapter.update(mHist);
                messageUtils.toastMessage(getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                setEmptyText();
                if (getActivity() != null){
                    JobReportUtils.hideKeyboard(getActivity());
                }
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist update " +e.toString(), ConfigApps.T_ERROR);
            }
        }else{
            try{
                MasterTransHistory mHist = new MasterTransHistory();
                mHist.setId_site(preference.getCustID());
                mHist.setUn_user(preference.getUn());
                mHist.setInsert_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getString(R.string.action_trans));
                mHist.setIs_submited(0);

                transHistAdapter.create(mHist);
                messageUtils.toastMessage(getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                setEmptyText();
                if (getActivity() != null){
                    JobReportUtils.hideKeyboard(getActivity());
                }
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist insert " +e.toString(), ConfigApps.T_ERROR);
            }
        }
    }

    private void setEmptyText(){
        txt_action_time.setText("");
        txt_action_desc.setText("");
    }

    @OnClick(R.id.imgBtn_actionTimer)
    void getDate(){
        datePicker();
    }

    private void datePicker(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedMonth, selectedDay;
                if (month < 10) {
                    selectedMonth = "0"+month;
                }else{
                    selectedMonth = String.valueOf(month);
                }

                if (dayOfMonth < 10) {
                    selectedDay = "0"+dayOfMonth;
                }else {
                    selectedDay = String.valueOf(dayOfMonth);
                }

                tempDate = String.valueOf(year)+"-"+ selectedMonth +"-"+selectedDay;
                timePicker();
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePicker(){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSecond = c.get(Calendar.SECOND);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String selectedHour, selectedMinutes, selectedSecond;
                if (hourOfDay < 10){
                    selectedHour = "0"+String.valueOf(hourOfDay);
                }else{
                    selectedHour = String.valueOf(hourOfDay);
                }

                if (minute < 10){
                    selectedMinutes = "0"+String.valueOf(minute);
                }else{
                    selectedMinutes = String.valueOf(minute);
                }

                if (mSecond < 10){
                    selectedSecond = "0"+String.valueOf(mSecond);
                }else{
                    selectedSecond = String.valueOf(mSecond);
                }

                String validTime = selectedHour +":"+selectedMinutes+":"+selectedSecond;
                String validDateTime = "";
                if (!tempDate.equals("")){
                    validDateTime = tempDate +", "+validTime;
                }

                txt_action_time.setText(validDateTime.trim());

            }
        }, mHour, mMinute, true);
        timePickerDialog.show();

    }
}
