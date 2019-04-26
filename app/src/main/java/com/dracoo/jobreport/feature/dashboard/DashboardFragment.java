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
import com.dracoo.jobreport.database.adapter.M2mXpollAdapter;
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
import com.dracoo.jobreport.database.master.MasterProblem;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.database.master.MasterVsatReplace;
import com.dracoo.jobreport.database.master.MasterVsatSetup;
import com.dracoo.jobreport.database.master.MasterXpoll;
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
import static com.dracoo.jobreport.util.JobReportUtils.headTitleCell;
import static com.dracoo.jobreport.util.JobReportUtils.titleCell;

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
    @BindView(R.id.prg_dash)
    ProgressBar prg_dash;

    private MessageUtils messageUtils;
    private Preference preference;
    private ArrayList<MasterJobDesc> alJobDesc;
    private ArrayList<MasterInfoSite> alInfSite;
    private ArrayList<MasterTransHistory> alListTrans;
    private Dao<MasterTransHistory, Integer> transHistoryAdapter;
    private String[] arr_actionDateTime;
    private String[] arr_actionTrans;
    private String[] arr_actionEndTime;
    private StringBuilder stCopyClipBoard;
    private String stCustname = "";
    private String stUn  = "";
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
        }else if (!preference.getConnType().equals("") && !new ImageAdapter(getActivity()).isImageEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.toastMessage("Mohon diinput menu Document terlebih dahulu", ConfigApps.T_INFO);
        } else if (!preference.getConnType().equals("") && !new ActionAdapter(getActivity()).isActionEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.toastMessage("Mohon diinput menu Action terlebih dahulu", ConfigApps.T_INFO);
        } else{
            prg_dash.setVisibility(View.VISIBLE);
            convertPdf();
        }
    }

    private void convertPdf(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alInfSite.size() > 0){
                    File mFilePdf = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/JobReport/ReportPdf/DataPdf/"+preference.getCustName());
                    if (!mFilePdf.exists()) {
                        if (!mFilePdf.mkdirs()) { Log.d("####","Gagal create directory"); }
                    }
                    File mFileValidationPdf = new File(android.os.Environment.getExternalStorageDirectory().getPath(), "/JobReport/ReportPdf/DataPdf/"+preference.getCustName() + "/"+preference.getCustName()+".pdf");
                    if (mFileValidationPdf.exists()){
                        mFileValidationPdf.delete();
                    }

                    Document document = new Document(PageSize.A4, 30, 30, 30, 30);
                    try{
                        float mcontentFontSize = 5.7f;
                        float mHeadingFontSize = 7.0f;
                        BaseFont urName = BaseFont.createFont("assets/Asap-Regular.ttf", "UTF-8", BaseFont.EMBEDDED);
                        Font contentFont = new Font(urName, mcontentFontSize, Font.NORMAL, BaseColor.BLACK);
                        Font titleFont = new Font(urName, mHeadingFontSize, Font.NORMAL, BaseColor.BLACK);

                        PdfWriter.getInstance(document, new FileOutputStream(android.os.Environment.getExternalStorageDirectory().getPath() + "/JobReport/ReportPdf/DataPdf/"+preference.getCustName() + "/"+preference.getCustName()+".pdf"));
                        document.open();

                        stCopyClipBoard = new StringBuilder();
                        stCopyClipBoard.append("*Maintenance Report*\n\n");

                        PdfPTable mainTable = new PdfPTable(2);
                        mainTable.setWidthPercentage(90.0f);
                        mainTable.setHorizontalAlignment(Element.ALIGN_LEFT);
                        PdfPCell firstTableCell = new PdfPCell();
                        firstTableCell.setBorder(PdfPCell.NO_BORDER);
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
                                                        "Remote Name = " +alInfSite.get(0).getRemote_name().trim() + "\n" +
                                                        "Latitude             = " +alInfSite.get(0).getLat().trim()+ "\n"+
                                                        "Longitude          = " + alInfSite.get(0).getLongitude().trim() + "\n"+
                                                        "PIC                    = " +alJobDesc.get(0).getName_pic() + "\n";

                            PdfPTable table = new PdfPTable(2);
                            table.addCell(headTitleCell("*Maintenance Report*", titleFont));
                            table.addCell(createCell("Progress ", contentFont));
                            table.addCell(new Paragraph(preference.getProgress().trim(), contentFont));
                            table.addCell(createCell("Jenis Koneksi ", contentFont));
                            table.addCell(new Paragraph(preference.getConnType(), contentFont));
                            table.addCell(createCell("Nama Teknisi ", contentFont));
                            table.addCell(new Paragraph(preference.getUn(), contentFont));
                            table.addCell(createCell("Service Point ", contentFont));
                            table.addCell(new Paragraph(preference.getServicePoint().trim(), contentFont));
                            table.addCell(createCell("Nama Lokasi ", contentFont));
                            table.addCell(new Paragraph(alInfSite.get(0).getLocation_name().trim(), contentFont));
                            table.addCell(createCell("Alamat ", contentFont));
                            table.addCell(new Paragraph(alInfSite.get(0).getRemote_address().trim(), contentFont));
                            table.addCell(createCell("Kota ", contentFont));
                            table.addCell(new Paragraph(alInfSite.get(0).getCity().trim(), contentFont));
                            table.addCell(createCell("Kabupaten ", contentFont));
                            table.addCell(new Paragraph(alInfSite.get(0).getKabupaten().trim(), contentFont));
                            table.addCell(createCell("Provinsi ", contentFont));
                            table.addCell(new Paragraph(alInfSite.get(0).getProv().trim(), contentFont));
                            table.addCell(createCell("Remote Name ", contentFont));
                            table.addCell(new Paragraph(alInfSite.get(0).getRemote_name().trim(), contentFont));
                            table.addCell(createCell("Latitude ", contentFont));
                            table.addCell(new Paragraph(alInfSite.get(0).getLat().trim(), contentFont));
                            table.addCell(createCell("Longitude ", contentFont));
                            table.addCell(new Paragraph(alInfSite.get(0).getLongitude().trim(), contentFont));
                            table.addCell(createCell("PIC", contentFont));
                            table.addCell(new Paragraph(alJobDesc.get(0).getName_pic().trim() , contentFont));
                            table.setHorizontalAlignment(Element.ALIGN_LEFT);
                            float[] columnWidths = new float[]{40f, 100f};
                            table.setWidths(columnWidths);
                            table.setTotalWidth(170f);
                            table.setLockedWidth(true);
                            firstTableCell.addElement(table);
                            mainTable.addCell(firstTableCell);
                            stCopyClipBoard.append(maintenanceContent+"\n\n");
                        }

                        Paragraph pTitle2 = new Paragraph("\n",titleFont);
                        pTitle2.setAlignment(Element.ALIGN_LEFT);
                        pTitle2.setSpacingAfter(3f);
                        document.add(pTitle2);
                        stCopyClipBoard.append("*"+getActivity().getString(R.string.problemDesc_trans)+"*\n\n");

                        ArrayList<MasterProblem> alProblem = new ProblemAdapter(getActivity()).val_prob(preference.getCustID(), preference.getUn());
                        if (alProblem.size() > 0){
                            String problemContent = "Berangkat          = " +DateTimeUtils.getChangeDateFormat(alProblem.get(0).getBerangkat().trim()) +"\n"+
                                                    "Tiba                     = " +DateTimeUtils.getChangeDateFormat(alProblem.get(0).getTiba().trim())+ "\n"+
                                                    "Finish                  = " +DateTimeUtils.getChangeDateFormat(alProblem.get(0).getFinish().trim())+ "\n"+
                                                    "Delay                  = " +alProblem.get(0).getDelay_reason().trim() + "\n" +
                                                    "Pending               = " +alProblem.get(0).getPending().trim() +"\n"+
                                                    "Reason Pending = " +alProblem.get(0).getReason().trim() +"\n"+
                                                    "Upline                 = " +DateTimeUtils.getChangeDateFormat(alProblem.get(0).getUpline().trim()) +"\n";

                            PdfPCell secondTableCell = new PdfPCell();
                            secondTableCell.setBorder(PdfPCell.NO_BORDER);
                            PdfPTable table2 = new PdfPTable(2);
                            table2.addCell(headTitleCell("*"+getActivity().getString(R.string.problemDesc_trans)+"*", titleFont));
                            table2.addCell(createCell("Berangkat ", contentFont));
                            table2.addCell(new Paragraph(DateTimeUtils.getChangeDateFormat(alProblem.get(0).getBerangkat().trim()), contentFont));
                            table2.addCell(createCell("Tiba ", contentFont));
                            table2.addCell(new Paragraph(DateTimeUtils.getChangeDateFormat(alProblem.get(0).getTiba().trim()), contentFont));
                            table2.addCell(createCell("Finish ", contentFont));
                            table2.addCell(new Paragraph(DateTimeUtils.getChangeDateFormat(alProblem.get(0).getFinish().trim()), contentFont));
                            table2.addCell(createCell("Delay ", contentFont));
                            table2.addCell(new Paragraph(alProblem.get(0).getDelay_reason().trim(), contentFont));
                            table2.addCell(createCell("Pending ", contentFont));
                            table2.addCell(new Paragraph(alProblem.get(0).getPending().trim(), contentFont));
                            table2.addCell(createCell("Reason Pending ", contentFont));
                            table2.addCell(new Paragraph(alProblem.get(0).getReason().trim(), contentFont));
                            table2.addCell(createCell("Upline ", contentFont));
                            table2.addCell(new Paragraph(DateTimeUtils.getChangeDateFormat(alProblem.get(0).getUpline().trim()), contentFont));
                            table2.setHorizontalAlignment(Element.ALIGN_LEFT);
                            float[] columnWidths = new float[]{40f, 100f};
                            table2.setWidths(columnWidths);
                            table2.setTotalWidth(170f);
                            table2.setLockedWidth(true);

                            secondTableCell.addElement(table2);
                            mainTable.addCell(secondTableCell);
                            Paragraph paragraph1 = new Paragraph();
                            paragraph1.setAlignment(Element.ALIGN_LEFT);
                            paragraph1.add(mainTable);
                            document.add(paragraph1);
                            stCopyClipBoard.append(problemContent+"\n\n");

                        }

                        Paragraph envParagraph = new Paragraph("\n",titleFont);
                        envParagraph.setAlignment(Element.ALIGN_LEFT);
                        envParagraph.setSpacingAfter(1f);
                        document.add(envParagraph);
                        stCopyClipBoard.append("*"+getActivity().getString(R.string.electEnv_trans)+"*\n\n");

                        PdfPTable mainTable3 = new PdfPTable(2);
                        mainTable3.setWidthPercentage(90.0f);
                        mainTable3.setHorizontalAlignment(Element.ALIGN_LEFT);
                        PdfPCell cell4 = new PdfPCell();
                        cell4.setBorder(PdfPCell.NO_BORDER);
                        ArrayList<MasterEnvirontment> allEnv = new EnvAdapter(getActivity()).val_env(preference.getCustID(), preference.getUn());
                        if (allEnv.size() > 0){
                            String environtmentContent = "_PLN_\nTegangan (Vac) = "+allEnv.get(0).getTegangan_pln()+"\n"+
                                    "Grounding (Vac) = " +allEnv.get(0).getGrounding_pln()+"\n"+
                                    "_UPS_\nTegangan (Vac) = "+allEnv.get(0).getTegangan_ups()+ "\n"+
                                    "Grounding (Vac) = " +allEnv.get(0).getGrounding_ups()+"\n"+
                                    allEnv.get(0).getNotes().trim() +"\n"+
                                    "_AC_\n" +allEnv.get(0).getNotes_ac().trim() +"\n"+
                                    "Suhu " +allEnv.get(0).getSuhu();

                            PdfPTable table3 = new PdfPTable(2);
                            table3.addCell(headTitleCell("*"+getActivity().getString(R.string.electEnv_trans)+"*", titleFont));
                            table3.addCell(titleCell("PLN", contentFont));
                            table3.addCell(createCell("Tegangan (Vac) ", contentFont));
                            table3.addCell(new Paragraph(String.valueOf(allEnv.get(0).getTegangan_pln()), contentFont));
                            table3.addCell(createCell("Grounding (Vac) ", contentFont));
                            table3.addCell(new Paragraph(String.valueOf(allEnv.get(0).getTegangan_ups()), contentFont));
                            table3.addCell(titleCell("UPS", contentFont));
                            table3.addCell(createCell("Tegangan (Vac) ", contentFont));
                            table3.addCell(new Paragraph(String.valueOf(allEnv.get(0).getTegangan_ups()), contentFont));
                            table3.addCell(createCell("Grounding (Vac) ", contentFont));
                            table3.addCell(new Paragraph(String.valueOf(allEnv.get(0).getGrounding_ups()), contentFont));
                            table3.addCell(createCell("Notes ", contentFont));
                            table3.addCell(new Paragraph(allEnv.get(0).getNotes().trim(), contentFont));
                            table3.addCell(titleCell("AC", contentFont));
                            table3.addCell(createCell("Notes ", contentFont));
                            table3.addCell(new Paragraph(allEnv.get(0).getNotes_ac().trim(), contentFont));
                            table3.addCell(createCell("Suhu ", contentFont));
                            table3.addCell(new Paragraph(String.valueOf(allEnv.get(0).getSuhu()) , contentFont));
                            table3.setHorizontalAlignment(Element.ALIGN_LEFT);
                            float[] columnWidths = new float[]{40f, 100f};
                            table3.setWidths(columnWidths);
                            table3.setTotalWidth(170f);
                            table3.setLockedWidth(true);

                            cell4.addElement(table3);
                            mainTable3.addCell(cell4);
                            stCopyClipBoard.append(environtmentContent+"\n\n");
                        }

                        if(preference.getConnType().equals("VSAT")){
                            stCopyClipBoard.append("*I/0 Equipment*\n\n");

                            ArrayList<MasterVsatSetup> alVsat = new VsatSetupAdapter(getActivity()).val_vsatSetup(preference.getCustID(), preference.getUn());
                            if (alVsat.size() > 0){
                                String vsatSetup = "_OLD_\nS/N Modem = " +alVsat.get(0).getSn_modem().trim()+"\n"+
                                                    "S/N Adaptor = " +alVsat.get(0).getSn_adaptor().trim()+"\n"+
                                                    "S/N LNB = " +alVsat.get(0).getSn_lnb().trim()+"\n"+
                                                    "S/N RFU = " +alVsat.get(0).getSn_rfu().trim()+"\n"+
                                                    "S/N DIPLEXER ODU = " +alVsat.get(0).getSn_dip_odu().trim() +"\n"+
                                                    "S/N DIPLEXER IDU = " +alVsat.get(0).getSn_dip_idu().trim() +"\n"+
                                                    "Diameter Antena = " + alVsat.get(0).getAntena_size() + "\n"+
                                                    "Type Antena = " +alVsat.get(0).getAntena_type().trim() + "\n" +
                                                    "Pedestal Type = "+alVsat.get(0).getPedestal_type().trim()+ "\n"+
                                                    "Akses Antena = " +alVsat.get(0).getAccess_type().trim();

                                PdfPCell secondCellVsat = new PdfPCell();
                                secondCellVsat.setBorder(PdfPCell.NO_BORDER);
                                PdfPTable table8 = new PdfPTable(2);
                                table8.addCell(headTitleCell("*"+getActivity().getString(R.string.ioVSAT_trans)+"*", titleFont));
                                table8.addCell(titleCell("OLD", contentFont));
                                table8.addCell(createCell("S/N Modem ", contentFont));
                                table8.addCell(new Paragraph(alVsat.get(0).getSn_modem().trim(), contentFont));
                                table8.addCell(createCell("S/N Adaptor ", contentFont));
                                table8.addCell(new Paragraph(alVsat.get(0).getSn_adaptor().trim(), contentFont));
                                table8.addCell(createCell("S/N LNB ", contentFont));
                                table8.addCell(new Paragraph(alVsat.get(0).getSn_lnb().trim(), contentFont));
                                table8.addCell(createCell("S/N RFU ", contentFont));
                                table8.addCell(new Paragraph(alVsat.get(0).getSn_rfu().trim(), contentFont));
                                table8.addCell(createCell("S/N DIPLEXER ODU ", contentFont));
                                table8.addCell(new Paragraph(alVsat.get(0).getSn_dip_odu().trim(), contentFont));
                                table8.addCell(createCell("S/N DIPLEXER IDU ", contentFont));
                                table8.addCell(new Paragraph(alVsat.get(0).getSn_dip_idu().trim(), contentFont));
                                table8.addCell(createCell("Diameter Antena ", contentFont));
                                table8.addCell(new Paragraph(alVsat.get(0).getAntena_size().trim(), contentFont));
                                table8.addCell(createCell("Pedestal Type ", contentFont));
                                table8.addCell(new Paragraph(alVsat.get(0).getPedestal_type().trim(), contentFont));
                                table8.addCell(createCell("Akses Antena  ", contentFont));
                                table8.addCell(new Paragraph(alVsat.get(0).getAccess_type().trim(), contentFont));
                                float[] columnWidths = new float[]{40f, 100f};
                                table8.setWidths(columnWidths);
                                table8.setTotalWidth(170f);
                                table8.setLockedWidth(true);
                                table8.setHorizontalAlignment(Element.ALIGN_LEFT);

                                secondCellVsat.addElement(table8);
                                mainTable3.addCell(secondCellVsat);
                                Paragraph paragraph3 = new Paragraph();
                                paragraph3.add(mainTable3);
                                paragraph3.setAlignment(Element.ALIGN_LEFT);
                                document.add(paragraph3);
                                stCopyClipBoard.append(vsatSetup+"\n\n");
                            }
                            Paragraph spaceParagraph3 = new Paragraph("\n");
                            spaceParagraph3.setAlignment(Element.ALIGN_LEFT);
                            spaceParagraph3.setSpacingAfter(3f);
                            document.add(spaceParagraph3);

                            PdfPTable mainTableVsatX = new PdfPTable(2);
                            mainTableVsatX.setWidthPercentage(90.0f);
                            mainTableVsatX.setHorizontalAlignment(Element.ALIGN_LEFT);
                            PdfPCell vsatCell1 = new PdfPCell();
                            vsatCell1.setBorder(PdfPCell.NO_BORDER);
                            ArrayList<MasterVsatReplace> alReplace = new VsatReplaceAdapter(getActivity()).val_vsatReplace(preference.getCustID(), preference.getUn());
                            if (alReplace.size() > 0){
                                String snModem = alReplace.get(0).getSn_modem().trim();
                                String snAdaptor = alReplace.get(0).getSn_adaptor().trim();
                                String lnb = alReplace.get(0).getSn_lnb().trim();
                                String rfu = alReplace.get(0).getSn_rfu().trim();
                                String dipOdu = alReplace.get(0).getSn_dip_odu().trim();
                                String dipIdu = alReplace.get(0).getSn_dip_idu().trim();

                                if (snModem.equals("-")){snModem = "";}
                                if (snAdaptor.equals("-")){snAdaptor = "";}
                                if (lnb.equals("-")){lnb = "";}
                                if (rfu.equals("-")){rfu = "";}
                                if (dipOdu.equals("-")){dipOdu = "";}
                                if (dipIdu.equals("-")){dipIdu = "";}

                                String vsatReplace = "_NEW_\nS/N Modem = " +snModem+"\n"+
                                        "S/N Adaptor = " +snAdaptor+"\n"+
                                        "S/N LNB = " +lnb+"\n"+
                                        "S/N RFU = " +rfu+"\n"+
                                        "S/N DIPLEXER ODU = " +dipOdu +"\n"+
                                        "S/N DIPLEXER IDU = " +dipIdu +"\n";

                                PdfPTable table9 = new PdfPTable(2);
                                table9.addCell(headTitleCell("New", titleFont));
                                table9.addCell(createCell("S/N Modem ", contentFont));
                                table9.addCell(new Paragraph(snModem, contentFont));
                                table9.addCell(createCell("S/N Modem ", contentFont));
                                table9.addCell(new Paragraph(snModem, contentFont));
                                table9.addCell(createCell("S/N Adaptor", contentFont));
                                table9.addCell(new Paragraph(snAdaptor, contentFont));
                                table9.addCell(createCell("S/N LNB", contentFont));
                                table9.addCell(new Paragraph(lnb, contentFont));
                                table9.addCell(createCell("S/N RFU", contentFont));
                                table9.addCell(new Paragraph(rfu, contentFont));
                                table9.addCell(createCell("S/N DIPLEXER ODU", contentFont));
                                table9.addCell(new Paragraph(dipOdu, contentFont));
                                table9.addCell(createCell("S/N DIPLEXER IDU", contentFont));
                                table9.addCell(new Paragraph(dipIdu, contentFont));
                                float[] columnWidths = new float[]{40f, 100f};
                                table9.setWidths(columnWidths);
                                table9.setTotalWidth(170f);
                                table9.setLockedWidth(true);
                                table9.setHorizontalAlignment(Element.ALIGN_LEFT);
                                vsatCell1.addElement(table9);
                                mainTableVsatX.addCell(vsatCell1);

                                stCopyClipBoard.append(vsatReplace+"\n\n");
                            }

                            stCopyClipBoard.append("*XPOLL ITEM*\n");

                            ArrayList<MasterXpoll> alXpoll = new M2mXpollAdapter(getActivity()).val_xpoll(preference.getCustID(), preference.getUn());
                            if (alXpoll.size() > 0){
                                String xpollContent = "SAT = " +alXpoll.get(0).getSat().trim() +"\n"+
                                                      "LFT = " +alXpoll.get(0).getLft().trim() +"\n"+
                                                      "C/N = " +alXpoll.get(0).getCn().trim() + "\n"+
                                                      "CPI = " +alXpoll.get(0).getCpi().trim()+ "\n"+
                                                      "ASI = " +alXpoll.get(0).getAsi().trim()+ "\n"+
                                                      "DATE, TIME = " +DateTimeUtils.getChangeDateFormat(alXpoll.get(0).getInsert_time().trim())+"\n"+
                                                      "OP = " +alXpoll.get(0).getOp().trim();

                                PdfPCell vsatCell2 = new PdfPCell();
                                vsatCell2.setBorder(PdfPCell.NO_BORDER);
                                PdfPTable table10 = new PdfPTable(2);
                                table10.addCell(headTitleCell("*"+getActivity().getString(R.string.xpoll_trans)+"*", titleFont));
                                table10.addCell(createCell("SAT", contentFont));
                                table10.addCell(new Paragraph(alXpoll.get(0).getSat().trim(), contentFont));
                                table10.addCell(createCell("LFT", contentFont));
                                table10.addCell(new Paragraph(alXpoll.get(0).getLft().trim(), contentFont));
                                table10.addCell(createCell("C/N", contentFont));
                                table10.addCell(new Paragraph(alXpoll.get(0).getCn().trim(), contentFont));
                                table10.addCell(createCell("CPI", contentFont));
                                table10.addCell(new Paragraph(alXpoll.get(0).getCpi().trim(), contentFont));
                                table10.addCell(createCell("ASI", contentFont));
                                table10.addCell(new Paragraph(alXpoll.get(0).getAsi().trim(), contentFont));
                                table10.addCell(createCell("DATE, TIME", contentFont));
                                table10.addCell(new Paragraph(DateTimeUtils.getChangeDateFormat(alXpoll.get(0).getInsert_time().trim()), contentFont));
                                table10.addCell(createCell("OP", contentFont));
                                table10.addCell(new Paragraph(alXpoll.get(0).getOp().trim(), contentFont));
                                float[] columnWidths = new float[]{40f, 100f};
                                table10.setWidths(columnWidths);
                                table10.setTotalWidth(170f);
                                table10.setLockedWidth(true);
                                table10.setHorizontalAlignment(Element.ALIGN_LEFT);

                                vsatCell2.addElement(table10);
                                mainTableVsatX.addCell(vsatCell2);
                                Paragraph paragraph5 = new Paragraph();
                                paragraph5.setAlignment(Element.ALIGN_LEFT);
                                paragraph5.add(mainTableVsatX);
                                document.add(paragraph5);

                                stCopyClipBoard.append(xpollContent+"\n\n");
                            }

                            ArrayList<MasterConnectionParameter> alParam = new ConnectionParameterAdapter(getActivity()).val_param(preference.getCustID(), preference.getUn());
                            if (alParam.size() > 0){
                                String paramContent = "*LAN PARAMETER*\nIP Lan = " +alParam.get(0).getLan_parameter().trim() +" \n"+
                                                      "Subnet Mask = "+ alParam.get(0).getLan_subnetmask().trim() + "\n"+
                                                      "*MANAGEMENT PARAMETER*\nESN Modem = " +alParam.get(0).getManagement_esnmodem().trim() + "\n"+
                                                      "Gateway = " +alParam.get(0).getManagement_gateway().trim()+"\n"+
                                                      "SNMP = " +alParam.get(0).getManagement_snmp().trim()+"\n"+
                                                      "*RANGING PARAMETER*\nSIGNAL = " +alParam.get(0).getRanging_signal().trim()+ "\n"+
                                                      "DATA RATE = " +alParam.get(0).getRanging_data_rate().trim()+"\n"+
                                                      "FEC = " +alParam.get(0).getRanging_fec().trim() + "\n"+
                                                      "FINAL POWER SETTING = " +alParam.get(0).getRanging_power().trim()+"\n"+
                                                      "FINAL ESNO = " +alParam.get(0).getRanging_esno().trim() + "\n"+
                                                      "FINAL C/NO = " +alParam.get(0).getRanging_esno().trim() + "\n";

                                Paragraph paramContentParagraph = new Paragraph(paramContent,contentFont);
                                paramContentParagraph.setAlignment(Element.ALIGN_LEFT);
                                paramContentParagraph.setSpacingAfter(3f);
                                document.add(paramContentParagraph);
                                stCopyClipBoard.append(paramContent+"\n\n");
                            }

                        }else if (preference.getConnType().equals("M2M")){
                            stCopyClipBoard.append("*"+getActivity().getString(R.string.ioM2M_trans)+"*\n");

                            ArrayList<MasterM2mSetup> alM2m = new M2mSetupAdapter(getActivity()).val_m2mSetup(preference.getCustID(), preference.getUn());
                            if(alM2m.size() > 0){
                                String m2mSetupContent = "_OLD_\nM2M\nBrand / Type = " +alM2m.get(0).getBrand_type_m2m() +"\n"+
                                                         "S/N = "+alM2m.get(0).getSn_m2m().trim()+"\n"+
                                                         "ADAPTOR\nBrand / Type = " +alM2m.get(0).getBrand_type_adaptor().trim()+"\n"+
                                                         "S/N = " +alM2m.get(0).getSn_adaptor().trim() + "\n"+
                                                         "SIMCARD 1\nBrand / Type = " +alM2m.get(0).getSim_card1_type()+"\n"+
                                                         "S/N = " +alM2m.get(0).getSim_card1_sn() +"\n"+
                                                         "PUK = " +alM2m.get(0).getSim_card1_puk() + "\n"+
                                                         "SIMCARD 2 \nBrand / Type = " + alM2m.get(0).getSim_card2_type().trim() + "\n"+
                                                         "S/N = " +alM2m.get(0).getSim_card2_sn() + "\n"+
                                                         "PUK = " +alM2m.get(0).getSim_card2_puk() + "\n";

                                PdfPCell secondCell = new PdfPCell();
                                secondCell.setBorder(PdfPCell.NO_BORDER);
                                PdfPTable table4 = new PdfPTable(2);
                                table4.addCell(headTitleCell("*"+getActivity().getString(R.string.ioM2M_trans)+"*", titleFont));
                                table4.addCell(titleCell("M2M", contentFont));
                                table4.addCell(createCell("Brand / Type ", contentFont));
                                table4.addCell(new Paragraph(alM2m.get(0).getSn_m2m().trim(), contentFont));
                                table4.addCell(createCell("S/N ", contentFont));
                                table4.addCell(new Paragraph(alM2m.get(0).getSn_adaptor().trim(), contentFont));
                                table4.addCell(titleCell("ADAPTOR", contentFont));
                                table4.addCell(createCell("Brand / Type ", contentFont));
                                table4.addCell(new Paragraph(alM2m.get(0).getBrand_type_adaptor().trim(), contentFont));
                                table4.addCell(createCell("S/N ", contentFont));
                                table4.addCell(new Paragraph(alM2m.get(0).getSn_adaptor().trim(), contentFont));
                                table4.addCell(titleCell("SIMCARD 1", contentFont));
                                table4.addCell(createCell("Brand / Type ", contentFont));
                                table4.addCell(new Paragraph(alM2m.get(0).getSim_card1_type().trim(), contentFont));
                                table4.addCell(createCell("S/N ", contentFont));
                                table4.addCell(new Paragraph(alM2m.get(0).getSim_card1_sn().trim(), contentFont));
                                table4.addCell(createCell("PUK ", contentFont));
                                table4.addCell(new Paragraph(alM2m.get(0).getSim_card1_puk().trim(), contentFont));
                                table4.addCell(titleCell("SIMCARD 2", contentFont));
                                table4.addCell(createCell("Brand / Type ", contentFont));
                                table4.addCell(new Paragraph(alM2m.get(0).getSim_card2_type().trim(), contentFont));
                                table4.addCell(createCell("S/N ", contentFont));
                                table4.addCell(new Paragraph(alM2m.get(0).getSim_card2_sn().trim(), contentFont));
                                table4.addCell(createCell("PUK ", contentFont));
                                table4.addCell(new Paragraph(alM2m.get(0).getSim_card2_puk().trim(), contentFont));
                                table4.setHorizontalAlignment(Element.ALIGN_LEFT);
                                float[] columnWidths = new float[]{40f, 100f};
                                table4.setWidths(columnWidths);
                                table4.setTotalWidth(170f);
                                table4.setLockedWidth(true);

                                secondCell.addElement(table4);
                                mainTable3.addCell(secondCell);
                                Paragraph paragraph3 = new Paragraph();
                                paragraph3.add(mainTable3);
                                paragraph3.setAlignment(Element.ALIGN_LEFT);
                                document.add(paragraph3);
                                stCopyClipBoard.append(m2mSetupContent+"\n\n");
                            }

                            Paragraph spaceParagraph1 = new Paragraph("\n");
                            spaceParagraph1.setAlignment(Element.ALIGN_LEFT);
                            spaceParagraph1.setSpacingAfter(3f);
                            document.add(spaceParagraph1);

                            PdfPTable mainTable1 = new PdfPTable(2);
                            mainTable1.setWidthPercentage(90.0f);
                            mainTable1.setHorizontalAlignment(Element.ALIGN_LEFT);
                            PdfPCell cell1 = new PdfPCell();
                            cell1.setBorder(PdfPCell.NO_BORDER);
                            ArrayList<MasterM2mData> alM2mData = new M2mDataAdapter(getActivity()).val_dataM2m(preference.getCustID(), preference.getUn());
                            if (alM2mData.size() > 0){
                                String dataM2mContent = "Username = " +alM2mData.get(0).getUsername().trim()+ "\n"+
                                                        "Password = " +alM2mData.get(0).getPassword().trim() + "\n"+
                                                        "Ip machine = "+alM2mData.get(0).getIp_machine().trim() + "\n"+
                                                        "User = " +alM2mData.get(0).getUser().trim()+ "\n"+
                                                        "Remote = " +alM2mData.get(0).getRemote().trim()+"\n"+
                                                        "Tunnel ID 1 = " +alM2mData.get(0).getTunnel_id().trim()+"\n"+
                                                        "IP Bounding = " +alM2mData.get(0).getIp_bonding().trim()+"\n"+
                                                        "IP VLAN = " +alM2mData.get(0).getIp_vlan().trim()+"\n"+
                                                        "IP LAN = " +alM2mData.get(0).getIp_lan().trim()+"\n"+
                                                        "Subnetmask = " +alM2mData.get(0).getSubnet_mask().trim() +"\n"+
                                                        "AGG = " +alM2mData.get(0).getAgg().trim();

                                PdfPTable table5 = new PdfPTable(2);
                                table5.addCell(headTitleCell("*"+getActivity().getString(R.string.dataM2m_trans)+"*", titleFont));
                                table5.addCell(createCell("Username ", contentFont));
                                table5.addCell(new Paragraph(alM2mData.get(0).getUsername().trim(), contentFont));
                                table5.addCell(createCell("Password ", contentFont));
                                table5.addCell(new Paragraph(alM2mData.get(0).getPassword().trim(), contentFont));
                                table5.addCell(createCell("Ip machine ", contentFont));
                                table5.addCell(new Paragraph(alM2mData.get(0).getIp_machine().trim(), contentFont));
                                table5.addCell(createCell("User ", contentFont));
                                table5.addCell(new Paragraph(alM2mData.get(0).getUser().trim(), contentFont));
                                table5.addCell(createCell("Remote ", contentFont));
                                table5.addCell(new Paragraph(alM2mData.get(0).getRemote().trim(), contentFont));
                                table5.addCell(createCell("Tunnel ID 1 ", contentFont));
                                table5.addCell(new Paragraph(alM2mData.get(0).getTunnel_id().trim(), contentFont));
                                table5.addCell(createCell("IP Bounding ", contentFont));
                                table5.addCell(new Paragraph(alM2mData.get(0).getIp_bonding().trim(), contentFont));
                                table5.addCell(createCell("IP VLAN ", contentFont));
                                table5.addCell(new Paragraph(alM2mData.get(0).getIp_vlan().trim(), contentFont));
                                table5.addCell(createCell("IP LAN ", contentFont));
                                table5.addCell(new Paragraph(alM2mData.get(0).getIp_lan().trim(), contentFont));
                                table5.addCell(createCell("Subnetmask ", contentFont));
                                table5.addCell(new Paragraph(alM2mData.get(0).getSubnet_mask().trim(), contentFont));
                                table5.addCell(createCell("AGG ", contentFont));
                                table5.addCell(new Paragraph(alM2mData.get(0).getAgg().trim(), contentFont));
                                table5.setHorizontalAlignment(Element.ALIGN_LEFT);
                                float[] columnWidths = new float[]{40f, 100f};
                                table5.setWidths(columnWidths);
                                table5.setTotalWidth(170f);
                                table5.setLockedWidth(true);
                                cell1.addElement(table5);
                                mainTable1.addCell(cell1);
                                stCopyClipBoard.append(dataM2mContent+"\n\n");
                            }

                            ArrayList<MasterM2mReplace> alm2mReplace = new M2mReplaceAdapter(getActivity()).val_m2mReplace(preference.getCustID(), preference.getUn());
                            if (alm2mReplace.size() > 0){
                                String m2mType = alm2mReplace.get(0).getBrand_type_replace().trim();
                                String m2mSn = alm2mReplace.get(0).getBrand_type_adaptor().trim();
                                String adaptorType = alm2mReplace.get(0).getBrand_type_adaptor().trim();
                                String adaptorSn = alm2mReplace.get(0).getSn_adaptor().trim();
                                String simCard1Type = alm2mReplace.get(0).getSim_card1_type().trim();
                                String simCard1SN = alm2mReplace.get(0).getSim_card1_sn().trim();
                                String simcard1Puk = alm2mReplace.get(0).getSim_card1_puk().trim();
                                String simCard2Type = alm2mReplace.get(0).getSim_card2_type().trim();
                                String simCard2SN = alm2mReplace.get(0).getSim_card2_sn().trim();
                                String simcard2Puk = alm2mReplace.get(0).getSim_card2_puk().trim();

                                if (m2mType.equals("-")){m2mType = "";}
                                if (m2mSn.equals("-")){m2mSn = "";}
                                if (adaptorType.equals("-")){adaptorType = "";}
                                if (adaptorSn.equals("-")){adaptorSn = "";}
                                if (simCard1Type.equals("-")){simCard1Type = "";}
                                if (simCard1SN.equals("-")){simCard1SN = "";}
                                if (simcard1Puk.equals("-")){simcard1Puk = "";}
                                if (simCard2Type.equals("-")){simCard2Type = "";}
                                if (simCard2SN.equals("-")){simCard2SN = "";}
                                if (simcard2Puk.equals("-")){simcard2Puk = "";}

                                String m2mReplace = "_NEW_\nM2M\nBrand / Type = " +m2mType.trim()+"\n"+
                                                    "S/N = " +m2mSn.trim() +"\n"+
                                                    "ADAPTOR\nBrand / Type = "+adaptorType.trim() +"\n"+
                                                    "S/N = " +adaptorSn.trim() +"\n"+
                                                    "SIMCARD 1\nBrand / Type = " +simCard1Type.trim() +"\n"+
                                                    "S/N = " +simCard1SN.trim() + "\n" +
                                                    "PUK = " +simcard1Puk.trim() + "\n" +
                                                    "SIMCARD 2\nBrand / Type = " +simCard2Type.trim() + "\n"+
                                                    "S/N = " +simCard2SN.trim() + "\n" +
                                                    "PUK = " +simcard2Puk.trim();

                                PdfPCell cell3 = new PdfPCell();
                                cell3.setBorder(PdfPCell.NO_BORDER);
                                PdfPTable table6 = new PdfPTable(2);
                                table6.addCell(headTitleCell("NEW", titleFont));
                                table6.addCell(titleCell("M2M", contentFont));
                                table6.addCell(createCell("Brand / Type ", contentFont));
                                table6.addCell(new Paragraph(m2mType.trim(), contentFont));
                                table6.addCell(createCell("S/N ", contentFont));
                                table6.addCell(new Paragraph(m2mSn.trim(), contentFont));
                                table6.addCell(titleCell("ADAPTOR", contentFont));
                                table6.addCell(createCell("Brand / Type ", contentFont));
                                table6.addCell(new Paragraph(adaptorType.trim(), contentFont));
                                table6.addCell(createCell("S/N ", contentFont));
                                table6.addCell(new Paragraph(adaptorSn.trim(), contentFont));
                                table6.addCell(titleCell("SIMCARD 1", contentFont));
                                table6.addCell(createCell("Brand / Type  ", contentFont));
                                table6.addCell(new Paragraph(simCard1Type.trim(), contentFont));
                                table6.addCell(createCell("S/N ", contentFont));
                                table6.addCell(new Paragraph(simCard1SN.trim(), contentFont));
                                table6.addCell(createCell("PUK ", contentFont));
                                table6.addCell(new Paragraph(simcard1Puk.trim(), contentFont));
                                table6.addCell(titleCell("SIMCARD 2", contentFont));
                                table6.addCell(createCell("Brand / Type ", contentFont));
                                table6.addCell(new Paragraph(simCard2Type.trim(), contentFont));
                                table6.addCell(createCell("S/N", contentFont));
                                table6.addCell(new Paragraph(simCard2SN.trim(), contentFont));
                                table6.addCell(createCell("PUK ", contentFont));
                                table6.addCell(new Paragraph(simcard2Puk.trim(), contentFont));
                                table6.setHorizontalAlignment(Element.ALIGN_LEFT);
                                float[] columnWidths = new float[]{40f, 100f};
                                table6.setWidths(columnWidths);
                                table6.setTotalWidth(170f);
                                table6.setLockedWidth(true);

                                cell3.addElement(table6);
                                mainTable1.addCell(cell3);
                                Paragraph paragraph2 = new Paragraph();
                                paragraph2.setAlignment(Element.ALIGN_LEFT);
                                paragraph2.add(mainTable1);
                                document.add(paragraph2);
                                stCopyClipBoard.append(m2mReplace+"\n\n");
                            }
                        }

                        document.newPage();
                        PdfPTable table7 = new PdfPTable(2);
                        table7.addCell(headTitleCell("*"+getActivity().getString(R.string.action_trans)+"*", titleFont));
                        stCopyClipBoard.append("*"+getActivity().getString(R.string.action_trans)+"*\n\n");

                        ArrayList<MasterAction> al_listAction = new ActionAdapter(getActivity()).load_dataAction(preference.getCustID(), preference.getUn());
                        if (al_listAction.size() > 0){
                            arr_actionDateTime = new String[al_listAction.size()];
                            arr_actionTrans = new String[al_listAction.size()];
                            arr_actionEndTime = new String[al_listAction.size()];

                            int i = 0;
                            String actionContent = "";

                            for (MasterAction action : al_listAction){
                                arr_actionDateTime[i] = action.getAction_date_time();
                                arr_actionTrans[i] = action.getAction_desc();
                                arr_actionEndTime[i] = action.getAction_end_time();

                                String[] split = arr_actionDateTime[i].split(",");
                                String[] splitEndTime = arr_actionEndTime[i].split(",");
                                if (DateTimeUtils.getDateDiff(splitEndTime[0],split[0] ) > 1){
                                    if(i==0){ actionContent = split[0] + " -"+ splitEndTime[0]+ " | " + split[1] + "-" +splitEndTime[1]+ " : " +arr_actionTrans[i]; }
                                    else{ actionContent = actionContent +"\n"+split[0] + " -"+ splitEndTime[0]+ " | " + split[1] + "-" +splitEndTime[1]+ " : " +arr_actionTrans[i]; }

                                    table7.addCell(createCell(split[0] + " -"+ splitEndTime[0]+ " | " + split[1] + "-" +splitEndTime[1], contentFont));
                                    table7.addCell(new Paragraph(arr_actionTrans[i].trim(), contentFont));
                                }else{
                                    if (i==0){ actionContent = split[1]+ " -" +splitEndTime[1] + " : " + arr_actionTrans[i]; }
                                    else{ actionContent = actionContent +"\n"+split[1]+ " -" +splitEndTime[1] + " : " + arr_actionTrans[i]; }

                                    table7.addCell(createCell(split[1]+ " -" +splitEndTime[1], contentFont));
                                    table7.addCell(new Paragraph(arr_actionTrans[i].trim(), contentFont));
                                }
                                i++;
                            }

                            if (i == al_listAction.size()){
                                table7.setHorizontalAlignment(Element.ALIGN_LEFT);
                                float[] columnWidths = new float[]{40f, 100f};
                                table7.setWidths(columnWidths);
                                table7.setTotalWidth(170f);
                                table7.setLockedWidth(true);
                                document.add(table7);
                                stCopyClipBoard.append(actionContent+"\n\n");
                            }
                        }

                        document.close();
                        prg_dash.setVisibility(View.GONE);
                        alertChoose();
                    }catch (Exception e){
                        prg_dash.setVisibility(View.GONE);
                        messageUtils.toastMessage("err convert Pdf "+e.toString(), ConfigApps.T_ERROR);
                    }
                }
            }
        }, 1500);
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
            Log.d("### ","ke false " +e.toString());
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
//                            if (isSubmitReport()) {
//                                stCustname = preference.getCustName().trim();
//                                stUn = preference.getUn().trim();
//                                preference.clearDataTrans();
//                                emptyView(1);
//                                submitReport();
//                            }else{
//                                messageUtils.toastMessage("tidak terupdate", ConfigApps.T_ERROR);
//                            }
                        }else{
                            //send via WA
                            if (isSubmitReport()){
                                emptyView(1);
                                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("label", stCopyClipBoard);
                                clipboard.setPrimaryClip(clip);
                                messageUtils.toastMessage("Data Sukses tercopy, silahkan paste ke whatsapp ", ConfigApps.T_SUCCESS);
                                preference.clearDataTrans();
                            }else{
                               messageUtils.toastMessage("tidak terupdate", ConfigApps.T_ERROR);
                            }
                        }
                    }
                })
                .show();

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
                    }else{
                        messageUtils.toastMessage("File Tidak ditemukan", ConfigApps.T_WARNING);
                    }
                } catch(Exception e) {
                    messageUtils.toastMessage("err share message " +e.toString(), ConfigApps.T_ERROR);
                }
            }
        }, 1000);
    }

    @Override
    public void onResume(){
        super.onResume();
        loadDash();
        loadRcTrans();
        JobReportUtils.hideKeyboard(getActivity());
        if (preference.getCustID() == 0){
            emptyView(1);
        }
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
        }else{
            emptyView(0);
        }
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

}
