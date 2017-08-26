package com.hungryhackers.twitteruserinfo;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hungryhackers.twitteruserinfo.network.ApiClient;
import com.hungryhackers.twitteruserinfo.network.ApiInterface;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.annotation.ElementType.PACKAGE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Twitter.initialize(this);
        generateHashkey();
        getTwitterUser("codingninjashq");

    }

    public void generateHashkey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.hungryhackers.twitteruserinfo",PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

//                ((TextView) findViewById(R.id.hashkey))
//                        .setText(Base64.encodeToString(md.digest(),
//                                Base64.NO_WRAP));
                Log.d("generateHashkey", Base64.encodeToString(md.digest(),
                        Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }

    public void getLinkedInUser(View view) {
        LISessionManager.getInstance(getApplicationContext())
                .init(this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {

                        Toast.makeText(getApplicationContext(), "success" +
                                        LISessionManager
                                                .getInstance(getApplicationContext())
                                                .getSession().getAccessToken().toString(),
                                Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onAuthError(LIAuthError error) {

                        Toast.makeText(getApplicationContext(), "failed "
                                        + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }, true);
    }

    // set the permission to retrieve basic information of User's linkedIn account
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        LISessionManager.getInstance(getApplicationContext())
                .onActivityResult(this,
                        requestCode, resultCode, data);
        linkededinApiHelper();

    }

    public void linkededinApiHelper() {

        final String host = "api.linkedin.com";
        final String url = "https://" + host
                + "/v1/people/~:" +
                "(email-address,formatted-name,phone-numbers,picture-urls::(original))";

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(MainActivity.this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    showResult(result.getResponseDataAsJson());
//                    progress.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {

            }
        });
    }

    public void showResult(JSONObject response) {

        try {
            Toast.makeText(this, response.get("emailAddress").toString(), Toast.LENGTH_SHORT).show();
//            user_name.setText(response.get("formattedName").toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTwitterUser(String screenName) {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<User> call = apiInterface.getTwitterUserInfo(screenName);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, response.body().followersCount + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
