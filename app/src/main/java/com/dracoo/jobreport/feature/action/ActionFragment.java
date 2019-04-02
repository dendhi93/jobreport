package com.dracoo.jobreport.feature.action;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.DateTimeUtils;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActionFragment extends Fragment {
    private MessageUtils messageUtils;
    private Preference preference;
    private int mYear, mMonth, mDay;

    @BindView(R.id.txt_action_beginDate)
    EditText txt_action_beginDate;
    @BindView(R.id.txt_action_endDate)
    EditText txt_action_endDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_action, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageUtils = new MessageUtils(getActivity());
        preference = new Preference(getActivity());
    }


    @OnClick(R.id.imgB_action_search)
    void onActionSearch(){
        messageUtils.toastMessage("coba", ConfigApps.T_INFO);
    }

    @OnClick(R.id.txt_action_beginDate)
    void searchBeginDate(){
        datePicker(1);
    }

    @OnClick(R.id.txt_action_endDate)
    void searchEndDate(){
        datePicker(2);
    }

    private void datePicker(final int selectedColumn){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedMonth, selectedDay;
                if (month < 10) {
                    selectedMonth = "0"+month;
                }else{
                    selectedMonth = String.valueOf(month);
                }

                if (dayOfMonth < 10) {
                    selectedDay = "0"+dayOfMonth;
                }else {
                    selectedDay = String.valueOf(dayOfMonth);
                }

                if (selectedColumn == 1){
                    txt_action_beginDate.setText(String.valueOf(year)+"-"+ selectedMonth +"-"+selectedDay);
                }else{
                    //TO DO count day
                    String tempEndDate = String.valueOf(year)+"-"+ selectedMonth +"-"+selectedDay;
                    messageUtils.toastMessage("dateDiff"
                            + DateTimeUtils.getDateDiff(txt_action_beginDate.getText().toString().trim(),
                            tempEndDate), ConfigApps.T_INFO);
                    txt_action_endDate.setText(String.valueOf(year)+"-"+ selectedMonth +"-"+selectedDay);
                }


            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
