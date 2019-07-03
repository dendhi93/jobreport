package com.dracoo.jobreport.feature.connection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.M2mSetupAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.adapter.VsatSetupAdapter;
import com.dracoo.jobreport.database.master.MasterM2mSetup;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.database.master.MasterVsatSetup;
import com.dracoo.jobreport.feature.MenuActivity;
import com.dracoo.jobreport.feature.datam2m.DataM2mActivity;
import com.dracoo.jobreport.feature.replace.ReplaceActivity;
import com.dracoo.jobreport.feature.vsatparameter.ParameterActivity;
import com.dracoo.jobreport.feature.xpoll.XpollActivity;
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

public class ConnectionFragment extends Fragment {
    @BindView(R.id.sp_conn_connType)
    Spinner sp_conn_connType;
    @BindView(R.id.sp_conn_antena)
    Spinner sp_conn_antena;
    @BindView(R.id.sp_conn_pedestial)
    Spinner sp_conn_pedestial;
    @BindView(R.id.sp_conn_antenaAccess)
    Spinner sp_conn_antenaAccess;
    @BindView(R.id.ln_conn_vsat)
    LinearLayout ln_conn_vsat;
    @BindView(R.id.ln_conn_m2m)
    LinearLayout ln_conn_m2m;

    //vsat
    @BindView(R.id.txt_conn_vsatModem)
    EditText txt_conn_vsatModem;
    @BindView(R.id.txt_conn_vsatAdaptor)
    EditText txt_conn_vsatAdaptor;
    @BindView(R.id.txt_conn_vsatFh)
    EditText txt_conn_vsatFh;
    @BindView(R.id.txt_conn_vsatLnb)
    EditText txt_conn_vsatLnb;
    @BindView(R.id.txt_conn_vsatRfu)
    EditText txt_conn_vsatRfu;
    @BindView(R.id.txt_conn_vsatOdu)
    EditText txt_conn_vsatOdu;
    @BindView(R.id.txt_conn_vsatIdu)
    EditText txt_conn_vsatIdu;
    @BindView(R.id.txt_conn_vsat_antenaType)
    EditText txt_conn_vsat_antenaType;
    @BindView(R.id.txt_conn_vsat_antenaBrand)
    EditText txt_conn_vsat_antenaBrand;

    //m2m
    @BindView(R.id.txt_conn_m2m_brand)
    EditText txt_conn_m2m_brand;
    @BindView(R.id.txt_conn_m2m_sn)
    EditText txt_conn_m2m_sn;
    @BindView(R.id.txt_conn_m2m_adaptorBrand)
    EditText txt_conn_m2m_adaptorBrand;
    @BindView(R.id.txt_conn_m2m_adaptorSn)
    EditText txt_conn_m2m_adaptorSn;
    @BindView(R.id.txt_conn_m2m_sc1Sn)
    EditText txt_conn_m2m_sc1Sn;
    @BindView(R.id.txt_conn_m2m_sc1puk)
    EditText txt_conn_m2m_sc1puk;
    @BindView(R.id.txt_conn_m2m_sc2Brand)
    EditText txt_conn_m2m_sc2Brand;
    @BindView(R.id.txt_conn_m2m_sc2Sn)
    EditText txt_conn_m2m_sc2Sn;
    @BindView(R.id.txt_conn_m2m_sc2puk)
    EditText txt_conn_m2m_sc2puk;
    @BindView(R.id.txt_conn_m2m_sc1Brand)
    EditText txt_conn_m2m_sc1Brand;
    @BindView(R.id.imgB_con_submit)
    ImageButton imgB_con_submit;
    @BindView(R.id.imgB_con_cancel)
    ImageButton imgB_con_cancel;

