package com.example.sofra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sofra.R;
import com.example.sofra.data.model.general.region.GeneralResponse;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<GeneralResponse> cityDataList;
    public List<String> cityNamesList = new ArrayList<>();


    public SpinnerAdapter(Context context) {
        this.context = context;
        //this.cityDataList = cityDataList;

    }

    public void setCityNamesList(List<GeneralResponse> cityDataList,String hint) {

        this.cityDataList = cityDataList;
        cityNamesList.add(hint);
        for (int i = 0; i <cityDataList.size(); i++) {
            cityNamesList.add(this.cityDataList.get(i).getName());
        }
    }


    @Override
    public int getCount() {
        return cityNamesList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return cityDataList.get(i).getId();
    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.item_spinner_city_dropdown,viewGroup,false);
        TextView textView = view.findViewById(R.id.item_spinner_txt_view);
        textView.setText(cityNamesList.get(i));
        return view;
    }
}
