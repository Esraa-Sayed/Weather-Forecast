package com.example.weatherforecast.repo.RoomAPIrepo

import android.content.Context
import android.content.SharedPreferences
import com.example.weatherforecast.Model.WeatherModel
import retrofit2.Response

interface WeatherDataRepoInterface {
    suspend fun getCurrentWeatherOverNetwork(): Response<WeatherModel>
    fun getAppSharedPrefrences():SharedPreferences
    fun isLocationSet():Boolean
}