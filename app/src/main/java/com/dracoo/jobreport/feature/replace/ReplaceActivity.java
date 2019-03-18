package com.dracoo.jobreport.feature.replace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReplaceActivity extends AppCompatActivity {
    @BindView(R.id.ln_replace_vsat)
    LinearLayout ln_replace_vsat;
    @BindView(R.id.ln_replace_m2m)
    LinearLayout ln_replace_m2m;

    private MessageUtils messageUtils;
    public static final String EXTRA_CALLER_CONN = "connection_type";
    public static final Integer EXTRA_CALLER_VSATCONN = 1;
    public static final Integer EXTRA_CALLER_M2MCONN = 2;
    private Integer intentConnectionType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart(){
        super.onStart();
        messageUtils = new MessageUtils(ReplaceActivity.this);
        try{
            intentConnectionType = getIntent().getIntExtra(EXTRA_CALLER_CONN, 1);
        }catch (Exception e){ Log.d("ReplaceErr ",""+e.toString()); }

        if (intentConnectionType == 1){
            ln_replace_vsat.setVisibility(View.VISIBLE);
            ln_replace_m2m.setVisibility(View.GONE);
        }else{
            ln_replace_vsat.setVisibility(View.GONE);
            ln_replace_m2m.setVisibility(View.VISIBLE);
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
