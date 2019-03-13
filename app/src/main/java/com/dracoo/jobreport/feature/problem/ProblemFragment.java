package com.dracoo.jobreport.feature.problem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProblemFragment extends Fragment {

    private MessageUtils messageUtils;

    @BindView(R.id.rg_prob_closedBy)
    RadioGroup rg_prob_closedBy;

    private String valueRb = "";
    private RadioButton radioButtonClosed ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_problem, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        messageUtils = new MessageUtils(getActivity());
        rbListener();
    }

    private void rbListener(){
        rg_prob_closedBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButtonClosed =  (RadioButton) getView().findViewById(i);
                valueRb = radioButtonClosed.getText().toString();
            }
        });
    }

    @OnClick(R.id.imgB_problem_submit)
    void submitProblem (){
        messageUtils.toastMessage("coba", ConfigApps.T_DEFAULT);
    }

    @OnClick(R.id.imgB_problem_cancel)
    void probCancel(){
        messageUtils.toastMessage("coba 2", ConfigApps.T_DEFAULT);
    }
    @OnClick(R.id.imgBtn_timer1)
    void displayTime1(){
        messageUtils.toastMessage("coba 3", ConfigApps.T_DEFAULT);
    }
    @OnClick(R.id.imgBtn_timer2)
    void displayTime2(){
        messageUtils.toastMessage("coba 4", ConfigApps.T_DEFAULT);
    }
    @OnClick(R.id.imgBtn_timer3)
    void displayTime3(){
        messageUtils.toastMessage("coba 5", ConfigApps.T_DEFAULT);
    }
    @OnClick(R.id.imgBtn_timer4)
    void displayTime4(){
        messageUtils.toastMessage("coba 6", ConfigApps.T_DEFAULT);
    }
    @OnClick(R.id.imgBtn_timer5)
    void displayTime5(){
        messageUtils.toastMessage("coba 7", ConfigApps.T_DEFAULT);
    }
    @OnClick(R.id.imgBtn_timer6)
    void displayTime6(){
        messageUtils.toastMessage("coba 2", ConfigApps.T_DEFAULT);
    }









}
