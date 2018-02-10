package com.shrikanthravi.smartmirror.data.model.remote;

import com.shrikanthravi.smartmirror.data.model.GoogleNewsIndia;
import com.shrikanthravi.smartmirror.data.model.WeatherDetails;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by shrikanthravi on 11/12/17.
 */


public interface APIService {

    @POST("/data/2.5/weather?q=chennai&units=metric&APPID=APIKEY")
    Call<WeatherDetails> weatherDetailsCall(@Body String request);

    @GET("top-headlines")
    Call<GoogleNewsIndia> googleNewsIndiaCall(@Query("sources") String source,
                                              @Query("apikey") String apikey);
}
