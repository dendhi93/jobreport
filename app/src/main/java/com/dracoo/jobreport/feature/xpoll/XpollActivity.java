package com.dracoo.jobreport.feature.xpoll;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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

public class XpollActivity extends AppCompatActivity {

    @BindView(R.id.rg_xpoll)
    RadioGroup rg_xpoll;
    @BindView(R.id.txt_xpoll_dateTime)
    EditText txt_xpoll_dateTime;

    private String selectedRadio = "";
    private RadioButton rbXpoll;
    private MessageUtils messageUtils;
    private String tempXpollDate = "";
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xpoll);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart(){
        super.onStart();

        messageUtils = new MessageUtils(XpollActivity.this);
        xpollRadio();
    }

    private void xpollRadio(){
        rg_xpoll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rbXpoll =  findViewById(i);
                selectedRadio = rbXpoll.getText().toString();
            }
        });
    }

    @OnClick(R.id.imgB_xpoll_submit)
    void submitXpoll(){
        messageUtils.toastMessage("coba", ConfigApps.T_DEFAULT);
    }


    @OnClick(R.id.imgB_xpoll_cancel)
    void cancelXpoll(){
        messageUtils.toastMessage("coba2", ConfigApps.T_DEFAULT);
    }

    @OnClick(R.id.imgBtn_xpoll_timer)
    void clickTimePicker(){
        datePicker();
    }

    private void datePicker(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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

                tempXpollDate = String.valueOf(year)+"-"+ selectedMonth +"-"+selectedDay;
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
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
                if (!tempXpollDate.equals("")){
                    validDateTime = tempXpollDate +", "+validTime;
                }
                txt_xpoll_dateTime.setText(validDateTime.trim());

            }
        }, mHour, mMinute, true);
        timePickerDialog.show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }



}
