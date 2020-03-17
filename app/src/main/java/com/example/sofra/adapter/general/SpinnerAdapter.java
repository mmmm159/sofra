package com.example.sofra.adapter.general;

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
    public List<GeneralResponse> cityDataList;



    public SpinnerAdapter(Context context) {
        this.context = context;
        this.cityDataList = new ArrayList<>();
        //this.cityDataList = cityDataList;

    }

    public void setCityNamesList(List<GeneralResponse> cityDataList,String hint) {
        this.cityDataList.add(new GeneralResponse(0,hint));
        this.cityDataList.addAll(cityDataList);
        }

    public int getPositionById(int id){

        int position = 0;
        for (int i=0;i<cityDataList.size();i++){

            if (cityDataList.get(i).getId()==id)
                position=i;
        }

        return position;
    }


    @Override
    public int getCount() {
        return cityDataList.size();
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

//        view = LayoutInflater.from(context).inflate(R.layout.item_spinner_city_dropdown,viewGroup,false);
//        TextView textView = view.findViewById(R.id.item_spinner_city_dropdown_txt_view);
//        textView.setText(cityNamesList.get(i));
        return view;
    }
}
