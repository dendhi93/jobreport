package com.dracoo.jobreport.feature.vsatparameter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.ConnectionParameterAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.master.MasterConnectionParameter;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.DateTimeUtils;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParameterActivity extends AppCompatActivity {

    @BindView(R.id.rg_par_subnetmask)
    RadioGroup rg_par_subnetmask;
    @BindView(R.id.txt_parameter_ipLLan)
    EditText txt_parameter_ipLLan;
    @BindView(R.id.txt_parameter_long)
    EditText txt_parameter_long;
    @BindView(R.id.txt_parameter_symRate)
    EditText txt_parameter_symRate;
    @BindView(R.id.txt_parameter_freq)
    EditText txt_parameter_freq;
    @BindView(R.id.txt_parameter_esn)
    EditText txt_parameter_esn;
    @BindView(R.id.txt_parameter_gateway)
    EditText txt_parameter_gateway;
    @BindView(R.id.txt_parameter_snmp)
    EditText txt_parameter_snmp;
    @BindView(R.id.txt_parameter_signal)
    EditText txt_parameter_signal;
    @BindView(R.id.txt_parameter_dataRate)
    EditText txt_parameter_dataRate;
    @BindView(R.id.txt_parameter_fec)
    EditText txt_parameter_fec;
    @BindView(R.id.txt_parameter_powSetting)
    EditText txt_parameter_powSetting;
    @BindView(R.id.txt_parameter_esNo)
    EditText txt_parameter_esNo;
    @BindView(R.id.txt_parameter_cNo)
    EditText txt_parameter_cNo;

    private String selectedParameter = "null";
    private RadioButton rbParameter;
    private MessageUtils messageUtils;
    private Preference preference;
    private Dao<MasterConnectionParameter, Integer> connParamAdapter;
    private Dao<MasterTransHistory, Integer> transHistAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);
        ButterKnife.bind(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        messageUtils = new MessageUtils(ParameterActivity.this);
        preference = new Preference(ParameterActivity.this);
        displayParamRadio();
        try{
            connParamAdapter = new ConnectionParameterAdapter(getApplicationContext()).getAdapter();
            transHistAdapter = new TransHistoryAdapter(getApplicationContext()).getAdapter();
        }catch (Exception e){}

    }

    private void displayParamRadio(){
        rg_par_subnetmask.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rbParameter =  findViewById(i);
                selectedParameter = ""+rbParameter.getText().toString();
            }
        });
    }

    @OnClick(R.id.imgB_par_submit)
    void submitPar(){
        if (selectedParameter.equals("null") || selectedParameter == null){
            messageUtils.snackBar_message("Mohon dipilih pilhan pada kolom SubnetMask", ParameterActivity.this,ConfigApps.SNACKBAR_NO_BUTTON);
        }else if (!valEmptyText()){
            messageUtils.snackBar_message(getString(R.string.emptyString), ParameterActivity.this,ConfigApps.SNACKBAR_NO_BUTTON);
        } else if(preference.getCustID() == 0){
            messageUtils.snackBar_message(getString(R.string.customer_validation), ParameterActivity.this, ConfigApps.SNACKBAR_NO_BUTTON);
        } else{
            messageUtils.toastMessage("coba", ConfigApps.T_DEFAULT);
        }
    }

    private void parTrans(){
        ArrayList<MasterConnectionParameter> al_valPar = new ConnectionParameterAdapter(getApplicationContext()).val_param(preference.getCustID(), preference.getUn());
        if (al_valPar.size() > 0){
            try{
                MasterConnectionParameter mConnPar = connParamAdapter.queryForId(al_valPar.get(0).getId_parameter());
                mConnPar.setUpdate_date(DateTimeUtils.getCurrentTime().trim());
                mConnPar.setLan_parameter(txt_parameter_ipLLan.getText().toString().trim());
                mConnPar.setLan_subnetmask(selectedParameter.trim());
                mConnPar.setSat_parameter(txt_parameter_long.getText().toString().trim());
                mConnPar.setSat_symrate(txt_parameter_symRate.getText().toString().trim());
                mConnPar.setSat_freq(txt_parameter_freq.getText().toString().trim());
                mConnPar.setManagement_esnmodem(txt_parameter_esn.getText().toString().trim());
                mConnPar.setManagement_gateway(txt_parameter_gateway.getText().toString().trim());
                mConnPar.setManagement_snmp(txt_parameter_snmp.getText().toString().trim());
                mConnPar.setRanging_signal(txt_parameter_signal.getText().toString().trim());
                mConnPar.setRanging_data_rate(txt_parameter_dataRate.getText().toString().trim());
                mConnPar.setRanging_fec(txt_parameter_fec.getText().toString().trim());
                mConnPar.setRanging_power(txt_parameter_powSetting.getText().toString().trim());
                mConnPar.setRanging_esno(txt_parameter_esNo.getText().toString().trim());
                mConnPar.setRanging_cno(txt_parameter_cNo.getText().toString().trim());

                connParamAdapter.update(mConnPar);
                //add transHist

            }catch (Exception e){messageUtils.toastMessage("Err Parr update " +e.toString(), ConfigApps.T_ERROR);}
        }else{
            try{
                MasterConnectionParameter mConnPar = new MasterConnectionParameter();
                mConnPar.setId_site(preference.getCustID());
                mConnPar.setProgress_type(preference.getProgress().trim());
                mConnPar.setConnection_type(preference.getConnType().trim());
                mConnPar.setInsert_date(DateTimeUtils.getCurrentTime().trim());
                mConnPar.setUn_user(preference.getUn().trim());
                mConnPar.setLan_parameter(txt_parameter_ipLLan.getText().toString().trim());
                mConnPar.setLan_subnetmask(selectedParameter.trim());
                mConnPar.setSat_parameter(txt_parameter_long.getText().toString().trim());
                mConnPar.setSat_symrate(txt_parameter_symRate.getText().toString().trim());
                mConnPar.setSat_freq(txt_parameter_freq.getText().toString().trim());
                mConnPar.setManagement_esnmodem(txt_parameter_esn.getText().toString().trim());
                mConnPar.setManagement_gateway(txt_parameter_gateway.getText().toString().trim());
                mConnPar.setManagement_snmp(txt_parameter_snmp.getText().toString().trim());
                mConnPar.setRanging_signal(txt_parameter_signal.getText().toString().trim());
                mConnPar.setRanging_data_rate(txt_parameter_dataRate.getText().toString().trim());
                mConnPar.setRanging_fec(txt_parameter_fec.getText().toString().trim());
                mConnPar.setRanging_power(txt_parameter_powSetting.getText().toString().trim());
                mConnPar.setRanging_esno(txt_parameter_esNo.getText().toString().trim());
                mConnPar.setRanging_cno(txt_parameter_cNo.getText().toString().trim());

                connParamAdapter.create(mConnPar);


            }catch (Exception e){messageUtils.toastMessage("Err Parr insert " +e.toString(), ConfigApps.T_ERROR);}
        }
    }

    private boolean valEmptyText(){
        if (txt_parameter_ipLLan.getText().toString().trim().equals("") ||
                txt_parameter_long.getText().toString().trim().equals("") ||
                txt_parameter_symRate.getText().toString().trim().equals("") ||
                txt_parameter_freq.getText().toString().trim().equals("") ||
                txt_parameter_esn.getText().toString().trim().equals("") ||
                txt_parameter_gateway.getText().toString().trim().equals("") ||
                txt_parameter_snmp.getText().toString().trim().equals("") ||
                txt_parameter_signal.getText().toString().trim().equals("") ||
                txt_parameter_dataRate.getText().toString().trim().equals("") ||
                txt_parameter_fec.getText().toString().trim().equals("") ||
                txt_parameter_powSetting.getText().toString().trim().equals("") ||
                txt_parameter_esNo.getText().toString().trim().equals("") ||
                txt_parameter_cNo.getText().toString().trim().equals("")){
            return false;
        }else{
            return true;
        }
    }


    @OnClick(R.id.imgB_par_cancel)
    void cancelPar(){
        setEmptyText();
    }

    private void setEmptyText(){
        txt_parameter_ipLLan.setText("");
        txt_parameter_long.setText("");
        txt_parameter_symRate.setText("");
        txt_parameter_freq.setText("");
        txt_parameter_esn.setText("");
        txt_parameter_gateway.setText("");
        txt_parameter_snmp.setText("");
        txt_parameter_signal.setText("");
        txt_parameter_dataRate.setText("");
        txt_parameter_fec.setText("");
        txt_parameter_powSetting.setText("");
        txt_parameter_esNo.setText("");
        txt_parameter_cNo.setText("");
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
