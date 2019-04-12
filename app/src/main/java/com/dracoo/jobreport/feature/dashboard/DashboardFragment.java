package com.dracoo.jobreport.feature.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.ActionAdapter;
import com.dracoo.jobreport.database.adapter.ConnectionParameterAdapter;
import com.dracoo.jobreport.database.adapter.EnvAdapter;
import com.dracoo.jobreport.database.adapter.InfoSiteAdapter;
import com.dracoo.jobreport.database.adapter.JobDescAdapter;
import com.dracoo.jobreport.database.adapter.M2mDataAdapter;
import com.dracoo.jobreport.database.adapter.M2mReplaceAdapter;
import com.dracoo.jobreport.database.adapter.M2mXpollAdapter;
import com.dracoo.jobreport.database.adapter.MachineAdapter;
import com.dracoo.jobreport.database.adapter.ProblemAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.adapter.VsatReplaceAdapter;
import com.dracoo.jobreport.database.master.MasterAction;
import com.dracoo.jobreport.database.master.MasterInfoSite;
import com.dracoo.jobreport.database.master.MasterJobDesc;
import com.dracoo.jobreport.database.master.MasterProblem;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.feature.dashboard.adapter.CustomList_Dashboard_Adapter;
import com.dracoo.jobreport.feature.useractivity.UserActivity;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.DateTimeUtils;
import com.dracoo.jobreport.util.JobReportUtils;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.j256.ormlite.dao.Dao;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
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
    private Dao<MasterTransHistory, Integer> transHistoryAdapter;
    private String[] arr_actionDateTime;
    private String[] arr_actionTrans;


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

        try{
            transHistoryAdapter = new TransHistoryAdapter(getActivity()).getAdapter();
        }catch (Exception e){}
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
        if (preference.getCustID() == 0 || preference.getUn().equals("")){
            messageUtils.toastMessage(getActivity().getString(R.string.customer_validation), ConfigApps.T_INFO);
        }else if (!new ProblemAdapter(getActivity()).isProblemEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.toastMessage("Mohon diinput menu Problem desc terlebih dahulu", ConfigApps.T_INFO);
        }else if (!new EnvAdapter(getActivity()).isEnvEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.toastMessage("Mohon diinput menu Electrical Environtment terlebih dahulu", ConfigApps.T_INFO);
        }else if (!new MachineAdapter(getActivity()).isMachineEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.toastMessage("Mohon diinput menu Machine terlebih dahulu", ConfigApps.T_INFO);
        }else if (preference.getConnType().equals("")){
            messageUtils.toastMessage("Mohon diinput menu Connection terlebih dahulu", ConfigApps.T_INFO);
        }else if (preference.getConnType().equals("VSAT") && !new VsatReplaceAdapter(getActivity()).isVsatReplaceEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.toastMessage("Mohon diinput menu Vsat Replace terlebih dahulu", ConfigApps.T_INFO);
        }else if (preference.getConnType().equals("VSAT") && !new M2mXpollAdapter(getActivity()).isVsatXpollEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.toastMessage("Mohon diinput menu Xpoll terlebih dahulu", ConfigApps.T_INFO);
        }else if (preference.getConnType().equals("VSAT") && !new ConnectionParameterAdapter(getActivity()).isParamEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.toastMessage("Mohon diinput menu Connection Parameter terlebih dahulu", ConfigApps.T_INFO);
        }else if (preference.getConnType().equals("M2M") && !new M2mReplaceAdapter(getActivity()).isM2mReplaceEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.toastMessage("Mohon diinput menu M2m Replace terlebih dahulu", ConfigApps.T_INFO);
        }else if (preference.getConnType().equals("M2M") && !new M2mDataAdapter(getActivity()).isM2mDataEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.toastMessage("Mohon diinput menu Data M2M terlebih dahulu", ConfigApps.T_INFO);
        }else if (!preference.getConnType().equals("") && !new ActionAdapter(getActivity()).isActionEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.toastMessage("Mohon diinput menu Action terlebih dahulu", ConfigApps.T_INFO);
        } else{
            convertPdf();
        }
    }

    private void convertPdf(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alInfSite.size() > 0){
                    File mFilePdf = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/JobReport/ReportPdf/DataPdf/"+alInfSite.get(0).getCustomer_name());
                    if (!mFilePdf.exists()) {
                        if (!mFilePdf.mkdirs()) {
                            Log.d("####","Gagal create directory");
                        }
                    }
                    File mFileValidationPdf = new File(android.os.Environment.getExternalStorageDirectory().getPath(), "/JobReport/ReportPdf/DataPdf/"+alInfSite.get(0).getCustomer_name() + "/"+alInfSite.get(0).getCustomer_name()+".pdf");
                    if (mFileValidationPdf.exists()){
                        mFileValidationPdf.delete();
                    }

                    Document document = new Document(PageSize.A4, 20, 20, 20, 40);
                    try{
                        float mcontentFontSize = 15.0f;
                        float mHeadingFontSize = 20.0f;
                        BaseFont urName = BaseFont.createFont("assets/Asap-Regular.ttf", "UTF-8", BaseFont.EMBEDDED);
                        Font contentFont = new Font(urName, mcontentFontSize, Font.NORMAL, BaseColor.BLACK);
                        Font titleFont = new Font(urName, mHeadingFontSize, Font.UNDERLINE, BaseColor.BLACK);

                        PdfWriter.getInstance(document, new FileOutputStream(android.os.Environment.getExternalStorageDirectory().getPath() + "/JobReport/ReportPdf/DataPdf/"+alInfSite.get(0).getCustomer_name() + "/"+alInfSite.get(0).getCustomer_name()+".pdf"));
                        document.open();

                        Paragraph pTitle1 = new Paragraph("*Maintenance Report*",titleFont);
                        pTitle1.setAlignment(Element.ALIGN_LEFT);
                        pTitle1.setSpacingAfter(20f);
                        document.add(pTitle1);

                        ArrayList<MasterJobDesc> alJobDesc = new JobDescAdapter(getActivity()).load_trans(preference.getCustID(), preference.getUn());
                        if (alJobDesc.size() > 0){
                            String maintenanceContent = "Progress               = " +preference.getProgress().trim() +"\n" +
                                                        "Jenis Koneksi      = " +preference.getConnType() + "\n"+
                                                        "Nama Teknisi       = " +preference.getUn() + "\n"+
                                                        "Service Point       = " +preference.getServicePoint() + "\n"+
                                                        "Nama Lokasi     = " +alInfSite.get(0).getLocation_name().trim() + "\n" +
                                                        "Alamat                = " +alInfSite.get(0).getRemote_address().trim() + "\n" +
                                                        "Kota                     = " +alInfSite.get(0).getCity().trim() + "\n"+
                                                        "Kabupaten       = " +alInfSite.get(0).getKabupaten().trim()+ "\n"+
                                                        "Provinsi            = " +alInfSite.get(0).getProv().trim() + "\n" +
                                                        "Remote Name =  Unknown\n" +
                                                        "Latitude             = " +alInfSite.get(0).getLat().trim()+ "\n"+
                                                        "Longitude          = " + alInfSite.get(0).getLongitude().trim() + "\n"+
                                                        "PIC                    = " +alJobDesc.get(0).getName_pic() + "\n";

                            Paragraph pContent1 = new Paragraph(maintenanceContent,contentFont);
                            pContent1.setAlignment(Element.ALIGN_LEFT);
                            pContent1.setSpacingAfter(20f);
                            document.add(pContent1);
                        }

                        Paragraph pTitle2 = new Paragraph("*Desc*",titleFont);
                        pTitle2.setAlignment(Element.ALIGN_LEFT);
                        pTitle2.setSpacingAfter(20f);
                        document.add(pTitle2);

                        //add transacttion jobdesc
                        ArrayList<MasterProblem> alProblem = new ProblemAdapter(getActivity()).val_prob(preference.getCustID(), preference.getUn());
                        if (alProblem.size() > 0){
                            String problemContent = "Berangkat          = " +alProblem.get(0).getBerangkat().trim() +"\n"+
                                                    "Tiba                     = " +alProblem.get(0).getTiba().trim()+ "\n"+
                                                    "Finish                  = " +alProblem.get(0).getFinish().trim()+ "\n"+
                                                    "Delay                  = unknown\n"+
                                                    "Pending               = " +alProblem.get(0).getPending().trim() +"\n"+
                                                    "Reason Pending = " +alProblem.get(0).getReason().trim() +"\n"+
                                                    "Upline                 = " +alProblem.get(0).getUpline().trim() +"\n";

                            Paragraph pContent2 = new Paragraph(problemContent,contentFont);
                            pContent2.setAlignment(Element.ALIGN_LEFT);
                            pContent2.setSpacingAfter(20f);
                            document.add(pContent2);

                        }

                        Paragraph pAction = new Paragraph(getActivity().getString(R.string.action_trans),titleFont);
                        pAction.setAlignment(Element.ALIGN_LEFT);
                        pAction.setSpacingAfter(20f);
                        document.add(pAction);

                        ArrayList<MasterAction> al_listAction = new ActionAdapter(getActivity()).load_dataAction(preference.getCustID(), preference.getUn());
                        if (al_listAction.size() > 0){
                            arr_actionDateTime = new String[al_listAction.size()];
                            arr_actionTrans = new String[al_listAction.size()];

                            int i = 0;
                            String actionContent = "";
                            for (MasterAction action : al_listAction){
                                arr_actionDateTime[i] = action.getAction_date_time();
                                arr_actionTrans[i] = action.getAction_desc();

                                String[] split = arr_actionDateTime[i].split(",");
                                if (DateTimeUtils.getDateDiff(DateTimeUtils.getCurrentDate().trim(), split[0]) > 1){
                                    if(i==0){
                                        actionContent = arr_actionDateTime[i]+ "\t\t\t\t : " + arr_actionTrans[i];
                                    }else{
                                        actionContent = actionContent +"\n"+arr_actionDateTime[i]+ "\t\t\t\t : " + arr_actionTrans[i];
                                    }
                                }else{
                                    if (i==0){
                                        actionContent = split[1]+ "\t\t\t\t : " + arr_actionTrans[i];
                                    }else{
                                        actionContent = actionContent +"\n"+split[1]+ "\t\t\t\t : " + arr_actionTrans[i];
                                    }
                                }

                                i++;
                            }

                            Paragraph actionParagraph = new Paragraph(actionContent,contentFont);
                            actionParagraph.setAlignment(Element.ALIGN_LEFT);
                            actionParagraph.setSpacingAfter(20f);
                            document.add(actionParagraph);

                            document.close();

                            //klo doc ud ke convert semua
                            //            if (isSubmitReport()){
                            //                messageUtils.toastMessage("test", ConfigApps.T_INFO);
                            //                preference.clearDataTrans();
                            //                loadDash();
                            //                loadRcTrans();
                            //            }else{
                            //                messageUtils.toastMessage("tidak terupdate", ConfigApps.T_ERROR);
                            //            }
                        }

                    }catch (Exception e){
                        messageUtils.toastMessage("err convert Pdf "+e.toString(), ConfigApps.T_ERROR);
                    }
                }
            }
        }, 1000);
    }

    private boolean isSubmitReport(){
        try{
            transHistoryAdapter
                    .updateRaw("update from t_trans_history " +
                                  "set is_submited = 1, " +
                                  "update_date = '"+ DateTimeUtils.getCurrentTime() +"' " +
                                    " where id_site = "+preference.getCustID()+" " +
                                    " and un_user = '"+preference.getUn()+"' ");
            return true;
        }catch (SQLException e){
            Log.d("err update ","" +e.toString());
            return false;
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        loadDash();
        loadRcTrans();
        JobReportUtils.hideKeyboard(getActivity());
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
