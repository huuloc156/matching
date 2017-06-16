package com.rentracks.matching.activity.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rentracks.matching.R;
import com.rentracks.matching.listener.ListenResult;

/**
 * Created by HuuLoc on 6/5/17.
 */

public class GoogleSignInActivity implements GoogleApiClient.OnConnectionFailedListener{

    /*
   *
   * Login with google
   *
   * */
    LoginActivity mContext;
    ListenResult mListener;
    /*
    * init
    * */
    public GoogleSignInActivity(LoginActivity activity, ListenResult l){
        mContext = activity;
        mListener = l;
        google_init();
    }
    GoogleApiClient mGoogleApiClient;
    int RC_SIGN_IN = 157;


    private void google_init(){

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(mContext /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) mContext.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mContext.startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mListener.Fail( connectionResult);
    }
}
