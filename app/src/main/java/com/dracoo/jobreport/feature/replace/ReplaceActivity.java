package com.dracoo.jobreport.feature.replace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.master.MasterM2mReplace;
import com.dracoo.jobreport.database.master.MasterVsatReplace;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;
import com.j256.ormlite.dao.Dao;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReplaceActivity extends AppCompatActivity {
    @BindView(R.id.ln_replace_vsat)
    LinearLayout ln_replace_vsat;
    @BindView(R.id.ln_replace_m2m)
    LinearLayout ln_replace_m2m;

    private MessageUtils messageUtils;
    private Preference preference;
    public static final String EXTRA_CALLER_CONN = "connection_type";
    public static final Integer EXTRA_CALLER_VSATCONN = 1;
    public static final Integer EXTRA_CALLER_M2MCONN = 2;
    private Integer intentConnectionType = 0;
    private Dao<MasterVsatReplace, Integer> vsatReplaceDao;
    private Dao<MasterM2mReplace, Integer> m2mReplaceDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        messageUtils = new MessageUtils(ReplaceActivity.this);
        preference = new Preference(ReplaceActivity.this);
        try{
            intentConnectionType = getIntent().getIntExtra(EXTRA_CALLER_CONN, 0);
        }catch (Exception e){ Log.d("ReplaceErr ",""+e.toString()); }

        if (intentConnectionType == 1){
            ln_replace_vsat.setVisibility(View.VISIBLE);
            ln_replace_m2m.setVisibility(View.GONE);
        }else{
            ln_replace_vsat.setVisibility(View.GONE);
            ln_replace_m2m.setVisibility(View.VISIBLE);
        }

        if (getSupportActionBar() != null && intentConnectionType == 1){
            getSupportActionBar().setSubtitle("VSAT");
        }else if (getSupportActionBar() != null && intentConnectionType != 1){
            getSupportActionBar().setSubtitle("M2M");
        }

    }

    @OnClick(R.id.imgB_rep_submit)
    void submitRep(){
        messageUtils.toastMessage("coba", ConfigApps.T_DEFAULT);
    }


    @OnClick(R.id.imgB_rep_cancel)
    void cancelRep(){
        messageUtils.toastMessage("coba2", ConfigApps.T_DEFAULT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
