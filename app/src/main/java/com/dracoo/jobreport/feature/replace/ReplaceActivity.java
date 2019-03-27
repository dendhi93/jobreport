package com.dracoo.jobreport.feature.replace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.M2mReplaceAdapter;
import com.dracoo.jobreport.database.adapter.VsatReplaceAdapter;
import com.dracoo.jobreport.database.adapter.VsatSetupAdapter;
import com.dracoo.jobreport.database.master.MasterM2mReplace;
import com.dracoo.jobreport.database.master.MasterVsatReplace;
import com.dracoo.jobreport.database.master.MasterVsatSetup;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReplaceActivity extends AppCompatActivity {
    @BindView(R.id.ln_replace_vsat)
    LinearLayout ln_replace_vsat;
    @BindView(R.id.ln_replace_m2m)
    LinearLayout ln_replace_m2m;
    //vsat
    @BindView(R.id.txt_rep_vsatModem)
    EditText txt_rep_vsatModem;
    @BindView(R.id.txt_rep_vsatAdaptor)
    EditText txt_rep_vsatAdaptor;
    @BindView(R.id.txt_rep_vsatFh)
    EditText txt_rep_vsatFh;
    @BindView(R.id.txt_rep_vsatLnb)
    EditText txt_rep_vsatLnb;
    @BindView(R.id.txt_rep_vsatRfu)
    EditText txt_rep_vsatRfu;

    //m2m
    @BindView(R.id.txt_rep_m2m_brand)
    EditText txt_rep_m2m_brand;
    @BindView(R.id.txt_rep_m2m_sn)
    EditText txt_rep_m2m_sn;
    @BindView(R.id.txt_rep_m2m_adaptorSn)
    EditText txt_rep_m2m_adaptorSn;
    @BindView(R.id.txt_rep_m2m_adaptorBrand)
    EditText txt_rep_m2m_adaptorBrand;
    @BindView(R.id.txt_rep_m2m_sc1Brand)
    EditText txt_rep_m2m_sc1Brand;
    @BindView(R.id.txt_rep_m2m_sc1Sn)
    EditText txt_rep_m2m_sc1Sn;
    @BindView(R.id.txt_rep_m2m_sc1puk)
    EditText txt_rep_m2m_sc1puk;
    @BindView(R.id.txt_rep_m2m_sc2Brand)
    EditText txt_rep_m2m_sc2Brand;
    @BindView(R.id.txt_rep_m2m_sc2Sn)
    EditText txt_rep_m2m_sc2Sn;
    @BindView(R.id.txt_rep_m2m_sc2puk)
    EditText txt_rep_m2m_sc2puk;

    private MessageUtils messageUtils;
    private Preference preference;
    public static final String EXTRA_CALLER_CONN = "connection_type";
    public static final Integer EXTRA_CALLER_VSATCONN = 1;
    public static final Integer EXTRA_CALLER_M2MCONN = 2;
    private Integer intentConnectionType = 0;
    private Dao<MasterVsatReplace, Integer> vsatReplaceDao;
    private Dao<MasterM2mReplace, Integer> m2mReplaceDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        messageUtils = new MessageUtils(ReplaceActivity.this);
        preference = new Preference(ReplaceActivity.this);
        try{
            intentConnectionType = getIntent().getIntExtra(EXTRA_CALLER_CONN, 1);
        }catch (Exception e){ Log.d("ReplaceErr ",""+e.toString()); }

        if (intentConnectionType == EXTRA_CALLER_VSATCONN){
            ln_replace_vsat.setVisibility(View.VISIBLE);
            ln_replace_m2m.setVisibility(View.GONE);
        }else if (intentConnectionType == EXTRA_CALLER_M2MCONN){
            ln_replace_vsat.setVisibility(View.GONE);
            ln_replace_m2m.setVisibility(View.VISIBLE);
        }

        if (getSupportActionBar() != null && intentConnectionType == EXTRA_CALLER_VSATCONN){
            getSupportActionBar().setSubtitle("VSAT");
        }else if (getSupportActionBar() != null && intentConnectionType == EXTRA_CALLER_M2MCONN){
            getSupportActionBar().setSubtitle("M2M");
        }

        try {
            vsatReplaceDao = new VsatReplaceAdapter(getApplicationContext()).getAdapter();
            m2mReplaceDao = new M2mReplaceAdapter(getApplicationContext()).getAdapter();
        }catch (Exception e){}

    }

    @OnClick(R.id.imgB_rep_submit)
    void submitRep(){
        if (intentConnectionType == EXTRA_CALLER_VSATCONN){
            if (!vsatReplaceVal()){
                messageUtils.snackBar_message(getString(R.string.emptyString), ReplaceActivity.this, ConfigApps.SNACKBAR_NO_BUTTON);
            }else{
                vsatReplaceTrans();
            }
        }else if (intentConnectionType == EXTRA_CALLER_M2MCONN){
            if (!m2mReplaceVal()){
                messageUtils.snackBar_message(getString(R.string.emptyString), ReplaceActivity.this, ConfigApps.SNACKBAR_NO_BUTTON);
            }else{

            }
        }
    }

    @OnClick(R.id.imgB_rep_cancel)
    void cancelRep(){
        finish();
    }

    //vsat
    public boolean vsatReplaceVal(){
        if(txt_rep_vsatModem.getText().toString().trim().equals("") ||
                txt_rep_vsatAdaptor.getText().toString().trim().equals("") ||
                txt_rep_vsatFh.getText().toString().trim().equals("")||
                txt_rep_vsatLnb.getText().toString().trim().equals("") ||
                txt_rep_vsatRfu.getText().toString().trim().equals("")){
            return false;
        }else{
            return true;
        }
    }

    private void vsatReplaceTrans(){
        ArrayList<MasterVsatSetup> alVsatSetup = new VsatSetupAdapter(getApplicationContext()).val_vsatSetup(preference.getCustID(), preference.getUn());
        if (alVsatSetup.size() > 0){
            ArrayList<MasterVsatReplace> alVsatReplace = new VsatReplaceAdapter(ReplaceActivity.this).val_vsatReplace(preference.getCustID(), preference.getUn());
            if (alVsatReplace.size() > 0){
                try{

                }catch (Exception e){ messageUtils.toastMessage("err vsatReplace Update " +e.toString(), ConfigApps.T_ERROR); }
            }else{
                try{

                }catch (Exception e){ messageUtils.toastMessage("err vsatReplace insert " +e.toString(), ConfigApps.T_ERROR); }
            }
        }else{
            messageUtils.toastMessage("Transaksi pada menu Connection Type belum diinput, transaksi dibatalkan", ConfigApps.T_WARNING);
        }
    }

    public boolean m2mReplaceVal(){
        if (txt_rep_m2m_brand.getText().toString().trim().equals("")||
                txt_rep_m2m_sn.getText().toString().trim().equals("")||
                txt_rep_m2m_adaptorBrand.getText().toString().trim().equals("") ||
                txt_rep_m2m_adaptorSn.getText().toString().trim().equals("") ||
                txt_rep_m2m_sc1Brand.getText().toString().equals("") ||
                txt_rep_m2m_sc1Sn.getText().toString().equals("") ||
                txt_rep_m2m_sc1puk.getText().toString().trim().equals("") ||
                txt_rep_m2m_sc2Brand.getText().toString().trim().equals("") ||
                txt_rep_m2m_sc2Sn.getText().toString().trim().equals("") ||
                txt_rep_m2m_sc2puk.getText().toString().trim().equals("")){
            return false;
        }else{
            return true;
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
