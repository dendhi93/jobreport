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
import com.dracoo.jobreport.database.adapter.M2mXpollAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.database.master.MasterXpoll;
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

public class XpollActivity extends AppCompatActivity {

    @BindView(R.id.rg_xpoll)
    RadioGroup rg_xpoll;
    @BindView(R.id.txt_xpoll_dateTime)
    EditText txt_xpoll_dateTime;
    @BindView(R.id.txt_xpoll_transponder)
    EditText txt_xpoll_transponder;
    @BindView(R.id.txt_xpoll_lft)
    EditText txt_xpoll_lft;
    @BindView(R.id.txt_xpoll_cn)
    EditText txt_xpoll_cn;
    @BindView(R.id.txt_xpoll_cpi)
    EditText txt_xpoll_cpi;
    @BindView(R.id.txt_xpoll_asi)
    EditText txt_xpoll_asi;
    @BindView(R.id.txt_xpoll_op)
    EditText txt_xpoll_op;

    private String selectedRadio = "null";
    private RadioButton rbXpoll;
    private MessageUtils messageUtils;
    private String tempXpollDate = "";
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;
    private Dao<MasterTransHistory, Integer> transHistAdapter;
    private Dao<MasterXpoll, Integer> xpollAdapter;
    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xpoll);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null){ getSupportActionBar().setDisplayHomeAsUpEnabled(true); }

        if (getSupportActionBar() != null){ getSupportActionBar().setSubtitle("VSAT"); }
    }

    @Override
    public void onStart(){
        super.onStart();

        messageUtils = new MessageUtils(XpollActivity.this);
        preference = new Preference(XpollActivity.this);
        xpollRadio();

        try{
            transHistAdapter = new TransHistoryAdapter(getApplicationContext()).getAdapter();
            xpollAdapter = new M2mXpollAdapter(getApplicationContext()).getAdapter();
        }catch (Exception e){

        }
    }

    private void xpollRadio(){
        rg_xpoll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rbXpoll =  findViewById(i);
                selectedRadio = ""+rbXpoll.getText().toString();
            }
        });
    }

    @OnClick(R.id.imgB_xpoll_submit)
    void submitXpoll(){
        if (!validateEmpty()){
            messageUtils.snackBar_message(getString(R.string.emptyString), XpollActivity.this,ConfigApps.SNACKBAR_NO_BUTTON);
        }else if (preference.getCustID() == 0){
            messageUtils.snackBar_message(getString(R.string.customer_validation), XpollActivity.this, ConfigApps.SNACKBAR_NO_BUTTON);
        } else{
            xpollTrans();
        }
    }

    private boolean validateEmpty(){
        if (txt_xpoll_dateTime.getText().toString().trim().equals("") ||
                txt_xpoll_transponder.getText().toString().trim().equals("") ||
                txt_xpoll_lft.getText().toString().trim().equals("") ||
                txt_xpoll_cn.getText().toString().trim().equals("") ||
                txt_xpoll_cpi.getText().toString().trim().equals("") ||
                txt_xpoll_asi.getText().toString().trim().equals("") ||
                txt_xpoll_op.getText().toString().trim().equals("") ||
                selectedRadio.equals("null") || selectedRadio == null){
            return false;
        }else {
            return true;
        }
    }

    private void xpollTrans(){
        ArrayList<MasterXpoll> al_valXpoll = new M2mXpollAdapter(getApplicationContext()).val_xpoll(preference.getCustID(), preference.getUn());
        if (al_valXpoll.size() > 0){
            try{
                MasterXpoll masterXpoll = xpollAdapter.queryForId(al_valXpoll.get(0).getId_xpoll());
                masterXpoll.setSat(selectedRadio.trim());
                masterXpoll.setTransponder(txt_xpoll_transponder.getText().toString().trim());
                masterXpoll.setLft(txt_xpoll_lft.getText().toString().trim());
                masterXpoll.setCn(txt_xpoll_cn.getText().toString().trim());
                masterXpoll.setCpi(txt_xpoll_cpi.getText().toString().trim());
                masterXpoll.setAsi(txt_xpoll_asi.getText().toString().trim());
                masterXpoll.setOp(txt_xpoll_op.getText().toString().trim());
                masterXpoll.setInsert_time(txt_xpoll_dateTime.getText().toString().trim());
                masterXpoll.setUpdate_date(DateTimeUtils.getCurrentTime());

                xpollAdapter.update(masterXpoll);
                transHist(ConfigApps.TRANS_HIST_UPDATE);
            }catch (Exception e){
                messageUtils.toastMessage("Err xpoll update " +e.toString(), ConfigApps.T_ERROR);
            }
        }else{
            try{
                MasterXpoll masterXpoll = new MasterXpoll();
                masterXpoll.setProgress_type(preference.getProgress().trim());
                masterXpoll.setConnection_type(preference.getConnType().trim());
                masterXpoll.setId_site(preference.getCustID());
                masterXpoll.setUn_user(preference.getUn());
                masterXpoll.setInsert_date(DateTimeUtils.getCurrentTime());
                masterXpoll.setInsert_time(txt_xpoll_dateTime.getText().toString().trim());
                masterXpoll.setSat(selectedRadio.trim());
                masterXpoll.setTransponder(txt_xpoll_transponder.getText().toString().trim());
                masterXpoll.setLft(txt_xpoll_lft.getText().toString().trim());
                masterXpoll.setCn(txt_xpoll_cn.getText().toString().trim());
                masterXpoll.setCpi(txt_xpoll_cpi.getText().toString().trim());
                masterXpoll.setAsi(txt_xpoll_asi.getText().toString().trim());
                masterXpoll.setOp(txt_xpoll_op.getText().toString().trim());

                xpollAdapter.create(masterXpoll);
                transHist(ConfigApps.TRANS_HIST_INSERT);
            }catch (Exception e){
                messageUtils.toastMessage("Err xpoll insert " +e.toString(), ConfigApps.T_ERROR);
            }
        }
    }

    private void transHist(int transType){
        ArrayList<MasterTransHistory> al_valTransHist = new TransHistoryAdapter(getApplicationContext())
                .val_trans(preference.getCustID(), preference.getUn(),getString(R.string.dataM2m_trans));
        if (al_valTransHist.size() > 0){
            try{
                MasterTransHistory mHist = transHistAdapter.queryForId(al_valTransHist.get(0).getId_trans());
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getString(R.string.xpoll_trans));
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setIs_submited(0);

                transHistAdapter.update(mHist);
                if (transType == ConfigApps.TRANS_HIST_UPDATE){
                    messageUtils.toastMessage(getString(R.string.transaction_success) +" diupdate", ConfigApps.T_SUCCESS);
                }else{
                    messageUtils.toastMessage(getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                }
                setEmptyText();
                JobReportUtils.hideKeyboard(XpollActivity.this);
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist update " +e.toString(), ConfigApps.T_ERROR);
            }
        }else{
            try{
                MasterTransHistory mHist = new MasterTransHistory();
                mHist.setId_site(preference.getCustID());
                mHist.setUn_user(preference.getUn());
                mHist.setInsert_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getString(R.string.xpoll_trans));
                mHist.setIs_submited(0);

                transHistAdapter.create(mHist);
                if (transType == ConfigApps.TRANS_HIST_UPDATE){
                    messageUtils.toastMessage(getString(R.string.transaction_success) +" diupdate", ConfigApps.T_SUCCESS);
                }else{
                    messageUtils.toastMessage(getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                }
                setEmptyText();
                JobReportUtils.hideKeyboard(XpollActivity.this);
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist insert " +e.toString(), ConfigApps.T_ERROR);
            }
        }
    }

    @OnClick(R.id.imgB_xpoll_cancel)
    void cancelXpoll(){
        setEmptyText();
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

    private void setEmptyText(){
        txt_xpoll_transponder.setText("");
        txt_xpoll_lft.setText("");
        txt_xpoll_cn.setText("");
        txt_xpoll_cpi.setText("");
        txt_xpoll_asi.setText("");
        txt_xpoll_op.setText("");
        txt_xpoll_dateTime.setText("");
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
