package com.example.sofra.view.fragment.authcycle.client;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sofra.R;
import com.example.sofra.view.fragment.BaseFragment;

import butterknife.ButterKnife;

public class RegisterClientFragment extends BaseFragment {


    public RegisterClientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        View view =  inflater.inflate(R.layout.fragment_register_client, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onBack() {
        super.onBack();
    }

}
