package com.example.sofra.view.fragment.homecycle.restaurant;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiService;
import com.example.sofra.data.api.RetrofitClient;
import com.example.sofra.data.local.SharedPreference;
import com.example.sofra.data.model.restaurant.newitem.NewItem;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.fragment.BaseFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRestaurantAddCategoryItemFragment extends BaseFragment {


    @BindView(R.id.fragment_home_restaurant_add_category_item_img)
    ImageView fragmentHomeRestaurantAddCategoryItemImg;
    @BindView(R.id.fragment_home_restaurant_add_category_item_edt_txt_product_name)
    EditText fragmentHomeRestaurantAddCategoryItemEdtTxtProductName;
    @BindView(R.id.fragment_home_restaurant_add_category_item_edt_txt_short_desc)
    EditText fragmentHomeRestaurantAddCategoryItemEdtTxtShortDesc;
    @BindView(R.id.fragment_home_restaurant_add_category_item_edt_txt_price)
    EditText fragmentHomeRestaurantAddCategoryItemEdtTxtPrice;
    @BindView(R.id.fragment_home_restaurant_add_category_item_edt_txt_offer_price)
    EditText fragmentHomeRestaurantAddCategoryItemEdtTxtOfferPrice;
    @BindView(R.id.fragment_home_restaurant_add_category_item_btn_add)
    Button fragmentHomeRestaurantAddCategoryItemBtnAdd;
    @BindView(R.id.fragment_home_restaurant_add_category_item_progressbar)
    ProgressBar fragmentHomeRestaurantAddCategoryItemProgressbar;

    private ApiService apiService;
    private String imgPath;
    public int categoryId;


    public HomeRestaurantAddCategoryItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        View view = inflater.inflate(R.layout.fragment_home_restaurant_add_category_item, container, false);
        ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        fragmentHomeRestaurantAddCategoryItemEdtTxtOfferPrice.setText("10");
        fragmentHomeRestaurantAddCategoryItemEdtTxtProductName.setText("adf");
        fragmentHomeRestaurantAddCategoryItemEdtTxtShortDesc.setText("this so good لذيذ جدا جدا ");
        fragmentHomeRestaurantAddCategoryItemEdtTxtPrice.setText("30");


        return view;
    }

    private void addNewItem() {

        if (Utils.path==null){
            Toast.makeText(baseActivity,
                    baseActivity.getString(R.string.default_response_should_select_a_photo),
                    Toast.LENGTH_SHORT).show();
        }
        else {
            MultipartBody.Part imgfile = Utils.convertFileToMultipart(Utils.path, "photo");
            Utils.path=null;
            RequestBody name = RequestBody.create
                    (MediaType.parse("text/plain"), fragmentHomeRestaurantAddCategoryItemEdtTxtProductName.getText().toString());

            RequestBody description = RequestBody.create
                    (MediaType.parse("text/plain"), fragmentHomeRestaurantAddCategoryItemEdtTxtShortDesc.getText().toString());

            RequestBody price = RequestBody.create
                    (MediaType.parse("text/plain"), fragmentHomeRestaurantAddCategoryItemEdtTxtPrice.getText().toString());

            RequestBody offerPrice = RequestBody.create
                    (MediaType.parse("text/plain"), fragmentHomeRestaurantAddCategoryItemEdtTxtOfferPrice.getText().toString());

            RequestBody catId = RequestBody.create
                    (MediaType.parse("text/plain"), String.valueOf(categoryId));

            RequestBody apiToken = RequestBody.create(MediaType.parse("text/plain"),
                    SharedPreference.loadString(baseActivity,SharedPreference.API_TOKEN_KEY));

            fragmentHomeRestaurantAddCategoryItemProgressbar.setVisibility(View.VISIBLE);

            apiService.addNewItem(description, price, name, imgfile, apiToken, offerPrice, catId).enqueue(new Callback<NewItem>() {
                @Override
                public void onResponse(Call<NewItem> call, Response<NewItem> response) {
                    try {
                        if (response.body().getStatus() == 1) {

                            fragmentHomeRestaurantAddCategoryItemProgressbar.setVisibility(View.GONE);
                            Toast.makeText(baseActivity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            Utils.replaceFragment(baseActivity.getSupportFragmentManager()
                            ,R.id.activity_home_frame,new HomeRestaurantCategoryFragment());

                        } else {

                            Toast.makeText(baseActivity, response.body().getMsg(), Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<NewItem> call, Throwable t) {

                    fragmentHomeRestaurantAddCategoryItemProgressbar.setVisibility(View.GONE);
                    fragmentHomeRestaurantAddCategoryItemImg.setImageResource(R.drawable.ic_images_folder);
                     Toast.makeText(baseActivity, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("itemFragment", "onFailure: " + t.getMessage());

                }
            });
        }



    }

    @Override
    public void onBack() {

        Utils.replaceFragment(baseActivity.getSupportFragmentManager()
                ,R.id.activity_home_frame,new HomeRestaurantCategoryFragment());
    }

    @OnClick({R.id.fragment_home_restaurant_add_category_item_img, R.id.fragment_home_restaurant_add_category_item_btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_home_restaurant_add_category_item_img:
              Utils.selectImage(baseActivity, fragmentHomeRestaurantAddCategoryItemImg);
                break;
            case R.id.fragment_home_restaurant_add_category_item_btn_add:
                addNewItem();
                break;
        }
    }
}
