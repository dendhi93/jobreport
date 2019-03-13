package com.dracoo.jobreport.feature.xpoll;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.MessageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

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
}
