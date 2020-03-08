package com.example.sofra.view.fragment.authcycle.restaurant;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiService;
import com.example.sofra.data.api.RetrofitClient;
import com.example.sofra.data.model.general.auth.Auth;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.fragment.BaseFragment;
import com.google.android.material.textfield.TextInputEditText;
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

public class RegisterRestaurantStep2Fragment extends BaseFragment {


    @BindView(R.id.fragment_register_res_step_2_edt_txt_phone)
    TextInputEditText fragmentRegisterResStep2EdtTxtPhone;
    @BindView(R.id.fragment_register_res_step_2_edt_txt_whats)
    TextInputEditText fragmentRegisterResStep2EdtTxtWhats;
    @BindView(R.id.fragment_register_res_step_2_img_shop_photo)
    ImageView fragmentRegisterResStep2ImgShopPhoto;
    @BindView(R.id.fragment_register_res_step_2_btn_register)
    Button fragmentRegisterResStep2BtnRegister;

    private static final int IMAGE_REQUEST_CODE = 1;

    private String path;

    private ApiService apiService;


    public RegisterRestaurantStep2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        View view = inflater.inflate(R.layout.fragment_register_restaurant_step_2, container, false);
        ButterKnife.bind(this, view);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        fragmentRegisterResStep2EdtTxtPhone.setText("01550533538");
        fragmentRegisterResStep2EdtTxtWhats.setText("01271224412");
        return view;
    }

    @Override
    public void onBack() {

        Utils.replaceFragment(baseActivity.getSupportFragmentManager(), R.id.activity_auth_frame
                , new RegisterRestaurantStep1Fragment());
    }

    @OnClick({R.id.fragment_register_res_step_2_img_shop_photo, R.id.fragment_register_res_step_2_btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_register_res_step_2_img_shop_photo:
                selectImage();
                break;
            case R.id.fragment_register_res_step_2_btn_register:
                sendData();
                break;
        }
    }

    private void sendData() {

        MultipartBody.Part file = Utils.convertFileToMultipart(path, "photo");
        RequestBody restaurantName = RequestBody.create(MediaType.parse("text/plain"), RegisterRestaurantStep1Fragment.restaurantName);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), RegisterRestaurantStep1Fragment.email);
        RequestBody deliveryTime = RequestBody.create(MediaType.parse("text/plain"), RegisterRestaurantStep1Fragment.deliverTime);
        RequestBody regionId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(RegisterRestaurantStep1Fragment.regionId));
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), RegisterRestaurantStep1Fragment.password);
        RequestBody confirmPassword = RequestBody.create(MediaType.parse("text/plain"), RegisterRestaurantStep1Fragment.confirmPassword);
        RequestBody deliverCost = RequestBody.create(MediaType.parse("text/plain"), RegisterRestaurantStep1Fragment.deliveryCharge);
        RequestBody minAmountToOrder = RequestBody.create(MediaType.parse("text/plain"), RegisterRestaurantStep1Fragment.minAmountToOrder);

        String phoneString = "";
        String whatsString = "";
        if (fragmentRegisterResStep2EdtTxtPhone.getText() != null) {
            phoneString = fragmentRegisterResStep2EdtTxtPhone.getText().toString();
        }
        if (fragmentRegisterResStep2EdtTxtWhats.getText() != null) {
            whatsString = fragmentRegisterResStep2EdtTxtWhats.getText().toString();
        }
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), phoneString);
        RequestBody whats = RequestBody.create(MediaType.parse("text/plain"), whatsString);

        Call<Auth> call = apiService.register(restaurantName, email, password, confirmPassword, phone, whats, regionId
                , deliverCost, minAmountToOrder, file, deliveryTime);

        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        Toast.makeText(baseActivity, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(baseActivity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        //Log.d("registerFragment", "status 0 : " + file.getName());

                        //Log.d("registerFragment"," status 0 : " + file);


                    }
                } catch (Exception e) {
                    Toast.makeText(baseActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {

                Toast.makeText(baseActivity, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("registerFragment", "onFailure: " + t.getMessage());


            }
        });

    }

    private void selectImage() {

        Utils.selectImage(baseActivity, new Action<ArrayList<AlbumFile>>() {
            @Override
            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                path = result.get(0).getPath();
                Utils.onLoadImageFromUrl(fragmentRegisterResStep2ImgShopPhoto, path, baseActivity);
            }
        });

    }


}
