package com.rentracks.matching.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.rentracks.matching.R;
import com.rentracks.matching.activity.BaseActivity;
import com.rentracks.matching.activity.MainActivity;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.dto.ObjectDto;
import com.rentracks.matching.data.api.dto.login.RegistrationItem;
import com.rentracks.matching.utils.ToastUtils;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
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
        //            hide keyboard
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

//        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);

        callApiRegistration(email, password, password_confirmation);
    }

    public void callApiRegistration(String email, String password, String password_confirmation) {
        showProgressDialog();

        Observable<ObjectDto<RegistrationItem>> objectDtoObservable = matchingApi.signUpMail(email, password);

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
//                sharePreferenceData.setUserId(data.user_id);
                //TODO xu ly logic thanh cong
                Intent main = new Intent(getBaseContext(), MainActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main);
                finish();
            }

            @Override
            public void onCompleted() {
                closeDialog();
            }
        });
    }
}