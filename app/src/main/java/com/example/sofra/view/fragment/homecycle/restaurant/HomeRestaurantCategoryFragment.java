package com.example.sofra.view.fragment.homecycle.restaurant;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.restaurant.RestaurantCategoryItemAdapter;
import com.example.sofra.data.api.ApiService;
import com.example.sofra.data.api.RetrofitClient;
import com.example.sofra.data.local.SharedPreference;
import com.example.sofra.data.model.general.itemrestaurant.ItemRestaurant;
import com.example.sofra.data.model.general.itemrestaurant.ItemRestaurantData;
import com.example.sofra.utils.OnEndLess;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.fragment.BaseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRestaurantCategoryFragment extends BaseFragment {


    @BindView(R.id.fragment_home_restaurant_category_progressbar)
    ProgressBar fragmentHomeRestaurantItemProgressbar;
    @BindView(R.id.fragment_home_restaurant_category_txt_view_error)
    TextView fragmentHomeRestaurantItemTxtViewError;
    @BindView(R.id.fragment_home_restaurant_category_txt_view_top)
    TextView fragmentHomeRestaurantItemTxtViewTop;
    @BindView(R.id.fragment_home_restaurant_category_constraint_empty_view_container)
    ConstraintLayout fragmentHomeRestaurantItemConstraintEmptyViewContainer;
    @BindView(R.id.fragment_home_restaurant_category_recycler)
    RecyclerView fragmentHomeRestaurantItemRecycler;
    @BindView(R.id.fragment_home_restaurant_category_floating_btn)
    FloatingActionButton fragmentHomeRestaurantItemFloatingBtn;

    private ApiService apiService;
    public static int categoryId;
    public static String categoryName;

    private RestaurantCategoryItemAdapter restaurantCategoryItemAdapter;
    private List<ItemRestaurantData> categoryItemDataList;

    public HomeRestaurantCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        View view = inflater.inflate(R.layout.fragment_home_restaurant_category, container, false);
        ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        fragmentHomeRestaurantItemTxtViewTop.setText(categoryName);
        getAllItems();
        return view;
    }

    private void getAllItems() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        fragmentHomeRestaurantItemRecycler.setLayoutManager(linearLayoutManager);

        OnEndLess onEndLess = new OnEndLess(linearLayoutManager,1) {
            @Override
            public void onLoadMore(int current_page) {

                getPages(current_page);
            }
        };

        fragmentHomeRestaurantItemRecycler.addOnScrollListener(onEndLess);
        categoryItemDataList = new ArrayList<>();
        restaurantCategoryItemAdapter = new RestaurantCategoryItemAdapter(baseActivity);
        restaurantCategoryItemAdapter.setData(categoryItemDataList , baseActivity.getSupportFragmentManager());
        fragmentHomeRestaurantItemRecycler.setAdapter(restaurantCategoryItemAdapter);

        Utils.showProgressBar(fragmentHomeRestaurantItemRecycler
        ,fragmentHomeRestaurantItemTxtViewError,fragmentHomeRestaurantItemProgressbar);
        getPages(1);
    }

    private void getPages(int page) {
        apiService.getRestaurantCategoryItems(SharedPreference.loadString(baseActivity,SharedPreference.API_TOKEN_KEY)
        ,categoryId,page).enqueue(new Callback<ItemRestaurant>() {
            @Override
            public void onResponse(Call<ItemRestaurant> call, Response<ItemRestaurant> response) {

                try {
                    if (response.body().getStatus()==1) {

                        if (response.body().getData().getData().size()==0&&page==1){

                            Toast.makeText(baseActivity, "هخخخخههههخخههه", Toast.LENGTH_SHORT).show();
                            Utils.showIfRecylerViewIsEmpty(fragmentHomeRestaurantItemConstraintEmptyViewContainer
                            ,fragmentHomeRestaurantItemRecycler,fragmentHomeRestaurantItemProgressbar);
                        }
                        else {
                            Utils.showContainer(fragmentHomeRestaurantItemRecycler
                                    ,fragmentHomeRestaurantItemTxtViewError,fragmentHomeRestaurantItemProgressbar);
                            categoryItemDataList.addAll(response.body().getData().getData());
                            restaurantCategoryItemAdapter.notifyDataSetChanged();
                        }



                    }
                    else {

                        fragmentHomeRestaurantItemTxtViewError.setText( response.body().getMsg());
                        Utils.showErrorText(fragmentHomeRestaurantItemRecycler,fragmentHomeRestaurantItemTxtViewError
                                ,fragmentHomeRestaurantItemProgressbar);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ItemRestaurant> call, Throwable t) {

                fragmentHomeRestaurantItemTxtViewError.setText(
                        baseActivity.getString(R.string.default_response_no_internet_connection)
                );
                Utils.showErrorText(fragmentHomeRestaurantItemRecycler,fragmentHomeRestaurantItemTxtViewError
                ,fragmentHomeRestaurantItemProgressbar);
            }
        });
    }

    @Override
    public void onBack() {

        Utils.replaceFragment(baseActivity.getSupportFragmentManager()
        ,R.id.activity_home_frame,new HomeRestaurantFragment());
    }

    @OnClick(R.id.fragment_home_restaurant_category_floating_btn)
    public void onViewClicked() {

        HomeRestaurantAddCategoryItemFragment homeRestaurantAddCategoryItemFragment =
                new HomeRestaurantAddCategoryItemFragment();
       // homeRestaurantAddCategoryItemFragment.categoryId = categoryId;
        Utils.replaceFragment(baseActivity.getSupportFragmentManager()
        ,R.id.activity_home_frame,homeRestaurantAddCategoryItemFragment);
    }
}
