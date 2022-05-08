package com.example.weatherforecast.Network

import com.example.weatherforecast.Model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("onecall")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float,
        @Query("lang") language: String,
        @Query("units") measurementUnit: String
    ): Response<WeatherModel>
}