package com.hungryhackers.twitteruserinfo.network;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.internal.network.GuestAuthInterceptor;
import com.twitter.sdk.android.core.internal.network.OAuth1aInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by YourFather on 19-03-2017.
 */

public class ApiClient {
    private static Retrofit retrofit;
    private static ApiInterface apiInterface;
    public static ApiInterface getApiInterface(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.twitter.com/1.1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    // Twitter interceptor
                    .client(new OkHttpClient.Builder()
                            .addInterceptor(new GuestAuthInterceptor(TwitterCore.getInstance().getGuestSessionProvider()))
                            .build())
                    .build();

            apiInterface = retrofit.create(ApiInterface.class);
        }
        return apiInterface;
    }
}
