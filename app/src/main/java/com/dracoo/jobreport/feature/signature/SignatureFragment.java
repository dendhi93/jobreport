package com.dracoo.jobreport.feature.signature;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.JobDescAdapter;
import com.dracoo.jobreport.database.adapter.SignatureAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.master.MasterJobDesc;
import com.dracoo.jobreport.database.master.MasterSignature;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.feature.documentation.contract.ItemCallback;
import com.dracoo.jobreport.feature.signature.adapter.CustomList_Sign_Adapter;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.DateTimeUtils;
import com.dracoo.jobreport.util.JobReportUtils;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;
import com.j256.ormlite.dao.Dao;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignatureFragment extends Fragment implements ItemCallback {

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
    private boolean isUp = false;
    private Bitmap bitmap;
    private canvasView mCanvasView;
    private Handler handler;
    private String DIRECTORY, StoredPath;
    private Dao<MasterTransHistory, Integer> transHistoryAdapter;
    private Dao<MasterSignature, Integer> signatureAdapter;
    private View viewCanves;
    FileOutputStream mFileOutStream;
    private String tempFile;
    private ArrayList<MasterSignature> al_dataSignature;
    RecyclerView.LayoutManager layoutManager;
    private CustomList_Sign_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        handler = new Handler();
        preference = new Preference(getActivity());
        mCanvasView = new canvasView(getActivity(), null);
        mCanvasView.setBackgroundColor(Color.WHITE);
        canvasLayout.addView(mCanvasView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewCanves = canvasLayout;
        initSign();
        try{
            transHistoryAdapter = new TransHistoryAdapter(getActivity()).getAdapter();
            signatureAdapter = new SignatureAdapter(getActivity()).getAdapter();
        }catch (Exception e){}
    }

    @OnClick(R.id.imgB_sign_arrow)
    void visibleView(){
        if (!isUp){
            isUp = true;
            imgB_sign_arrow.setImageResource(R.drawable.ic_arrow_down_32);
            cardView_sign_1.setVisibility(View.GONE);
            rl_sign_list.setVisibility(View.VISIBLE);
            init_loadSignature();
        }else{
            isUp = false;
            imgB_sign_arrow.setImageResource(R.drawable.ic_arrow_up_32);
            cardView_sign_1.setVisibility(View.VISIBLE);
            rl_sign_list.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.imgB_sign_submit)
    void onSignSubmit(){
        try{
            if (selectedUserType == null){ messageUtils.snackBar_message("Mohon dipilih jenis user ", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
            } else{ valSignature(); }
        }catch (Exception e){ messageUtils.toastMessage("err submit " +e.toString(), ConfigApps.T_ERROR); }
    }

    @OnClick(R.id.imgB_sign_cancel)
    void onCancel(){
        mCanvasView.clear();
    }

    private void initSign(){
        if (preference.getProgress().equals("")){
            messageUtils.snackBar_message("Mohon diisi Menu Customer terlebih dahulu", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
            controlView(ConfigApps.DISABLE_TYPE);
        }else if (preference.getConnType().equals("")){
            messageUtils.snackBar_message("Mohon diisi Menu Cuonnection terlebih dahulu", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
            controlView(ConfigApps.DISABLE_TYPE);
        }else{ controlView(ConfigApps.ENABLE_TYPE); }
    }

    private void controlView(int intOptionView){
        if (intOptionView == ConfigApps.DISABLE_TYPE){
            imgB_sign_submit.setEnabled(false);
        }else if (intOptionView == ConfigApps.ENABLE_TYPE){
            imgB_sign_submit.setEnabled(true);
        }
    }

    private void initUserSpinner(){
        try {
            if (preference.getCustID().equals("")){ messageUtils.toastMessage("Mohon diisi menu Customer terlebih dahulu", ConfigApps.T_WARNING);
            }else{
                ArrayList<MasterJobDesc> al_JobDesc = new JobDescAdapter(getActivity()).load_trans(preference.getCustID(), preference.getUn());
                if (al_JobDesc.size() > 0){
                    String[] arrUserType = new String[]{"Jenis User",preference.getTechName(), al_JobDesc.get(0).getName_pic()};
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
        }catch (Exception e){ messageUtils.toastMessage("err spinner " +e.toString(), ConfigApps.T_ERROR); }
    }

    private void valSignature(){
        new AlertDialog.Builder(getActivity(), R.style.AlertDialog)
                .setTitle("Warning")
                .setMessage("Mohon dipastikan anda sudah tanda tangan, Apakah anda ingin simpan transaksi ?")
                .setIcon(R.drawable.ic_logo)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { submitSignature();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { dialog.dismiss();
                    }
                })
                .show();
    }

    @SuppressWarnings("deprecation")
    private void submitSignature(){
        try{
            DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/JobReport/images/"+preference.getCustName()+"/"+selectedUserType;
            File file = new File(DIRECTORY);
            if (!file.exists()) { file.mkdirs(); }
            StoredPath = DIRECTORY +"/"+ selectedUserType + ".png";
            tempFile = "/JobReport/images/"+preference.getCustName()+"/"+selectedUserType+"/"+ selectedUserType + ".png";
            ArrayList<MasterSignature> al_valSign = new SignatureAdapter(getActivity()).val_dataSign(preference.getCustID(), preference.getUn(), selectedUserType);
            if (al_valSign.size() > 0){
                messageUtils.snackBar_message("Tandatangan sudah ada, mohon pilih tipe lain",
                        getActivity(),ConfigApps.SNACKBAR_WITH_BUTTON);
            } else{
                viewCanves.setDrawingCacheEnabled(true);
                mCanvasView.saveImage(viewCanves,StoredPath);

                MasterSignature mSign = new MasterSignature();
                mSign.setT_user_type(selectedUserType.trim());
                mSign.setId_site(preference.getCustID());
                mSign.setConn_type(preference.getConnType().trim());
                mSign.setT_sign_path(tempFile.trim());
                mSign.setProgress_type(preference.getProgress().trim());
                mSign.setUn_user(preference.getUn().trim());
                mSign.setInsert_date(DateTimeUtils.getCurrentTime());
                signatureAdapter.create(mSign);
                transHistImage();
            }
        }catch (Exception e){ messageUtils.toastMessage("err submit sign " +e.toString(), ConfigApps.T_ERROR); }
    }

    private void transHistImage(){
        ArrayList<MasterTransHistory> al_valTransHist = new TransHistoryAdapter(getActivity())
                .val_trans(preference.getCustID(), preference.getUn(), getActivity().getString(R.string.doc_trans));
        if (al_valTransHist.size() > 0){
            try{
                MasterTransHistory mHist = transHistoryAdapter.queryForId(al_valTransHist.get(0).getId_trans());
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getActivity().getString(R.string.signature_trans));
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setIs_submited(0);

                transHistoryAdapter.update(mHist);
                messageUtils.toastMessage(getActivity().getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
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
                mHist.setTrans_step(getActivity().getString(R.string.signature_trans));
                mHist.setIs_submited(0);

                transHistoryAdapter.create(mHist);
                messageUtils.toastMessage(getActivity().getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                if (getActivity() != null){
                    JobReportUtils.hideKeyboard(getActivity());
                }
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist 2 " +e.toString(), ConfigApps.T_ERROR);
            }
        }
    }

    @Override
    public void itemSelected(int pos, String imageUrl) {
        //todo hapus file belum
    }

    @Override
    public void selectedImage(String imageUrl) {

    }

    public List<MasterSignature> init_dataSignature(){
        List<MasterSignature> list = new ArrayList<>();
        al_dataSignature = new SignatureAdapter(getActivity()).init_dataSign(preference.getCustID(), preference.getUn());
        if (al_dataSignature.size() > 0){
            list = al_dataSignature;
            rc_sign_activity.setVisibility(View.VISIBLE);
            lbl_sign_empty.setVisibility(View.GONE);
        }else{
            rc_sign_activity.setVisibility(View.GONE);
            lbl_sign_empty.setVisibility(View.VISIBLE);
        }
        return list;
    }

    private void init_loadSignature(){
        if (preference.getCustID() != 0 || !preference.getConnType().equals("")){
            rc_sign_activity.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            rc_sign_activity.setLayoutManager(layoutManager);
            List<MasterSignature> list = init_dataSignature();
            adapter = new CustomList_Sign_Adapter(getActivity(), list);
            adapter.signCallBack(this);
            rc_sign_activity.setAdapter(adapter);
        }
    }

    public class canvasView extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public canvasView(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void saveImage(View v, String StoredPath) {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if (bitmap == null) {
                Log.v("###", "ke if " + v.getHeight());
                bitmap = Bitmap.createBitmap(canvasLayout.getWidth(), canvasLayout.getHeight(), Bitmap.Config.RGB_565);
            }else {
                Log.v("###", "ke else " );
            }
            Canvas canvas = new Canvas(bitmap);
            try {
                mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();
            } catch (Exception e) { messageUtils.toastMessage("err " +e.toString(), ConfigApps.T_ERROR); }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
            Log.v("log_tag", string);
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }
}
