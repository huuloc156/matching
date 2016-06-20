package com.finatext.investgate.activity.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.finatext.investgate.R;
import com.finatext.investgate.activity.BaseActivity;
import com.finatext.investgate.data.api.dto.login.GetLoginData;
import com.finatext.investgate.data.api.ApiSubscriber;
import com.finatext.investgate.data.api.dto.ObjectDto;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.userName)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.btnLogin)
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    @OnClick(R.id.btnLogin)
    public void callApiRegistration() {
        showProgressDialog();

        Observable<ObjectDto<GetLoginData>> objectDtoObservable = investgateApi.loginActivity(
                username.getText().toString(),
                password.getText().toString());
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto<GetLoginData>>(this, true) {

            @Override
            protected void onDataError(ObjectDto<GetLoginData> registrationItemObjectDto) {
                closeDialog();
            }

            @Override
            public void onDataSuccess(ObjectDto<GetLoginData> registrationItemObjectDto) {
                closeDialog();
                GetLoginData data = registrationItemObjectDto.data;
                sharePreferenceData.setUserToken(data.token);
                sharePreferenceData.setUserId(data.user_id);
                //TODO xu ly logic thanh cong
            }
        });
    }
}