    private MessageUtils messageUtils;
    private String selectedConn="";
    private String selectedAntena = "";
    private String selectedPedestial = "";
    private String selectedAccess = "";
    private Preference preference;
    private Dao<MasterVsatSetup, Integer> vsatSetupDao;
    private Dao<MasterM2mSetup, Integer> m2mSetupDao;
    private Dao<MasterTransHistory, Integer> transHistAdapter;
    private String[] arrConnectionType;
    private String[] arrAntenaType;
    private String[] arrPedestialType;
    private String[] arrAntenaAccess;
    private String intentConnView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        messageUtils = new MessageUtils(getActivity());
        preference = new Preference(getActivity());
        displayAllSpinner();
        try{
            vsatSetupDao = new VsatSetupAdapter(getActivity()).getAdapter();
            m2mSetupDao = new M2mSetupAdapter(getActivity()).getAdapter();
            transHistAdapter = new TransHistoryAdapter(getActivity()).getAdapter();
        }catch (Exception e){}
        viewValidation();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        try{
            if (intentConnView.equals("") || intentConnView != null){ inflater.inflate(R.menu.menu, menu); }
        }catch (Exception e){}

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_menu :
                imgB_con_submit.setVisibility(View.VISIBLE);
                imgB_con_cancel.setVisibility(View.VISIBLE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void viewValidation(){
        try{
            intentConnView = getActivity().getIntent().getStringExtra(MenuActivity.EXTRA_CALLER_VIEW);
            if (!intentConnView.trim().equals("") || intentConnView != null){
                if (preference.getConnType().equals("VSAT")){
                    ArrayList<MasterVsatSetup> al_vsat = new VsatSetupAdapter(getActivity()).val_vsatSetup(preference.getCustID(), preference.getUn());
                    if (al_vsat.size() > 0){
                        ln_conn_vsat.setVisibility(View.VISIBLE);
                        ln_conn_m2m.setVisibility(View.GONE);
                        txt_conn_vsatModem.setText(al_vsat.get(0).getSn_modem().trim());
                        txt_conn_vsatAdaptor.setText(al_vsat.get(0).getSn_adaptor().trim());
                        txt_conn_vsatFh.setText(al_vsat.get(0).getSn_fh().trim());
                        txt_conn_vsatLnb.setText(al_vsat.get(0).getSn_lnb().trim());
                        txt_conn_vsatRfu.setText(al_vsat.get(0).getSn_rfu().trim());
                        txt_conn_vsatOdu.setText(al_vsat.get(0).getSn_dip_odu().trim());
                        txt_conn_vsatIdu.setText(al_vsat.get(0).getSn_dip_idu().trim());
                        String antennaSize = al_vsat.get(0).getAntena_size().trim();
                        if (antennaSize.equals(getActivity().getString(R.string.conn_12m))){ sp_conn_antena.setSelection(1);
                        }else if (antennaSize.equals(getActivity().getString(R.string.conn_18m))){ sp_conn_antena.setSelection(2);
                        }else if (antennaSize.equals(getActivity().getString(R.string.conn_24m))){ sp_conn_antena.setSelection(3); }
                        txt_conn_vsat_antenaBrand.setText(al_vsat.get(0).getAntena_brand().trim());
                        txt_conn_vsat_antenaType.setText(al_vsat.get(0).getAntena_type().trim());
                        String pedestialType = al_vsat.get(0).getPedestal_type().trim();
                        if (pedestialType.equals(getActivity().getString(R.string.rb_nprm))){ sp_conn_pedestial.setSelection(1);
                        }else if (pedestialType.equals(getActivity().getString(R.string.rb_wallmount))){ sp_conn_pedestial.setSelection(2);
                        }else if (pedestialType.equals(getActivity().getString(R.string.rb_groundmount))){ sp_conn_pedestial.setSelection(3); }
                        String setAccessType = al_vsat.get(0).getAccess_type().trim();
                        if (setAccessType.equals(getActivity().getString(R.string.rb_machine_24))){ sp_conn_antenaAccess.setSelection(1);
                        }else if (setAccessType.equals(getActivity().getString(R.string.rb_machine_24))){ sp_conn_antenaAccess.setSelection(2); }
                        String setConnType = preference.getConnType().trim();
                        if (setConnType.equals(getActivity().getString(R.string.vsat))){ sp_conn_connType.setSelection(1);
                        }else if (setConnType.equals(getActivity().getString(R.string.m2m))){ sp_conn_connType.setSelection(2); }
                    }
                }else if (preference.getConnType().equals("M2M")){
                    ln_conn_vsat.setVisibility(View.GONE);
                    ln_conn_m2m.setVisibility(View.VISIBLE);
                    ArrayList<MasterM2mSetup> al_m2mSetup = new M2mSetupAdapter(getActivity()).val_m2mSetup(preference.getCustID(), preference.getUn());
                    if (al_m2mSetup.size() > 0){
                        String setConnType = preference.getConnType().trim();
                        if (setConnType.equals(getActivity().getString(R.string.vsat))){ sp_conn_connType.setSelection(1);
                        }else if (setConnType.equals(getActivity().getString(R.string.m2m))){ sp_conn_connType.setSelection(2); }
                        txt_conn_m2m_brand.setText(al_m2mSetup.get(0).getBrand_type_m2m().trim());
                        txt_conn_m2m_sn.setText(al_m2mSetup.get(0).getSn_m2m().trim());
                        txt_conn_m2m_adaptorBrand.setText(al_m2mSetup.get(0).getBrand_type_adaptor().trim());
                        txt_conn_m2m_adaptorSn.setText(al_m2mSetup.get(0).getSn_adaptor().trim());
                        txt_conn_m2m_sc1Brand.setText(al_m2mSetup.get(0).getSim_card1_type().trim());
                        txt_conn_m2m_sc1Sn.setText(al_m2mSetup.get(0).getSim_card1_sn().trim());
                        txt_conn_m2m_sc1puk.setText(al_m2mSetup.get(0).getSim_card2_type().trim());
                        txt_conn_m2m_sc2Brand.setText(al_m2mSetup.get(0).getSim_card2_type().trim());
                        txt_conn_m2m_sc2puk.setText(al_m2mSetup.get(0).getSim_card2_puk().trim());
                        txt_conn_m2m_sc2Sn.setText(al_m2mSetup.get(0).getSim_card2_sn().trim());
                    }
                }
                imgB_con_submit.setVisibility(View.GONE);
                imgB_con_cancel.setVisibility(View.GONE);
            }else{
                imgB_con_submit.setVisibility(View.VISIBLE);
                imgB_con_cancel.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            Log.d("###",""+e.toString());
            imgB_con_submit.setVisibility(View.VISIBLE);
            imgB_con_cancel.setVisibility(View.VISIBLE);
        }
    }
    private void displayAllSpinner(){
        arrConnectionType  = new String[]{"Jenis Koneksi",getActivity().getString(R.string.vsat), getActivity().getString(R.string.m2m)};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrConnectionType);
        sp_conn_connType.setAdapter(adapter);
        sp_conn_connType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    selectedConn = adapter.getItem(position);

                    if (selectedConn.equals("VSAT")){
                        ln_conn_vsat.setVisibility(View.VISIBLE);
                        ln_conn_m2m.setVisibility(View.GONE);
                    }else{
                        ln_conn_vsat.setVisibility(View.GONE);
                        ln_conn_m2m.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        arrAntenaType = new String[]{getActivity().getString(R.string.diameter_antena),"1.2", "1.8","2.4"};
        final ArrayAdapter<String> adapterAntena = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrAntenaType);
        sp_conn_antena.setAdapter(adapterAntena);
        sp_conn_antena.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){ selectedAntena = adapterAntena.getItem(position); }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        arrPedestialType = new String[]{getActivity().getString(R.string.pedestal_type),getActivity().getString(R.string.rb_nprm), getActivity().getString(R.string.rb_wallmount),getActivity().getString(R.string.rb_groundmount)};
        final ArrayAdapter<String> adapterPedestial = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrPedestialType);
        sp_conn_pedestial.setAdapter(adapterPedestial);
        sp_conn_pedestial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){selectedPedestial = adapterPedestial.getItem(position);}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        arrAntenaAccess = new String[]{getActivity().getString(R.string.access_antena),getActivity().getString(R.string.rb_machine_24), getActivity().getString(R.string.rb_machine_not24)};
        final ArrayAdapter<String> adapterAccessAntena = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrAntenaAccess);
        sp_conn_antenaAccess.setAdapter(adapterAccessAntena);
        sp_conn_antenaAccess.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){selectedAccess = adapterAccessAntena.getItem(position);}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    @OnClick(R.id.imgB_con_submit)
    void submitConn(){
        if (preference.getCustID() == 0){
            messageUtils.snackBar_message(getActivity().getString(R.string.customer_validation),getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
        }else if (selectedConn.equals("")){
            messageUtils.snackBar_message("mohon dipilih jenis koneksi", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
        }else if (selectedConn.equals("VSAT")){
            if (!vsatValidation()){ messageUtils.snackBar_message(getActivity().getString(R.string.emptyString), getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
            }else{ vsatTrans(); }
        }else if(selectedConn.equals("M2M")){
            if (!m2mValidation()){ messageUtils.snackBar_message(getActivity().getString(R.string.emptyString),getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
            }else { m2mTrans(); }
        }
    }

    //vsat
    private boolean vsatValidation(){
        if(txt_conn_vsatModem.getText().toString().trim().equals("") ||
                txt_conn_vsatAdaptor.getText().toString().trim().equals("") ||
                txt_conn_vsatFh.getText().toString().trim().equals("") ||
                txt_conn_vsatLnb.getText().toString().trim().equals("") ||
                txt_conn_vsatRfu.getText().toString().trim().equals("") ||
                txt_conn_vsatOdu.getText().toString().trim().equals("") ||
                txt_conn_vsatIdu.getText().toString().trim().equals("") ||
                txt_conn_vsat_antenaType.getText().toString().trim().equals("") ||
                txt_conn_vsat_antenaBrand.getText().toString().trim().equals("") ||
                selectedAntena.equals("") || selectedAccess.equals("") ||
                selectedPedestial.equals("")){
            return false;
        }else{
            return true;
        }
    }

    private void vsatTrans(){
        ArrayList<MasterM2mSetup> al_valM2mSetup = new M2mSetupAdapter(getActivity()).val_m2mSetup(preference.getCustID(), preference.getUn());
        if (al_valM2mSetup.size() > 0){
            messageUtils.toastMessage("Tipe koneksi M2M sudah diinput, transaksi dibatalkan", ConfigApps.T_INFO);
        }else{
            ArrayList<MasterVsatSetup> al_vsatSetup = new VsatSetupAdapter(getActivity())
                    .val_vsatSetup(preference.getCustID(), preference.getUn());
            if (al_vsatSetup.size() > 0){
                try{
                    MasterVsatSetup mVsatSetup = vsatSetupDao.queryForId(al_vsatSetup.get(0).getId_setup());
                    mVsatSetup.setSn_modem(txt_conn_vsatModem.getText().toString().trim());
                    mVsatSetup.setSn_adaptor(txt_conn_vsatAdaptor.getText().toString().trim());
                    mVsatSetup.setSn_fh(txt_conn_vsatFh.getText().toString().trim());
                    mVsatSetup.setSn_lnb(txt_conn_vsatLnb.getText().toString().trim());
                    mVsatSetup.setSn_rfu(txt_conn_vsatRfu.getText().toString().trim());
                    mVsatSetup.setSn_dip_odu(txt_conn_vsatOdu.getText().toString().trim());
                    mVsatSetup.setSn_dip_idu(txt_conn_vsatIdu.getText().toString().trim());
                    mVsatSetup.setAntena_size(selectedAntena.trim());
                    mVsatSetup.setAntena_brand(txt_conn_vsat_antenaBrand.getText().toString().trim());
                    mVsatSetup.setAntena_type(txt_conn_vsat_antenaType.getText().toString().trim());
                    mVsatSetup.setPedestal_type(selectedPedestial.trim());
                    mVsatSetup.setAccess_type(selectedAccess.trim());
                    mVsatSetup.setProgress_type(preference.getProgress().trim());
                    mVsatSetup.setUpdate_date(DateTimeUtils.getCurrentTime());

                    vsatSetupDao.update(mVsatSetup);
                    transHist(getActivity().getString(R.string.ioVSAT_trans), ConfigApps.TRANS_HIST_UPDATE);
                }catch (Exception e){ messageUtils.toastMessage("Err Vsat Setup 1 " +e.toString(), ConfigApps.T_ERROR ); }
            }else{
                try{
                    MasterVsatSetup mVsatSetup = new MasterVsatSetup();
                    mVsatSetup.setId_site(preference.getCustID());
                    mVsatSetup.setUn_user(preference.getUn());
                    mVsatSetup.setSn_modem(txt_conn_vsatModem.getText().toString().trim());
                    mVsatSetup.setSn_adaptor(txt_conn_vsatAdaptor.getText().toString().trim());
                    mVsatSetup.setSn_fh(txt_conn_vsatFh.getText().toString().trim());
                    mVsatSetup.setSn_lnb(txt_conn_vsatLnb.getText().toString().trim());
                    mVsatSetup.setSn_rfu(txt_conn_vsatRfu.getText().toString().trim());
                    mVsatSetup.setSn_dip_odu(txt_conn_vsatOdu.getText().toString().trim());
                    mVsatSetup.setSn_dip_idu(txt_conn_vsatIdu.getText().toString().trim());
                    mVsatSetup.setAntena_size(selectedAntena.trim());
                    mVsatSetup.setAntena_brand(txt_conn_vsat_antenaBrand.getText().toString().trim());
                    mVsatSetup.setAntena_type(txt_conn_vsat_antenaType.getText().toString().trim());
                    mVsatSetup.setPedestal_type(selectedPedestial.trim());
                    mVsatSetup.setAccess_type(selectedAccess.trim());
                    mVsatSetup.setProgress_type(preference.getProgress().trim());
                    mVsatSetup.setInsert_date(DateTimeUtils.getCurrentTime());

                    vsatSetupDao.create(mVsatSetup);
                    preference.saveConnection(selectedConn.trim());
                    transHist(getActivity().getString(R.string.ioVSAT_trans), ConfigApps.TRANS_HIST_INSERT);
                }catch (Exception e){ messageUtils.toastMessage("Err Vsat Setup 2 " +e.toString(), ConfigApps.T_ERROR ); }
            }
        }
    }

    private boolean m2mValidation(){
        if (txt_conn_m2m_brand.getText().toString().trim().equals("") ||
                txt_conn_m2m_sn.getText().toString().trim().equals("") ||
                txt_conn_m2m_adaptorBrand.getText().toString().trim().equals("") ||
                txt_conn_m2m_adaptorSn.getText().toString().trim().equals("") ||
                txt_conn_m2m_sc1Brand.getText().toString().trim().equals("") ||
                txt_conn_m2m_sc1Sn.getText().toString().trim().equals("") ||
                txt_conn_m2m_sc1puk.getText().toString().trim().equals("") ||
                txt_conn_m2m_sc2Brand.getText().toString().trim().equals("") ||
                txt_conn_m2m_sc2Sn.getText().toString().trim().equals("") ||
                txt_conn_m2m_sc2puk.getText().toString().trim().equals("")){
            return false;
        }else{
            return true;
        }
    }

    private void m2mTrans(){
        ArrayList<MasterVsatSetup> al_vsatSetup = new VsatSetupAdapter(getActivity())
                .val_vsatSetup(preference.getCustID(), preference.getUn());
        if (al_vsatSetup.size() > 0){
            messageUtils.toastMessage("Tipe koneksi VSAT sudah diinput, transaksi dibatalkan", ConfigApps.T_INFO);
        }else{
            ArrayList<MasterM2mSetup> al_valM2mSetup = new M2mSetupAdapter(getActivity()).val_m2mSetup(preference.getCustID(), preference.getUn());
            if (al_valM2mSetup.size() > 0){
                try{
                    MasterM2mSetup m2mSetup = m2mSetupDao.queryForId(al_valM2mSetup.get(0).getId_setup());
                    m2mSetup.setBrand_type_m2m(txt_conn_m2m_brand.getText().toString().trim());
                    m2mSetup.setSn_m2m(txt_conn_m2m_sn.getText().toString().trim());
                    m2mSetup.setBrand_type_adaptor(txt_conn_m2m_adaptorBrand.getText().toString().trim());
                    m2mSetup.setSn_adaptor(txt_conn_m2m_adaptorSn.getText().toString().trim());
                    m2mSetup.setSim_card1_type(txt_conn_m2m_sc1Brand.getText().toString().trim());
                    m2mSetup.setSim_card1_sn(txt_conn_m2m_sc1Sn.getText().toString().trim());
                    m2mSetup.setSim_card1_puk(txt_conn_m2m_sc1puk.getText().toString().trim());
                    m2mSetup.setSim_card2_type(txt_conn_m2m_sc2Brand.getText().toString().trim());
                    m2mSetup.setSim_card2_puk(txt_conn_m2m_sc2puk.getText().toString().trim());
                    m2mSetup.setSim_card2_sn(txt_conn_m2m_sc2Sn.getText().toString().trim());
                    m2mSetup.setUpdate_date(DateTimeUtils.getCurrentTime());
                    m2mSetup.setProgress_type(preference.getProgress().trim());

                    m2mSetupDao.update(m2mSetup);
                    transHist(getActivity().getString(R.string.ioM2M_trans), ConfigApps.TRANS_HIST_UPDATE);
                }catch (Exception e){
                    messageUtils.toastMessage("Err m2m Setup 1 " +e.toString(), ConfigApps.T_ERROR );
                }
            }else{
                try {
                    MasterM2mSetup m2mSetup = new MasterM2mSetup();
                    m2mSetup.setId_site(preference.getCustID());
                    m2mSetup.setUn_user(preference.getUn());
                    m2mSetup.setConnection_type(selectedConn.trim());
                    m2mSetup.setBrand_type_m2m(txt_conn_m2m_brand.getText().toString().trim());
                    m2mSetup.setSn_m2m(txt_conn_m2m_sn.getText().toString().trim());
                    m2mSetup.setBrand_type_adaptor(txt_conn_m2m_adaptorBrand.getText().toString().trim());
                    m2mSetup.setSn_adaptor(txt_conn_m2m_adaptorSn.getText().toString().trim());
                    m2mSetup.setSim_card1_type(txt_conn_m2m_sc1Brand.getText().toString().trim());
                    m2mSetup.setSim_card1_sn(txt_conn_m2m_sc1Sn.getText().toString().trim());
                    m2mSetup.setSim_card1_puk(txt_conn_m2m_sc1puk.getText().toString().trim());
                    m2mSetup.setSim_card2_type(txt_conn_m2m_sc2Brand.getText().toString().trim());
                    m2mSetup.setSim_card2_puk(txt_conn_m2m_sc2puk.getText().toString().trim());
                    m2mSetup.setSim_card2_sn(txt_conn_m2m_sc2Sn.getText().toString().trim());
                    m2mSetup.setInsert_date(DateTimeUtils.getCurrentTime());
                    m2mSetup.setProgress_type(preference.getProgress().trim());

                    m2mSetupDao.create(m2mSetup);
                    preference.saveConnection(selectedConn.trim());
                    transHist(getActivity().getString(R.string.ioM2M_trans), ConfigApps.TRANS_HIST_INSERT);
                }catch (Exception e){messageUtils.toastMessage("Err m2m Setup 1 " +e.toString(), ConfigApps.T_ERROR );}
            }
        }
    }

    //transHist
    private void transHist(String transType, int updateType){
        ArrayList<MasterTransHistory> al_valTransHist = new TransHistoryAdapter(getActivity())
                .val_trans(preference.getCustID(), preference.getUn(),transType);
        if (al_valTransHist.size() > 0){
            try{
                MasterTransHistory mHist = transHistAdapter.queryForId(al_valTransHist.get(0).getId_trans());
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(transType.trim());
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setIs_submited(0);

                transHistAdapter.update(mHist);
                if (updateType == ConfigApps.TRANS_HIST_UPDATE){
                    messageUtils.toastMessage(getString(R.string.transaction_success) + " diupdate", ConfigApps.T_SUCCESS);
                }else{
                    messageUtils.toastMessage(getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                }

               setEmptyConText();
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
                mHist.setTrans_step(transType.trim());
                mHist.setIs_submited(0);

                transHistAdapter.create(mHist);
                if (updateType == ConfigApps.TRANS_HIST_UPDATE){
                    messageUtils.toastMessage(getString(R.string.transaction_success) + " diupdate", ConfigApps.T_SUCCESS);
                }else{
                    messageUtils.toastMessage(getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                }
                setEmptyConText();
                if (getActivity() != null){
                    JobReportUtils.hideKeyboard(getActivity());
                }
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist insert " +e.toString(), ConfigApps.T_ERROR);
            }
        }

    }

    @OnClick(R.id.imgB_con_menu)
    void chooseConnMenu(View view){
        Context wrapper = new ContextThemeWrapper(getActivity(), R.style.PopupMenu);
        PopupMenu popup = new PopupMenu(wrapper, view);
        if (selectedConn.equals("")){
            messageUtils.snackBar_message("Mohon dipilih jenis koneksi", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
        } else if(selectedConn.equals("VSAT")){
            if (preference.getCustID() == 0){
                messageUtils.snackBar_message(getActivity().getString(R.string.customer_validation),getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
            }else if (preference.getConnType().equals("")){
                messageUtils.snackBar_message("Mohon diinput dahulu Menu Connection ", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
            } else if (!preference.getConnType().equals("") && !preference.getConnType().equals("VSAT") ){
                messageUtils.snackBar_message("Transaksi M2M sudah diinput, mohon pilih jenis koneksi M2M", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
            } else{
                popup.getMenuInflater().inflate(R.menu.vsat_menu, popup.getMenu());
            }
        }else {
            if (preference.getCustID() == 0){
                messageUtils.snackBar_message(getActivity().getString(R.string.customer_validation), getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
            }else if (preference.getConnType().trim().equals("")){
                messageUtils.snackBar_message("Mohon diinput dahulu Menu Connection ", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
            } else if (!preference.getConnType().equals("M2M") && !preference.getConnType().equals("")){
                messageUtils.snackBar_message("Transaksi VSAT sudah diinput, mohon pilih jenis koneksi VSAT", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
            } else{
                popup.getMenuInflater().inflate(R.menu.m2m_menu, popup.getMenu());
            }
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()){
                    case R.id.mnu_vsat_replace :
                        intent = new Intent(getActivity(), ReplaceActivity.class);
                        intent.putExtra(ReplaceActivity.EXTRA_CALLER_CONN, ReplaceActivity.EXTRA_CALLER_VSATCONN);

                        break;
                    case R.id.mnu_xpoll :
                        intent = new Intent(getActivity(), XpollActivity.class);
                        break;
                    case R.id.mnu_parameter :
                        intent = new Intent(getActivity(), ParameterActivity.class);
                        break;
                    case R.id.mnu_m2m_replace :
                        intent = new Intent(getActivity(), ReplaceActivity.class);
                        intent.putExtra(ReplaceActivity.EXTRA_CALLER_CONN, ReplaceActivity.EXTRA_CALLER_M2MCONN);
                        break;
                    case R.id.mnu_data_m2m :
                        intent = new Intent(getActivity(), DataM2mActivity.class);
                        break;
                        default:
                            break;
                }
                if (intent != null){
                    startActivity(intent);
                }
                return true;
            }
        });
        popup.show();
    }

    @OnClick(R.id.imgB_con_cancel)
    void cancelConn(){
        setEmptyConText();

    }

    private void setEmptyConText(){
        txt_conn_vsatModem.setText("");
        txt_conn_vsatAdaptor.setText("");
        txt_conn_vsatFh.setText("");
        txt_conn_vsatLnb.setText("");
        txt_conn_vsatRfu.setText("");
        txt_conn_vsatOdu.setText("");
        txt_conn_vsatIdu.setText("");
        txt_conn_vsat_antenaType.setText("");
        txt_conn_vsat_antenaBrand.setText("");

        txt_conn_m2m_brand.setText("");
        txt_conn_m2m_sn.setText("");
        txt_conn_m2m_adaptorBrand.setText("");
        txt_conn_m2m_adaptorSn.setText("");
        txt_conn_m2m_sc1Sn.setText("");
        txt_conn_m2m_sc1puk.setText("");
        txt_conn_m2m_sc2Brand.setText("");
        txt_conn_m2m_sc2Sn.setText("");
        txt_conn_m2m_sc2puk.setText("");
        txt_conn_m2m_sc1Brand.setText("");

    }




}
