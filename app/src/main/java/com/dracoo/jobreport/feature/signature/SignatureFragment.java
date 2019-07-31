package com.dracoo.jobreport.feature.signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.JobDescAdapter;
import com.dracoo.jobreport.database.master.MasterJobDesc;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SignatureFragment extends Fragment {

    @BindView(R.id.sp_sign_userType)
    Spinner sp_sign_userType;
    @BindView(R.id.cardView_sign_1)
    CardView cardView_sign_1;
    @BindView(R.id.rl_sign_list)
    RelativeLayout rl_sign_list;
    @BindView(R.id.canvasLayout)
    LinearLayout canvasLayout;
    @BindView(R.id.rc_sign_activity)
    RecyclerView rc_sign_activity;
    @BindView(R.id.lbl_sign_empty)
    TextView lbl_sign_empty;
    @BindView(R.id.imgB_sign_arrow)
    ImageButton imgB_sign_arrow;
    @BindView(R.id.imgB_sign_cancel)
    ImageButton imgB_sign_cancel;
    @BindView(R.id.imgB_sign_submit)
    ImageButton imgB_sign_submit;


    private String selectedUserType;
    private MessageUtils messageUtils;
    private Preference preference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signature, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        messageUtils = new MessageUtils(getActivity());
        preference = new Preference(getActivity());
        initUserSpinner();
    }

    private void initUserSpinner(){
        try {
            if (preference.getCustID().equals("")){ messageUtils.toastMessage("Mohon diisi menu Customer terlebih dahulu", ConfigApps.T_WARNING);
            }else{
                ArrayList<MasterJobDesc> al_JobDesc = new JobDescAdapter(getActivity()).load_trans(preference.getCustID(), preference.getUn());
                if (al_JobDesc.size() > 0){
                    String[] arrUserType = new String[]{"Jenis User",preference.getUn(), al_JobDesc.get(0).getName_pic()};
                    final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrUserType);
                    adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                    sp_sign_userType.setAdapter(adapter);
                    sp_sign_userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position > 0){ selectedUserType  = adapter.getItem(position); }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) { }
                    });
                }
            }
        }catch (Exception e){

        }

    }


}
