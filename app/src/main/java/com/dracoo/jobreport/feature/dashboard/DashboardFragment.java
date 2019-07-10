package com.dracoo.jobreport.feature.dashboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.ActionAdapter;
import com.dracoo.jobreport.database.adapter.ConnectionParameterAdapter;
import com.dracoo.jobreport.database.adapter.EnvAdapter;
import com.dracoo.jobreport.database.adapter.ImageAdapter;
import com.dracoo.jobreport.database.adapter.InfoSiteAdapter;
import com.dracoo.jobreport.database.adapter.JobDescAdapter;
import com.dracoo.jobreport.database.adapter.M2mDataAdapter;
import com.dracoo.jobreport.database.adapter.M2mReplaceAdapter;
import com.dracoo.jobreport.database.adapter.M2mSetupAdapter;
import com.dracoo.jobreport.database.adapter.XpollAdapter;
import com.dracoo.jobreport.database.adapter.MachineAdapter;
import com.dracoo.jobreport.database.adapter.ProblemAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.adapter.VsatReplaceAdapter;
import com.dracoo.jobreport.database.adapter.VsatSetupAdapter;
import com.dracoo.jobreport.database.master.MasterAction;
import com.dracoo.jobreport.database.master.MasterConnectionParameter;
import com.dracoo.jobreport.database.master.MasterEnvirontment;
import com.dracoo.jobreport.database.master.MasterInfoSite;
import com.dracoo.jobreport.database.master.MasterJobDesc;
import com.dracoo.jobreport.database.master.MasterM2mData;
import com.dracoo.jobreport.database.master.MasterM2mReplace;
import com.dracoo.jobreport.database.master.MasterM2mSetup;
import com.dracoo.jobreport.database.master.MasterMachine;
import com.dracoo.jobreport.database.master.MasterProblem;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.database.master.MasterVsatReplace;
import com.dracoo.jobreport.database.master.MasterVsatSetup;
import com.dracoo.jobreport.database.master.MasterXpoll;
import com.dracoo.jobreport.feature.MenuActivity;
import com.dracoo.jobreport.feature.dashboard.adapter.CustomList_Dashboard_Adapter;
import com.dracoo.jobreport.feature.dashboard.contract.DashboardItemClickBack;
import com.dracoo.jobreport.feature.datam2m.DataM2mActivity;
import com.dracoo.jobreport.feature.replace.ReplaceActivity;
import com.dracoo.jobreport.feature.useractivity.UserActivity;
import com.dracoo.jobreport.feature.vsatparameter.ParameterActivity;
import com.dracoo.jobreport.feature.xpoll.XpollActivity;
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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
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

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.dracoo.jobreport.util.JobReportUtils.createCell;

public class DashboardFragment extends Fragment implements DashboardItemClickBack {
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
    @BindView(R.id.prg_dash)
    ProgressBar prg_dash;

    private MessageUtils messageUtils;
    private Preference preference;
    private ArrayList<MasterJobDesc> alJobDesc;
    private ArrayList<MasterInfoSite> alInfSite;
    private ArrayList<MasterTransHistory> alListTrans;
    private ArrayList<MasterProblem> alProblem;
    private ArrayList<MasterEnvirontment> alEnv;
    private ArrayList<MasterVsatSetup> alVsatSetup;
    private ArrayList<MasterVsatReplace> alVsatReplace;
    private ArrayList<MasterXpoll> alXpoll;
    private ArrayList<MasterConnectionParameter> alConnParam;
    private ArrayList<MasterM2mSetup> alM2mSetup;
    private ArrayList<MasterM2mReplace> alM2mReplace;
    private ArrayList<MasterM2mData> alM2mData;
    private ArrayList<MasterMachine> alMachine;
    private ArrayList<MasterAction> alAction;

    private Dao<MasterTransHistory, Integer> transHistoryAdapter;
    private String[] arr_actionDateTime;
    private String[] arr_actionTrans;
    private String[] arr_actionEndTime;
    private StringBuilder stCopyClipBoard;
    private String stCustname = "";
    private String stUn  = "";
    private String vsatTemp ="";
    private RequestQueue queue;
    CustomList_Dashboard_Adapter adapter;
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
        prg_dash.setVisibility(View.GONE);
        try{
            transHistoryAdapter = new TransHistoryAdapter(getActivity()).getAdapter();
        }catch (Exception e){ Log.d("###","failed create adapter " +e.toString());}
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
            adapter.initCallBack(this);
            rc_dash_activity.setAdapter(adapter);
        }else{ Log.d("###","ke sama dengan 0"); }
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
        }
//        else if (preference.getConnType().equals("VSAT") && !new VsatReplaceAdapter(getActivity()).isVsatReplaceEmpty(preference.getUn(), preference.getCustID())){
//            messageUtils.toastMessage("Mohon diinput menu Vsat Replace terlebih dahulu", ConfigApps.T_INFO);
//        }
//        else if (preference.getConnType().equals("VSAT") && !new XpollAdapter(getActivity()).isVsatXpollEmpty(preference.getUn(), preference.getCustID())){
//            messageUtils.toastMessage("Mohon diinput menu Xpoll terlebih dahulu", ConfigApps.T_INFO);
//        }
        else if (preference.getConnType().equals("VSAT") && !new ConnectionParameterAdapter(getActivity()).isParamEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.toastMessage("Mohon diinput menu Connection Parameter terlebih dahulu", ConfigApps.T_INFO);
        }
