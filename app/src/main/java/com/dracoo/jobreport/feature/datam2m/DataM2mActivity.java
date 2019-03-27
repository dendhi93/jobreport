package com.dracoo.jobreport.feature.datam2m;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataM2mActivity extends AppCompatActivity {
    private MessageUtils messageUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_m2m);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        messageUtils = new MessageUtils(DataM2mActivity.this);
    }



    @OnClick(R.id.imgB_dataM2m_submit)
    void submitM2m(){
        messageUtils.toastMessage("coba", ConfigApps.T_DEFAULT);
    }

    @OnClick(R.id.imgB_dataM2m_cancel)
    void cancelM2m(){
        messageUtils.toastMessage("coba1", ConfigApps.T_DEFAULT);
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
