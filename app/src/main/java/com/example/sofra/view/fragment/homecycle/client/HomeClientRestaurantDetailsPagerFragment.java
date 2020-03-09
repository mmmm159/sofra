package com.example.sofra.view.fragment.homecycle.client;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.example.sofra.R;
import com.example.sofra.adapter.FragmentViewPagerAdapter;
import com.example.sofra.view.fragment.BaseFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeClientRestaurantDetailsPagerFragment extends BaseFragment {


    @BindView(R.id.fragment_home_client_restaurant_details_pager_tab_layout)
    TabLayout fragmentHomeClientRestaurantDetailsPagerTabLayout;
    @BindView(R.id.fragment_home_client_restaurant_details_pager_view_pager)
    ViewPager fragmentHomeClientRestaurantDetailsPagerViewPager;

    public HomeClientRestaurantDetailsPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        View view = inflater.inflate(R.layout.fragment_home_client_restaurant_details_pager, container, false);
        ButterKnife.bind(this, view);
        addPagerFragments();
        return view;
    }

    private void addPagerFragments() {
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getChildFragmentManager(), 1);
        adapter.adFragment(new HomeClientRestaurantDetailsMenuFragment()
                ,baseActivity.getString(R.string.home_client_restaurant_details_pager_tab_txt_food_menu));

        adapter.adFragment(new HomeClientRestaurantDetailsCommentsFragment()
                ,baseActivity.getString(R.string.home_client_restaurant_details_pager_tab_txt_comments_rate));

        adapter.adFragment(new HomeClientRestaurantDetailsStoreInfoFragment()
                ,baseActivity.getString(R.string.home_client_restaurant_details_pager_tab_txt_store_info));

       fragmentHomeClientRestaurantDetailsPagerViewPager.setAdapter(adapter);
       fragmentHomeClientRestaurantDetailsPagerTabLayout.setupWithViewPager(fragmentHomeClientRestaurantDetailsPagerViewPager);

    }

    @Override
    public void onBack() {
        super.onBack();
    }

}
