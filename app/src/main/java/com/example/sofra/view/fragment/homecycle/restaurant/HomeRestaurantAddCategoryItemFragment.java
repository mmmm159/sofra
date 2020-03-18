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
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.api.ApiService;
import com.example.sofra.data.api.RetrofitClient;
import com.example.sofra.data.local.SharedPreference;
import com.example.sofra.data.model.restaurant.newitem.NewItem;
import com.example.sofra.data.model.restaurant.updateitem.UpdateItem;
import com.example.sofra.utils.ConstantVars;
import com.example.sofra.utils.DialogUtils;
import com.example.sofra.utils.NetworkUtils;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.activity.HomeActivity;
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



    //instance vars
   public String itemImgurl;
   public String itemName;
   public String itemDescription;
   public String itemPrice;
   public String itemOfferPrice;
   public String itemId;
   public boolean isUpdateItemFragment;

    private ApiService apiService;
    private String path;
    private RequestBody categoryId;
    private MultipartBody.Part imgFile;
    private RequestBody name;
    private RequestBody description;
    private RequestBody offerPrice;
    private RequestBody price;
    private RequestBody apiToken;
    private RequestBody itemIdReqBody;


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


        if (isUpdateItemFragment){
            Glide.with(baseActivity).load(itemImgurl).into(fragmentHomeRestaurantAddCategoryItemImg);
            fragmentHomeRestaurantAddCategoryItemEdtTxtProductName.setText(itemName);
            fragmentHomeRestaurantAddCategoryItemEdtTxtPrice.setText(itemPrice);
            fragmentHomeRestaurantAddCategoryItemEdtTxtOfferPrice.setText(itemOfferPrice);
            fragmentHomeRestaurantAddCategoryItemEdtTxtShortDesc.setText(itemDescription);
        }

        return view;
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
              Utils.selectImage(baseActivity,  new Action<ArrayList<AlbumFile>>() {
                  @Override
                  public void onAction(@NonNull ArrayList<AlbumFile> result) {
                      path = result.get(0).getPath();
                      Utils.onLoadImageFromUrl(fragmentHomeRestaurantAddCategoryItemImg, path, baseActivity);
                  }
              });
                break;
            case R.id.fragment_home_restaurant_add_category_item_btn_add:

                imgFile = Utils.convertFileToMultipart(path, ConstantVars.PHOTO_MULTIPART_TAG);

                 name = Utils.convertToRequestBody(fragmentHomeRestaurantAddCategoryItemEdtTxtProductName
                        .getText().toString());

                 description = Utils.convertToRequestBody(fragmentHomeRestaurantAddCategoryItemEdtTxtShortDesc
                        .getText().toString());

                 offerPrice = Utils.convertToRequestBody(fragmentHomeRestaurantAddCategoryItemEdtTxtOfferPrice
                        .getText().toString());

                 price = Utils.convertToRequestBody(fragmentHomeRestaurantAddCategoryItemEdtTxtPrice
                        .getText().toString());

                 itemIdReqBody = Utils.convertToRequestBody(this.itemId);

                 apiToken = Utils.convertToRequestBody(SharedPreference.loadString(baseActivity,SharedPreference.API_TOKEN_KEY));

                 categoryId = Utils.convertToRequestBody(String.valueOf(HomeRestaurantCategoryFragment.categoryId));

                fragmentHomeRestaurantAddCategoryItemProgressbar.setVisibility(View.VISIBLE);

                if (NetworkUtils.isNetworkAvailable(baseActivity)) {

                    if (isUpdateItemFragment)
                        updateItem();

                    else addNewItem();
                }
                else Utils.customToast(baseActivity,
                        baseActivity.getString(R.string.default_response_no_internet_connection),true);


                break;
        }
    }


    private void addNewItem() {

        Call<NewItem> call = apiService.addNewItem(description, price, name, imgFile, apiToken, offerPrice, categoryId);
        call.enqueue(new Callback<NewItem>() {
            @Override
            public void onResponse(Call<NewItem> call, Response<NewItem> response) {
                try {
                    if (response.body().getStatus() == 1) {

                        fragmentHomeRestaurantAddCategoryItemProgressbar.setVisibility(View.GONE);
                        DialogUtils.alertDialog(baseActivity,
                                response.body().getMsg(),
                                true,baseActivity.getSupportFragmentManager(),
                                R.id.activity_home_frame,new HomeRestaurantCategoryFragment());

                    } else {

                        fragmentHomeRestaurantAddCategoryItemProgressbar.setVisibility(View.GONE);
                        Utils.customToast(baseActivity,response.body().getMsg(),true);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<NewItem> call, Throwable t) {

                fragmentHomeRestaurantAddCategoryItemProgressbar.setVisibility(View.GONE);
                Utils.customToast(baseActivity,
                        baseActivity.getString(R.string.default_response_something_wrong_happened_please_try_again),true);


            }
        });

    }

    private void updateItem() {

        apiService.updateItem(description,price,name,imgFile,itemIdReqBody,apiToken,offerPrice
        ,categoryId).enqueue(new Callback<UpdateItem>() {
            @Override
            public void onResponse(Call<UpdateItem> call, Response<UpdateItem> response) {
                try {
                    if (response.body().getStatus()==1) {

                        fragmentHomeRestaurantAddCategoryItemProgressbar.setVisibility(View.GONE);

                        DialogUtils.alertDialog(baseActivity,
                                baseActivity.getString(R.string.default_dialog_msg_update_done),
                        true,baseActivity.getSupportFragmentManager(),
                        R.id.activity_home_frame,new HomeRestaurantCategoryFragment());

                    }
                    else {
                        fragmentHomeRestaurantAddCategoryItemProgressbar.setVisibility(View.GONE);
                        Utils.customToast(baseActivity,response.body().getMsg(),true);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UpdateItem> call, Throwable t) {

                fragmentHomeRestaurantAddCategoryItemProgressbar.setVisibility(View.GONE);
                Utils.customToast(baseActivity,
                        baseActivity.getString(R.string.default_response_something_wrong_happened_please_try_again),true);

            }
        });
    }
}
