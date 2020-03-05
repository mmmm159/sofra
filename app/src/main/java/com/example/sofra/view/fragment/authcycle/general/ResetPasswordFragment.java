package com.example.sofra.view.fragment.authcycle.general;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiService;
import com.example.sofra.data.api.RetrofitClient;
import com.example.sofra.data.model.general.reset.Reset;
import com.example.sofra.data.local.SharedPreference;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordFragment extends BaseFragment {


    @BindView(R.id.fragment_reset_password_edt_txt_code)
    EditText fragmentResetPasswordEdtTxtCode;
    @BindView(R.id.fragment_reset_password_edt_txt_password)
    EditText fragmentResetPasswordEdtTxtPassword;
    @BindView(R.id.fragment_reset_password_edt_txt_password_confirm)
    EditText fragmentResetPasswordEdtTxtPasswordConfirm;
    @BindView(R.id.fragment_reset_password_btn_send)
    Button fragmentResetPasswordBtnSend;
    private ApiService apiService;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        ButterKnife.bind(this, view);

        apiService = RetrofitClient.getClient().create(ApiService.class);
//        if (forgetPasswordFragment.code!=null) {
//         //   fragmentResetPasswordEdtTxtCode.setText(forgetPasswordFragment.code);
//
//        }
        return view;
    }

    @Override
    public void onBack() {

        Utils.replaceFragment(baseActivity.getSupportFragmentManager(),
                R.id.activity_auth_frame,new forgetPasswordFragment());
    }

    @OnClick(R.id.fragment_reset_password_btn_send)
    public void onViewClicked() {

        String code = fragmentResetPasswordEdtTxtCode.getText().toString();
        String password = fragmentResetPasswordEdtTxtPassword.getText().toString();
        String confirmPassword = fragmentResetPasswordEdtTxtPasswordConfirm.getText().toString();

        sendData(code,password,confirmPassword);

    }

    private void sendData(String code, String password, String confirmPassword) {

        int userType = SharedPreference.loadInt(baseActivity,SharedPreference.USER_TYPE_KEY);
        Call<Reset> call;
        switch (userType){
            case SharedPreference.USER_TYPE_BUY:
                call = apiService.newPasswordClient(code,password,confirmPassword);
                callWebService(call);
                break;
            case SharedPreference.USER_TYPE_sell:
                call = apiService.newPasswordRestaurant(code,password,confirmPassword);
                callWebService(call);
                break;
        }

    }

    private void callWebService(Call<Reset> call) {
        call.enqueue(new Callback<Reset>() {
            @Override
            public void onResponse(Call<Reset> call, Response<Reset> response) {
                try {
                  if (response.body().getStatus()==1) {

                        Toast.makeText(baseActivity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        Utils.replaceFragment(baseActivity.getSupportFragmentManager()
                        ,R.id.activity_auth_frame,new LoginFragment());
                  }
                    else {
                        Toast.makeText(baseActivity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(baseActivity,
                            baseActivity.getString(R.string.default_response_no_internet_connection),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reset> call, Throwable t) {

                Toast.makeText(baseActivity,
                        baseActivity.getString(R.string.default_response_no_internet_connection),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


}
