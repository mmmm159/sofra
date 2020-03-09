package com.example.sofra.utils;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sofra.adapter.general.SpinnerAdapter;
import com.example.sofra.data.model.general.region.GeneralResponse;
import com.example.sofra.data.model.general.region.Region;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralSpinnerRequest {

    public static void setSpinnerData(Activity activity, Call<Region> call, Spinner spinner ,
                                      SpinnerAdapter spinnerAdapter , String hint
    , ProgressBar progressBar , int selectedItemResId ,int dropDownItemResId ){

        call.enqueue(new Callback<Region>() {
            @Override
            public void onResponse(Call<Region> call, Response<Region> response) {
                if (response.body().getStatus()==1) {

                    progressBar.setVisibility(View.GONE);
                    List<GeneralResponse> generalResponseList = response.body().getData().getData();

                    spinnerAdapter.setCityNamesList(generalResponseList,hint);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, selectedItemResId
                    ,spinnerAdapter.cityNamesList);
                    arrayAdapter.setDropDownViewResource(dropDownItemResId);
                    spinner.setAdapter(arrayAdapter);


                }
            }

            @Override
            public void onFailure(Call<Region> call, Throwable t) {

            }
        });

    }


    public static void setSpinnerData(Activity activity, Call<Region> call, Spinner spinner
            ,  SpinnerAdapter spinnerAdapter , ConstraintLayout constraintLayout
            , String hint , ProgressBar progressBar , int selectedItemResId ,int dropDownItemResId){

        call.enqueue(new Callback<Region>() {
            @Override
            public void onResponse(Call<Region> call, Response<Region> response) {
                if (response.body().getStatus()==1) {

                    progressBar.setVisibility(View.GONE);
                    List<GeneralResponse> generalResponseList = response.body().getData().getData();

                    spinnerAdapter.setCityNamesList(generalResponseList,hint);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, selectedItemResId
                            ,spinnerAdapter.cityNamesList);
                    arrayAdapter.setDropDownViewResource(dropDownItemResId);
                    constraintLayout.setVisibility(View.VISIBLE);
                    spinner.setAdapter(arrayAdapter);

                }
            }

            @Override
            public void onFailure(Call<Region> call, Throwable t) {

            }
        });

    }
}
