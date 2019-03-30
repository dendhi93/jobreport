package com.dracoo.jobreport.feature.machine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.MachineAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.master.MasterMachine;
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


public class MachineFragment extends Fragment {

    @BindView(R.id.rg_machine_location)
    RadioGroup rg_machine_location;

    @BindView(R.id.rg_machine_qty)
    RadioGroup rg_machine_qty;

    @BindView(R.id.rg_machine_id)
    RadioGroup rg_machine_id;

    @BindView(R.id.rg_machine_24)
    RadioGroup rg_machine_24;

    private MessageUtils messageUtils;
    private RadioButton rb_machineLocation;
    private RadioButton rb_machineQty;
    private RadioButton rb_machineId;
    private RadioButton rb_machine24;
    private Preference preference;
    private Dao<MasterMachine, Integer> machineAdapter;
    private Dao<MasterTransHistory, Integer> transHistAdapter;
    private String selectedMachineLoc, selectedMachineQty, selectedMachineId, selectedMachine24;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_machine, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        messageUtils = new MessageUtils(getActivity());
        preference = new Preference(getActivity());
        rbMachineLocation();
        rbMachineQty();
        rbMachineId();
        rbMachine24();

        try{
            machineAdapter = new MachineAdapter(getActivity()).getAdapter();
            transHistAdapter = new TransHistoryAdapter(getActivity()).getAdapter();
        }catch (Exception e){}
    }

    private void rbMachineLocation(){
        rg_machine_location.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_machineLocation =  getView().findViewById(i);
                selectedMachineLoc = ""+rb_machineLocation.getText().toString().trim();
            }
        });
    }

    private void rbMachineQty(){
        rg_machine_qty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_machineQty =  getView().findViewById(i);
                selectedMachineQty = ""+rb_machineQty.getText().toString().trim();
            }
        });
    }

    private void rbMachineId(){
        rg_machine_id.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_machineId =  getView().findViewById(i);
                selectedMachineId = ""+rb_machineId.getText().toString().trim();
            }
        });
    }

    private void rbMachine24(){
        rg_machine_24.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_machine24 =  getView().findViewById(i);
                selectedMachine24 = ""+rb_machine24.getText().toString().trim();
            }
        });
    }

    @OnClick(R.id.imgB_machine_submit)
    void submitMachine(){
        try{
            if (selectedMachineLoc == null || selectedMachineLoc.equals("null") ||
                selectedMachineQty == null || selectedMachineQty.equals("null") ||
                selectedMachineId == null || selectedMachineId.equals("null") ||
                selectedMachine24 == null || selectedMachine24.equals("null")){
                messageUtils.toastMessage("Mohon dipilih pilihan yang belum dipilih", ConfigApps.T_WARNING);
            }else{
                machineTrans();
            }
        }catch (Exception e){
            messageUtils.toastMessage("Mohon dipilh pilihan ", ConfigApps.T_WARNING);
        }
    }

    @OnClick(R.id.imgB_machine_cancel)
    void cancelMachine(){
        setEmptyRb();
    }

    private void setEmptyRb(){
        rg_machine_location.clearCheck();
        rg_machine_qty.clearCheck();
        rg_machine_id.clearCheck();
        rg_machine_24.clearCheck();
    }

    private void machineTrans(){
        if(preference.getCustID().equals("")){
            messageUtils.toastMessage(getActivity().getString(R.string.customer_validation), ConfigApps.T_WARNING);
        }else{
            ArrayList<MasterMachine> al_valMachine = new MachineAdapter(getActivity()).val_machine(preference.getCustID(), preference.getUn());
            if (al_valMachine.size() > 0){
                try{
                    MasterMachine mMachine = machineAdapter.queryForId(al_valMachine.get(0).getId_machine());
                    mMachine.setProgress_type(preference.getProgress().trim());
                    mMachine.setUpdate_date(DateTimeUtils.getCurrentTime());
                    mMachine.setAccess_type(""+rb_machine24.getText().toString().trim());
                    mMachine.setMachine_type(""+rb_machineLocation.getText().toString().trim());
                    mMachine.setMachine_no(""+rb_machineId.getText().toString().trim());
                    mMachine.setMachine_qty(Integer.parseInt(""+rb_machineQty.getText().toString().trim()));

                    machineAdapter.update(mMachine);
                    transHistProb();
                }catch (Exception e){
                    messageUtils.toastMessage("Err trans Machine1 " +e.toString(), ConfigApps.T_ERROR);
                }
            }else{
                try{
                    MasterMachine mMachine = new MasterMachine();
                    mMachine.setProgress_type(preference.getProgress().trim());
                    mMachine.setInsert_date(DateTimeUtils.getCurrentTime());
                    mMachine.setAccess_type(""+rb_machine24.getText().toString().trim());
                    mMachine.setMachine_type(""+rb_machineLocation.getText().toString().trim());
                    mMachine.setMachine_no(""+rb_machineId.getText().toString().trim());
                    mMachine.setMachine_qty(Integer.parseInt(""+rb_machineQty.getText().toString().trim()));
                    mMachine.setId_site(preference.getCustID());
                    mMachine.setUn_user(preference.getUn());

                    machineAdapter.create(mMachine);
                    transHistProb();
                }catch (Exception e){
                    messageUtils.toastMessage("Err trans Machine2 " +e.toString(), ConfigApps.T_ERROR);
                }
            }
        }
    }


    private void transHistProb(){
        ArrayList<MasterTransHistory> al_valTransHist = new TransHistoryAdapter(getActivity())
                .val_trans(preference.getCustID(), preference.getUn(), getActivity().getString(R.string.machine_trans));
        if (al_valTransHist.size() > 0){
            try{
                MasterTransHistory mHist = transHistAdapter.queryForId(al_valTransHist.get(0).getId_site());
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getActivity().getString(R.string.machine_trans));
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setIs_submited(0);

                transHistAdapter.update(mHist);
                messageUtils.toastMessage(getActivity().getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                setEmptyRb();
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist 1 " +e.toString(), ConfigApps.T_ERROR);
            }
        }else{
            try{
                MasterTransHistory mHist = new MasterTransHistory();
                mHist.setId_site(preference.getCustID());
                mHist.setUn_user(preference.getUn());
                mHist.setInsert_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getActivity().getString(R.string.machine_trans));
                mHist.setIs_submited(0);

                transHistAdapter.create(mHist);
                messageUtils.toastMessage(getActivity().getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                setEmptyRb();
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist 2 " +e.toString(), ConfigApps.T_ERROR);
            }
        }
    }


}
