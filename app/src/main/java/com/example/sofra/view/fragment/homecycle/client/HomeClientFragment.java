package com.example.sofra.view.fragment.homecycle.client;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.RestaurantListAdapter;
import com.example.sofra.adapter.SpinnerAdapter;
import com.example.sofra.data.api.ApiService;
import com.example.sofra.data.api.RetrofitClient;
import com.example.sofra.data.model.general.auth.User;
import com.example.sofra.data.model.general.region.Region;
import com.example.sofra.data.model.client.restaurant.Restaurant;
import com.example.sofra.utils.GeneralSpinnerRequest;
import com.example.sofra.utils.OnEndLess;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeClientFragment extends BaseFragment {


    @BindView(R.id.fragment_home_client_spinner_city)
    AppCompatSpinner fragmentHomeClientSpinnerCity;
    @BindView(R.id.fragment_home_client_edt_txt_search)
    EditText fragmentHomeClientEdtTxtSearch;
    @BindView(R.id.fragment_home_client_recycler)
    RecyclerView fragmentHomeClientRecycler;
    @BindView(R.id.fragment_home_client_img_search)
    ImageView fragmentHomeClientImgSearch;
    @BindView(R.id.fragment_home_client_progress_bar)
    ProgressBar fragmentHomeClientProgressBar;
    @BindView(R.id.fragment_home_client_txt_view_no_search_results)
    TextView fragmentHomeClientTxtViewNoSearchResults;

    private ApiService apiService;
    private List<User> restaurantDataList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private RestaurantListAdapter restaurantListAdapter;

    private OnEndLess onEndLess;
    private SpinnerAdapter spinnerAdapter;
    private List<User> restaurantDataListWithFilter;

    public HomeClientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        View view = inflater.inflate(R.layout.fragment_home_client, container, false);
        ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        setUpCitySpinner();
        getAllRestaurants();
        return view;
    }

    private void setUpCitySpinner() {
        Call<Region> cityCall = apiService.getCities();
        String hint = baseActivity.getString(R.string.spinner_city_def_city);
        int itemSelectedResId = R.layout.item_spinner_city_selected_home;
        int dropDownResId = R.layout.item_spinner_city_drop_down_home;
        spinnerAdapter = new SpinnerAdapter(baseActivity);

        GeneralSpinnerRequest.setSpinnerData(baseActivity, cityCall, fragmentHomeClientSpinnerCity,
                spinnerAdapter, hint, fragmentHomeClientProgressBar, itemSelectedResId, dropDownResId);

    }

    private void getAllRestaurants() {

        linearLayoutManager = new LinearLayoutManager(baseActivity);
        fragmentHomeClientRecycler.setLayoutManager(linearLayoutManager);

        onEndLess = new OnEndLess(linearLayoutManager, 4) {
            @Override
            public void onLoadMore(int current_page) {

                getPage(current_page);
            }
        };

        fragmentHomeClientRecycler.addOnScrollListener(onEndLess);
        restaurantListAdapter = new RestaurantListAdapter(baseActivity, restaurantDataList);
        fragmentHomeClientRecycler.setAdapter(restaurantListAdapter);

        fragmentHomeClientProgressBar.setVisibility(View.VISIBLE);
        getPage(1);

    }

    private void getPage(int page) {

        apiService.getRestaurants(page).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                try {
                    if (response.body().getStatus() == 1) {

                        Utils.showContainer(fragmentHomeClientRecycler,fragmentHomeClientTxtViewNoSearchResults,
                                fragmentHomeClientProgressBar);
                        restaurantDataList.addAll(response.body().getData().getData());
                        restaurantListAdapter.notifyDataSetChanged();

                    } else
                        Toast.makeText(baseActivity, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    fragmentHomeClientTxtViewNoSearchResults.setText(
                            baseActivity.getString(R.string.default_response_no_internet_connection));
                    Utils.showErrorText(fragmentHomeClientRecycler,fragmentHomeClientTxtViewNoSearchResults,
                            fragmentHomeClientProgressBar);
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {

                fragmentHomeClientTxtViewNoSearchResults.setText(
                        baseActivity.getString(R.string.default_response_no_internet_connection));
                Utils.showErrorText(fragmentHomeClientRecycler,fragmentHomeClientTxtViewNoSearchResults,
                        fragmentHomeClientProgressBar);

            }
        });

    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @OnClick(R.id.fragment_home_client_img_search)
    public void onViewClicked() {

        String keyWord = fragmentHomeClientEdtTxtSearch.getText().toString();
        int spinnerPosition = fragmentHomeClientSpinnerCity.getSelectedItemPosition();
        int regionId;

        if (spinnerPosition == 0 && fragmentHomeClientEdtTxtSearch.getText().toString().equals("")) {
            Utils.showProgressBar(fragmentHomeClientRecycler,fragmentHomeClientTxtViewNoSearchResults,
                    fragmentHomeClientProgressBar);
            getAllRestaurants();
        } else {
            if (spinnerPosition == 0) {

                Utils.showProgressBar(fragmentHomeClientRecycler,fragmentHomeClientTxtViewNoSearchResults,
                        fragmentHomeClientProgressBar);
                fragmentHomeClientTxtViewNoSearchResults.setText(baseActivity.getString(R.string.catch_should_select_city));
                Utils.showErrorText(fragmentHomeClientRecycler,fragmentHomeClientTxtViewNoSearchResults,
                        fragmentHomeClientProgressBar);

            } else {

                regionId = (int) spinnerAdapter.getItemId(spinnerPosition - 1);
                Utils.showProgressBar(fragmentHomeClientRecycler,fragmentHomeClientTxtViewNoSearchResults,
                        fragmentHomeClientProgressBar);

                apiService.getRestaurantsFilter(keyWord, regionId).enqueue(new Callback<Restaurant>() {
                    @Override
                    public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {

                        try {
                            if (response.body().getStatus() == 1) {


                                Utils.showContainer(fragmentHomeClientRecycler,fragmentHomeClientTxtViewNoSearchResults,
                                        fragmentHomeClientProgressBar);
                                restaurantDataListWithFilter = response.body().getData().getData();
                                RestaurantListAdapter restaurantListAdapterWithFilter =
                                        new RestaurantListAdapter(baseActivity, restaurantDataListWithFilter);
                                fragmentHomeClientRecycler.setAdapter(restaurantListAdapterWithFilter);


                            } else {

                                fragmentHomeClientTxtViewNoSearchResults.setText(response.body().getMsg());
                                Utils.showErrorText(fragmentHomeClientRecycler,fragmentHomeClientTxtViewNoSearchResults,
                                        fragmentHomeClientProgressBar);

                            }
                        } catch (Exception e) {
                            Toast.makeText(baseActivity, baseActivity.getString(R.string.default_response_no_internet_connection)
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<Restaurant> call, Throwable t) {

                        fragmentHomeClientTxtViewNoSearchResults.setText(
                                baseActivity.getString(R.string.default_response_no_internet_connection));
                        Utils.showErrorText(fragmentHomeClientRecycler,fragmentHomeClientTxtViewNoSearchResults,
                                fragmentHomeClientProgressBar);
                    }
                });
            }


        }

    }






}



