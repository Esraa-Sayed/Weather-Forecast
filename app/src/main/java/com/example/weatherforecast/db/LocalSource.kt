package com.example.weatherforecast.db

import androidx.lifecycle.LiveData
import com.example.weatherforecast.Model.WeatherModel

interface LocalSource {
    fun insertWeatherModel(movie: WeatherModel)
    fun deleteWeatherModel(movie: WeatherModel)
    val allStoredWeatherModel: LiveData<List<WeatherModel>>
}