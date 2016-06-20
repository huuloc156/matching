package com.finatext.investgate.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.finatext.investgate.R;
import com.finatext.investgate.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class FirstStartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_start);
    }
    @OnClick(R.id.btn_start)
    public void clickStart(){
        startActivity(new Intent(this,RegistrationActivity.class));
    }
    @OnClick(R.id.btn_login)
    public void clickLogin(){
        startActivity(new Intent(this,LoginActivity.class));
    }
}
