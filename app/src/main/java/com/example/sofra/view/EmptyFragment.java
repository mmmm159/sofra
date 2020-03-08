package com.example.sofra.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sofra.R;
import com.example.sofra.view.fragment.BaseFragment;


import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EmptyFragment extends BaseFragment {


    private Unbinder unbiner;


    public EmptyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
       unbiner =  ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbiner.unbind();
    }
}
