package com.dracoo.jobreport.preparation;

import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.txt_login_un)
    EditText txt_login_un;
    @BindView(R.id.txt_login_servicePoint)
    EditText txt_login_servicePoint;
    @BindView(R.id.txt_login_handphone)
    EditText txt_login_handphone;
    @BindView(R.id.lbl_login_version)
    TextView lbl_login_version;

    private MessageUtils messageUtils;
    private AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart(){
        super.onStart();

        messageUtils = new MessageUtils(LoginActivity.this);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        getValidation();

//        PackageInfo pInfo =  getPackageManager().getPackageInfo(getPackageName(), 0);
//        lbl_login_version.setText(pInfo.versionName);
    }

    public void actClick(View view){
        String un = txt_login_un.getText().toString().trim();
        String servicePoint = txt_login_servicePoint.getText().toString().trim();
        String handphone = txt_login_handphone.getText().toString().trim();

        if(un.equals("") || servicePoint.equals("") || handphone.equals("")){
            messageUtils.snackBar_message(getString(R.string.emptyString),
                    LoginActivity.this, ConfigApps.SNACKBAR_NO_BUTTON);
        } else if(awesomeValidation.validate()){
            messageUtils.snackBar_message("Under Maintenance", LoginActivity.this, ConfigApps.SNACKBAR_WITH_BUTTON);
        }
    }

    private void getValidation(){
        awesomeValidation.addValidation(this,R.id.txt_login_handphone, Patterns.PHONE, R.string.phone_validation);
        awesomeValidation.addValidation(this,R.id.txt_login_un, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_validation);
    }
}
