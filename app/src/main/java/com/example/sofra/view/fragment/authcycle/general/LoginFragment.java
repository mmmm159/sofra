package com.example.sofra.view.fragment.authcycle.general;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiService;
import com.example.sofra.data.api.RetrofitClient;
import com.example.sofra.data.local.SharedPreference;
import com.example.sofra.data.model.general.auth.Auth;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.activity.HomeActivity;
import com.example.sofra.view.fragment.BaseFragment;
import com.example.sofra.view.fragment.authcycle.restaurant.RegisterRestaurantStep1Fragment;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {


    @BindView(R.id.fragment_login_txt_view_did_you_forget)
    TextView fragmentLoginTxtViewDidYouForget;
    @BindView(R.id.fragment_login_btn_enter)
    Button fragmentLoginBtnEnter;

    @BindView(R.id.fragment_login_txt_view_you_dont_have_acc)
    TextView fragmentLoginTxtViewYouDontHaveAcc;
    @BindView(R.id.fragment_login_img_background)
    ImageView fragmentLoginImgBackground;
    @BindView(R.id.fragment_login_edt_txt_password)
    TextInputEditText fragmentLoginEdtTxtPassword;
    @BindView(R.id.fragment_login_edt_txt_email)
    TextInputEditText fragmentLoginEdtTxtEmail;
    @BindView(R.id.fragment_login_txt_view_login)
    TextView fragmentLoginTxtViewLogin;
    @BindView(R.id.fragment_login_progressbar)
    ProgressBar fragmentLoginProgressbar;

    private ApiService apiService;
    private String userTypeString;
    private int userType;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        fragmentLoginEdtTxtEmail.setText("mmmm@gmail.com");
        fragmentLoginEdtTxtPassword.setText("123456");
        userType = SharedPreference.loadInt(baseActivity, SharedPreference.USER_TYPE_KEY);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        return view;
    }

    @Override
    public void onBack() {
        if (userType == SharedPreference.USER_TYPE_SELL) {

            super.onBack();
        } else {

            //TODO*******************
        }
    }


    @OnClick({R.id.fragment_login_txt_view_did_you_forget,
            R.id.fragment_login_txt_view_you_dont_have_acc,
            R.id.fragment_login_btn_enter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_login_txt_view_did_you_forget:
                Utils.replaceFragment(baseActivity.getSupportFragmentManager()
                        , R.id.activity_auth_frame, new forgetPasswordFragment());
                break;
            case R.id.fragment_login_txt_view_you_dont_have_acc:

                Utils.replaceFragment(baseActivity.getSupportFragmentManager()
                        , R.id.activity_auth_frame, new RegisterRestaurantStep1Fragment());
                break;

            case R.id.fragment_login_btn_enter:

                if (fragmentLoginEdtTxtEmail.getText() != null && fragmentLoginEdtTxtPassword.getText() != null)
                    login(fragmentLoginEdtTxtEmail.getText().toString(), fragmentLoginEdtTxtPassword.getText().toString());

//                Utils.replaceFragment(baseActivity.getSupportFragmentManager(),R.id.activity_auth_frame
//                ,new ResetPasswordFragment());
                break;
        }
    }

    private void login(String email, String password) {

        Utils.showProgressBar(null,null,fragmentLoginProgressbar);

        Call<Auth> call;

        switch (userType) {

            case SharedPreference.USER_TYPE_BUY:
//                call = apiService.loginClient(email,password);
//                userTypeString = "Client";
//                callApiService(call);


                break;
            case SharedPreference.USER_TYPE_SELL:

                call = apiService.loginRestaurant(email, password);
                callApiService(call);
                break;
        }


    }

    private void callApiService(Call<Auth> call) {

        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                try {
                    if (response.body().getStatus() == 1) {

                        if (response.body().getData()!=null) {

                            SharedPreference.saveData(baseActivity,SharedPreference.HAS_USER_DATA_SAVED_CORRECT_TAG,true);

                            fragmentLoginProgressbar.setVisibility(View.GONE);

                            SharedPreference.saveData(baseActivity, SharedPreference.API_TOKEN_KEY,
                                    response.body().getData().getApiToken());

                            SharedPreference.saveData(baseActivity, SharedPreference.IMG_URL_KEY,
                                    response.body().getData().getUser().getPhotoUrl());

                            SharedPreference.saveData(baseActivity, SharedPreference.NAME_KEY,
                                    response.body().getData().getUser().getName());

                            SharedPreference.saveData(baseActivity, SharedPreference.EMAIL_KEY,
                                    response.body().getData().getUser().getEmail());

                            SharedPreference.saveData(baseActivity, SharedPreference.PHONE_KEY,
                                    response.body().getData().getUser().getPhone());

                            SharedPreference.saveData(baseActivity, SharedPreference.CITY_ID_KEY,
                                    response.body().getData().getUser().getRegion().getCity().getId());

                            SharedPreference.saveData(baseActivity, SharedPreference.REGION_ID_KEY,
                                    response.body().getData().getUser().getRegion().getId());



                            if (SharedPreference.loadInt(baseActivity,SharedPreference.USER_TYPE_KEY)
                                    ==SharedPreference.USER_TYPE_SELL) {

                                SharedPreference.saveData(baseActivity, SharedPreference.DELIVERY_CHARGE_KEY,
                                        response.body().getData().getUser().getDeliveryCost());

                                SharedPreference.saveData(baseActivity, SharedPreference.DELIVERY_TIME_KEY,
                                        response.body().getData().getUser().getDeliveryTime());

                                SharedPreference.saveData(baseActivity, SharedPreference.MIN_CHARGE_KEY,
                                        response.body().getData().getUser().getMinimumCharger());

                                SharedPreference.saveData(baseActivity, SharedPreference.WHATSAPP_KEY,
                                        response.body().getData().getUser().getWhatsapp());

                                SharedPreference.saveData(baseActivity, SharedPreference.STATUS_KEY,
                                        response.body().getData().getUser().getAvailability());

                            }
                        }



                        Toast.makeText(baseActivity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(baseActivity, HomeActivity.class));

                    } else {
                        Toast.makeText(baseActivity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {

                Toast.makeText(baseActivity,
                        baseActivity.getString(R.string.default_response_no_internet_connection),
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
}
