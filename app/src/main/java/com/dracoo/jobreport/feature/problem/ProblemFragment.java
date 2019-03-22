package com.dracoo.jobreport.feature.problem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProblemFragment extends Fragment {

    private MessageUtils messageUtils;

    @BindView(R.id.rg_prob_closedBy)
    RadioGroup rg_prob_closedBy;
    @BindView(R.id.txt_problem_berangkat)
    EditText txt_problem_berangkat;
    @BindView(R.id.txt_problem_tiba)
    EditText txt_problem_tiba;
    @BindView(R.id.txt_problem_start)
    EditText txt_problem_start;
    @BindView(R.id.txt_problem_finish)
    EditText txt_problem_finish;
    @BindView(R.id.txt_problem_upline)
    EditText txt_problem_upline;
    @BindView(R.id.txt_problem_online)
    EditText txt_problem_online;

    @BindView(R.id.txt_prob_modemDisplay)
    EditText txt_prob_modemDisplay;
    @BindView(R.id.txt_problem_symptom)
    EditText txt_problem_symptom;
    @BindView(R.id.txt_action_text)
    EditText txt_action_text;
    @BindView(R.id.txt_action_pending)
    EditText txt_action_pending;
    @BindView(R.id.txt_action_reasonPending)
    EditText txt_action_reasonPending;
    @BindView(R.id.txt_problem_closedBy)
    EditText txt_problem_closedBy;
    @BindView(R.id.rb_prob_eos)
    RadioButton rb_prob_eos;
    @BindView(R.id.rb_conn_m2m)
    RadioButton rb_conn_m2m;

    private String valueRb = "";
    private String tempDate = "";
    private RadioButton radioButtonClosed ;
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_problem, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        messageUtils = new MessageUtils(getActivity());
        rbListener();
    }


    private void rbListener(){
        rg_prob_closedBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                radioButtonClosed = getView().findViewById(i);
//                valueRb = ""+radioButtonClosed.getText().toString();
                if (i == R.id.rb_prob_eos){
                    valueRb = rb_prob_eos.getText().toString();
                }else{
                    valueRb = rb_conn_m2m.getText().toString();
                }
            }
        });
    }

    @OnClick(R.id.imgB_problem_submit)
    void submitProblem (){

        if (!emptyValidation()){
            messageUtils.snackBar_message(getActivity().getString(R.string.emptyString), getActivity() ,ConfigApps.SNACKBAR_NO_BUTTON);
        }   else if (!rb_prob_eos.isChecked() || !rb_conn_m2m.isChecked()){
            messageUtils.snackBar_message("Mohon dipilih pilihan pada kolom closed", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
        } else {
            messageUtils.toastMessage("coba", ConfigApps.T_DEFAULT);
        }
    }

    @OnClick(R.id.imgB_problem_cancel)
    void probCancel(){
        messageUtils.toastMessage("coba 2", ConfigApps.T_DEFAULT);
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
        timePicker(2);
    }
    @OnClick(R.id.imgBtn_timer4)
    void displayTime4(){
        timePicker(4);
    }
    @OnClick(R.id.imgBtn_timer5)
    void displayTime5(){
        datePicker(5);
    }
    @OnClick(R.id.imgBtn_timer6)
    void displayTime6(){
        datePicker(7);
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
                if (selectedColumn == 2){
                    txt_problem_start.setText(validTime.trim());
                }else if (selectedColumn == 4){
                    txt_problem_finish.setText(validTime.trim());
                }else if (selectedColumn == 1){
                    txt_problem_berangkat.setText(validDateTime.trim());
                }else if (selectedColumn == 3){
                    txt_problem_tiba.setText(validDateTime.trim());
                }else if (selectedColumn == 5){
                    txt_problem_upline.setText(validDateTime.trim());
                }else if (selectedColumn == 7){
                    txt_problem_online.setText(validDateTime.trim());
                }
            }
        }, mHour, mMinute, true);
        timePickerDialog.show();

    }

    private boolean emptyValidation(){
        if (txt_action_pending.getText().toString().trim().equals("")||
            txt_action_reasonPending.getText().toString().trim().equals("") ||
            txt_action_text.getText().toString().trim().equals("") ||
            txt_prob_modemDisplay.getText().toString().trim().equals("") ||
            txt_problem_berangkat.getText().toString().trim().equals("") ||
            txt_problem_closedBy.getText().toString().trim().equals("") ||
            txt_problem_finish.getText().toString().trim().equals("") ||
            txt_problem_online.getText().toString().trim().equals("") ||
            txt_problem_start.getText().toString().trim().equals("") ||
            txt_problem_symptom.getText().toString().trim().equals("") ||
            txt_problem_tiba.getText().toString().trim().equals("") ||
            txt_problem_upline.getText().toString().trim().equals("")){
            return false;
        }
        return true;
    }









}
