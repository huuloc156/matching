package com.rentracks.matching.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.rentracks.matching.R;
import com.rentracks.matching.activity.BaseActivity;
import com.rentracks.matching.activity.MainActivity;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.dto.ObjectDto;
import com.rentracks.matching.data.api.dto.login.GetLoginData;
import com.rentracks.matching.listener.ListenResult;
import com.rentracks.matching.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;


public class LoginActivity extends BaseActivity{
    @BindView(R.id.userName)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.txt_forget_password)
    TextView txtForgetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        facebook_init();
        google_init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_Login)
    public void loginActivity()
    {
        boolean check=true;
        if(password.getText().toString().length()< 4)
        {
            ToastUtils.show(LoginActivity.this, "Password is too short");
            check=false;
        }
        if(password.getText().toString().length()>12 )
        {
            ToastUtils.show(LoginActivity.this, "Password is too long");
            check=false;
        }
        if( !android.util.Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches())
        {
            ToastUtils.show(LoginActivity.this, "Wrong email");
            check=false;
        }
        if(check) {
//            hide keyboard
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

//            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                    InputMethodManager.HIDE_NOT_ALWAYS);
            callApiLogin();
        }

    }
    @OnClick(R.id.btnLoginRegister)
    public void register(){
        startActivity(new Intent(this, RegistrationActivity.class));
//            finish();
    }

    public void init(){
        SpannableString content = new SpannableString(getString(R.string.if_you_forget_password));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtForgetPass.setText(content);
    }
    @OnClick(R.id.txt_forget_password)
    public void forgetPass(){
        startActivity(new Intent(this, ForgetPassActivity.class));
    }
    /*
    *
    * Login with facebook
    *
    * */
    CallbackManager callbackManager;
    public void facebook_init(){
        LoginWithFacebook mFacebook = new LoginWithFacebook(this, new ListenResult() {
            @Override
            public void Success(Object data) {
                String accesstoken = (String) data;
                callApiLoginFacebook(accesstoken);
            }

            @Override
            public void Fail(Object data) {

            }
        });
        callbackManager = mFacebook.getCallbackManager();
    }


    /*
    *
    * Login with google
    *
    * */
    GoogleSignInActivity loginGoogle;
    private void google_init(){
        loginGoogle = new GoogleSignInActivity(this, new ListenResult() {
            @Override
            public void Success(Object data) {
            }

            @Override
            public void Fail(Object data) {

            }
        });
    }
    private void handleSignInResult(GoogleSignInResult result) {

        /*if login ok but result.isSuccess = false
        * get sha1 from android studio
        * --> add sha-1 tren web firebase
        * https://console.firebase.google.com/project/matching-39679/settings/general/android:com.rentracks.matching
        *
        *
        *
            C:\Program Files (x86)\Java\jre1.8.0_131\bin

             keytool -list -v -keystore D:\android\projects\matching\keystore_tu_tao.jks -alias key0
        * */

        Log.d("", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String accesstoken;
            accesstoken = acct.getIdToken();
            sharePreferenceData.setUserNameSocila(acct.getDisplayName());
            Log.d("", "accesstoken:" + accesstoken);
            callApiLoginGoogle(accesstoken);
        } else {
            // Signed out, show unauthenticated UI.
        }
    }


    /*
    *
    * Handle result
    *
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == loginGoogle.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }

    }

    public void callApiLoginFacebook(String token) {
        Observable<ObjectDto<GetLoginData>> objectDtoObservable = matchingApi.loginFacebook();
        callAPILoginSocial(token, objectDtoObservable);
    }

    public void callApiLoginGoogle(String token) {
        Observable<ObjectDto<GetLoginData>> objectDtoObservable = matchingApi.loginGoogle();
        callAPILoginSocial(token, objectDtoObservable);
    }

    private void callAPILoginSocial(String token, Observable<ObjectDto<GetLoginData>> objectDtoObservable){

        sharePreferenceData.setUserToken(token);
        showProgressDialog();
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto<GetLoginData>>(this, true) {

            @Override
            protected void onDataError(ObjectDto<GetLoginData> registrationItemObjectDto) {
                closeDialog();
            }

            @Override
            public void onDataSuccess(ObjectDto<GetLoginData> registrationItemObjectDto) {
                closeDialog();
                loginSuccess(registrationItemObjectDto);
            }


            @Override
            public void onCompleted() {
                closeDialog();
            }
        });
    }

    public void callApiLogin() {
        showProgressDialog();

        Observable<ObjectDto<GetLoginData>> objectDtoObservable = matchingApi.loginActivity(
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
                loginSuccess(registrationItemObjectDto);
            }

            @Override
            public void onCompleted() {
                closeDialog();
            }
        });
    }

    private void loginSuccess(ObjectDto<GetLoginData> registrationItemObjectDto){
        GetLoginData data = registrationItemObjectDto.data;
        sharePreferenceData.setUserToken(data.token);
        sharePreferenceData.setUserId(data.user_id);
        ToastUtils.show(LoginActivity.this,"Login successful");
        //TODO xu ly logic thanh cong
        Intent main = new Intent(getBaseContext(), MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(main);
        finish();
    }

}
