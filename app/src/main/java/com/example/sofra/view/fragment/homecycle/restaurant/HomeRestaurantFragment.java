package com.example.sofra.view.fragment.homecycle.restaurant;


import android.app.Dialog;
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
import com.example.sofra.utils.OnEndLess;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.fragment.BaseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.fragment_home_restaurant_progressbar)
    ProgressBar fragmentHomeRestaurantProgressbar;
    @BindView(R.id.fragment_home_restaurant_txt_view_error)
    TextView fragmentHomeRestaurantTxtViewError;
    @BindView(R.id.fragment_home_restaurant_constraint_empty_view_container)
    ConstraintLayout fragmentHomeRestaurantConstraintEmptyViewContainer;


    private ApiService apiService;
    private List<ItemRestaurantData> categoryList = new ArrayList<>();
    private RestaurantCategoriesAdapter adapter;

    private String dialogImgPath;
    private boolean isCategoryAdded;
    private String path;

    public HomeRestaurantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        View view = inflater.inflate(R.layout.fragment_home_restaurant, container, false);
        ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        getAllCategories();
        return view;
    }

    public void getAllCategories() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        fragmentHomeRestaurantRecycler.setLayoutManager(linearLayoutManager);

        OnEndLess onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                getPages(current_page);
            }
        };
        fragmentHomeRestaurantRecycler.addOnScrollListener(onEndLess);

        adapter = new RestaurantCategoriesAdapter(baseActivity);
        adapter.setData(categoryList , baseActivity.getSupportFragmentManager() , fragmentHomeRestaurantProgressbar);
        fragmentHomeRestaurantRecycler.setAdapter(adapter);


        getPages(1);
        Utils.showProgressBar(fragmentHomeRestaurantRecycler, fragmentHomeRestaurantTxtViewError
                , fragmentHomeRestaurantProgressbar);


    }

    private void getPages(int page) {
        apiService.getRestaurantCategories(SharedPreference.loadString(baseActivity,SharedPreference.API_TOKEN_KEY)
                , page).enqueue(new Callback<CategoryWithPagination>() {
            @Override
            public void onResponse(Call<CategoryWithPagination> call, Response<CategoryWithPagination> response) {

                try {
                    if (response.body().getStatus() == 1) {

                        if (response.body().getData().getData().size() == 0 & page == 1) {

                            Utils.showIfRecylerViewIsEmpty(fragmentHomeRestaurantConstraintEmptyViewContainer,
                                    fragmentHomeRestaurantRecycler,fragmentHomeRestaurantProgressbar);

                        }
                        else {
                            Utils.showContainer(fragmentHomeRestaurantRecycler, fragmentHomeRestaurantTxtViewError
                                    , fragmentHomeRestaurantProgressbar);
                            categoryList.addAll(response.body().getData().getData());
                            adapter.notifyDataSetChanged();
                        }


                    } else {
                        fragmentHomeRestaurantTxtViewError.setText(response.body().getMsg());
                        Utils.showErrorText(fragmentHomeRestaurantRecycler, fragmentHomeRestaurantTxtViewError
                                , fragmentHomeRestaurantProgressbar);

                    }
                } catch (Exception e) {
                    fragmentHomeRestaurantTxtViewError.setText(
                            baseActivity.getString(R.string.default_response_something_wrong_happened_please_refresh)
                    );
                    Utils.showErrorText(fragmentHomeRestaurantRecycler, fragmentHomeRestaurantTxtViewError
                            , fragmentHomeRestaurantProgressbar);
                }

            }

            @Override
            public void onFailure(Call<CategoryWithPagination> call, Throwable t) {

                fragmentHomeRestaurantTxtViewError.setText(
                        baseActivity.getString(R.string.default_response_no_internet_connection)
                );
                Utils.showErrorText(fragmentHomeRestaurantRecycler, fragmentHomeRestaurantTxtViewError
                        , fragmentHomeRestaurantProgressbar);

            }
        });
    }

    @Override
    public void onBack() {
        super.onBack();
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
        Dialog dialog = Utils.dialog(baseActivity, R.layout.dialog_category_restaurant
        ,true,baseActivity.getSupportFragmentManager(),R.id.activity_home_frame,new HomeRestaurantFragment());

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
                        Utils.onLoadImageFromUrl(dialogImg,path,baseActivity);
                    }
                });

                break;
        }

    }

    private void addNewCategory() {

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), dialogEdtTxt.getText().toString());
            RequestBody apiToken = RequestBody.create(MediaType.parse("text/plain")
                    , SharedPreference.loadString(baseActivity,SharedPreference.API_TOKEN_KEY));

            MultipartBody.Part file = Utils.convertFileToMultipart(path, "photo");

            Utils.showProgressBar(dialogConstraintContainer, dialogErrorTxtView, dialogProgressbar);

            apiService.addNewCategory(name, file, apiToken).enqueue(new Callback<NewCategory>() {
                @Override
                public void onResponse(Call<NewCategory> call, Response<NewCategory> response) {

                    try {
                        if (response.body().getStatus() == 1) {

                            dialogErrorTxtView.setText(response.body().getMsg());
                            Utils.showErrorText(dialogConstraintContainer, dialogErrorTxtView, dialogProgressbar);
                            Utils.customToast(baseActivity,response.body().getMsg(),false);
                            isCategoryAdded = true;

                        } else {

                            Toast.makeText(baseActivity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            Utils.showContainer(dialogConstraintContainer, dialogErrorTxtView, dialogProgressbar);

                        }
                    } catch (Exception e) {
                        Log.d("exception", "onResponse: " + e.getMessage());
                        dialogErrorTxtView.setText(baseActivity.
                                getString(R.string.default_response_something_wrong_happened_please_refresh));
                        Utils.showErrorText(dialogConstraintContainer, dialogErrorTxtView, dialogProgressbar);
                    }

                }

                @Override
                public void onFailure(Call<NewCategory> call, Throwable t) {

                    Utils.showContainer(dialogConstraintContainer, dialogErrorTxtView, dialogProgressbar);
                    if (path==null) {

                        Toast.makeText(baseActivity,
                                baseActivity.getString(R.string.default_response_should_select_a_photo),
                                Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Log.d("restaurant", "onFailure: " + t.getMessage());
                        Toast.makeText(baseActivity,
                                baseActivity.getString(R.string.default_response_no_internet_connection),
                                Toast.LENGTH_SHORT).show();


                    }


                }
            });



    }
}
