package com.dracoo.jobreport.feature.machine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.Spinner;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.MachineAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.master.MasterMachine;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.feature.MenuActivity;
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

    @BindView(R.id.sp_machine_location)
    Spinner sp_machine_location;
    @BindView(R.id.sp_machine_qty)
    Spinner sp_machine_qty;
    @BindView(R.id.sp_machine_24)
    Spinner sp_machine_24;
    @BindView(R.id.txt_machine_idMachine)
    EditText txt_machine_idMachine;
    @BindView(R.id.imgB_machine_submit)
    ImageButton imgB_machine_submit;

    private MessageUtils messageUtils;
    private Preference preference;
    private Dao<MasterMachine, Integer> machineAdapter;
    private Dao<MasterTransHistory, Integer> transHistAdapter;
    private String selectedMachineLoc = "";
    private String selectedMachineQty = "";
    private String selectedMachine24 = "";
    private String[] arrMachineLoc;
    private String[] arrMachineQty;
    private String[] arrMachine24;
    private String intentEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_machine, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        messageUtils = new MessageUtils(getActivity());
        preference = new Preference(getActivity());
        displayAllMachineForm();
        try{
            machineAdapter = new MachineAdapter(getActivity()).getAdapter();
            transHistAdapter = new TransHistoryAdapter(getActivity()).getAdapter();
        }catch (Exception e){}
        editMachineView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        try{
            if (intentEdit.equals(ConfigApps.VIEW_TYPE)){ inflater.inflate(R.menu.menu, menu); }
        }catch (Exception e){}
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_menu :
                imgB_machine_submit.setVisibility(View.VISIBLE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void editMachineView(){
        try{
            intentEdit = getArguments().getString(ConfigApps.EXTRA_CALLER_VIEW);
            if (intentEdit.trim().equals(ConfigApps.VIEW_TYPE)){
                ArrayList<MasterMachine> alMachine = new MachineAdapter(getActivity()).val_machine(preference.getCustID(), preference.getUn());
                if (alMachine.size() > 0){
                    String item24 = alMachine.get(0).getAccess_type().trim();
                    String machineLoc = alMachine.get(0).getMachine_type().trim();
                    String machineQty = String.valueOf(alMachine.get(0).getMachine_qty());

                    if (item24.trim().equals(getActivity().getString(R.string.rb_machine_24))){ sp_machine_24.setSelection(1);
                    }else if (item24.trim().equals(getActivity().getString(R.string.rb_machine_not24))){ sp_machine_24.setSelection(2); }

                    if (machineLoc.trim().equals(getActivity().getString(R.string.rb_sendiri))){ sp_machine_location.setSelection(1);
                    }else if (machineLoc.trim().equals(getActivity().getString(R.string.rb_center))){ sp_machine_location.setSelection(2); }

                    if (machineQty.trim().equals(getActivity().getString(R.string.rb_machine_1))){ sp_machine_qty.setSelection(1);
                    }else if (machineQty.trim().equals(getActivity().getString(R.string.rb_machine_2))){ sp_machine_qty.setSelection(2);
                    }else if (machineQty.trim().equals(getActivity().getString(R.string.rb_machine_3))){ sp_machine_qty.setSelection(3); }

                    imgB_machine_submit.setVisibility(View.GONE);
                }else{
                    imgB_machine_submit.setVisibility(View.VISIBLE);
                }
            }
        }catch (Exception e){
            Log.d("###",""+e.toString());
            imgB_machine_submit.setVisibility(View.VISIBLE);
        }
    }

    private void displayAllMachineForm(){
        arrMachineLoc = new String[]{getActivity().getString(R.string.machine_location),
                getActivity().getString(R.string.rb_sendiri),
                getActivity().getString(R.string.rb_center)};
        final ArrayAdapter<String> adapterMachineLoc = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrMachineLoc);
        adapterMachineLoc.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        sp_machine_location.setAdapter(adapterMachineLoc);
        sp_machine_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){selectedMachineLoc = adapterMachineLoc.getItem(position);}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        arrMachineQty = new String[]{getActivity().getString(R.string.machine_qty),
                getActivity().getString(R.string.rb_machine_1),
                getActivity().getString(R.string.rb_machine_2),
                getActivity().getString(R.string.rb_machine_3)};

        final ArrayAdapter<String> adapterMachineQty = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrMachineQty);
        adapterMachineQty.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        sp_machine_qty.setAdapter(adapterMachineQty);
        sp_machine_qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){selectedMachineQty = adapterMachineQty.getItem(position);}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        arrMachine24 = new String[]{getActivity().getString(R.string.access),
                getActivity().getString(R.string.rb_machine_24),
                getActivity().getString(R.string.rb_machine_not24)};
        final ArrayAdapter<String> adapterMachine24 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrMachine24);
        adapterMachine24.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        sp_machine_24.setAdapter(adapterMachine24);
        sp_machine_24.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {selectedMachine24 = adapterMachine24.getItem(position);}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    @OnClick(R.id.imgB_machine_submit)
    void submitMachine(){
        try{
            if (selectedMachineLoc.equals("") || selectedMachineQty.equals("") ||
                txt_machine_idMachine.getText().toString().trim().equals("") || selectedMachine24.equals("")){
                messageUtils.toastMessage("Mohon dipilih pilihan yang belum dipilih", ConfigApps.T_WARNING);
            }else{
                machineTrans();
            }
        }catch (Exception e){
            messageUtils.toastMessage("Mohon dipilh pilihan ", ConfigApps.T_WARNING);
        }
    }


    private void machineTrans(){
        if(preference.getCustID() == 0){
            messageUtils.toastMessage(getActivity().getString(R.string.customer_validation), ConfigApps.T_WARNING);
        }else{
            ArrayList<MasterMachine> al_valMachine = new MachineAdapter(getActivity()).val_machine(preference.getCustID(), preference.getUn());
            if (al_valMachine.size() > 0){
                try{
                    MasterMachine mMachine = machineAdapter.queryForId(al_valMachine.get(0).getId_machine());
                    mMachine.setProgress_type(preference.getProgress().trim());
                    mMachine.setUpdate_date(DateTimeUtils.getCurrentTime());
                    mMachine.setAccess_type(selectedMachine24.trim());
                    mMachine.setMachine_type(selectedMachineLoc.trim());
                    mMachine.setMachine_no(txt_machine_idMachine.getText().toString().trim());
                    mMachine.setMachine_qty(Integer.parseInt(selectedMachineLoc.trim()));

                    machineAdapter.update(mMachine);
                    transHistProb(ConfigApps.TRANS_HIST_UPDATE);
                }catch (Exception e){
                    messageUtils.toastMessage("Err trans Machine1 " +e.toString(), ConfigApps.T_ERROR);
                }
            }else{
                try{
                    MasterMachine mMachine = new MasterMachine();
                    mMachine.setProgress_type(preference.getProgress().trim());
                    mMachine.setInsert_date(DateTimeUtils.getCurrentTime());
                    mMachine.setAccess_type(selectedMachine24.trim());
                    mMachine.setMachine_type(selectedMachineLoc.trim());
                    mMachine.setMachine_no(txt_machine_idMachine.getText().toString().trim());
                    mMachine.setMachine_qty(Integer.parseInt(selectedMachineQty.trim()));
                    mMachine.setId_site(preference.getCustID());
                    mMachine.setUn_user(preference.getUn());

                    machineAdapter.create(mMachine);
                    transHistProb(ConfigApps.TRANS_HIST_INSERT);
                }catch (Exception e){
                    messageUtils.toastMessage("Err trans Machine2 " +e.toString(), ConfigApps.T_ERROR);
                }
            }
        }
    }

    private void transHistProb(int transType){
        ArrayList<MasterTransHistory> al_valTransHist = new TransHistoryAdapter(getActivity())
                .val_trans(preference.getCustID(), preference.getUn(), getActivity().getString(R.string.machine_trans));
        if (al_valTransHist.size() > 0){
            try{
                MasterTransHistory mHist = transHistAdapter.queryForId(al_valTransHist.get(0).getId_trans());
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getActivity().getString(R.string.machine_trans));
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setIs_submited(0);

                transHistAdapter.update(mHist);
                if (transType == ConfigApps.TRANS_HIST_UPDATE){ messageUtils.toastMessage(getActivity().getString(R.string.transaction_success) + " diupdate", ConfigApps.T_SUCCESS);
                }else{ messageUtils.toastMessage(getActivity().getString(R.string.transaction_success), ConfigApps.T_SUCCESS); }

                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY, MenuActivity.EXTRA_FLAG_DASH);
                startActivity(intent);

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
                if (transType == ConfigApps.TRANS_HIST_UPDATE){
                    messageUtils.toastMessage(getActivity().getString(R.string.transaction_success) + " diupdate", ConfigApps.T_SUCCESS);
                }else{
                    messageUtils.toastMessage(getActivity().getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                }

                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY, MenuActivity.EXTRA_FLAG_DASH);
                startActivity(intent);
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist 2 " +e.toString(), ConfigApps.T_ERROR);
            }
        }
    }
}
