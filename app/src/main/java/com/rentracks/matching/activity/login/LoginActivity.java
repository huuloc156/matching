package com.rentracks.matching.activity.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rentracks.matching.R;
import com.rentracks.matching.activity.BaseActivity;
import com.rentracks.matching.activity.MainActivity;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.dto.ObjectDto;
import com.rentracks.matching.data.api.dto.login.GetLoginData;
import com.rentracks.matching.utils.ToastUtils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;


public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener{
    @BindView(R.id.userName)
    EditText username;
    @BindView(R.id.password)
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
            callApiRegistration();
        }

    }
    @OnClick(R.id.btnLoginRegister)
    public void register(){
        startActivity(new Intent(this, RegistrationActivity.class));
//            finish();
    }

    public void callApiRegistration() {
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

    CallbackManager callbackManager;

    public void facebook_init(){
        //Initialize facebook
        FacebookSdk.sdkInitialize(getApplicationContext());


        callbackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager,  new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                String accesstoken = loginResult.getAccessToken().getToken();
                callApiLoginFacebook(accesstoken);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if(accessToken == null) {
                    LoginManager.getInstance().logInWithReadPermissions(getActivty(), Arrays.asList("public_profile", "email"));
                }else{
                    callApiLoginFacebook(accessToken.getToken());
                }
            }
        });

    }
    public Activity getActivty(){
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }


    }

    public void callApiLoginFacebook(String token) {

        Observable<ObjectDto<GetLoginData>> objectDtoObservable = matchingApi.loginFacebook();
        callAPILogin(token, objectDtoObservable);
    }
    private void callAPILogin(String token, Observable<ObjectDto<GetLoginData>> objectDtoObservable){

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
    private void loginSuccess(ObjectDto<GetLoginData> registrationItemObjectDto){
        GetLoginData data = registrationItemObjectDto.data;
        sharePreferenceData.setUserToken(data.token);
//                sharePreferenceData.setUserId(data.user_id);
        ToastUtils.show(LoginActivity.this,"Login successful");
        //TODO xu ly logic thanh cong
        Intent main = new Intent(getBaseContext(), MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(main);
        finish();
    }


    /*
    * google
    * */
    GoogleApiClient mGoogleApiClient;
    int RC_SIGN_IN = 157;


    private void google_init(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile().requestId().requestIdToken(getString(R.string.server_client_ID))
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,  this/* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String accesstoken = acct.getIdToken();
            Log.d("", "accesstoken:" + accesstoken);
            callApiLoginGoogle(accesstoken);
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void callApiLoginGoogle(String token) {

        Observable<ObjectDto<GetLoginData>> objectDtoObservable = matchingApi.loginGoogle();
        callAPILogin(token, objectDtoObservable);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
