package com.android.aqimonitor.Api;

import com.android.aqimonitor.models.AirData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by trikh on 05-04-2017.
 */

public interface ApiInterface {
    @GET("feed/{geo}/")
    Call<AirData> getAirData(@Path("geo") String geo, @Query("token") String key);
}
