package com.dracoo.jobreport.feature.env;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.EnvAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.master.MasterEnvirontment;
import com.dracoo.jobreport.database.master.MasterTransHistory;
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


public class EnvironmentFragment extends Fragment {
    private MessageUtils messageUtils;

    @BindView(R.id.txt_env_plnTegangan)
    EditText txt_env_plnTegangan;
    @BindView(R.id.txt_env_plnGrounding)
    EditText txt_env_plnGrounding;
    @BindView(R.id.txt_env_upsTegangan)
    EditText txt_env_upsTegangan;
    @BindView(R.id.txt_env_upsGrounding)
    EditText txt_env_upsGrounding;
    @BindView(R.id.txt_env_upsNote)
    EditText txt_env_upsNote;
    @BindView(R.id.txt_env_acSuhu)
    EditText txt_env_acSuhu;
    @BindView(R.id.txt_env_acNote)
    EditText txt_env_acNote;
    @BindView(R.id.imgB_env_submit)
    ImageButton imgB_env_submit;
    @BindView(R.id.imgB_env_cancel)
    ImageButton imgB_env_cancel;

    private Dao<MasterEnvirontment, Integer> envAdapter;
    private Dao<MasterTransHistory, Integer> transHistAdapter;
    private Preference preference;
    private String intentEnvEdit = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_environtment, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        messageUtils = new MessageUtils(getActivity());
        preference = new Preference(getActivity());
        try{
            envAdapter = new EnvAdapter(getActivity()).getAdapter();
            transHistAdapter = new TransHistoryAdapter(getActivity()).getAdapter();
        }catch (Exception e){ }
        editValidation();
    }

    @OnClick(R.id.imgB_env_submit)
    void submitEnv(){
        if (!isEmptyField()){
            messageUtils.toastMessage(getActivity().getString(R.string.emptyString), ConfigApps.T_INFO);
        }else{
            transEnv();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_menu :
                imgB_env_submit.setVisibility(View.VISIBLE);
                imgB_env_cancel.setVisibility(View.VISIBLE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void editValidation(){
        try{
            intentEnvEdit = getActivity().getIntent().getStringExtra(MenuActivity.EXTRA_CALLER_VIEW);
            if (!intentEnvEdit.equals("") || intentEnvEdit != null){
                ArrayList<MasterEnvirontment> alValEnv = new EnvAdapter(getActivity()).val_env(preference.getCustID(), preference.getUn());
                if (alValEnv.size() > 0){
                    txt_env_plnGrounding.setText(alValEnv.get(0).getGrounding_pln().trim());
                    txt_env_upsGrounding.setText(alValEnv.get(0).getGrounding_ups().trim());
                    txt_env_upsNote.setText(alValEnv.get(0).getNotes().trim());
                    txt_env_acNote.setText(alValEnv.get(0).getNotes_ac().trim());
                    txt_env_acSuhu.setText(alValEnv.get(0).getSuhu().trim());
                    txt_env_plnTegangan.setText(alValEnv.get(0).getTegangan_pln().trim());
                    txt_env_upsTegangan.setText(alValEnv.get(0).getTegangan_ups().trim());
                    imgB_env_submit.setVisibility(View.GONE);
                    imgB_env_cancel.setVisibility(View.GONE);
                }
            }else{
                imgB_env_submit.setVisibility(View.VISIBLE);
                imgB_env_cancel.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            Log.d("err edit ",""+e.toString());
            imgB_env_submit.setVisibility(View.VISIBLE);
            imgB_env_cancel.setVisibility(View.VISIBLE);
        }
    }

    private void transEnv(){
        if(preference.getCustID() == 0){
            messageUtils.toastMessage(getActivity().getString(R.string.customer_validation), ConfigApps.T_WARNING);
        }else{
            ArrayList<MasterEnvirontment> al_valEnv = new EnvAdapter(getActivity()).val_env(preference.getCustID(), preference.getUn());
            if (al_valEnv.size() > 0){
                try{
                    MasterEnvirontment mEnv = envAdapter.queryForId(al_valEnv.get(0).getId_env());
                    mEnv.setGrounding_pln(txt_env_plnGrounding.getText().toString().trim());
                    mEnv.setGrounding_ups(txt_env_upsGrounding.getText().toString().trim());
                    mEnv.setUpdate_date(DateTimeUtils.getCurrentTime());
                    mEnv.setNotes(txt_env_upsNote.getText().toString().trim());
                    mEnv.setNotes_ac(txt_env_acNote.getText().toString().trim());
                    mEnv.setSuhu(txt_env_acSuhu.getText().toString().trim());
                    mEnv.setTegangan_pln(txt_env_plnTegangan.getText().toString().trim());
                    mEnv.setTegangan_ups(txt_env_upsTegangan.getText().toString().trim());

                    envAdapter.update(mEnv);
                    transHistEnv(ConfigApps.TRANS_HIST_UPDATE);
                }catch (Exception e){
                    messageUtils.toastMessage("Err mEnv1 " +e.toString(), ConfigApps.T_ERROR);
                }
            }else{
                try {
                    MasterEnvirontment mEnv = new MasterEnvirontment();
                    mEnv.setGrounding_pln(txt_env_plnGrounding.getText().toString().trim());
                    mEnv.setGrounding_ups(txt_env_upsGrounding.getText().toString().trim());
                    mEnv.setId_site(preference.getCustID());
                    mEnv.setInsert_date(DateTimeUtils.getCurrentTime());
                    mEnv.setNotes(txt_env_upsNote.getText().toString().trim());
                    mEnv.setNotes_ac(txt_env_acNote.getText().toString().trim());
                    mEnv.setProgress_type(preference.getProgress().trim());
                    mEnv.setSuhu(txt_env_acSuhu.getText().toString().trim());
                    mEnv.setTegangan_pln(txt_env_plnTegangan.getText().toString().trim());
                    mEnv.setTegangan_ups(txt_env_upsTegangan.getText().toString().trim());
                    mEnv.setUn_user(preference.getUn());

                    envAdapter.create(mEnv);
                    transHistEnv(ConfigApps.TRANS_HIST_INSERT);
                }catch (Exception e){
                    messageUtils.toastMessage("Err mEnv2 " +e.toString(), ConfigApps.T_ERROR);
                }
            }
        }

    }

    private void transHistEnv(int typeTrans){
        ArrayList<MasterTransHistory> al_valTransHist = new TransHistoryAdapter(getActivity())
                .val_trans(preference.getCustID(), preference.getUn(), getActivity().getString(R.string.electEnv_trans));
        if (al_valTransHist.size() > 0){
            try{
                MasterTransHistory mHist = transHistAdapter.queryForId(al_valTransHist.get(0).getId_trans());
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getActivity().getString(R.string.electEnv_trans));
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setIs_submited(0);

                transHistAdapter.update(mHist);
                if (typeTrans == ConfigApps.TRANS_HIST_UPDATE){
                    messageUtils.toastMessage(getActivity().getString(R.string.transaction_success) + " diupdate", ConfigApps.T_SUCCESS);
                }else{
                    messageUtils.toastMessage(getActivity().getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                }
                setEmptyText();
                if (getActivity() != null){
                    JobReportUtils.hideKeyboard(getActivity());
                }
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist 1 " +e.toString(), ConfigApps.T_ERROR);
            }
        }else{
            try{
                MasterTransHistory mHist = new MasterTransHistory();
                mHist.setId_site(preference.getCustID());
                mHist.setUn_user(preference.getUn());
                mHist.setInsert_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getActivity().getString(R.string.electEnv_trans));
                mHist.setIs_submited(0);

                transHistAdapter.create(mHist);
                if (typeTrans == ConfigApps.TRANS_HIST_UPDATE){
                    messageUtils.toastMessage(getActivity().getString(R.string.transaction_success) + " diupdate", ConfigApps.T_SUCCESS);
                }else{
                    messageUtils.toastMessage(getActivity().getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                }
                setEmptyText();
                if (getActivity() != null){
                    JobReportUtils.hideKeyboard(getActivity());
                }
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist 2 " +e.toString(), ConfigApps.T_ERROR);
            }
        }
    }

    @OnClick(R.id.imgB_env_cancel)
    void cancelEnv(){
        setEmptyText();
    }

    private boolean isEmptyField(){
        if (txt_env_acNote.getText().toString().trim().equals("") ||
        txt_env_acSuhu.getText().toString().trim().equals("") ||
        txt_env_plnGrounding.getText().toString().trim().equals("") ||
        txt_env_plnTegangan.getText().toString().trim().equals("") ||
        txt_env_upsGrounding.getText().toString().trim().equals("") ||
                txt_env_upsNote.getText().toString().trim().equals("") ||
        txt_env_upsTegangan.getText().toString().trim().equals("")){
            return false;
        }else{
            return true;
        }
    }

    private void setEmptyText(){
        txt_env_plnTegangan.setText("");
        txt_env_plnGrounding.setText("");
        txt_env_upsTegangan.setText("");
        txt_env_upsGrounding.setText("");
        txt_env_upsNote.setText("");
        txt_env_acSuhu.setText("");
        txt_env_acNote.setText("");
    }



}
