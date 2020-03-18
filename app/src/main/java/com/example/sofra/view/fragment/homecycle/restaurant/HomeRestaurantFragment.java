package com.example.sofra.view.fragment.homecycle.restaurant;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.restaurant.RestaurantCategoriesAdapter;
import com.example.sofra.data.api.ApiService;
import com.example.sofra.data.api.RetrofitClient;
import com.example.sofra.data.local.SharedPreference;
import com.example.sofra.data.model.general.itemrestaurant.ItemRestaurantData;
import com.example.sofra.data.model.restaurant.categorywithpagination.CategoryWithPagination;
import com.example.sofra.data.model.restaurant.newcategory.NewCategory;
import com.example.sofra.utils.ConstantVars;
import com.example.sofra.utils.DialogUtils;
import com.example.sofra.utils.NetworkUtils;
import com.example.sofra.utils.OnEndLess;
import com.example.sofra.utils.RecyclerUtils;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.activity.HomeActivity;
import com.example.sofra.view.fragment.BaseFragment;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRestaurantFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.fragment_home_restaurant_recycler)
    RecyclerView fragmentHomeRestaurantRecycler;
    @BindView(R.id.fragment_home_restaurant_float_button)
    FloatingActionButton fragmentHomeRestaurantFloatButton;
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
    private List<ItemRestaurantData> categoryList = new ArrayList<>();
    private RestaurantCategoriesAdapter adapter;


    private String path;
    private Unbinder unBinder;
    private Integer maxPage;
    private OnEndLess onEndLess;
    private Dialog dialog;
    private DialogInterface.OnDismissListener action;

    public HomeRestaurantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        View view = inflater.inflate(R.layout.fragment_home_restaurant, container, false);
        unBinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        getAllCategories();
        return view;
    }

    private void getAllCategories() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        fragmentHomeRestaurantRecycler.setLayoutManager(linearLayoutManager);

         onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        loadMoreCl.setVisibility(View.VISIBLE);
                        getPages(current_page);
                    }
                }
            }
        };
        fragmentHomeRestaurantRecycler.addOnScrollListener(onEndLess);

        adapter = new RestaurantCategoriesAdapter(baseActivity);
        adapter.setData(categoryList, baseActivity.getSupportFragmentManager());
        fragmentHomeRestaurantRecycler.setAdapter(adapter);

        getPages(1);



    }

    private void getPages(int page) {

        if (page==1){
            RecyclerUtils.showView(fragmentHomeRestaurantShimmerContainer,fragmentHomeRestaurantRecycler,
                    dataErrorCl,null);
        }

        Call<CategoryWithPagination> call = apiService.getRestaurantCategories(SharedPreference.loadString(baseActivity,SharedPreference.API_TOKEN_KEY),page);
        startCall(call,page);
    }

    private void startCall(Call<CategoryWithPagination> call , int page) {

        if (NetworkUtils.isNetworkAvailable(baseActivity)) {
            call.enqueue(new Callback<CategoryWithPagination>() {
                @Override
                public void onResponse(Call<CategoryWithPagination> call, Response<CategoryWithPagination> response) {

                    try {
                        if (response.body().getStatus() == 1) {

                            maxPage = response.body().getData().getLastPage();

                            if (response.body().getData().getData().size() == 0 & page == 1) {
                                setEmptyView(baseActivity.getString(R.string.default_response_no_data_for_this_category),
                                        R.drawable.ic_plus_empty_recycler);

                            } else {
                                RecyclerUtils.showView(fragmentHomeRestaurantRecycler,fragmentHomeRestaurantShimmerContainer,loadMoreCl,dataErrorCl);
                                categoryList.addAll(response.body().getData().getData());
                                adapter.notifyDataSetChanged();
                            }


                        } else {
                            setError(baseActivity.getString(R.string.default_response_something_wrong_happened_please_try_again)
                                    ,baseActivity.getString(R.string.refresh),R.drawable.ic_wrong);

                        }
                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(Call<CategoryWithPagination> call, Throwable t) {

                    setError(baseActivity.getString(R.string.default_response_something_wrong_happened_please_try_again),
                            baseActivity.getString(R.string.refresh),R.drawable.ic_wrong);

                }
            });
        }
        else {
            setError(baseActivity.getString(R.string.default_response_no_internet_connection)
                    ,baseActivity.getString(R.string.data_error_btn_Reload),R.drawable.ic_no_wifi);
        }


    }

    private void setError(String reason, String stringAction, int imgRes) {

        View.OnClickListener action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPages(1);
            }
        };

        RecyclerUtils.showView(dataErrorCl,fragmentHomeRestaurantRecycler,fragmentHomeRestaurantShimmerContainer,loadMoreCl);
        RecyclerUtils.setRecyclerError(dataErrorIv,dataErrorTvReason,dataErrorAction,imgRes
                ,reason,stringAction,action);
    }

    private void setEmptyView(String reason , int imgRes) {

        RecyclerUtils.showView(dataErrorCl,fragmentHomeRestaurantRecycler,fragmentHomeRestaurantShimmerContainer,loadMoreCl);
        RecyclerUtils.setRecyclerError(dataErrorIv,dataErrorTvReason,dataErrorAction,imgRes
        ,reason,null,null);
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unBinder.unbind();
    }

    @OnClick(R.id.fragment_home_restaurant_float_button)
    public void onViewClicked() {
        showAddDialog();
    }


    private ProgressBar dialogProgressbar;
    private TextView dialogErrorTxtView;
    private TextView dialogTopTxtView;
    private ImageView dialogImg;
    private EditText dialogEdtTxt;
    private Button dialogBtn;
    private ConstraintLayout dialogConstraintContainer;


    private void showAddDialog() {

        action = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                HomeActivity homeActivity = (HomeActivity) baseActivity;
                homeActivity.getActivityBottomNav().setVisibility(View.VISIBLE);
                Utils.replaceFragment(baseActivity.getSupportFragmentManager()
                ,R.id.activity_home_frame,new HomeRestaurantFragment());
            }
        };
         dialog = DialogUtils.dialog(baseActivity, R.layout.dialog_category_restaurant);

        dialogBtn = dialog.findViewById(R.id.dialog_category_restaurant_btn);
        dialogConstraintContainer = dialog.findViewById(R.id.dialog_category_restaurant_constraint_container);
        dialogProgressbar = dialog.findViewById(R.id.dialog_category_restaurant_progressbar);
        dialogEdtTxt = dialog.findViewById(R.id.dialog_category_restaurant_edt_txt);
        dialogErrorTxtView = dialog.findViewById(R.id.dialog_category_restaurant_txt_view_error);
        dialogTopTxtView = dialog.findViewById(R.id.dialog_category_restaurant_txt_view_top);
        dialogImg = dialog.findViewById(R.id.dialog_category_restaurant_img);

        dialogImg.setOnClickListener(this);
        dialogBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.dialog_category_restaurant_btn:
                addNewCategory();
                break;
            case R.id.dialog_category_restaurant_img:
                Utils.selectImage(baseActivity, new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        path = result.get(0).getPath();
                        Utils.onLoadImageFromUrl(dialogImg, path, baseActivity);
                    }
                });

                break;
        }

    }

    private void addNewCategory() {

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), dialogEdtTxt.getText().toString());
        RequestBody apiToken = RequestBody.create(MediaType.parse("text/plain")
                , SharedPreference.loadString(baseActivity, SharedPreference.API_TOKEN_KEY));

        MultipartBody.Part file = Utils.convertFileToMultipart(path, ConstantVars.PHOTO_MULTIPART_TAG);

        Utils.showProgressBar(dialogConstraintContainer, dialogErrorTxtView, dialogProgressbar);

        apiService.addNewCategory(name, file, apiToken).enqueue(new Callback<NewCategory>() {
            @Override
            public void onResponse(Call<NewCategory> call, Response<NewCategory> response) {

                try {
                    if (response.body().getStatus() == 1) {

                        dialogErrorTxtView.setText(response.body().getMsg());
                        DialogUtils.showTextDialog(dialogConstraintContainer, dialogErrorTxtView, dialogProgressbar);
                        Utils.customToast(baseActivity, response.body().getMsg(), false);
                        dialog.setOnDismissListener(action);

                    } else {

                        Utils.customToast(baseActivity, response.body().getMsg(), true);
                        DialogUtils.showContainerDialog(dialogConstraintContainer, dialogErrorTxtView, dialogProgressbar);

                    }
                } catch (Exception e) {
                    Log.d("exception", "onResponse: " + e.getMessage());
                    dialogErrorTxtView.setText(baseActivity.
                            getString(R.string.default_response_something_wrong_happened_please_try_again));
                    DialogUtils.showTextDialog(dialogConstraintContainer, dialogErrorTxtView, dialogProgressbar);
                }

            }

            @Override
            public void onFailure(Call<NewCategory> call, Throwable t) {

                DialogUtils.showContainerDialog(dialogConstraintContainer, dialogErrorTxtView, dialogProgressbar);
                if (path == null) {

                    Utils.customToast(baseActivity,baseActivity.getString(R.string.default_response_should_select_a_photo)
                    ,true);
                } else {
                    Log.d("restaurant", "onFailure: " + t.getMessage());
                    Utils.customToast(baseActivity, baseActivity.getString(R.string.default_response_no_internet_connection)
                    ,true);


                }


            }
        });


    }
}
