package com.example.sofra.view.activity;

import android.os.Bundle;

import com.example.sofra.R;

import com.example.sofra.view.fragment.authcycle.general.LoginFragment;

public class AuthActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_auth_frame,new LoginFragment())
                .commit();

    }
}
