package com.dracoo.jobreport.feature.connection;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.M2mSetupAdapter;
import com.dracoo.jobreport.database.adapter.VsatSetupAdapter;
import com.dracoo.jobreport.database.master.MasterM2mSetup;
import com.dracoo.jobreport.database.master.MasterVsatSetup;
import com.dracoo.jobreport.feature.datam2m.DataM2mActivity;
import com.dracoo.jobreport.feature.replace.ReplaceActivity;
import com.dracoo.jobreport.feature.vsatparameter.ParameterActivity;
import com.dracoo.jobreport.feature.xpoll.XpollActivity;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;
import com.j256.ormlite.dao.Dao;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectionFragment extends Fragment {


    @BindView(R.id.rg_conn_type)
    RadioGroup rg_conn_type;
    @BindView(R.id.rg_conn_antena)
    RadioGroup rg_conn_antena;
    @BindView(R.id.rg_conn_pedestial)
    RadioGroup rg_conn_pedestial;
    @BindView(R.id.rg_conn_access)
    RadioGroup rg_conn_access;
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

    private MessageUtils messageUtils;
    private RadioButton rb_selectedConn;
    private String selectedConn = "";
    private RadioButton rb_selectedAntena;
    private String selectedAntena = "";
    private RadioButton rb_selectedPedestial;
    private String selectedPedestial = "";
    private RadioButton rb_selectedAccess;
    private String selectedAccess = "";
    private Animation anim_slideDown;
    private Preference preference;
    private Dao<MasterVsatSetup, Integer> vsatSetupDao;
    private Dao<MasterM2mSetup, Integer> m2mSetupDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        messageUtils = new MessageUtils(getActivity());
        preference = new Preference(getActivity());
        radioConnListener();
        radioAntenaListener();
        radioPedestialListener();
        radioAccessListener();

        try{
            vsatSetupDao = new VsatSetupAdapter(getActivity()).getAdapter();
            m2mSetupDao = new M2mSetupAdapter(getActivity()).getAdapter();
        }catch (Exception e){}
    }

    private void radioConnListener(){
        rg_conn_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_selectedConn =  getView().findViewById(i);
                selectedConn = ""+rb_selectedConn.getText().toString();

                if (selectedConn.equals("VSAT")){
                    ln_conn_vsat.setVisibility(View.VISIBLE);
                    ln_conn_m2m.setVisibility(View.GONE);
                }else{
                    ln_conn_vsat.setVisibility(View.GONE);
                    ln_conn_m2m.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void radioAntenaListener(){
        rg_conn_antena.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_selectedAntena =  getView().findViewById(i);
                selectedAntena = ""+rb_selectedAntena.getText().toString();
            }
        });
    }

    private void radioPedestialListener(){
        rg_conn_pedestial.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_selectedPedestial =  getView().findViewById(i);
                selectedPedestial = ""+rb_selectedPedestial.getText().toString();
            }
        });
    }

    private void radioAccessListener(){
        rg_conn_access.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_selectedAccess =  getView().findViewById(i);
                selectedAccess = ""+rb_selectedAccess.getText().toString();
            }
        });
    }

    @OnClick(R.id.imgB_con_submit)
    void submitConn(){
        if (preference.getCustID() == 0){
            messageUtils.toastMessage(getActivity().getString(R.string.customer_validation), ConfigApps.T_WARNING);
        }else if (selectedConn.equals("null") || selectedConn == null){
            messageUtils.toastMessage("mohon dipilih jenis koneksi", ConfigApps.T_DEFAULT);
        }else if (selectedConn.equals("VSAT")){
            if (!vsatValidation()){
                messageUtils.toastMessage(getActivity().getString(R.string.emptyString), ConfigApps.T_WARNING);
            }else{
                messageUtils.toastMessage("coba", ConfigApps.T_INFO);
            }
        }else if(selectedConn.equals("M2M")){
            if (!m2mValidation()){
                messageUtils.toastMessage(getActivity().getString(R.string.emptyString), ConfigApps.T_WARNING);
            }else {
                messageUtils.toastMessage("coba", ConfigApps.T_INFO);
            }
        }
    }

    private boolean vsatValidation(){
        if(txt_conn_vsatModem.getText().toString().trim().equals("") ||
                txt_conn_vsatAdaptor.getText().toString().trim().equals("") ||
                txt_conn_vsatFh.getText().toString().trim().equals("") ||
                txt_conn_vsatLnb.getText().toString().trim().equals("") ||
                txt_conn_vsatRfu.getText().toString().trim().equals("") ||
                txt_conn_vsatOdu.getText().toString().trim().equals("") ||
                txt_conn_vsatIdu.getText().toString().trim().equals("") ||
                txt_conn_vsat_antenaType.getText().toString().trim().equals("") ||
                txt_conn_vsat_antenaBrand.getText().toString().trim().equals("")){
            return false;
        }else{
            return true;
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

    @OnClick(R.id.imgB_con_menu)
    void chooseConnMenu(View view){
        Context wrapper = new ContextThemeWrapper(getActivity(), R.style.PopupMenu);
        PopupMenu popup = new PopupMenu(wrapper, view);
        if (selectedConn.equals("")){
            messageUtils.toastMessage("Mohon dipilih jenis koneksi", ConfigApps.T_WARNING);
        } else if(selectedConn.equals("VSAT")){
            if (preference.getCustID() == 0){
                messageUtils.toastMessage(getActivity().getString(R.string.customer_validation), ConfigApps.T_WARNING);
            }else{
                popup.getMenuInflater().inflate(R.menu.vsat_menu, popup.getMenu());
            }
        }else {
            if (preference.getCustID() == 0){
                messageUtils.toastMessage(getActivity().getString(R.string.customer_validation), ConfigApps.T_WARNING);
            }else{
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
        messageUtils.toastMessage("coba2", ConfigApps.T_DEFAULT);
    }




}
