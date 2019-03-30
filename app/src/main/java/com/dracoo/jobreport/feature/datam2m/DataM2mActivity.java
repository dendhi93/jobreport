package com.dracoo.jobreport.feature.datam2m;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.M2mDataAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.master.MasterM2mData;
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

public class DataM2mActivity extends AppCompatActivity {
    private MessageUtils messageUtils;
    private Preference preference;
    private Dao<MasterTransHistory, Integer> transHistDao;
    private Dao<MasterM2mData, Integer> dataM2mDao;

    @BindView(R.id.txt_dm2m_un)
    EditText txt_dm2m_un;
    @BindView(R.id.txt_dm2m_password)
    EditText txt_dm2m_password;
    @BindView(R.id.txt_dm2m_ipMachine)
    EditText txt_dm2m_ipMachine;
    @BindView(R.id.txt_dm2m_remote)
    EditText txt_dm2m_remote;
    @BindView(R.id.txt_dm2m_tunnelId)
    EditText txt_dm2m_tunnelId;
    @BindView(R.id.txt_dm2m_ipBouding)
    EditText txt_dm2m_ipBouding;
    @BindView(R.id.txt_dm2m_ipVLan)
    EditText txt_dm2m_ipVLan;
    @BindView(R.id.txt_dm2m_ipLLan)
    EditText txt_dm2m_ipLLan;
    @BindView(R.id.txt_dm2m_dataM2m_subnetMask)
    EditText txt_dm2m_dataM2m_subnetMask;
    @BindView(R.id.txt_dm2m_dataM2m_agg)
    EditText txt_dm2m_dataM2m_agg;
    @BindView(R.id.txt_dm2m_user)
    EditText txt_dm2m_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_m2m);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        messageUtils = new MessageUtils(DataM2mActivity.this);
        preference = new Preference(DataM2mActivity.this);

        try{
            transHistDao = new TransHistoryAdapter(getApplicationContext()).getAdapter();
            dataM2mDao = new M2mDataAdapter(getApplicationContext()).getAdapter();
        }catch (Exception e){}
    }

    @OnClick(R.id.imgB_dataM2m_submit)
    void submitM2m(){
        if (!valEmptyText()){
            messageUtils.snackBar_message(getString(R.string.emptyString), DataM2mActivity.this, ConfigApps.SNACKBAR_NO_BUTTON);
        }else{
            transDatam2m();
        }
    }

    @OnClick(R.id.imgB_dataM2m_cancel)
    void cancelM2m(){
        setEmptyText();
    }

    private void setEmptyText(){
        txt_dm2m_un.setText("");
        txt_dm2m_password.setText("");
        txt_dm2m_ipMachine.setText("");
        txt_dm2m_remote.setText("");
        txt_dm2m_tunnelId.setText("");
        txt_dm2m_ipBouding.setText("");
        txt_dm2m_ipVLan.setText("");
        txt_dm2m_ipLLan.setText("");
        txt_dm2m_dataM2m_subnetMask.setText("");
        txt_dm2m_dataM2m_agg.setText("");
        txt_dm2m_user.setText("");
    }

    private boolean valEmptyText(){
        if (txt_dm2m_un.getText().toString().equals("")||
                txt_dm2m_password.getText().toString().equals("")||
                txt_dm2m_ipMachine.getText().toString().equals("") ||
                txt_dm2m_user.getText().toString().equals("") ||
                txt_dm2m_remote.getText().toString().equals("") ||
                txt_dm2m_tunnelId.getText().toString().equals("") ||
                txt_dm2m_ipBouding.getText().toString().equals("") ||
                txt_dm2m_ipVLan.getText().toString().equals("") ||
                txt_dm2m_ipLLan.getText().toString().equals("") ||
                txt_dm2m_dataM2m_subnetMask.getText().toString().equals("") ||
                txt_dm2m_dataM2m_agg.getText().toString().equals("")
        ){
            return false;
        }else{
            return true;
        }
    }

    private void transDatam2m(){
        ArrayList<MasterM2mData> al_valDatam2m = new M2mDataAdapter(getApplicationContext()).val_dataM2m(preference.getCustID(), preference.getUn());
        if (al_valDatam2m.size() > 0){
            try{
                MasterM2mData m2mData = dataM2mDao.queryForId(al_valDatam2m.get(0).getId_data());
                m2mData.setUsername(txt_dm2m_user.getText().toString().trim());
                m2mData.setPassword(txt_dm2m_password.getText().toString().trim());
                m2mData.setUser(txt_dm2m_user.getText().toString().trim());
                m2mData.setRemote(txt_dm2m_remote.getText().toString().trim());
                m2mData.setTunnel_id(txt_dm2m_tunnelId.getText().toString().trim());
                m2mData.setIp_bonding(txt_dm2m_ipBouding.getText().toString().trim());
                m2mData.setAgg(txt_dm2m_dataM2m_agg.getText().toString().trim());
                m2mData.setIp_lan(txt_dm2m_ipLLan.getText().toString().trim());
                m2mData.setIp_vlan(txt_dm2m_ipLLan.getText().toString().trim());
                m2mData.setUpdate_date(DateTimeUtils.getCurrentTime());

                dataM2mDao.update(m2mData);
                transHist();
            }catch (Exception e){
                messageUtils.toastMessage("Err datam2m update " +e.toString(), ConfigApps.T_ERROR);
            }
        }else{
            try{
                MasterM2mData m2mData = new MasterM2mData();
                m2mData.setProgress_type(preference.getProgress());
                m2mData.setUsername(txt_dm2m_user.getText().toString().trim());
                m2mData.setPassword(txt_dm2m_password.getText().toString().trim());
                m2mData.setUser(txt_dm2m_user.getText().toString().trim());
                m2mData.setRemote(txt_dm2m_remote.getText().toString().trim());
                m2mData.setTunnel_id(txt_dm2m_tunnelId.getText().toString().trim());
                m2mData.setIp_bonding(txt_dm2m_ipBouding.getText().toString().trim());
                m2mData.setAgg(txt_dm2m_dataM2m_agg.getText().toString().trim());
                m2mData.setConnection_type(preference.getConnType().trim());
                m2mData.setId_site(preference.getCustID());
                m2mData.setInsert_date(DateTimeUtils.getCurrentTime());
                m2mData.setIp_lan(txt_dm2m_ipLLan.getText().toString().trim());
                m2mData.setIp_vlan(txt_dm2m_ipLLan.getText().toString().trim());

                dataM2mDao.create(m2mData);
                transHist();
            }catch (Exception e){
                messageUtils.toastMessage("Err datam2m insert " +e.toString(), ConfigApps.T_ERROR);
            }
        }
    }

    private void transHist(){
        ArrayList<MasterTransHistory> al_valTransHist = new TransHistoryAdapter(getApplicationContext())
                .val_trans(preference.getCustID(), preference.getUn(),getString(R.string.dataM2m_trans));
        if (al_valTransHist.size() > 0){
            try{
                MasterTransHistory mHist = transHistDao.queryForId(al_valTransHist.get(0).getId_site());
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getString(R.string.dataM2m_trans));
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setIs_submited(0);

                transHistDao.update(mHist);
                messageUtils.toastMessage(getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                setEmptyText();
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist update " +e.toString(), ConfigApps.T_ERROR);
            }
        }else{
            try{
                MasterTransHistory mHist = new MasterTransHistory();
                mHist.setId_site(preference.getCustID());
                mHist.setUn_user(preference.getUn());
                mHist.setInsert_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getString(R.string.dataM2m_trans));
                mHist.setIs_submited(0);

                transHistDao.create(mHist);
                messageUtils.toastMessage(getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                setEmptyText();
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist insert " +e.toString(), ConfigApps.T_ERROR);
            }
        }
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
