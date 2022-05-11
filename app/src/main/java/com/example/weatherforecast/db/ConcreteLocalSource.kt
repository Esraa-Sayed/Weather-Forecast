package com.example.weatherforecast.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherforecast.Model.WeatherModel

class ConcreteLocalSource(context: Context) : LocalSource {
    private val dao: WeatherModelDAO?
    override val allStoredWeatherModel: LiveData<List<WeatherModel>>

    init {
        val db: AppDataBase = AppDataBase.getInstance(context)
        dao = db.weatherModelDAO()
        allStoredWeatherModel= dao?.getWeatherModel!!
    }

    override fun insertWeatherModel(weatherModel: WeatherModel) {
        dao?.insertWeatherModel(weatherModel)
    }

    override fun deleteWeatherModel(weatherModel: WeatherModel) {
        dao?.deleteWeatherModel(weatherModel)
    }
}