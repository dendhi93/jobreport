package com.dracoo.jobreport.feature.replace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.M2mReplaceAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.adapter.VsatReplaceAdapter;
import com.dracoo.jobreport.database.master.MasterM2mReplace;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.database.master.MasterVsatReplace;
import com.dracoo.jobreport.feature.MenuActivity;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.DateTimeUtils;
import com.dracoo.jobreport.util.JobReportUtils;
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
    @BindView(R.id.txt_rep_vsatDipIdu)
    EditText txt_rep_vsatDipIdu;
    @BindView(R.id.txt_rep_vsatDipOdu)
    EditText txt_rep_vsatDipOdu;

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
    @BindView(R.id.imgB_rep_submit)
    ImageButton imgB_rep_submit;
    @BindView(R.id.imgB_rep_cancel)
    ImageButton imgB_rep_cancel;

    private MessageUtils messageUtils;
    private Preference preference;
    public static final String EXTRA_CALLER_CONN = "connection_type";
    public static final Integer EXTRA_CALLER_VSATCONN = 1;
    public static final Integer EXTRA_CALLER_M2MCONN = 2;
    private Integer intentConnectionType = 0;
    private Dao<MasterVsatReplace, Integer> vsatReplaceDao;
    private Dao<MasterM2mReplace, Integer> m2mReplaceDao;
    private Dao<MasterTransHistory, Integer> transHistDao;
    private String intentReplaceView;


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
            transHistDao = new TransHistoryAdapter(getApplicationContext()).getAdapter();
        }catch (Exception e){}
        viewReplace();
    }

    @OnClick(R.id.imgB_rep_submit)
    void submitRep(){
        if (intentConnectionType == EXTRA_CALLER_VSATCONN){
            if (!vsatReplaceVal()){
                messageUtils.snackBar_message(getString(R.string.emptyString), ReplaceActivity.this, ConfigApps.SNACKBAR_NO_BUTTON);
            }else if (preference.getCustID() == 0){
              messageUtils.snackBar_message(getString(R.string.customer_validation),ReplaceActivity.this, ConfigApps.SNACKBAR_NO_BUTTON);
            } else{
                vsatReplaceTrans();
            }
        }else if (intentConnectionType == EXTRA_CALLER_M2MCONN){
            if (!m2mReplaceVal()){
                messageUtils.snackBar_message(getString(R.string.emptyString), ReplaceActivity.this, ConfigApps.SNACKBAR_NO_BUTTON);
            }else if (preference.getCustID() == 0){
                messageUtils.snackBar_message(getString(R.string.customer_validation),ReplaceActivity.this, ConfigApps.SNACKBAR_NO_BUTTON);
            } else{
                m2mRepTrans();
            }
        }
    }

    @OnClick(R.id.imgB_rep_cancel)
    void cancelRep(){
        setEmptyText();
    }

    private void viewReplace(){
        try{
            intentReplaceView = getIntent().getStringExtra(ConfigApps.EXTRA_CALLER_VIEW);
            if (!intentReplaceView.equals("") || intentReplaceView != null){
                if (preference.getConnType().equals(getString(R.string.vsat))){
                    ln_replace_vsat.setVisibility(View.VISIBLE);
                    ln_replace_m2m.setVisibility(View.GONE);
                    ArrayList<MasterVsatReplace> al_repVsat = new VsatReplaceAdapter(getApplicationContext()).val_vsatReplace(preference.getCustID(), preference.getUn());
                    if (al_repVsat.size() > 0){
                        txt_rep_vsatModem.setText(al_repVsat.get(0).getSn_modem().trim());
                        txt_rep_vsatAdaptor.setText(al_repVsat.get(0).getSn_adaptor().trim());
                        txt_rep_vsatFh.setText(al_repVsat.get(0).getSn_fh().trim());
                        txt_rep_vsatLnb.setText(al_repVsat.get(0).getSn_lnb().trim());
                        txt_rep_vsatRfu.setText(al_repVsat.get(0).getSn_rfu().trim());
                        txt_rep_vsatDipOdu.setText(al_repVsat.get(0).getSn_dip_odu().trim());
                        txt_rep_vsatDipIdu.setText(al_repVsat.get(0).getSn_dip_idu().trim());
                    }
                }else if (preference.getConnType().equals(getString(R.string.m2m))){
                    ln_replace_vsat.setVisibility(View.GONE);
                    ln_replace_m2m.setVisibility(View.VISIBLE);
                    ArrayList<MasterM2mReplace> al_replaceM2m = new M2mReplaceAdapter(getApplicationContext()).val_m2mReplace(preference.getCustID(), preference.getUn());
                    if (al_replaceM2m.size() > 0){
                        txt_rep_m2m_brand.setText(al_replaceM2m.get(0).getBrand_type_replace().trim());
                        txt_rep_m2m_sn.setText(al_replaceM2m.get(0).getSn_replace().trim());
                        txt_rep_m2m_adaptorBrand.setText(al_replaceM2m.get(0).getBrand_type_adaptor().trim());
                        txt_rep_m2m_adaptorSn.setText(al_replaceM2m.get(0).getSn_adaptor().trim());
                        txt_rep_m2m_sc1Brand.setText(al_replaceM2m.get(0).getSim_card1_type().trim());
                        txt_rep_m2m_sc1Sn.setText(al_replaceM2m.get(0).getSim_card1_sn().trim());
                        txt_rep_m2m_sc1puk.setText(al_replaceM2m.get(0).getSim_card1_puk().trim());
                        txt_rep_m2m_sc2Brand.setText(al_replaceM2m.get(0).getSim_card2_type().trim());
                        txt_rep_m2m_sc2Sn.setText(al_replaceM2m.get(0).getSim_card2_sn().trim());
                        txt_rep_m2m_sc2puk.setText(al_replaceM2m.get(0).getSim_card2_puk().trim());
                    }
                }
                imgB_rep_submit.setVisibility(View.GONE);
                imgB_rep_cancel.setVisibility(View.GONE);
            }else{
                imgB_rep_submit.setVisibility(View.VISIBLE);
                imgB_rep_cancel.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            Log.d("###",""+e.toString());
            imgB_rep_submit.setVisibility(View.VISIBLE);
            imgB_rep_cancel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try{
            if (!intentReplaceView.equals("") || intentReplaceView != null){getMenuInflater().inflate(R.menu.menu, menu); }
        }catch (Exception e){}
        return true;
    }

    //vsat
    public boolean vsatReplaceVal(){
        if(txt_rep_vsatModem.getText().toString().trim().equals("") ||
                txt_rep_vsatAdaptor.getText().toString().trim().equals("") ||
                txt_rep_vsatFh.getText().toString().trim().equals("")||
                txt_rep_vsatLnb.getText().toString().trim().equals("") ||
                txt_rep_vsatRfu.getText().toString().trim().equals("") ||
                txt_rep_vsatDipIdu.getText().toString().trim().equals("") ||
                txt_rep_vsatDipOdu.getText().toString().trim().equals("")){
            return false;
        }else{
            return true;
        }
    }

    private void vsatReplaceTrans(){
            ArrayList<MasterVsatReplace> alVsatReplace = new VsatReplaceAdapter(ReplaceActivity.this).val_vsatReplace(preference.getCustID(), preference.getUn());
            if (alVsatReplace.size() > 0){
                try{
                    MasterVsatReplace vsatRep = vsatReplaceDao.queryForId(alVsatReplace.get(0).getId_replace());
                    vsatRep.setSn_modem(txt_rep_vsatModem.getText().toString().trim());
                    vsatRep.setSn_adaptor(txt_rep_vsatAdaptor.getText().toString().trim());
                    vsatRep.setSn_fh(txt_rep_vsatFh.getText().toString().trim());
                    vsatRep.setSn_lnb(txt_rep_vsatLnb.getText().toString().trim());
                    vsatRep.setSn_rfu(txt_rep_vsatRfu.getText().toString().trim());
                    vsatRep.setSn_dip_odu(txt_rep_vsatDipOdu.getText().toString().trim());
                    vsatRep.setSn_dip_idu(txt_rep_vsatDipIdu.getText().toString().trim());
                    vsatRep.setUpdate_date(DateTimeUtils.getCurrentTime());

                    vsatReplaceDao.update(vsatRep);
                    transHist(getString(R.string.repVSAT_trans), ConfigApps.TRANS_HIST_UPDATE);
                }catch (Exception e){ messageUtils.toastMessage("err vsatReplace Update " +e.toString(), ConfigApps.T_ERROR); }
            }else{
                try{
                    MasterVsatReplace vsatRep = new MasterVsatReplace();
                    vsatRep.setId_site(preference.getCustID());
                    vsatRep.setSn_modem(txt_rep_vsatModem.getText().toString().trim());
                    vsatRep.setSn_adaptor(txt_rep_vsatAdaptor.getText().toString().trim());
                    vsatRep.setSn_fh(txt_rep_vsatFh.getText().toString().trim());
                    vsatRep.setSn_lnb(txt_rep_vsatLnb.getText().toString().trim());
                    vsatRep.setSn_rfu(txt_rep_vsatRfu.getText().toString().trim());
                    vsatRep.setSn_dip_odu(txt_rep_vsatDipOdu.getText().toString().trim());
                    vsatRep.setSn_dip_idu(txt_rep_vsatDipIdu.getText().toString().trim());
                    vsatRep.setProgress_type(preference.getProgress());
                    vsatRep.setConnection_type(preference.getConnType());
                    vsatRep.setInsert_date(DateTimeUtils.getCurrentTime());
                    vsatRep.setUn_user(preference.getUn());

                    vsatReplaceDao.create(vsatRep);
                    transHist(getString(R.string.repVSAT_trans), ConfigApps.TRANS_HIST_INSERT);
                }catch (Exception e){
                    messageUtils.toastMessage("err vsatReplace insert " +e.toString(), ConfigApps.T_ERROR);
                    Log.d("###",""+e.toString());
                }
            }
    }

    //m2m
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

    private void m2mRepTrans(){
            ArrayList<MasterM2mReplace> al_m2mRep = new M2mReplaceAdapter(ReplaceActivity.this).val_m2mReplace(preference.getCustID(), preference.getUn());
            if (al_m2mRep.size() > 0){
                try{
                    MasterM2mReplace m2mRep = m2mReplaceDao.queryForId(al_m2mRep.get(0).getId_replace());
                    m2mRep.setBrand_type_replace(txt_rep_m2m_brand.getText().toString().trim());
                    m2mRep.setSn_replace(txt_rep_m2m_sn.getText().toString().trim());
                    m2mRep.setBrand_type_adaptor(txt_rep_m2m_adaptorBrand.getText().toString().trim());
                    m2mRep.setSn_adaptor(txt_rep_m2m_adaptorSn.getText().toString().trim());
                    m2mRep.setSim_card1_type(txt_rep_m2m_sc1Brand.getText().toString().trim());
                    m2mRep.setSim_card1_sn(txt_rep_m2m_sc1Sn.getText().toString().trim());
                    m2mRep.setSim_card1_puk(txt_rep_m2m_sc1puk.getText().toString().trim());
                    m2mRep.setSim_card2_type(txt_rep_m2m_sc2Brand.getText().toString().trim());
                    m2mRep.setSim_card2_sn(txt_rep_m2m_sc2Sn.getText().toString().trim());
                    m2mRep.setSim_card2_puk(txt_rep_m2m_sc2puk.getText().toString().trim());
                    m2mRep.setUpdate_date(DateTimeUtils.getCurrentTime());

                    m2mReplaceDao.update(m2mRep);
                    transHist(getString(R.string.repM2M_trans), ConfigApps.TRANS_HIST_UPDATE);
                }catch (Exception e){ messageUtils.toastMessage("err m2mReplace update " +e.toString(), ConfigApps.T_ERROR); }
            }else{
                try{
                    MasterM2mReplace m2mRep = new MasterM2mReplace();
                    m2mRep.setBrand_type_replace(txt_rep_m2m_brand.getText().toString().trim());
                    m2mRep.setSn_replace(txt_rep_m2m_sn.getText().toString().trim());
                    m2mRep.setBrand_type_adaptor(txt_rep_m2m_adaptorBrand.getText().toString().trim());
                    m2mRep.setSn_adaptor(txt_rep_m2m_adaptorSn.getText().toString().trim());
                    m2mRep.setSim_card1_type(txt_rep_m2m_sc1Brand.getText().toString().trim());
                    m2mRep.setSim_card1_sn(txt_rep_m2m_sc1Sn.getText().toString().trim());
                    m2mRep.setSim_card1_puk(txt_rep_m2m_sc1puk.getText().toString().trim());
                    m2mRep.setSim_card2_type(txt_rep_m2m_sc2Brand.getText().toString().trim());
                    m2mRep.setSim_card2_sn(txt_rep_m2m_sc2Sn.getText().toString().trim());
                    m2mRep.setSim_card2_puk(txt_rep_m2m_sc2puk.getText().toString().trim());
                    m2mRep.setInsert_date(DateTimeUtils.getCurrentTime().trim());
                    m2mRep.setId_site(preference.getCustID());
                    m2mRep.setUn_user(preference.getUn());
                    m2mRep.setProgress_type(preference.getProgress().trim());
                    m2mRep.setConnection_type(preference.getConnType().trim());

                    m2mReplaceDao.create(m2mRep);
                    transHist(getString(R.string.repM2M_trans), ConfigApps.TRANS_HIST_INSERT);
                }catch (Exception e){
                    messageUtils.toastMessage("err m2mReplace insert " +e.toString(), ConfigApps.T_ERROR);
                }
            }
    }

    //transHist
    private void transHist(String transType, int updateType){
        ArrayList<MasterTransHistory> al_valTransHist = new TransHistoryAdapter(getApplicationContext())
                .val_trans(preference.getCustID(), preference.getUn(),transType);
        if (al_valTransHist.size() > 0){
            try{
                MasterTransHistory mHist = transHistDao.queryForId(al_valTransHist.get(0).getId_trans());
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(transType.trim());
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setIs_submited(0);

                transHistDao.update(mHist);
                if (updateType == ConfigApps.TRANS_HIST_UPDATE){
                    messageUtils.toastMessage(getString(R.string.transaction_success) + " diupdate", ConfigApps.T_SUCCESS);
                }else{
                    messageUtils.toastMessage(getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                }

                JobReportUtils.hideKeyboard(ReplaceActivity.this);
                finish();
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist update " +e.toString(), ConfigApps.T_ERROR);
            }
        }else{
            try{
                MasterTransHistory mHist = new MasterTransHistory();
                mHist.setId_site(preference.getCustID());
                mHist.setUn_user(preference.getUn());
                mHist.setInsert_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(transType.trim());
                mHist.setIs_submited(0);

                transHistDao.create(mHist);
                if (updateType == ConfigApps.TRANS_HIST_UPDATE){
                    messageUtils.toastMessage(getString(R.string.transaction_success) + " diupdate", ConfigApps.T_SUCCESS);
                }else{
                    messageUtils.toastMessage(getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                }

                JobReportUtils.hideKeyboard(ReplaceActivity.this);
                finish();
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist insert " +e.toString(), ConfigApps.T_ERROR);
            }
        }

    }

    private void setEmptyText(){
        txt_rep_vsatModem.setText("");
        txt_rep_vsatAdaptor.setText("");
        txt_rep_vsatFh.setText("");
        txt_rep_vsatLnb.setText("");
        txt_rep_vsatRfu.setText("");
        txt_rep_vsatDipIdu.setText("");
        txt_rep_vsatDipOdu.setText("");
        txt_rep_m2m_brand.setText("");
        txt_rep_m2m_sn.setText("");
        txt_rep_m2m_adaptorSn.setText("");
        txt_rep_m2m_adaptorBrand.setText("");
        txt_rep_m2m_sc1Brand.setText("");
        txt_rep_m2m_sc1Sn.setText("");
        txt_rep_m2m_sc1puk.setText("");
        txt_rep_m2m_sc2Brand.setText("");
        txt_rep_m2m_sc2Sn.setText("");
        txt_rep_m2m_sc2puk.setText("");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
            case R.id.edit_menu :
                imgB_rep_submit.setVisibility(View.VISIBLE);
                imgB_rep_cancel.setVisibility(View.VISIBLE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
