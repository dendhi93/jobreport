package com.dracoo.jobreport.feature.action;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dracoo.jobreport.R;

import butterknife.ButterKnife;

public class ActionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_action, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


}
