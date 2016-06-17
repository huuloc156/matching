package com.finatext.investgate.activity.login;

import android.content.Intent;
import android.os.Bundle;

import com.finatext.investgate.R;
import com.finatext.investgate.activity.BaseActivity;
import com.finatext.investgate.activity.MainActivity;

import butterknife.OnClick;

public class RegistrationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }
    @OnClick(R.id.btn_main)
    public void openMain(){
        startActivity(new Intent(this, MainActivity.class));
    }
}
