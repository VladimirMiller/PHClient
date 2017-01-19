package com.example.user.rhclient.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ProductHuntApi {

    String DEV_TOKEN = "591f99547f569b05ba7d8777e2e0824eea16c440292cce1f8dfb3952cc9937ff";

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "Authorization: Bearer " + DEV_TOKEN,
            "Host: api.producthunt.com"
    })
    @GET("/v1/categories/{category}/posts")
    Call<JsonObject> getProducts(@Path("category") String category);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "Authorization: Bearer " + DEV_TOKEN,
            "Host: api.producthunt.com"
    })
    @GET("/v1/posts/{id}")
    Call<JsonObject> getProductById(@Path("id") int id);
}
