package com.example.weatherforecast.Network

import com.example.day3.Network.RetrofitHelper
import com.example.weatherforecast.Model.WeatherModel
import retrofit2.Response

class WeatherClient private constructor(): RemoteSource {

    companion object{
        private var instance: WeatherClient? = null
        fun getInstance(): WeatherClient {
            return instance ?: WeatherClient()
        }
    }

    override suspend fun getCurrentWeatherOverNetwork(latitude: Float, longitude: Float, language: String, measurementUnit: String): Response<WeatherModel> {
        return RetrofitHelper.getInstance().create(WeatherService::class.java).getCurrentWeather(latitude, longitude, language, measurementUnit)
    }
}