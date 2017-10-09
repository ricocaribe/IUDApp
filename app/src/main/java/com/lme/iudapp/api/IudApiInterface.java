package com.lme.iudapp.api;

import com.jmricop.weatherapp.model.Cities;
import com.jmricop.weatherapp.model.Stations;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IudApiInterface {

    String BASE_URL = "http://api.geonames.org/";

    @GET("/searchJSON")
    Call<Cities> searchCity(
            @Query(value = "q") String query,
            @Query(value = "maxRows") int maxRows,
            @Query(value = "startRow") int startRow,
            @Query(value = "lang") String lang,
            @Query(value = "isNameRequired") String isNameRequired,
            @Query(value = "style") String style,
            @Query(value = "username") String username);

    @GET("/weatherJSON")
    Call<Stations> searchCityWeatherInfo(
            @Query(value = "north") double north,
            @Query(value = "south") double south,
            @Query(value = "east") double east,
            @Query(value = "west") double west,
            @Query(value = "username") String username);
}
