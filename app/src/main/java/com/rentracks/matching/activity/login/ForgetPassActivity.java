package com.rentracks.matching.activity.login;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.rentracks.matching.R;
import com.rentracks.matching.activity.BaseActivity;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.dto.ObjectDto;
import com.rentracks.matching.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

public class ForgetPassActivity extends BaseActivity {

    @BindView(R.id.edt_email_forget)
    EditText edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);
    }

    @OnClick(R.id.btn_submit_forget_pass)
    public void forget_pass() {
        String email = edtEmail.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ToastUtils.show(ForgetPassActivity.this, "Wrong email");
            edtEmail.setText("");
            edtEmail.requestFocus();
            return;
        }

        //            hide keyboard
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

//        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);

        callApiRegistration(email);
    }

    public void callApiRegistration(String email) {
        showProgressDialog();

        Observable<ObjectDto> objectDtoObservable = matchingApi.forgetMail(email);

        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto>(this, true) {

            @Override
            protected void onDataError(ObjectDto data) {
                closeDialog();
            }

            @Override
            public void onDataSuccess(ObjectDto data) {
                closeDialog();
                showpopupStatus(true, data.message, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }

            @Override
            public void onCompleted() {
                closeDialog();
            }
        });
    }
}