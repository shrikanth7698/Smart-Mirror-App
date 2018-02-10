package com.shrikanthravi.smartmirror.data.model.remote;

/**
 * Created by shrikanthravi on 11/12/17.
 */

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL1 = "https://api.openweathermap.org/";

    public static final String BASE_URL2 = "https://newsapi.org/v2/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL1).create(APIService.class);
    }

    public static APIService getAPIService2(){

        return RetrofitClient.getClient(BASE_URL2).create(APIService.class);
    }
}