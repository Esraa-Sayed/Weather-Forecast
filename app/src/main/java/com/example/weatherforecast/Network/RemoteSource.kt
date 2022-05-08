package com.example.weatherforecast.Network

import com.example.weatherforecast.Model.WeatherModel
import retrofit2.Response

interface RemoteSource {
    suspend fun getCurrentWeatherOverNetwork(latitude: Float, longitude: Float, language: String, measurementUnit: String): Response<WeatherModel>
}