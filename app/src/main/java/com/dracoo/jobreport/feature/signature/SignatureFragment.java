package com.dracoo.jobreport.feature.signature;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.JobDescAdapter;
import com.dracoo.jobreport.database.master.MasterJobDesc;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


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
    private boolean isUp = false;
    private Bitmap bitmap;
    private canvasView mCanvasView;

    // Creating Separate Directory for saving Generated Images
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/JobReport/images/"+preference.getCustName();
    String StoredPath = DIRECTORY + preference.getCustName() + ".png";

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
        mCanvasView = new canvasView(getActivity(), null);
        mCanvasView.setBackgroundColor(Color.WHITE);

        File file = new File(DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    @OnClick(R.id.imgB_sign_arrow)
    void visibleView(){
        if (!isUp){
            isUp = true;
            imgB_sign_arrow.setImageResource(R.drawable.ic_arrow_down_32);
            cardView_sign_1.setVisibility(View.GONE);
            rl_sign_list.setVisibility(View.VISIBLE);
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
            } else{
                messageUtils.toastMessage("under maintenance", ConfigApps.T_INFO);
            }
        }catch (Exception e){ messageUtils.toastMessage("err submit " +e.toString(), ConfigApps.T_ERROR); }
    }

    @OnClick(R.id.imgB_xpoll_cancel)
    void onCancel(){
        mCanvasView.clear();
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
        }catch (Exception e){ messageUtils.toastMessage("err spinner " +e.toString(), ConfigApps.T_ERROR); }
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

        public void save(View v, String StoredPath) {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(canvasLayout.getWidth(), canvasLayout.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap);
            try {
                // Output the file
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);

                // Convert the output file to Image such as .png hrs pke asyntask
                final boolean compress = bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();

            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }

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
