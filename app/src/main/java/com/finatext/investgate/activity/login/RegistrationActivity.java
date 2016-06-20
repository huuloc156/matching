package com.finatext.investgate.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;

import com.finatext.investgate.R;
import com.finatext.investgate.activity.BaseActivity;
import com.finatext.investgate.activity.MainActivity;
import com.finatext.investgate.data.api.ApiSubscriber;
import com.finatext.investgate.data.api.dto.ObjectDto;
import com.finatext.investgate.data.api.dto.login.RegistrationItem;
import com.finatext.investgate.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

public class RegistrationActivity extends BaseActivity {

    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.edt_password_confirmation)
    EditText edtPasswordConfirmation;

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
    public void register() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String password_confirmation = edtPasswordConfirmation.getText().toString();
        int password_lenght = password.length();
        int password_confirmation_lenght = password_confirmation.length();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ToastUtils.show(RegistrationActivity.this, "Wrong email");
            edtEmail.setText("");
            edtEmail.requestFocus();
            return;
        }

        if (!(password_lenght >= 6
                && password_confirmation_lenght >= 6
                && password_lenght <= 12
                && password_confirmation_lenght <= 12)) {
            ToastUtils.show(RegistrationActivity.this, "Password length must be between in 6 and 12");
            edtPassword.setText("");
            edtPasswordConfirmation.setText("");
            edtPassword.requestFocus();
            return;
        }

        callApiRegistration(email, password, password_confirmation);
    }

    public void callApiRegistration(String email, String password, String password_confirmation) {
        showProgressDialog();

        Observable<ObjectDto<RegistrationItem>> objectDtoObservable = investgateApi.signUpMail(email, password, password_confirmation);

        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto<RegistrationItem>>(this, true) {

            @Override
            protected void onDataError(ObjectDto<RegistrationItem> registrationItemObjectDto) {
                closeDialog();
            }

            @Override
            public void onDataSuccess(ObjectDto<RegistrationItem> registrationItemObjectDto) {
                closeDialog();
                ToastUtils.show(RegistrationActivity.this, "Registration successful");
                RegistrationItem data = registrationItemObjectDto.data;
                sharePreferenceData.setUserToken(data.token);
                sharePreferenceData.setUserId(data.user_id);
                //TODO xu ly logic thanh cong
            }

            @Override
            public void onCompleted() {
                closeDialog();
            }
        });
    }
}