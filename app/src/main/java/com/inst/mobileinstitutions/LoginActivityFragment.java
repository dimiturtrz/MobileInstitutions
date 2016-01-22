package com.inst.mobileinstitutions;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.content.pm.Signature;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {

    private TextView textView;
    private CallbackManager mCallbackManager;
    private Profile profile;
    private AccessTokenTracker tokenTracker;
    private ProfileTracker profileTracker;

    public LoginActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.inst.mobileinstitutions",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
        mCallbackManager = CallbackManager.Factory.create();
        tokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken1) {

            }
        };
        profileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
                textView.setText(displayMessage(profile1));
            }
        };
        tokenTracker.startTracking();
        profileTracker.startTracking();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView=(TextView)view.findViewById(R.id.text);
        textView.setText("You are not logged in");
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setCompoundDrawables(null, null, null, null);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, mFacebookCallback);
    }

    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            profile = Profile.getCurrentProfile();
            textView.setText(displayMessage(profile));
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        textView.setText(displayMessage(profile));
    }

    @Override
    public void onStop() {
        super.onStop();
        profileTracker.stopTracking();
        tokenTracker.stopTracking();
    }

    private String displayMessage(Profile profile) {
        StringBuilder stringBuilder = new StringBuilder();
        if (profile != null) {
            stringBuilder.append("Logged In "+profile.getFirstName());
            Toast.makeText(getActivity(), "Start Playing with the data "+profile.getFirstName(), Toast.LENGTH_SHORT).show();
        }else{
            stringBuilder.append("You are not logged in");
        }
        return stringBuilder.toString();
    }

}