package com.lme.iudapp.api;

import com.lme.iudapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IudApiInterface {

    String API_BASE_URL = "http://hello-world.innocv.com/";

    @GET("/api/user/getall")
    Call<List<User>> getAllusers();

    @GET("/api/user/get/")
    Call<User> getUser(@Query(value = "id") int id);

    @POST("/api/user/create")
    Call<User> createUser(@Query(value = "id") User user);

    @POST("/api/user/update")
    Call<User> updateUser(@Query(value = "id") User user);

    @GET("/api/user/remove/")
    Call<Void> removeUser(@Query(value = "id") int id);
}
