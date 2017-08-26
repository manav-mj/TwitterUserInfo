package com.hungryhackers.twitteruserinfo.network;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by YourFather on 19-03-2017.
 */

public interface ApiInterface {

    @GET("users/show.json")
    Call<User> getTwitterUserInfo(@Query("screen_name") String screenName);
}
