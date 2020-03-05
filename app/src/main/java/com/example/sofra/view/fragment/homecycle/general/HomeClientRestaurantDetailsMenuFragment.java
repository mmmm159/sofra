package com.example.sofra.view.fragment.homecycle.general;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.FoodCategoriesAdapter;
import com.example.sofra.adapter.FoodItemsAdapter;
import com.example.sofra.data.api.ApiService;
import com.example.sofra.data.api.RetrofitClient;
import com.example.sofra.data.model.general.category.Category;
import com.example.sofra.data.model.general.itemrestaurant.ItemRestaurant;
import com.example.sofra.data.model.general.itemrestaurant.ItemRestaurantData;
import com.example.sofra.utils.OnEndLess;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeClientRestaurantDetailsMenuFragment extends BaseFragment {


    @BindView(R.id.fragment_home_client_restaurant_details_menu_progress_bar)
    ProgressBar fragmentHomeClientRestaurantDetailsMenuProgressBar;
    @BindView(R.id.fragment_home_client_restaurant_details_menu_txt_view_no_internet)
    TextView fragmentHomeClientRestaurantDetailsMenuTxtViewNoInternet;
    @BindView(R.id.fragment_home_client_restaurant_details_menu_recycler_categories)
    RecyclerView fragmentHomeClientRestaurantDetailsMenuRecyclerCategories;
    @BindView(R.id.fragment_home_client_restaurant_details_menu_recycler_items)
    RecyclerView fragmentHomeClientRestaurantDetailsMenuRecyclerItems;


    private ApiService apiService;
    private List<ItemRestaurantData> foodItemsList = new ArrayList<>();
    private FoodItemsAdapter foodItemsAdapter;

    public int categoryId = 0;


    public HomeClientRestaurantDetailsMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        View view = inflater.inflate(R.layout.fragment_home_client_restaurant_details_menu, container, false);
        ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        setAllCategories();
        setAllFoodItems();
        return view;
    }

    private void setAllFoodItems() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        fragmentHomeClientRestaurantDetailsMenuRecyclerItems.setLayoutManager(linearLayoutManager);
        OnEndLess onEndLess = new OnEndLess(linearLayoutManager,1) {
            @Override
            public void onLoadMore(int current_page) {
                getPages(current_page);
            }
        };

        fragmentHomeClientRestaurantDetailsMenuRecyclerItems.addOnScrollListener(onEndLess);
         foodItemsAdapter = new FoodItemsAdapter(baseActivity);
         foodItemsAdapter.setData(foodItemsList);
         fragmentHomeClientRestaurantDetailsMenuRecyclerItems.setAdapter(foodItemsAdapter);

        Utils.showProgressBar(fragmentHomeClientRestaurantDetailsMenuRecyclerItems
                , fragmentHomeClientRestaurantDetailsMenuTxtViewNoInternet
                , fragmentHomeClientRestaurantDetailsMenuProgressBar);

        getPages(1);
    }

    private void getPages(int page) {


        apiService.getRestuarantItems(1,categoryId,page).enqueue(new Callback<ItemRestaurant>() {
            @Override
            public void onResponse(Call<ItemRestaurant> call, Response<ItemRestaurant> response) {

                try {
                    if (response.body().getStatus()==1) {

                        if (response.body().getData().getData().size()==0&&page==1) {
                            fragmentHomeClientRestaurantDetailsMenuTxtViewNoInternet.setText(
                                    baseActivity.getString(R.string.default_response_no_data_for_this_category)
                            );
                            Utils.showErrorText(fragmentHomeClientRestaurantDetailsMenuRecyclerItems
                                    , fragmentHomeClientRestaurantDetailsMenuTxtViewNoInternet
                                    , fragmentHomeClientRestaurantDetailsMenuProgressBar);

                        }
                        else {


                            Utils.showContainer(fragmentHomeClientRestaurantDetailsMenuRecyclerItems
                                    , fragmentHomeClientRestaurantDetailsMenuTxtViewNoInternet
                                    , fragmentHomeClientRestaurantDetailsMenuProgressBar);

                            foodItemsList.addAll(response.body().getData().getData());
                            foodItemsAdapter.notifyDataSetChanged();

                        }

                    }
                    else {

                        fragmentHomeClientRestaurantDetailsMenuTxtViewNoInternet.setText(response.body().getMsg());
                        Utils.showErrorText(fragmentHomeClientRestaurantDetailsMenuRecyclerItems
                                , fragmentHomeClientRestaurantDetailsMenuTxtViewNoInternet
                                , fragmentHomeClientRestaurantDetailsMenuProgressBar);


                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ItemRestaurant> call, Throwable t) {

                fragmentHomeClientRestaurantDetailsMenuTxtViewNoInternet.setText(
                        baseActivity.getString(R.string.default_response_no_internet_connection));
                Utils.showErrorText(fragmentHomeClientRestaurantDetailsMenuRecyclerItems
                        , fragmentHomeClientRestaurantDetailsMenuTxtViewNoInternet
                        , fragmentHomeClientRestaurantDetailsMenuProgressBar);

            }
        });
    }

    private void setAllCategories() {
        Utils.showProgressBar(fragmentHomeClientRestaurantDetailsMenuRecyclerCategories
                , fragmentHomeClientRestaurantDetailsMenuTxtViewNoInternet
                , fragmentHomeClientRestaurantDetailsMenuProgressBar);


        apiService.getCategories(1).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {

                try {
                    if (response.body().getStatus() == 1) {
                        Utils.showContainer(fragmentHomeClientRestaurantDetailsMenuRecyclerCategories
                                , fragmentHomeClientRestaurantDetailsMenuTxtViewNoInternet
                                , fragmentHomeClientRestaurantDetailsMenuProgressBar);

                        ArrayList<ItemRestaurantData> categoriesList = new ArrayList<>();
                        categoriesList.add(new ItemRestaurantData(-1, "جديد العروض",
                                "https://www.ramhardware.co.nz/uploads/8/8/5/0/8850496/s577246565128489517_p211_i3_w1280.jpeg" ));
                        categoriesList.add(new ItemRestaurantData(0, "الكل",
                                "https://pbs.twimg.com/profile_images/950768161925816320/AO7Zwu-u_400x400.jpg"));

                        categoriesList.addAll(response.body().getData());
                        FoodCategoriesAdapter foodCategoriesAdapter = new FoodCategoriesAdapter(baseActivity);
                        foodCategoriesAdapter.setData(categoriesList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity
                                , RecyclerView.HORIZONTAL, false);
                        fragmentHomeClientRestaurantDetailsMenuRecyclerCategories.setLayoutManager(linearLayoutManager);
                        fragmentHomeClientRestaurantDetailsMenuRecyclerCategories.setAdapter(foodCategoriesAdapter);
                    } else {

                        fragmentHomeClientRestaurantDetailsMenuTxtViewNoInternet.setText(response.body().getMsg());

                        Utils.showErrorText(fragmentHomeClientRestaurantDetailsMenuRecyclerCategories
                                , fragmentHomeClientRestaurantDetailsMenuTxtViewNoInternet
                                , fragmentHomeClientRestaurantDetailsMenuProgressBar);


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {

                fragmentHomeClientRestaurantDetailsMenuTxtViewNoInternet.setText(
                        baseActivity.getString(R.string.default_response_no_internet_connection));

                Utils.showErrorText(fragmentHomeClientRestaurantDetailsMenuRecyclerCategories
                        , fragmentHomeClientRestaurantDetailsMenuTxtViewNoInternet
                        , fragmentHomeClientRestaurantDetailsMenuProgressBar);

            }
        });

    }

    @Override
    public void onBack() {
        super.onBack();
    }

}
