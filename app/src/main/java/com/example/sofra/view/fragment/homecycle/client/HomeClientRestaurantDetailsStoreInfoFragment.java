package com.example.sofra.view.fragment.homecycle.client;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiService;
import com.example.sofra.data.api.RetrofitClient;
import com.example.sofra.data.model.client.onerestaurant.OneRestaurant;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeClientRestaurantDetailsStoreInfoFragment extends BaseFragment {


    @BindView(R.id.fragment_home_client_restaurant_details_store_info_txt_view_status_value)
    TextView fragmentHomeClientRestaurantDetailsStoreInfoTxtViewStatusValue;
    @BindView(R.id.fragment_home_client_restaurant_details_store_info_txt_view_city_value)
    TextView fragmentHomeClientRestaurantDetailsStoreInfoTxtViewCityValue;
    @BindView(R.id.fragment_home_client_restaurant_details_store_info_txt_view_region_value)
    TextView fragmentHomeClientRestaurantDetailsStoreInfoTxtViewRegionValue;
    @BindView(R.id.fragment_home_client_restaurant_details_store_info_txt_view_min_charge_value)
    TextView fragmentHomeClientRestaurantDetailsStoreInfoTxtViewMinChargeValue;
    @BindView(R.id.fragment_home_client_restaurant_details_store_info_txt_view_delivery_cost_value)
    TextView fragmentHomeClientRestaurantDetailsStoreInfoTxtViewDeliveryCostValue;
    @BindView(R.id.fragment_home_client_restaurant_details_store_info_progress_bar)
    ProgressBar fragmentHomeClientRestaurantDetailsStoreInfoProgressBar;
    @BindView(R.id.fragment_home_client_restaurant_details_store_info_txt_view_network_error)
    TextView fragmentHomeClientRestaurantDetailsStoreInfoTxtViewNetworkError;
    @BindView(R.id.fragment_home_client_restaurant_details_store_info_constraint_container)
    ConstraintLayout fragmentHomeClientRestaurantDetailsStoreInfoConstraintContainer;
    private ApiService apiService;

    public HomeClientRestaurantDetailsStoreInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        View view = inflater.inflate(R.layout.fragment_home_client_restaurant_details_store_info, container, false);
        ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        getRestaurantData();
        return view;
    }

    private void getRestaurantData() {
        Utils.showProgressBar(fragmentHomeClientRestaurantDetailsStoreInfoConstraintContainer
        ,fragmentHomeClientRestaurantDetailsStoreInfoTxtViewNetworkError
        ,fragmentHomeClientRestaurantDetailsStoreInfoProgressBar);

        apiService.getRestaurantInfo(2).enqueue(new Callback<OneRestaurant>() {
            @Override
            public void onResponse(Call<OneRestaurant> call, Response<OneRestaurant> response) {

                try {
                    if (response.body().getStatus()==1) {
                        if (response.body().getData()!=null){

                            Utils.showContainer(fragmentHomeClientRestaurantDetailsStoreInfoConstraintContainer
                                    ,fragmentHomeClientRestaurantDetailsStoreInfoTxtViewNetworkError
                                    ,fragmentHomeClientRestaurantDetailsStoreInfoProgressBar);

                            fragmentHomeClientRestaurantDetailsStoreInfoTxtViewStatusValue
                                    .setText(response.body().getData().getAvailability());
                            fragmentHomeClientRestaurantDetailsStoreInfoTxtViewCityValue
                                    .setText(response.body().getData().getRegion().getCity().getName());
                            fragmentHomeClientRestaurantDetailsStoreInfoTxtViewRegionValue
                                    .setText(response.body().getData().getRegion().getName());
                            fragmentHomeClientRestaurantDetailsStoreInfoTxtViewMinChargeValue
                                    .setText(response.body().getData().getMinimumCharger());
                            fragmentHomeClientRestaurantDetailsStoreInfoTxtViewDeliveryCostValue
                                    .setText(response.body().getData().getDeliveryCost());
                        }
                        else {
                            fragmentHomeClientRestaurantDetailsStoreInfoTxtViewNetworkError
                                    .setText(baseActivity.getString(R.string.default_response_no_internet_connection));

                            Utils.showErrorText(fragmentHomeClientRestaurantDetailsStoreInfoConstraintContainer
                                    ,fragmentHomeClientRestaurantDetailsStoreInfoTxtViewNetworkError
                                    ,fragmentHomeClientRestaurantDetailsStoreInfoProgressBar);

                        }

                    }
                    else {
                        fragmentHomeClientRestaurantDetailsStoreInfoTxtViewNetworkError
                                .setText(response.body().getMsg());

                        Utils.showErrorText(fragmentHomeClientRestaurantDetailsStoreInfoConstraintContainer
                                ,fragmentHomeClientRestaurantDetailsStoreInfoTxtViewNetworkError
                                ,fragmentHomeClientRestaurantDetailsStoreInfoProgressBar);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<OneRestaurant> call, Throwable t) {

                fragmentHomeClientRestaurantDetailsStoreInfoTxtViewNetworkError
                        .setText(baseActivity.getString(R.string.default_response_no_internet_connection));

                Utils.showErrorText(fragmentHomeClientRestaurantDetailsStoreInfoConstraintContainer
                        ,fragmentHomeClientRestaurantDetailsStoreInfoTxtViewNetworkError
                        ,fragmentHomeClientRestaurantDetailsStoreInfoProgressBar);

            }

        });

    }

    @Override
    public void onBack() {
        super.onBack();
    }

}
