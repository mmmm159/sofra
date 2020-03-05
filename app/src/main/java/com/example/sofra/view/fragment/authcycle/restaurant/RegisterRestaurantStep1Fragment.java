package com.example.sofra.view.fragment.authcycle.restaurant;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sofra.R;
import com.example.sofra.adapter.SpinnerAdapter;
import com.example.sofra.data.api.ApiService;
import com.example.sofra.data.api.RetrofitClient;
import com.example.sofra.data.model.general.region.Region;
import com.example.sofra.utils.GeneralSpinnerRequest;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.fragment.BaseFragment;
import com.example.sofra.view.fragment.authcycle.general.LoginFragment;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;


public class RegisterRestaurantStep1Fragment extends BaseFragment implements AdapterView.OnItemSelectedListener {


    @BindView(R.id.fragment_register_res_step_1_edt_txt_res_name)
    TextInputEditText fragmentRegisterResStep1EdtTxtResName;
    @BindView(R.id.fragment_register_res_step_1_edt_txt_email)
    TextInputEditText fragmentRegisterResStep1EdtTxtEmail;
    @BindView(R.id.fragment_register_res_step_1_edt_txt_deliver_time)
    TextInputEditText fragmentRegisterResStep1EdtTxtDeliverTime;
    @BindView(R.id.fragment_register_res_step_1_spinner_city)
    Spinner fragmentRegisterResStep1SpinnerCity;
    @BindView(R.id.fragment_register_res_step_1_spinner_neighbourhood)
    Spinner fragmentRegisterResStep1SpinnerNeighbourhood;
    @BindView(R.id.fragment_register_res_step_1_edt_txt_password)
    TextInputEditText fragmentRegisterResStep1EdtTxtPassword;
    @BindView(R.id.fragment_register_res_step_1_edt_txt_confirm_password)
    TextInputEditText fragmentRegisterResStep1EdtTxtConfirmPassword;
    @BindView(R.id.fragment_register_res_step_1_edt_txt_min_amount_order)
    TextInputEditText fragmentRegisterResStep1EdtTxtMinAmountOrder;
    @BindView(R.id.fragment_register_res_step_1_edt_txt_delivery_charge)
    TextInputEditText fragmentRegisterResStep1EdtTxtDeliveryCharge;
    @BindView(R.id.fragment_register_res_step_1_btn_continue)
    Button fragmentRegisterResStep1BtnContinue;
    @BindView(R.id.fragment_register_res_step_1_constraint_region)
    ConstraintLayout fragmentRegisterResStep1ConstraintRegion;
    @BindView(R.id.fragment_register_res_step_1_progress_bar)
    ProgressBar fragmentRegisterResStep1ProgressBar;

    private ApiService apiService;
    private SpinnerAdapter citySpinnerAdapter;
    private SpinnerAdapter regionSpinnerAdapter;

    public static String restaurantName;
    public static String email;
    public static String deliverTime;
    public static int cityId;
    public static int regionId;
    public static String password;
    public static String confirmPassword;
    public static String minAmountToOrder;
    public static String deliveryCharge;
    private int itemSelectedResId;
    private int dropDownResId;


    public RegisterRestaurantStep1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        View view = inflater.inflate(R.layout.fragment_register_restaurant_step_1, container, false);
        ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        setSpinners();
        fragmentRegisterResStep1EdtTxtResName.setText("pizzaugh");
        fragmentRegisterResStep1EdtTxtEmail.setText("mmmm@gmail.com");
        fragmentRegisterResStep1EdtTxtDeliverTime.setText("50");
        fragmentRegisterResStep1EdtTxtPassword.setText("123456");
        fragmentRegisterResStep1EdtTxtConfirmPassword.setText("123456");
        fragmentRegisterResStep1EdtTxtMinAmountOrder.setText("5");
        fragmentRegisterResStep1EdtTxtDeliveryCharge.setText("10");


        return view;
    }

    private void setStaticData() {
        if (fragmentRegisterResStep1EdtTxtResName.getText() != null)
            restaurantName = fragmentRegisterResStep1EdtTxtResName.getText().toString();
        if (fragmentRegisterResStep1EdtTxtEmail.getText() != null)
            email = fragmentRegisterResStep1EdtTxtEmail.getText().toString();
        if (fragmentRegisterResStep1EdtTxtDeliverTime.getText() != null)
            deliverTime = fragmentRegisterResStep1EdtTxtDeliverTime.getText().toString();
        if (fragmentRegisterResStep1EdtTxtPassword.getText() != null)
            password = fragmentRegisterResStep1EdtTxtPassword.getText().toString();
       if (fragmentRegisterResStep1EdtTxtConfirmPassword.getText() != null)
            confirmPassword = fragmentRegisterResStep1EdtTxtConfirmPassword.getText().toString();
        if (fragmentRegisterResStep1EdtTxtMinAmountOrder.getText() != null)
            minAmountToOrder = fragmentRegisterResStep1EdtTxtMinAmountOrder.getText().toString();
        if (fragmentRegisterResStep1EdtTxtDeliveryCharge.getText() != null)
            deliveryCharge = fragmentRegisterResStep1EdtTxtDeliveryCharge.getText().toString();

    }

    private void setSpinners() {

        itemSelectedResId = R.layout.item_spinner_city_selected;
        dropDownResId =R.layout.item_spinner_city_dropdown;

        Call<Region> cityCall = apiService.getCities();
        String cityHint = baseActivity.getString(R.string.spinner_city_def_city);
        citySpinnerAdapter = new SpinnerAdapter(baseActivity);
        GeneralSpinnerRequest.setSpinnerData(baseActivity, cityCall, fragmentRegisterResStep1SpinnerCity,
                citySpinnerAdapter, cityHint, fragmentRegisterResStep1ProgressBar,itemSelectedResId,dropDownResId);
        fragmentRegisterResStep1SpinnerCity.setOnItemSelectedListener(this);
        fragmentRegisterResStep1SpinnerNeighbourhood.setOnItemSelectedListener(this);
    }

    @Override
    public void onBack() {

        Utils.replaceFragment(baseActivity.getSupportFragmentManager(),R.id.activity_auth_frame
                ,new LoginFragment());

    }

    @OnClick(R.id.fragment_register_res_step_1_btn_continue)
    public void onViewClicked() {

        setStaticData();
       Utils.replaceFragment(baseActivity.getSupportFragmentManager(), R.id.activity_auth_frame
                , new RegisterRestaurantStep2Fragment());

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        switch (adapterView.getId()) {
            case R.id.fragment_register_res_step_1_spinner_city:

                if (position != 0) {
                    fragmentRegisterResStep1ProgressBar.setVisibility(View.VISIBLE);
                    cityId = (int) citySpinnerAdapter.getItemId(position - 1);
                    Call<Region> regionCall = apiService.getRegions(cityId);
                    regionSpinnerAdapter = new SpinnerAdapter(baseActivity);
                    String regionHint = baseActivity.getString(R.string.spinner_region_def_region);
                    GeneralSpinnerRequest.setSpinnerData(baseActivity, regionCall, fragmentRegisterResStep1SpinnerNeighbourhood,
                            regionSpinnerAdapter, fragmentRegisterResStep1ConstraintRegion, regionHint,
                            fragmentRegisterResStep1ProgressBar,itemSelectedResId,dropDownResId);
                }
                break;
            case R.id.fragment_register_res_step_1_spinner_neighbourhood:
                if (position != 0) {
                    regionId = (int) regionSpinnerAdapter.getItemId(position - 1);
                }
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
