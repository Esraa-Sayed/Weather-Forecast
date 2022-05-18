package com.example.weatherforecast.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherforecast.Model.FavouriteModel
import com.example.weatherforecast.Model.UserAlerts
import com.example.weatherforecast.Model.WeatherModel

class ConcreteLocalSource(context: Context) : LocalSource {
    private val dao: WeatherModelDAO?
    private val favouriteDAO: FavouriteDAO?
    private val alertsUserDAO: AlertsUserDAO?
    override val allStoredWeatherModel: LiveData<List<WeatherModel>>
    override val allStoredFavouriteModel: LiveData<List<FavouriteModel>>
    override val allStoredUserAlerts: LiveData<List<UserAlerts>>

    init {
        val db: AppDataBase = AppDataBase.getInstance(context)
        dao = db.weatherModelDAO()
        favouriteDAO = db.favouriateModelDAO()
        alertsUserDAO= db.alertsUserDAO()
        allStoredWeatherModel= dao?.getWeatherModel!!
        allStoredFavouriteModel = favouriteDAO?.getFavouriteModel!!
        allStoredUserAlerts = alertsUserDAO?.getUserAlerts!!
    }

    override fun insertWeatherModel(weatherModel: WeatherModel) {
        dao?.insertWeatherModel(weatherModel)
    }

    override fun deleteWeatherModel(weatherModel: WeatherModel) {
        dao?.deleteWeatherModel(weatherModel)
    }
    override fun insertFavouriateModel(favouriteModel: FavouriteModel) {
       favouriteDAO?.insertFavouriteModel(favouriteModel)
    }

    override fun deleteFavouriateModelmovie(favouriteModel: FavouriteModel){
        favouriteDAO?.deleteFavouriteModel(favouriteModel)
    }

    override fun insertUserAlerts(userAlerts: UserAlerts):Long {
      return alertsUserDAO?.insertUserAlerts(userAlerts)!!
    }

    override fun deleteUserAlerts(userAlerts: UserAlerts) {
        alertsUserDAO?.deleteUserAlerts(userAlerts)
    }


}