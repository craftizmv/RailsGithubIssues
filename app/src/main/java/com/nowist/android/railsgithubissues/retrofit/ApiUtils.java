package com.nowist.android.railsgithubissues.retrofit;

/**
 * Created by mayankverma on 09/07/17.
 */

public class ApiUtils {
    public static final String BASE_URL = "https://api.github.com/repos/rails/rails/";

    public static RestApiService getApiService() {
        return RetrofitClient.getClient(BASE_URL).create(RestApiService.class);
    }

    public static RestApiService getApiService(String customUrl) {
        return RetrofitClient.getClient(customUrl).create(RestApiService.class);
    }
}
