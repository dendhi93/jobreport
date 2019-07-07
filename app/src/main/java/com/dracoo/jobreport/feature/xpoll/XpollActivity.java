package com.dracoo.jobreport.feature.xpoll;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.XpollAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.database.master.MasterXpoll;
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

public class XpollActivity extends AppCompatActivity {

    @BindView(R.id.sp_xpoll_choose)
    Spinner sp_xpoll_choose;
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
    @BindView(R.id.imgB_xpoll_submit)
    ImageButton imgB_xpoll_submit;
    @BindView(R.id.imgB_xpoll_cancel)
    ImageButton imgB_xpoll_cancel;

    private String selectedRadio = "";
    private MessageUtils messageUtils;
    private String tempXpollDate = "";
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;
    private Dao<MasterTransHistory, Integer> transHistAdapter;
    private Dao<MasterXpoll, Integer> xpollAdapter;
    private Preference preference;
    private String[] arrXpoll;
    private String intentXpollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xpoll);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null){ getSupportActionBar().setDisplayHomeAsUpEnabled(true); }
        if (getSupportActionBar() != null){ getSupportActionBar().setSubtitle(getString(R.string.vsat)); }
    }

    @Override
    public void onStart(){
        super.onStart();

        messageUtils = new MessageUtils(XpollActivity.this);
        preference = new Preference(XpollActivity.this);
        displaySpinnerXpoll();
        try{
            transHistAdapter = new TransHistoryAdapter(getApplicationContext()).getAdapter();
            xpollAdapter = new XpollAdapter(getApplicationContext()).getAdapter();
        }catch (Exception e){ }
        viewXpoll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try{
            getMenuInflater().inflate(R.menu.menu, menu);
        }catch (Exception e){}
        return true;
    }

    private void viewXpoll(){
        try{
            intentXpollView = getIntent().getStringExtra(ConfigApps.EXTRA_CALLER_VIEW);
            if (intentXpollView.equals("") || intentXpollView != null){
                ArrayList<MasterXpoll> al_xpoll = new XpollAdapter(getApplicationContext()).val_xpoll(preference.getCustID(), preference.getUn());
                if (al_xpoll.size() > 0){
                    String chooseSat = al_xpoll.get(0).getSat().trim();
                    if (chooseSat.equals(getString(R.string.t3s))){sp_xpoll_choose.setSelection(1);
                    }else if (chooseSat.equals(getString(R.string.apt6))){sp_xpoll_choose.setSelection(2);
                    }else if (chooseSat.equals(getString(R.string.apt9))){ sp_xpoll_choose.setSelection(3); }
                    txt_xpoll_dateTime.setText(al_xpoll.get(0).getInsert_time().trim());
                    txt_xpoll_transponder.setText(al_xpoll.get(0).getTransponder().trim());
                    txt_xpoll_lft.setText(al_xpoll.get(0).getLft().trim());
                    txt_xpoll_cn.setText(al_xpoll.get(0).getCn().trim());
                    txt_xpoll_cpi.setText(al_xpoll.get(0).getCpi().trim());
                    txt_xpoll_asi.setText(al_xpoll.get(0).getAsi().trim());
                    txt_xpoll_op.setText(al_xpoll.get(0).getOp().trim());

                    imgB_xpoll_submit.setVisibility(View.GONE);
                    imgB_xpoll_cancel.setVisibility(View.GONE);
                }
            }else{
                imgB_xpoll_submit.setVisibility(View.VISIBLE);
                imgB_xpoll_cancel.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            Log.d("###",""+e+toString());
            imgB_xpoll_submit.setVisibility(View.VISIBLE);
            imgB_xpoll_cancel.setVisibility(View.VISIBLE);
        }

    }
    private void displaySpinnerXpoll(){
        arrXpoll = new String[]{getString(R.string.sat),
                getString(R.string.t3s),
                getString(R.string.apt6),
                getString(R.string.apt9)};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrXpoll);
        sp_xpoll_choose.setAdapter(adapter);
        sp_xpoll_choose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){selectedRadio = adapter.getItem(position);}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
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
                selectedRadio.equals("")){
            return false;
        }else {
            return true;
        }
    }

    private void xpollTrans(){
        ArrayList<MasterXpoll> al_valXpoll = new XpollAdapter(getApplicationContext()).val_xpoll(preference.getCustID(), preference.getUn());
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
            case R.id.edit_menu:
                imgB_xpoll_submit.setVisibility(View.VISIBLE);
                imgB_xpoll_cancel.setVisibility(View.VISIBLE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }



}
