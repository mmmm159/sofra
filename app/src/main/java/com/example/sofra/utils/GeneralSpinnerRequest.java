package com.example.sofra.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sofra.R;
import com.example.sofra.adapter.general.SpinnerAdapter;
import com.example.sofra.data.local.SharedPreference;
import com.example.sofra.data.model.general.region.GeneralResponse;
import com.example.sofra.data.model.general.region.Region;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralSpinnerRequest {

    public static void setSpinnerData(Activity activity, Call<Region> call, Spinner spinner ,
                                      SpinnerAdapter spinnerAdapter , String hint
    , ProgressBar progressBar , int selectedItemResId ,int dropDownItemResId , Integer selectedId
    ,AdapterView.OnItemSelectedListener action){

        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<Region>() {
            @Override
            public void onResponse(Call<Region> call, Response<Region> response) {
                if (response.body().getStatus()==1) {

                    progressBar.setVisibility(View.GONE);
                    List<GeneralResponse> generalResponseList = response.body().getData().getData();

                    spinnerAdapter.setCityNamesList(generalResponseList,hint);
                    List<String> cityNamesList = new ArrayList<>();
                    for (GeneralResponse cityData : spinnerAdapter.cityDataList)
                    {
                        cityNamesList.add(cityData.getName());
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, selectedItemResId,
                    cityNamesList);
                    arrayAdapter.setDropDownViewResource(dropDownItemResId);
                    spinner.setAdapter(arrayAdapter);

                    if (selectedId!=null){
                        spinner.setSelection(spinnerAdapter.getPositionById(selectedId));
                    }
                    spinner.setOnItemSelectedListener(action);


                }
            }

            @Override
            public void onFailure(Call<Region> call, Throwable t) {

            }
        });

    }


    public static void setSpinnerData(Activity activity, Call<Region> call, Spinner spinner
            ,  SpinnerAdapter spinnerAdapter , ViewGroup viewGroup
            , String hint , ProgressBar progressBar , int selectedItemResId ,int dropDownItemResId , Integer selectedId){

        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<Region>() {
            @Override
            public void onResponse(Call<Region> call, Response<Region> response) {
                if (response.body().getStatus()==1) {

                    progressBar.setVisibility(View.GONE);
                    List<GeneralResponse> generalResponseList = response.body().getData().getData();

                    spinnerAdapter.setCityNamesList(generalResponseList,hint);
                    List<String> cityNamesList = new ArrayList<>();
                    for (GeneralResponse cityData : spinnerAdapter.cityDataList)
                    {
                        cityNamesList.add(cityData.getName());
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, selectedItemResId
                            ,cityNamesList);
                    arrayAdapter.setDropDownViewResource(dropDownItemResId);
                    viewGroup.setVisibility(View.VISIBLE);
                    spinner.setAdapter(arrayAdapter);

                    if (selectedId!=null){
                        spinner.setSelection(spinnerAdapter.getPositionById(selectedId));
                    }

                }
            }

            @Override
            public void onFailure(Call<Region> call, Throwable t) {

            }
        });

    }
}
