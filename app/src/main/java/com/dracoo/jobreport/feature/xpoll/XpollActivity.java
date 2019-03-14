package com.dracoo.jobreport.feature.xpoll;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XpollActivity extends AppCompatActivity {

    @BindView(R.id.rg_xpoll)
    RadioGroup rg_xpoll;

    private String selectedRadio = "";
    private RadioButton rbXpoll;
    private MessageUtils messageUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xpoll);
        ButterKnife.bind(this);
    }

    @Override
    public void onStart(){
        super.onStart();

        messageUtils = new MessageUtils(XpollActivity.this);
        xpollRadio();
    }

    private void xpollRadio(){
        rg_xpoll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rbXpoll =  findViewById(i);
                selectedRadio = rbXpoll.getText().toString();
            }
        });
    }

    @OnClick(R.id.imgB_xpoll_submit)
    void submitXpoll(){
        messageUtils.toastMessage("coba", ConfigApps.T_DEFAULT);
    }

    @OnClick(R.id.imgB_xpoll_menu)
    void menuXpoll(){
        messageUtils.toastMessage("coba menu", ConfigApps.T_DEFAULT);
    }

    @OnClick(R.id.imgB_xpoll_cancel)
    void cancelXpoll(){
        messageUtils.toastMessage("coba2", ConfigApps.T_DEFAULT);
    }

}