//        else if (preference.getConnType().equals("M2M") && !new M2mReplaceAdapter(getActivity()).isM2mReplaceEmpty(preference.getUn(), preference.getCustID())){
//            messageUtils.toastMessage("Mohon diinput menu M2m Replace terlebih dahulu", ConfigApps.T_INFO);
//        }
        else if (preference.getConnType().equals("M2M") && !new M2mDataAdapter(getActivity()).isM2mDataEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.toastMessage("Mohon diinput menu Data M2M terlebih dahulu", ConfigApps.T_INFO);
        }else if (!preference.getConnType().equals("") && !new ImageAdapter(getActivity()).isImageEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.toastMessage("Mohon diinput menu Document terlebih dahulu", ConfigApps.T_INFO);
        } else if (!preference.getConnType().equals("") && !new ActionAdapter(getActivity()).isActionEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.toastMessage("Mohon diinput menu Action terlebih dahulu", ConfigApps.T_INFO);
        } else if (MessageUtils.isConnected()){
            prg_dash.setVisibility(View.VISIBLE);
            runQueryForConvert();
        } else{ messageUtils.toastMessage("Tidak terkoneksi dengan internet", ConfigApps.T_INFO); }
    }

    private void runQueryForConvert(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    alProblem = new ProblemAdapter(getActivity()).val_prob(preference.getCustID(), preference.getUn());
                    alEnv = new EnvAdapter(getActivity()).val_env(preference.getCustID(), preference.getUn());
                    if(preference.getConnType().equals("VSAT")){
                        alVsatSetup = new VsatSetupAdapter(getActivity()).val_vsatSetup(preference.getCustID(), preference.getUn());
                        alVsatReplace = new VsatReplaceAdapter(getActivity()).val_vsatReplace(preference.getCustID(), preference.getUn());
                        alXpoll = new XpollAdapter(getActivity()).val_xpoll(preference.getCustID(), preference.getUn());
                        alConnParam = new ConnectionParameterAdapter(getActivity()).val_param(preference.getCustID(), preference.getUn());
                    }else{
                        alM2mSetup = new M2mSetupAdapter(getActivity()).val_m2mSetup(preference.getCustID(), preference.getUn());
                        alM2mData = new M2mDataAdapter(getActivity()).val_dataM2m(preference.getCustID(), preference.getUn());
                        alM2mReplace = new M2mReplaceAdapter(getActivity()).val_m2mReplace(preference.getCustID(), preference.getUn());
                    }
                    alMachine  = new MachineAdapter(getActivity()).val_machine(preference.getCustID(), preference.getUn());
                    alAction = new ActionAdapter(getActivity()).load_dataAction(preference.getCustID(), preference.getUn());
                    prg_dash.setVisibility(View.GONE);
                    alertChoose();
                }catch (Exception e){
                    prg_dash.setVisibility(View.GONE);
                    messageUtils.toastMessage("err run query "+e.toString(), ConfigApps.T_ERROR);
                }
            }
        }, 500);
    }

    private boolean isSubmitReport(){
        try{
            transHistoryAdapter
                    .updateRaw("update t_trans_history " +
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

    private void alertChoose(){
        String[] listItems = {"Send File Pdf", "Copy into whatsapp"};
        new AlertDialog.Builder(getActivity())
                .setTitle("Send via")
                .setCancelable(false)
                .setItems(listItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0){
                            sendViaPdf();
                        }else if (i == 1){
                            //send via WA
                            sendViaWA();
                        }
                    }
                })
                .show();

    }

    private void sendViaPdf(){
        prg_dash.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    File mFilePdf = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/JobReport/ReportPdf/DataPdf/"+preference.getCustName());
                    if (!mFilePdf.exists()) {
                        if (!mFilePdf.mkdirs()) { Log.d("####","Gagal create directory"); }
                    }
                    File mFileValidationPdf = new File(android.os.Environment.getExternalStorageDirectory().getPath(), "/JobReport/ReportPdf/DataPdf/"+preference.getCustName() + "/"+preference.getCustName()+".pdf");
                    if (mFileValidationPdf.exists()){
                        mFileValidationPdf.delete();
                    }

                    Document document = new Document(PageSize.A4, 30, 30, 30, 30);
                    float mcontentFontSize = 5.7f;
                    float mHeadingFontSize = 7.0f;
                    BaseFont urName = BaseFont.createFont("assets/Asap-Regular.ttf", "UTF-8", BaseFont.EMBEDDED);
                    Font contentFont = new Font(urName, mcontentFontSize, Font.NORMAL, BaseColor.BLACK);
                    Font titleFont = new Font(urName, mHeadingFontSize, Font.NORMAL, BaseColor.BLACK);

                    PdfWriter.getInstance(document, new FileOutputStream(android.os.Environment.getExternalStorageDirectory().getPath() + "/JobReport/ReportPdf/DataPdf/"+preference.getCustName() + "/"+preference.getCustName()+".pdf"));
                    document.open();

                    Paragraph paragraphNews = null;
                    PdfPTable tblTitleNews = new PdfPTable(1);
                    tblTitleNews.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tblTitleNews.addCell(JobReportUtils.bottomLineCell("BERITA ACARA", titleFont));
                    document.add(tblTitleNews);

                    Paragraph paragraphNewsNo = new Paragraph("No  : _______ / BA- ________ / ________ / ________ / _______ / _______", contentFont);
                    paragraphNewsNo.setAlignment(Element.ALIGN_LEFT);
                    paragraphNewsNo.setSpacingAfter(1f);
                    document.add(paragraphNewsNo);
                    document.add(JobReportUtils.singleSpace(contentFont));

                    PdfPTable tblTypeNews = new PdfPTable(1);
                    tblTypeNews.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tblTypeNews.addCell(JobReportUtils.bottomLineCell("JENIS KEGIATAN", titleFont));
                    document.add(tblTypeNews);

                    PdfPTable tblTypeProgress = new PdfPTable(2);
                    tblTypeProgress.setHorizontalAlignment(Element.ALIGN_LEFT);
                    if (preference.getProgress().equals("CM")){
                        tblTypeProgress.addCell(JobReportUtils.borderlessCell("(v) CM ", titleFont, 10f));
                        tblTypeProgress.addCell(JobReportUtils.borderlessCell("PM ", titleFont, 10f));
                    }else{
                        tblTypeProgress.addCell(JobReportUtils.borderlessCell("CM ", titleFont, 10f));
                        tblTypeProgress.addCell(JobReportUtils.borderlessCell("(v) PM ", titleFont, 10f));
                    }

                    document.add(tblTypeProgress);
                    paragraphNews = new Paragraph("\n", contentFont);
                    paragraphNews.setAlignment(Element.ALIGN_LEFT);
                    paragraphNews.setSpacingAfter(1);
                    document.add(paragraphNews);
                    PdfPTable tblDescNews = new PdfPTable(1);
                    tblDescNews.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tblDescNews.addCell(JobReportUtils.bottomLineCell("Deskripsi Kegiatan", titleFont));
                    document.add(tblDescNews);
                    String finishDateTime = alProblem.get(0).getFinish().trim();
                    String[] splitFinish = finishDateTime.split(",");
                    String finishDate = splitFinish[0];
                    String[] splitDate = finishDate.split("-");

                    Paragraph paragraphContentDays = new Paragraph("Pada Hari "+DateTimeUtils.nameOfDay(finishDateTime)+" tanggal "+splitDate[2]+" bulan "+splitDate[2]+" tahun "+splitDate[0]+" " +
                            "( "+splitDate[2]+" - "+splitDate[1]+" - "+splitDate[0]+") , bertempat di ______________________________________ " +
                            "\nTelah dilakukan penandatanganan berita acara antara : " +
                            "\n\n1.PT. Visionet Jayapura dalam hal ini diwakilkan oleh :\n", contentFont);
                    paragraphContentDays.setAlignment(Element.ALIGN_LEFT);
                    paragraphContentDays.setSpacingAfter(1f);
                    document.add(paragraphContentDays);
                    PdfPTable tblcontentName = new PdfPTable(2);
                    tblcontentName.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tblcontentName.addCell(JobReportUtils.borderlessCell("Nama   : " +preference.getTechName().trim(), contentFont, 70f));
                    tblcontentName.addCell(JobReportUtils.borderlessCell("Jabatan : " +preference.getServicePoint().trim(), contentFont, 70f));
                    document.add(tblcontentName);
                    Paragraph paragraphContentUser1 = new Paragraph("Yang selamjutnya disebut sebagai PIHAK I\n" , contentFont);
                    paragraphContentUser1.setAlignment(Element.ALIGN_LEFT);
                    paragraphContentUser1.setSpacingAfter(1f);
                    document.add(paragraphContentUser1);

                    PdfPTable tblcontentCust = new PdfPTable(2);
                    tblcontentCust.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tblcontentCust.addCell(JobReportUtils.borderlessCell("Nama   : " +alJobDesc.get(0).getName_pic(), contentFont, 70f));
                    tblcontentCust.addCell(JobReportUtils.borderlessCell("Alamat : " +alInfSite.get(0).getRemote_address().trim(), contentFont, 70f));
                    tblcontentCust.addCell(JobReportUtils.borderlessCell("Jabatan : ", contentFont, 10f));
                    tblcontentCust.addCell(JobReportUtils.borderlessCell("", titleFont, 10f));
                    tblcontentCust.addCell(JobReportUtils.borderlessCell("Perusahaan : PT Visionet Jayapura", contentFont, 70f));
                    tblcontentCust.addCell(JobReportUtils.borderlessCell("Telepon : " +alJobDesc.get(0).getPic_phone(), contentFont, 70f));
                    document.add(tblcontentCust);
                    Paragraph paragraphContentUser2 = new Paragraph("Yang selamjutnya disebut sebagai PIHAK II" +
                            "\n  Dengan ini kedua belah pihak mengajukan setuju bahwa jaringan Sistem Komunikasi dengan spesifikasi :\n" , contentFont);
                    paragraphContentUser2.setAlignment(Element.ALIGN_LEFT);
                    paragraphContentUser2.setSpacingAfter(1f);
                    document.add(paragraphContentUser2);

                    PdfPTable tblcontentTypeJasa = new PdfPTable(1);
                    tblcontentTypeJasa.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tblcontentTypeJasa.addCell(JobReportUtils.borderlessCell("Jenis Jasa : "+preference.getConnType().trim(), contentFont, 100f));
                    tblcontentTypeJasa.addCell(JobReportUtils.borderlessCell("Hubungan Dari : Hub PT VIsionet Jayapura", contentFont, 100f));
                    tblcontentTypeJasa.addCell(JobReportUtils.borderlessCell("Ke : " +alInfSite.get(0).getLocation_name().trim(), contentFont, 100f));
                    tblcontentTypeJasa.setSpacingAfter(1f);
                    document.add(tblcontentTypeJasa);

                    PdfPTable tblcontentRelocate1 = new PdfPTable(2);
                    tblcontentRelocate1.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tblcontentRelocate1.addCell(JobReportUtils.borderlessCell("Lokasi Lama : _______________________________", contentFont, 60f));
                    tblcontentRelocate1.addCell(JobReportUtils.borderlessCell("Lokasi Baru : _______________________________", contentFont, 60f));
                    tblcontentRelocate1.setTotalWidth(110f);
                    tblcontentRelocate1.setLockedWidth(true);
                    document.add(tblcontentRelocate1);

                    PdfPTable tblcontentRelocate2 = new PdfPTable(2);
                    tblcontentRelocate2.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tblcontentRelocate2.addCell(JobReportUtils.borderlessCell("Dinyatakan ", contentFont, 60f));
                    tblcontentRelocate2.addCell(JobReportUtils.borderlessCell(": __________________________________________", contentFont, 60f));
                    tblcontentRelocate2.addCell(JobReportUtils.borderlessCell("Catatan ", contentFont, 60f));
                    tblcontentRelocate2.addCell(JobReportUtils.borderlessCell(": __________________________________________", contentFont, 60f));
                    float[] columnWidthRelocate2 = new float[]{40f, 100f};
                    tblcontentRelocate2.setWidths(columnWidthRelocate2);
                    tblcontentRelocate2.setSpacingAfter(1f);
                    tblcontentRelocate2.setTotalWidth(150f);
                    tblcontentRelocate2.setLockedWidth(true);
                    document.add(tblcontentRelocate2);

                    Paragraph paragraphClosed = new Paragraph("Demikian Bertia acara ini dibuat dan disetujui oleh kedua belah pihak yang tidak terpisahkan dari Perjanjian" +
                            "\nKerja sama Penyediaan Jasa Jaringan Sistem Komunikasi " , contentFont);
                    paragraphClosed.setAlignment(Element.ALIGN_LEFT);
                    paragraphClosed.setSpacingAfter(1f);
                    document.add(paragraphClosed);

                    //TODO BELUM SELESAI

                    document.newPage();
                    if (alAction.size() > 0){
                        PdfPTable table7 = new PdfPTable(4);
                        arr_actionDateTime = new String[alAction.size()];
                        arr_actionTrans = new String[alAction.size()];
                        arr_actionEndTime = new String[alAction.size()];

                        int i = 0;
                        int j = 1;
                        table7.addCell(createCell("No", contentFont));
                        table7.addCell(createCell("Tanggal", contentFont));
                        table7.addCell(createCell("Jam", contentFont));
                        table7.addCell(createCell("Kegiatan", contentFont));

                        for (MasterAction action : alAction){
                            arr_actionDateTime[i] = action.getAction_date_time();
                            arr_actionTrans[i] = action.getAction_desc();
                            arr_actionEndTime[i] = action.getAction_end_time();

                            String[] split = arr_actionDateTime[i].split(",");
                            String[] splitEndTime = arr_actionEndTime[i].split(",");
                            if (DateTimeUtils.getDateDiff(splitEndTime[0],split[0] ) > 1){
                                table7.addCell(createCell(String.valueOf(j), contentFont));
                                table7.addCell(createCell(split[0] +" - " + splitEndTime[0] , contentFont));
                                table7.addCell(createCell(split[1] + "-" +splitEndTime[1], contentFont));
                                table7.addCell(createCell(arr_actionTrans[i].trim(), contentFont));
                            }else{
                                table7.addCell(createCell(String.valueOf(i), contentFont));
                                table7.addCell(createCell(split[0], contentFont));
                                table7.addCell(createCell(split[1]+ " -" +splitEndTime[1], contentFont));
                                table7.addCell(createCell(arr_actionTrans[i].trim(), contentFont));
                            }
                            i++;
                            j++;
                        }
                        if (i == alAction.size()){
                            table7.setHorizontalAlignment(Element.ALIGN_LEFT);
                            float[] columnWidths7 = new float[]{20f, 40f, 60f,310f};
                            table7.setWidths(columnWidths7);
                            table7.setTotalWidth(420f);
                            table7.setLockedWidth(true);
                            document.add(table7);
                        }
                    }
                    document.close();
                    prg_dash.setVisibility(View.GONE);
                    choosePdf();
                }catch (Exception e){
                    messageUtils.toastMessage("Err send Pdf " +e.toString(), ConfigApps.T_ERROR);
                    prg_dash.setVisibility(View.GONE);
                }
            }
        }, 1000);

    }

    private void choosePdf(){
        stCustname = preference.getCustName().trim();
        stUn = preference.getTechName().trim();
        if (preference.getSendWA() == ConfigApps.SUBMIT_SEND){
            if (isSubmitReport()) {
                preference.clearDataTrans();
                emptyView(1);
                submitReport();
            }else{ messageUtils.toastMessage("tidak terupdate", ConfigApps.T_ERROR); }
        }else{
            preference.saveSend(ConfigApps.EMAIL_TYPE);
            submitReport();
        }
    }

    private void sendViaWA(){
        prg_dash.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stCopyClipBoard = new StringBuilder();
                stCopyClipBoard.append("Report\n\n");
                try{
                    String maintenanceContent;
                    if (alJobDesc.size() > 0){
                        maintenanceContent = "Progress = " +preference.getProgress().trim() +"\n" +
                                "TT / WO = " +alInfSite.get(0).getTtwo().trim() + "\n"+
                                "Jenis Koneksi = " +preference.getConnType() + "\n"+
                                "Nama Teknisi = " +preference.getTechName() + "\n"+
                                "Service Point = " +preference.getServicePoint() + "\n"+
                                "Nama Lokasi = " +alInfSite.get(0).getLocation_name().trim() + "\n" +
                                "Alamat = " +alInfSite.get(0).getRemote_address().trim() + "\n" +
                                "Kota = " +alInfSite.get(0).getCity().trim() + "\n"+
                                "Kabupaten = " +alInfSite.get(0).getKabupaten().trim()+ "\n"+
                                "Provinsi = " +alInfSite.get(0).getProv().trim() + "\n" +
                                "Remote Name = " +alInfSite.get(0).getRemote_name().trim() + "\n" +
                                "Latitude = " +alInfSite.get(0).getLat().trim()+ "\n"+
                                "Longitude = " + alInfSite.get(0).getLongitude().trim() + "\n"+
                                "PIC = " +alJobDesc.get(0).getName_pic() + "\n"+
                                "No PIC = " +alJobDesc.get(0).getPic_phone();
                        stCopyClipBoard.append(maintenanceContent+"\n\n");
                    }

                    stCopyClipBoard.append(getActivity().getString(R.string.problemDesc_trans)+"\n");
                    String problemContent;
                    if (alProblem.size() > 0){
                        String stProbRestart = alProblem.get(0).getRestart().trim();
                        if (stProbRestart.equals("-")){ stProbRestart = ""; }

                        problemContent = "Berangkat = " +DateTimeUtils.getChangeDateFormat(alProblem.get(0).getBerangkat().trim()) +"\n"+
                                "Tiba = " +DateTimeUtils.getChangeDateFormat(alProblem.get(0).getTiba().trim())+ "\n"+
                                "Start = " +DateTimeUtils.getChangeDateFormat(alProblem.get(0).getStart().trim())+ "\n"+
                                "Finish = " +DateTimeUtils.getChangeDateFormat(alProblem.get(0).getFinish().trim())+ "\n"+
                                "Delay = " +alProblem.get(0).getDelay_reason().trim() + "\n" +
                                "Pending Kegiatan  = " +DateTimeUtils.getChangeDateFormat(alProblem.get(0).getDelay_activity().trim()) +"\n"+
                                "Reason Pending = " +alProblem.get(0).getReason().trim() +"\n"+
                                "Start Ulang = " +stProbRestart +"\n" +
                                "Online = " +alProblem.get(0).getOnline().trim() +"\n"+
                                "Closed = " +alProblem.get(0).getClosed().trim() +"\n"+
                                "By = " +alProblem.get(0).getClosed_by().trim();
                        stCopyClipBoard.append(problemContent+"\n\n");
                    }


                    stCopyClipBoard.append(getActivity().getString(R.string.electEnv_trans)+"\n");
                    String environtmentContent;
                    if (alEnv.size() > 0){
                        environtmentContent = "PLN\nTegangan (Vac) = "+alEnv.get(0).getTegangan_pln()+"\n"+
                                "Grounding (Vac) = " +alEnv.get(0).getGrounding_pln()+"\n\n"+
                                "UPS\nTegangan (Vac) = "+alEnv.get(0).getTegangan_ups()+ "\n"+
                                "Grounding (Vac) = " +alEnv.get(0).getGrounding_ups()+"\n"+
                                "Catatan  = " +alEnv.get(0).getNotes().trim() +"\n\n"+
                                "AC\nSuhu = " + alEnv.get(0).getSuhu() +" \u00b0 \n"+
                                "Catatan = " +alEnv.get(0).getNotes_ac().trim();
                        stCopyClipBoard.append(environtmentContent+"\n\n");
                    }


                    if (preference.getConnType().equals("VSAT")){
                        stCopyClipBoard.append("I/0 Equipment\n");
                        String vsatSetup;
                        if (alVsatSetup.size() > 0){
                            vsatSetup = "OLD\nS/N Modem = " +alVsatSetup.get(0).getSn_modem().trim()+"\n"+
                                    "S/N Adaptor = " +alVsatSetup.get(0).getSn_adaptor().trim()+"\n"+
                                    "S/N LNB = " +alVsatSetup.get(0).getSn_lnb().trim()+"\n"+
                                    "S/N RFU = " +alVsatSetup.get(0).getSn_rfu().trim()+"\n"+
                                    "S/N DIPLEXER ODU = " +alVsatSetup.get(0).getSn_dip_odu().trim() +"\n"+
                                    "S/N DIPLEXER IDU = " +alVsatSetup.get(0).getSn_dip_idu().trim() +"\n";

                            vsatTemp  = "Diameter Antena = " + alVsatSetup.get(0).getAntena_size() + "\n"+
                                    "Type Antena = " +alVsatSetup.get(0).getAntena_type().trim() + "\n" +
                                    "Antena Brand = " +alVsatSetup.get(0).getAntena_brand().trim() + "\n" +
                                    "Pedestal Type = "+alVsatSetup.get(0).getPedestal_type().trim()+ "\n"+
                                    "Akses Antena = " +alVsatSetup.get(0).getAccess_type().trim();

                            stCopyClipBoard.append(vsatSetup+"\n\n");
                        }


                        String snModem, snAdaptor, lnb, rfu, dipOdu, dipIdu;
                        if (alVsatReplace.size() > 0){
                            snModem = alVsatReplace.get(0).getSn_modem().trim();
                            snAdaptor = alVsatReplace.get(0).getSn_adaptor().trim();
                            lnb = alVsatReplace.get(0).getSn_lnb().trim();
                            rfu = alVsatReplace.get(0).getSn_rfu().trim();
                            dipOdu = alVsatReplace.get(0).getSn_dip_odu().trim();
                            dipIdu = alVsatReplace.get(0).getSn_dip_idu().trim();
                        }else{
                            snModem = "";
                            snAdaptor = "";
                            lnb = "";
                            rfu = "";
                            dipOdu = "";
                            dipIdu = "";
                        }
                        String vsatReplace = "NEW\nS/N Modem = " +snModem+"\n"+
                                "S/N Adaptor = " +snAdaptor+"\n"+
                                "S/N LNB = " +lnb+"\n"+
                                "S/N RFU = " +rfu+"\n"+
                                "S/N DIPLEXER ODU = " +dipOdu +"\n"+
                                "S/N DIPLEXER IDU = " +dipIdu ;
                        stCopyClipBoard.append(vsatReplace+"\n\n");
                        stCopyClipBoard.append(vsatTemp +"\n\n");
                        stCopyClipBoard.append("XPOLL ITEM\n");
                        String xpollSat, xpolllft, xpollCn, xpollCpi, xpollAsi,xpollDateTime, xpollOp;
                        if(alXpoll.size() > 0){
                            xpollSat = alXpoll.get(0).getSat().trim();
                            xpolllft = alXpoll.get(0).getLft().trim();
                            xpollCn = alXpoll.get(0).getCn().trim();
                            xpollCpi = alXpoll.get(0).getCpi().trim();
                            xpollAsi = alXpoll.get(0).getAsi().trim();
                            xpollDateTime = DateTimeUtils.getChangeDateFormat(alXpoll.get(0).getInsert_time().trim());
                            xpollOp = alXpoll.get(0).getOp().trim();
                        }else{
                            xpollSat = "";
                            xpolllft = "";
                            xpollCn = "";
                            xpollCpi = "";
                            xpollAsi = "";
                            xpollDateTime = "";
                            xpollOp = "";
                        }
                        String xpollContent = "SAT = " +xpollSat +"\n"+
                                "LFT = " +xpolllft +"\n"+
                                "C/N = " +xpollCn + "\n"+
                                "CPI = " +xpollCpi+ "\n"+
                                "ASI = " +xpollAsi + "\n"+
                                "DATE, TIME = " +xpollDateTime+"\n"+
                                "OP = " +xpollOp;
                        stCopyClipBoard.append(xpollContent+"\n\n");

                        String paramContent;
                        if (alConnParam.size() > 0){
                            paramContent = "LAN PARAMETER\nIP Lan = " +alConnParam.get(0).getLan_parameter().trim() +" \n"+
                                    "Subnet Mask = "+ alConnParam.get(0).getLan_subnetmask().trim() + "\n\n"+
                                    "MANAGEMENT PARAMETER\nESN Modem = " +alConnParam.get(0).getManagement_esnmodem().trim() + "\n"+
                                    "Gateway = " +alConnParam.get(0).getManagement_gateway().trim()+"\n"+
                                    "SNMP = " +alConnParam.get(0).getManagement_snmp().trim()+"\n\n"+
                                    "RANGING PARAMETER\nSIGNAL = " +alConnParam.get(0).getRanging_signal().trim()+ "\n"+
                                    "DATA RATE = " +alConnParam.get(0).getRanging_data_rate().trim()+"\n"+
                                    "FEC = " +alConnParam.get(0).getRanging_fec().trim() + "\n"+
                                    "FINAL POWER SETTING = " +alConnParam.get(0).getRanging_power().trim()+"\n"+
                                    "FINAL ESNO = " +alConnParam.get(0).getRanging_esno().trim() + "\n"+
                                    "FINAL C/NO = " +alConnParam.get(0).getRanging_cno().trim();

                            stCopyClipBoard.append(paramContent+"\n\n");
                        }
                    }else{
                        stCopyClipBoard.append(getActivity().getString(R.string.ioM2M_trans)+"\n");
                        String m2mSetupContent;
                        if (alM2mSetup.size() > 0){
                            m2mSetupContent = "OLD\nM2M\nBrand / Type = " +alM2mSetup.get(0).getBrand_type_m2m() +"\n"+
                                    "S/N = "+alM2mSetup.get(0).getSn_m2m().trim()+"\n"+
                                    "ADAPTOR\nBrand / Type = " +alM2mSetup.get(0).getBrand_type_adaptor().trim()+"\n"+
                                    "S/N = " +alM2mSetup.get(0).getSn_adaptor().trim() + "\n"+
                                    "SIMCARD 1\nBrand / Type = " +alM2mSetup.get(0).getSim_card1_type()+"\n"+
                                    "S/N = " +alM2mSetup.get(0).getSim_card1_sn() +"\n"+
                                    "PUK = " +alM2mSetup.get(0).getSim_card1_puk() + "\n"+
                                    "SIMCARD 2 \nBrand / Type = " + alM2mSetup.get(0).getSim_card2_type().trim() + "\n"+
                                    "S/N = " +alM2mSetup.get(0).getSim_card2_sn() + "\n"+
                                    "PUK = " +alM2mSetup.get(0).getSim_card2_puk() + "\n";
                            stCopyClipBoard.append(m2mSetupContent+"\n\n");
                        }

                        String m2mType, m2mSn, adaptorType, adaptorSn, simCard1Type, simCard1SN, simcard1Puk, simCard2Type, simCard2SN, simcard2Puk;
                        if (alM2mReplace.size() > 0){
                            m2mType = alM2mReplace.get(0).getBrand_type_replace().trim();
                            m2mSn = alM2mReplace.get(0).getBrand_type_adaptor().trim();
                            adaptorType = alM2mReplace.get(0).getBrand_type_adaptor().trim();
                            adaptorSn = alM2mReplace.get(0).getSn_adaptor().trim();
                            simCard1Type = alM2mReplace.get(0).getSim_card1_type().trim();
                            simCard1SN = alM2mReplace.get(0).getSim_card1_sn().trim();
                            simcard1Puk = alM2mReplace.get(0).getSim_card1_puk().trim();
                            simCard2Type = alM2mReplace.get(0).getSim_card2_type().trim();
                            simCard2SN = alM2mReplace.get(0).getSim_card2_sn().trim();
                            simcard2Puk = alM2mReplace.get(0).getSim_card2_puk().trim();
                        }else{
                            m2mType = "";
                            m2mSn = "";
                            adaptorType = "";
                            adaptorSn = "";
                            simCard1Type = "";
                            simCard1SN = "";
                            simcard1Puk = "";
                            simCard2Type = "";
                            simCard2SN = "";
                            simcard2Puk = "";
                        }

                        String m2mReplace = "NEW\nM2M\nBrand / Type = " +m2mType.trim()+"\n"+
                                "S/N = " +m2mSn.trim() +"\n"+
                                "ADAPTOR\nBrand / Type = "+adaptorType.trim() +"\n"+
                                "S/N = " +adaptorSn.trim() +"\n"+
                                "SIMCARD 1\nBrand / Type = " +simCard1Type.trim() +"\n"+
                                "S/N = " +simCard1SN.trim() + "\n" +
                                "PUK = " +simcard1Puk.trim() + "\n" +
                                "SIMCARD 2\nBrand / Type = " +simCard2Type.trim() + "\n"+
                                "S/N = " +simCard2SN.trim() + "\n" +
                                "PUK = " +simcard2Puk.trim();
                        stCopyClipBoard.append(m2mReplace+"\n\n");
                    }

                    String dataM2mContent;
                    if (alM2mData.size() > 0){
                        dataM2mContent = "Data Teknis\nUsername = " +alM2mData.get(0).getUsername().trim()+ "\n"+
                                "Password = " +alM2mData.get(0).getPassword().trim() + "\n"+
                                "Ip machine = "+alM2mData.get(0).getIp_machine().trim() + "\n"+
                                "User = " +alM2mData.get(0).getUser().trim()+ "\n"+
                                "Remote Address = " +alM2mData.get(0).getRemote().trim()+"\n"+
                                "Tunnel ID 1 = " +alM2mData.get(0).getTunnel_id().trim()+"\n"+
                                "IP Bounding = " +alM2mData.get(0).getIp_bonding().trim()+"\n"+
                                "IP VLAN = " +alM2mData.get(0).getIp_vlan().trim()+"\n"+
                                "IP LAN = " +alM2mData.get(0).getIp_lan().trim()+"\n"+
                                "Subnetmask = " +alM2mData.get(0).getSubnet_mask().trim() +"\n"+
                                "AGG = " +alM2mData.get(0).getAgg().trim();
                        stCopyClipBoard.append(dataM2mContent+"\n\n");
                    }

                    String machineData;
                    if (alMachine.size() > 0){
                        machineData = "ATM Machine\nLokasi ATM = " +alMachine.get(0).getMachine_type().trim() +"\n"+
                                "Jumlah Mesin ATM = " +alMachine.get(0).getMachine_qty() + "\n"+
                                "ID ATM = " +alMachine.get(0).getId_machine() + "\n"+
                                "Akses ATM = " +alMachine.get(0).getAccess_type().trim();
                        stCopyClipBoard.append(machineData+"\n\n");
                    }

                    if (alAction.size() > 0){
                        int i = 0;
                        String actionContent = "";
                        stCopyClipBoard.append("ACTION\n");
                        for (MasterAction action : alAction){
                            arr_actionDateTime[i] = action.getAction_date_time();
                            arr_actionTrans[i] = action.getAction_desc();
                            arr_actionEndTime[i] = action.getAction_end_time();
                            String[] split = arr_actionDateTime[i].split(", ");
                            String[] splitEndTime = arr_actionEndTime[i].split(", ");

                            if (DateTimeUtils.getDateDiff(splitEndTime[0],split[0] ) > 1){
                                if(i==0){ actionContent = split[0] + " -"+ splitEndTime[0]+ " | " + split[1] + "-" +splitEndTime[1]+ " : " +arr_actionTrans[i]; }
                                else{ actionContent = actionContent +"\n"+split[0] + " -"+ splitEndTime[0]+ " | " + split[1] + "-" +splitEndTime[1]+ " : " +arr_actionTrans[i]; }
                            }else{
                                if (i==0){ actionContent = split[1]+ " -" +splitEndTime[1] + " : " + arr_actionTrans[i]; }
                                else{ actionContent = actionContent +"\n"+split[1]+ " -" +splitEndTime[1] + " : " + arr_actionTrans[i]; }
                            }
                            i++;
                        }
                        stCopyClipBoard.append(actionContent+"\n\n");
                    }
                    prg_dash.setVisibility(View.GONE);
                    chooseWA();
                }catch (Exception e){
                    prg_dash.setVisibility(View.GONE);
                    messageUtils.toastMessage("err Send WA " +e.toString(), ConfigApps.T_ERROR);
                }
            }
        }, 900);
    }

    private void chooseWA(){
        if (preference.getSendEmail() == ConfigApps.SUBMIT_SEND){
            if (isSubmitReport()){
                emptyView(1);
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", stCopyClipBoard);
                clipboard.setPrimaryClip(clip);
                messageUtils.toastMessage("Data Sukses tercopy, silahkan paste ke whatsapp ", ConfigApps.T_SUCCESS);
                preference.clearDataTrans();
            }else{ messageUtils.toastMessage("tidak terupdate", ConfigApps.T_ERROR); }
        }else{
            preference.saveSend(ConfigApps.WA_TYPE);
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", stCopyClipBoard);
            clipboard.setPrimaryClip(clip);
            messageUtils.toastMessage("Data Sukses tercopy, silahkan paste ke whatsapp ", ConfigApps.T_SUCCESS);
        }
    }

    private void submitReport(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                File mFileResultPdf = new File(android.os.Environment.getExternalStorageDirectory().getPath(), "/JobReport/ReportPdf/DataPdf/"+stCustname + "/"+stCustname+".pdf");
                String subjectEmail = "Kepada yth,\nBpk/Ibu Admin\n\nBerikut saya lampirkan Report Customer " +stCustname+
                        "\n\nDemikian yang bisa saya sampaikan\nTerima Kasih\n\n\n " + stUn.
                        trim().toLowerCase(java.util.Locale.getDefault());

                try {
                    if (mFileResultPdf.exists()) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Report Customer " +stCustname);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, subjectEmail);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mFileResultPdf));
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                        messageUtils.toastMessage("Data Sukses tersubmit ", ConfigApps.T_SUCCESS);
                    }else{ messageUtils.toastMessage("File Tidak ditemukan", ConfigApps.T_WARNING); }
                } catch(Exception e) { messageUtils.toastMessage("err share message " +e.toString(), ConfigApps.T_ERROR); }
            }
        }, 1000);
    }

    @Override
    public void onResume(){
        super.onResume();
        loadDash();
        loadRcTrans();
        JobReportUtils.hideKeyboard(getActivity());
        if (preference.getCustID() == 0){ emptyView(1); }
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
        }else{ emptyView(0); }
    }

    private void emptyView(int type){
        if (type == 1){
            rc_dash_activity.setVisibility(View.GONE);
            lbl_dash_empty.setVisibility(View.VISIBLE);
        }
        lbl_dash_locationName.setText("");
        lbl_dash_technician_name.setText("");
        lbl_dash_customer.setText("");
        lbl_dash_picPhone.setText("");
    }

    @Override
    public void listSelected(String transType) {
        Intent intent = null;
        if (transType.trim().equals(getActivity().getString(R.string.infoSite_trans))
            || transType.equals(getActivity().getString(R.string.jobDesc_trans))){
            intent = new Intent(getActivity(), UserActivity.class);
            intent.putExtra(ConfigApps.EXTRA_CALLER_VIEW, ConfigApps.VIEW_TYPE);
        }else if (transType.trim().equals(getActivity().getString(R.string.problemDesc_trans))) {
            intent = new Intent(getActivity(), MenuActivity.class);
            intent.putExtra(ConfigApps.EXTRA_CALLER_VIEW, ConfigApps.VIEW_TYPE);
            intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY, MenuActivity.EXTRA_FLAG_PROBLEM);
        }else if (transType.trim().equals(getActivity().getString(R.string.electEnv_trans))){
            intent = new Intent(getActivity(), MenuActivity.class);
            intent.putExtra(ConfigApps.EXTRA_CALLER_VIEW, ConfigApps.VIEW_TYPE);
            intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY, MenuActivity.EXTRA_FLAG_LIGHTNING);
        }else if (transType.trim().equals(getActivity().getString(R.string.machine_trans))){
            intent = new Intent(getActivity(), MenuActivity.class);
            intent.putExtra(ConfigApps.EXTRA_CALLER_VIEW, ConfigApps.VIEW_TYPE);
            intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY, MenuActivity.EXTRA_FLAG_MACHINE);
        }else if (transType.trim().equals(getActivity().getString(R.string.ioVSAT_trans))||
                transType.trim().equals(getActivity().getString(R.string.ioM2M_trans))){
            intent = new Intent(getActivity(), MenuActivity.class);
            intent.putExtra(ConfigApps.EXTRA_CALLER_VIEW, ConfigApps.VIEW_TYPE);
            intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY, MenuActivity.EXTRA_FLAG_CONN);
        }else if (transType.trim().equals(getActivity().getString(R.string.repVSAT_trans)) ||
                transType.trim().equals(getActivity().getString(R.string.repM2M_trans))){
            intent = new Intent(getActivity(), ReplaceActivity.class);
            intent.putExtra(ConfigApps.EXTRA_CALLER_VIEW, ConfigApps.VIEW_TYPE);
        }else if (transType.trim().equals(getActivity().getString(R.string.xpoll_trans))){
            intent = new Intent(getActivity(), XpollActivity.class);
            intent.putExtra(ConfigApps.EXTRA_CALLER_VIEW, ConfigApps.VIEW_TYPE);
        }else if (transType.trim().equals(getActivity().getString(R.string.networkParam_trans))){
            intent = new Intent(getActivity(), ParameterActivity.class);
            intent.putExtra(ConfigApps.EXTRA_CALLER_VIEW, ConfigApps.VIEW_TYPE);
        }else if (transType.trim().equals(getActivity().getString(R.string.dataM2m_trans))){
            intent = new Intent(getActivity(), DataM2mActivity.class);
            intent.putExtra(ConfigApps.EXTRA_CALLER_VIEW, ConfigApps.VIEW_TYPE);
        }else if (transType.trim().equals(getActivity().getString(R.string.doc_trans))){
            intent = new Intent(getActivity(), MenuActivity.class);
            intent.putExtra(ConfigApps.EXTRA_CALLER_VIEW, ConfigApps.VIEW_TYPE);
            intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY, MenuActivity.EXTRA_FLAG_DOC);
        }else if (transType.trim().equals(getActivity().getString(R.string.action_trans))){
            intent = new Intent(getActivity(), MenuActivity.class);
            intent.putExtra(ConfigApps.EXTRA_CALLER_VIEW, ConfigApps.VIEW_TYPE);
            intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY, MenuActivity.EXTRA_FLAG_ACTION);
        }
        if (intent != null){ startActivity(intent); }
    }
}
