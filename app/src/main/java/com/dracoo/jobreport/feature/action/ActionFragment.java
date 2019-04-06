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
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.DateTimeUtils;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActionFragment extends Fragment {
    private MessageUtils messageUtils;
    private Preference preference;
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;
    private String tempDate = "";

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
    }


    @OnClick(R.id.imgB_action_save)
    void onActionSubmit(){
        if (!validateEmpty()){
            messageUtils.snackBar_message(getActivity().getString(R.string.emptyString),getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
        }else{
            messageUtils.toastMessage("coba", ConfigApps.T_INFO);
        }

    }

    private boolean validateEmpty(){
        if (txt_action_time.getText().toString().trim().equals("") || txt_action_desc.getText().toString().trim().equals("")){
            return false;
        }else{
            return true;
        }
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
