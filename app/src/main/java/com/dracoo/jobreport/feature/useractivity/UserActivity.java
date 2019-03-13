package com.dracoo.jobreport.feature.useractivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.MessageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserActivity extends AppCompatActivity {

    @BindView(R.id.rg_userAct_progress)
    RadioGroup rg_userAct_progress;

    private RadioButton rb_progress;
    private String selectedProgress;
    private MessageUtils messageUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
    }

    @Override
    public void onStart(){
        super.onStart();

        messageUtils = new MessageUtils(UserActivity.this);
        rbProgressListener();
    }

    private void rbProgressListener(){
        rg_userAct_progress.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_progress =  findViewById(i);
                selectedProgress = rb_progress.getText().toString();
            }
        });
    }
}
