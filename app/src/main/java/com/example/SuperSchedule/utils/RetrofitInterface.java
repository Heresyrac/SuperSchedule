package com.example.SuperSchedule.utils;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @GET("customsearch/v1")
    Call<SearchResponse> customSearch(@Query("key") String API_KEY,
                                      @Query("cx") String SEARCH_ID_cx,
                                      @Query("q") String keyword);

    @GET("api/place/autocomplete/json")
    Call<String> citySearch(@Query("key") String API_KEY,
                                      @Query("input") String keyword);

}