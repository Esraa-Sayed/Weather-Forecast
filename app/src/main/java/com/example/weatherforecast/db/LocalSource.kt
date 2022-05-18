package com.example.weatherforecast.db

import androidx.lifecycle.LiveData
import com.example.weatherforecast.Model.FavouriteModel
import com.example.weatherforecast.Model.UserAlerts
import com.example.weatherforecast.Model.WeatherModel

interface LocalSource {
    fun insertWeatherModel(weatherModel: WeatherModel)
    fun deleteWeatherModel(weatherModel: WeatherModel)
    val allStoredWeatherModel: LiveData<List<WeatherModel>>

    fun insertFavouriateModel(favouriteModel: FavouriteModel)
    fun deleteFavouriateModelmovie(favouriteModel: FavouriteModel)
    val allStoredFavouriteModel: LiveData<List<FavouriteModel>>

    fun insertUserAlerts(userAlerts: UserAlerts):Long
    fun deleteUserAlerts(userAlerts: UserAlerts)
    val allStoredUserAlerts: LiveData<List<UserAlerts>>
}