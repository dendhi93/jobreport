package com.dracoo.jobreport.feature.datam2m;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataM2mActivity extends AppCompatActivity {
    private MessageUtils messageUtils;
    @BindView(R.id.txt_dm2m_un)
    EditText txt_dm2m_un;
    @BindView(R.id.txt_dm2m_password)
    EditText txt_dm2m_password;
    @BindView(R.id.txt_dm2m_ipMachine)
    EditText txt_dm2m_ipMachine;
    @BindView(R.id.txt_dm2m_remote)
    EditText txt_dm2m_remote;
    @BindView(R.id.txt_dm2m_tunnelId)
    EditText txt_dm2m_tunnelId;
    @BindView(R.id.txt_dm2m_ipBouding)
    EditText txt_dm2m_ipBouding;
    @BindView(R.id.txt_dm2m_ipVLan)
    EditText txt_dm2m_ipVLan;
    @BindView(R.id.txt_dm2m_ipLLan)
    EditText txt_dm2m_ipLLan;
    @BindView(R.id.txt_dm2m_dataM2m_subnetMask)
    EditText txt_dm2m_dataM2m_subnetMask;
    @BindView(R.id.txt_dm2m_dataM2m_agg)
    EditText txt_dm2m_dataM2m_agg;
    @BindView(R.id.txt_dm2m_user)
    EditText txt_dm2m_user;


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
        if (!valEmptyText()){
            messageUtils.snackBar_message(getString(R.string.emptyString), DataM2mActivity.this, ConfigApps.SNACKBAR_NO_BUTTON);
        }else{
            messageUtils.toastMessage("coba1", ConfigApps.T_DEFAULT);
        }
    }

    @OnClick(R.id.imgB_dataM2m_cancel)
    void cancelM2m(){
        messageUtils.toastMessage("coba1", ConfigApps.T_DEFAULT);
    }

    private boolean valEmptyText(){
        if (txt_dm2m_un.getText().toString().equals("")||
                txt_dm2m_password.getText().toString().equals("")||
                txt_dm2m_ipMachine.getText().toString().equals("") ||
                txt_dm2m_user.getText().toString().equals("") ||
                txt_dm2m_remote.getText().toString().equals("") ||
                txt_dm2m_tunnelId.getText().toString().equals("") ||
                txt_dm2m_ipBouding.getText().toString().equals("") ||
                txt_dm2m_ipVLan.getText().toString().equals("") ||
                txt_dm2m_ipLLan.getText().toString().equals("") ||
                txt_dm2m_dataM2m_subnetMask.getText().toString().equals("") ||
                txt_dm2m_dataM2m_agg.getText().toString().equals("")
        ){
            return false;
        }else{
            return true;
        }
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
