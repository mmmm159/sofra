package com.example.sofra.view.fragment.homecycle.restaurant;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.sofra.utils.NetworkUtils;
import com.example.sofra.utils.OnEndLess;
import com.example.sofra.utils.RecyclerUtils;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.fragment.BaseFragment;
import com.facebook.shimmer.ShimmerFrameLayout;
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


    @BindView(R.id.fragment_home_restaurant_category_txt_view_top)
    TextView fragmentHomeRestaurantItemTxtViewTop;
    @BindView(R.id.fragment_home_restaurant_category_recycler)
    RecyclerView fragmentHomeRestaurantItemRecycler;
    @BindView(R.id.fragment_home_restaurant_category_floating_btn)
    FloatingActionButton fragmentHomeRestaurantItemFloatingBtn;
    @BindView(R.id.fragment_home_restaurant_shimmer_container)
    ShimmerFrameLayout fragmentHomeRestaurantShimmerContainer;
    @BindView(R.id.data_error_iv)
    ImageView dataErrorIv;
    @BindView(R.id.data_error_tv_reason)
    TextView dataErrorTvReason;
    @BindView(R.id.data_error_action)
    Button dataErrorAction;
    @BindView(R.id.data_error_cl)
    ConstraintLayout dataErrorCl;
    @BindView(R.id.load_more_cl)
    ConstraintLayout loadMoreCl;

    private ApiService apiService;
    public static int categoryId;
    public static String categoryName;

    private RestaurantCategoryItemAdapter restaurantCategoryItemAdapter;
    private List<ItemRestaurantData> categoryItemDataList;
    private OnEndLess onEndLess;
    private int maxPage;

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

         onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page<=maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        loadMoreCl.setVisibility(View.VISIBLE);
                        getPages(current_page);
                    }
                }
            }
        };

        fragmentHomeRestaurantItemRecycler.addOnScrollListener(onEndLess);
        categoryItemDataList = new ArrayList<>();
        restaurantCategoryItemAdapter = new RestaurantCategoryItemAdapter(baseActivity);
        restaurantCategoryItemAdapter.setData(categoryItemDataList, baseActivity.getSupportFragmentManager());
        fragmentHomeRestaurantItemRecycler.setAdapter(restaurantCategoryItemAdapter);


        getPages(1);
    }

    private void getPages(int page) {

        if (page==1){

            RecyclerUtils.showView(fragmentHomeRestaurantShimmerContainer
                    ,fragmentHomeRestaurantItemRecycler,dataErrorCl,null);
        }
        Call<ItemRestaurant> call = apiService.getRestaurantCategoryItems(SharedPreference.loadString(baseActivity, SharedPreference.API_TOKEN_KEY)
                , categoryId, page);
        startCall(call,page);

    }

    private void startCall(Call<ItemRestaurant> call, int page) {

        if (NetworkUtils.isNetworkAvailable(baseActivity)) {
            call.enqueue(new Callback<ItemRestaurant>() {
                @Override
                public void onResponse(Call<ItemRestaurant> call, Response<ItemRestaurant> response) {

                    try {
                        if (response.body().getStatus() == 1) {

                            maxPage = response.body().getData().getLastPage();
                            RecyclerUtils.showView(fragmentHomeRestaurantItemRecycler,fragmentHomeRestaurantShimmerContainer
                            ,dataErrorCl,loadMoreCl);
                            if (response.body().getData().getData().size() == 0 && page == 1) {

                                setEmptyView(baseActivity.getString(R.string.default_response_no_data_for_this_category)
                                );
                            }
                            else {

                                categoryItemDataList.addAll(response.body().getData().getData());
                                restaurantCategoryItemAdapter.notifyDataSetChanged();
                            }


                        } else {

                            setError(baseActivity.getString(R.string.default_response_something_wrong_happened_please_try_again)
                                    ,baseActivity.getString(R.string.refresh),R.drawable.ic_wrong);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ItemRestaurant> call, Throwable t) {

                    setError(baseActivity.getString(R.string.default_response_something_wrong_happened_please_try_again)
                    ,baseActivity.getString(R.string.refresh),R.drawable.ic_wrong);
                }
            });
        }
        else {
            setError(baseActivity.getString(R.string.default_response_no_internet_connection)
            ,baseActivity.getString(R.string.data_error_btn_Reload),R.drawable.ic_no_wifi);
        }
    }

    private void setEmptyView(String reason) {

        RecyclerUtils.showView(dataErrorCl,fragmentHomeRestaurantItemRecycler,fragmentHomeRestaurantShimmerContainer
        ,loadMoreCl);
        RecyclerUtils.setRecyclerError(dataErrorIv,dataErrorTvReason,dataErrorAction, R.drawable.ic_plus_empty_recycler
        ,reason,null,null);
    }

    private void setError(String reason, String actionString, int imgResId) {

        View.OnClickListener action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPages(1);
            }
        };

        RecyclerUtils.showView(dataErrorCl,fragmentHomeRestaurantItemRecycler,fragmentHomeRestaurantShimmerContainer
                ,loadMoreCl);
        RecyclerUtils.setRecyclerError(dataErrorIv,dataErrorTvReason,dataErrorAction,imgResId
        ,reason,actionString,action);
    }

    @Override
    public void onBack() {

        Utils.replaceFragment(baseActivity.getSupportFragmentManager()
                , R.id.activity_home_frame, new HomeRestaurantFragment());
    }

    @OnClick(R.id.fragment_home_restaurant_category_floating_btn)
    public void onViewClicked() {

        HomeRestaurantAddCategoryItemFragment homeRestaurantAddCategoryItemFragment =
                new HomeRestaurantAddCategoryItemFragment();
        // homeRestaurantAddCategoryItemFragment.categoryId = categoryId;
        Utils.replaceFragment(baseActivity.getSupportFragmentManager()
                , R.id.activity_home_frame, homeRestaurantAddCategoryItemFragment);
    }
}
