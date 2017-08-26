package com.hungryhackers.twitteruserinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.hungryhackers.twitteruserinfo.network.ApiClient;
import com.hungryhackers.twitteruserinfo.network.ApiInterface;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Twitter.initialize(this);

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<User> call = apiInterface.getTwitterUserInfo("codingninjashq");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    Toast.makeText(MainActivity.this, response.body().followersCount + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }
}
