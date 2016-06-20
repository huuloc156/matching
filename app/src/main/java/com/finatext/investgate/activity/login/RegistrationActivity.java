package com.finatext.investgate.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.finatext.investgate.R;
import com.finatext.investgate.activity.BaseActivity;
import com.finatext.investgate.activity.MainActivity;
import com.finatext.investgate.data.api.ApiSubscriber;
import com.finatext.investgate.data.api.dto.ObjectDto;
import com.finatext.investgate.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

public class RegistrationActivity extends BaseActivity {

    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_password)
    EditText edtPasswordConfirmation;
    @BindView(R.id.edt_password_confirmation)
    EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    @OnClick(R.id.btn_main)
    public void openMain() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @OnClick(R.id.btn_register)
    public void callApiRegistration() {
        showProgressDialog();

        Observable<ObjectDto<RegistrationItem>> objectDtoObservable = investgateApi.signUpMail(
                edtEmail.getText().toString(),
                edtPassword.getText().toString(),
                edtPasswordConfirmation.getText().toString());

        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto<RegistrationItem>>(this, true) {

            @Override
            protected void onDataError(ObjectDto<RegistrationItem> registrationItemObjectDto) {
                closeDialog();
            }

            @Override
            public void onDataSuccess(ObjectDto<RegistrationItem> registrationItemObjectDto) {
                closeDialog();
                RegistrationItem data = registrationItemObjectDto.data;
                sharePreferenceData.setUserToken(data.token);
                sharePreferenceData.setUserId(data.user_id);
                //TODO xu ly logic thanh cong
            }
        });
    }
}