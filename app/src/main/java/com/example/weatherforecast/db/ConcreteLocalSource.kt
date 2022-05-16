package com.example.weatherforecast.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherforecast.Model.FavouriteModel
import com.example.weatherforecast.Model.WeatherModel

class ConcreteLocalSource(context: Context) : LocalSource {
    private val dao: WeatherModelDAO?
    private val favouriteDAO: FavouriteDAO?
    override val allStoredWeatherModel: LiveData<List<WeatherModel>>
    override val allStoredFavouriteModel: LiveData<List<FavouriteModel>>

    init {
        val db: AppDataBase = AppDataBase.getInstance(context)
        dao = db.weatherModelDAO()
        favouriteDAO = db.favouriateModelDAO()
        allStoredWeatherModel= dao?.getWeatherModel!!
        allStoredFavouriteModel = favouriteDAO?.getFavouriteModel!!
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

}