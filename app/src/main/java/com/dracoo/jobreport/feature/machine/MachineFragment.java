package com.dracoo.jobreport.feature.machine;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.MessageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    private String selectedMachineLocation = "";

    private RadioButton rb_machineQty;
    private String selectedMachineQty = "";

    private RadioButton rb_machineId;
    private String selectedMachineId = "";

    private RadioButton rb_machine24;
    private String selectedMachine24 = "";

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
        rbMachineLocation();
        rbMachineQty();
        rbMachineId();
        rbMachine24();
    }

    private void rbMachineLocation(){
        rg_machine_qty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_machineQty =  (RadioButton) getView().findViewById(i);
                selectedMachineLocation = rb_machineQty.getText().toString();
            }
        });
    }

    private void rbMachineQty(){
        rg_machine_location.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_machineQty =  (RadioButton) getView().findViewById(i);
                selectedMachineQty = rb_machineQty.getText().toString();
            }
        });
    }

    private void rbMachineId(){
        rg_machine_id.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_machineId =  (RadioButton) getView().findViewById(i);
                selectedMachineId = rb_machineId.getText().toString();
            }
        });
    }

    private void rbMachine24(){
        rg_machine_24.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_machine24 =  (RadioButton) getView().findViewById(i);
                selectedMachine24 = rb_machine24.getText().toString();
            }
        });
    }


}
