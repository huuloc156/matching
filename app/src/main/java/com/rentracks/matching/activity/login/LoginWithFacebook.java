package com.rentracks.matching.activity.login;

import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.rentracks.matching.R;
import com.rentracks.matching.listener.ListenResult;

import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by HuuLoc on 6/5/17.
 */

public class LoginWithFacebook {

    LoginActivity mContext;
    CallbackManager callbackManager;
    ListenResult mListener;

    public LoginWithFacebook(LoginActivity activity, ListenResult l) {
        mContext = activity;
        mListener = l;
        facebook_init();
    }

    public void facebook_init() {
        //Initialize facebook
        FacebookSdk.sdkInitialize(getApplicationContext());


        callbackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = (LoginButton) mContext.findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // App code
                String accesstoken = loginResult.getAccessToken().getToken();
                mListener.Success(accesstoken);
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
                if (accessToken == null) {
                    LoginManager.getInstance().logInWithReadPermissions(mContext, Arrays.asList("public_profile", "email"));
                } else {
                    mListener.Success(accessToken.getToken());
                }
            }
        });

    }
    public CallbackManager getCallbackManager(){
        return callbackManager;
    }
}
