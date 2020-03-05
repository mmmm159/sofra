package com.example.sofra.view.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.sofra.R;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.activity.BaseActivity;
import com.example.sofra.view.fragment.homecycle.general.HomeClientFragment;
import com.example.sofra.view.fragment.homecycle.general.HomeClientRestaurantDetailsCommentsFragment;
import com.example.sofra.view.fragment.homecycle.general.HomeClientRestaurantDetailsMenuFragment;
import com.example.sofra.view.fragment.homecycle.general.HomeClientRestaurantDetailsPagerFragment;
import com.example.sofra.view.fragment.homecycle.general.HomeClientRestaurantDetailsStoreInfoFragment;
import com.example.sofra.view.fragment.homecycle.restaurant.HomeRestaurantAddCategoryItemFragment;
import com.example.sofra.view.fragment.homecycle.restaurant.HomeRestaurantCategoryFragment;
import com.example.sofra.view.fragment.homecycle.restaurant.HomeRestaurantFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    public ImageView getActivityHomeImgNotification() {
        return activityHomeImgNotification;
    }

    public TextView getActivityHomeTxtViewResName() {
        return activityHomeTxtViewResName;
    }

    public ImageView getActivityHomeImgShoppingCart() {
        return activityHomeImgShoppingCart;
    }

    public Toolbar getActivityHomeToolBar() {
        return activityHomeToolBar;
    }

    public BottomNavigationView getActivityBottomNav(){
        return activityHomeNavView;
    }

    @BindView(R.id.activity_home_img_notification)
    ImageView activityHomeImgNotification;
    @BindView(R.id.activity_home_txt_view_res_name)
    TextView activityHomeTxtViewResName;
    @BindView(R.id.activity_home_img_shopping_cart)
    ImageView activityHomeImgShoppingCart;
    @BindView(R.id.activity_home_tool_bar)
    Toolbar activityHomeToolBar;
    @BindView(R.id.activity_home_nav_view)
    BottomNavigationView activityHomeNavView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    if (Utils.isUserRestaurant(HomeActivity.this)){
                        Utils.replaceFragment(getSupportFragmentManager()
                                , R.id.activity_home_frame, new HomeRestaurantFragment());
                    }
                    else
                    {
                        Utils.replaceFragment(getSupportFragmentManager()
                                , R.id.activity_home_frame, new HomeClientRestaurantDetailsPagerFragment());
                    }

                    return true;
                case R.id.navigation_order_list:

                    Utils.replaceFragment(getSupportFragmentManager()
                            , R.id.activity_home_frame, new HomeRestaurantAddCategoryItemFragment());

                    return true;
                case R.id.navigation_account:
                    Utils.replaceFragment(getSupportFragmentManager()
                            , R.id.activity_home_frame, new HomeRestaurantCategoryFragment());

                    return true;
                case R.id.navigation_more:
                    Utils.replaceFragment(getSupportFragmentManager()
                            , R.id.activity_home_frame, new HomeClientRestaurantDetailsMenuFragment());

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        BottomNavigationView navView = findViewById(R.id.activity_home_nav_view);

        if (Utils.isUserRestaurant(this)){
            Utils.replaceFragment(getSupportFragmentManager()
                    , R.id.activity_home_frame, new HomeRestaurantFragment());
        }
        else {
            Utils.replaceFragment(getSupportFragmentManager()
                    , R.id.activity_home_frame, new HomeClientRestaurantDetailsPagerFragment());
        }

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



    }



    @OnClick({R.id.activity_home_img_notification, R.id.activity_home_img_shopping_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_home_img_notification:
                break;
            case R.id.activity_home_img_shopping_cart:
                break;
        }
    }


}
