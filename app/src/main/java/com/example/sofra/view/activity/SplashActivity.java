package com.example.sofra.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sofra.R;

import com.example.sofra.data.local.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.activity_splash_btn_order_food)
    Button activitySplashBtnOrderFood;
    @BindView(R.id.activity_splash_btn_sell_food)
    Button activitySplashBtnSellFood;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.activity_splash_btn_order_food, R.id.activity_splash_btn_sell_food})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_splash_btn_order_food:


                SharedPreference.saveData(this,SharedPreference.USER_TYPE_KEY,SharedPreference.USER_TYPE_BUY);

                startActivity(new Intent(this,AuthActivity.class));


                break;
            case R.id.activity_splash_btn_sell_food:


                SharedPreference.saveData(this,SharedPreference.USER_TYPE_KEY,SharedPreference.USER_TYPE_SELL);
                startActivity(new Intent(this,AuthActivity.class));

                break;
        }
    }
}
