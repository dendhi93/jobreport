package com.dracoo.jobreport.feature.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.InfoSiteAdapter;
import com.dracoo.jobreport.database.adapter.JobDescAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.master.MasterInfoSite;
import com.dracoo.jobreport.database.master.MasterJobDesc;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.feature.dashboard.adapter.CustomList_Dashboard_Adapter;
import com.dracoo.jobreport.feature.useractivity.UserActivity;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardFragment extends Fragment {
    @BindView(R.id.lbl_dash_locationName)
    TextView lbl_dash_locationName;
    @BindView(R.id.lbl_dash_technician_name)
    TextView lbl_dash_technician_name;
    @BindView(R.id.lbl_dash_customer)
    TextView lbl_dash_customer;
    @BindView(R.id.lbl_dash_picPhone)
    TextView lbl_dash_picPhone;
    @BindView(R.id.rc_dash_activity)
    RecyclerView rc_dash_activity;
    @BindView(R.id.lbl_dash_empty)
    TextView lbl_dash_empty;

    private MessageUtils messageUtils;
    private Preference preference;
    private ArrayList<MasterJobDesc> alJobDesc;
    private ArrayList<MasterInfoSite> alInfSite;
    private ArrayList<MasterTransHistory> alListTrans;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        messageUtils = new MessageUtils(getActivity());
        preference = new Preference(getActivity());

    }

    public List<MasterTransHistory> getList_TransHist(){
        List<MasterTransHistory> list = new ArrayList<>();
        try {
            alListTrans = new TransHistoryAdapter(getActivity())
                    .load_trans(preference.getCustID(), preference.getUn());
            if (alListTrans.size() > 0){
                list = alListTrans;
                rc_dash_activity.setVisibility(View.VISIBLE);
                lbl_dash_empty.setVisibility(View.GONE);
            }else{
                rc_dash_activity.setVisibility(View.GONE);
                lbl_dash_empty.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            rc_dash_activity.setVisibility(View.GONE);
            lbl_dash_empty.setVisibility(View.VISIBLE);
            Log.d("###", "ke catch list " +e.toString());
        }
        return list;
    }

    private void loadRcTrans(){
        if (preference.getCustID() != 0){
            rc_dash_activity.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            rc_dash_activity.setLayoutManager(layoutManager);
            List<MasterTransHistory> list = getList_TransHist();
            adapter = new CustomList_Dashboard_Adapter(getActivity(), list);
            rc_dash_activity.setAdapter(adapter);
        }else{
            Log.d("###","ke sama dengan 0");
        }
    }


    @OnClick(R.id.imgB_dash_toActivity)
    void toActivity (){
        Intent intent = new Intent(getActivity(), UserActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.imgB_dash_confirm)
    void submitUpload(){
        messageUtils.toastMessage("test dulu", ConfigApps.T_INFO);
    }

    @Override
    public void onResume(){
        super.onResume();
        loadDash();
        loadRcTrans();
    }

    private void loadDash(){
        if (preference.getCustID() != 0){
            alJobDesc = new JobDescAdapter(getActivity()).load_trans(preference.getCustID(), preference.getUn());
            if (alJobDesc.size() > 0){
                    alInfSite = new InfoSiteAdapter(getActivity()).load_site(preference.getCustID(), preference.getUn());
                    if (alInfSite.size() > 0){
                        lbl_dash_locationName.setText(alInfSite.get(0).getLocation_name().trim());
                        lbl_dash_customer.setText(alInfSite.get(0).getCustomer_name().trim());

                        lbl_dash_technician_name.setText(alJobDesc.get(0).getName_user().trim());
                        lbl_dash_picPhone.setText(alJobDesc.get(0).getPic_phone().trim());
                    }
            }
        }
    }



}
