package com.example.weatherforecast.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherforecast.Model.WeatherModel

@Dao
interface WeatherModelDAO {
    @get:Query("Select * from WeatherModel")
    val getWeatherModel: LiveData<List<WeatherModel>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherModel(weatherModel: WeatherModel)
    @Delete
    fun deleteWeatherModel(weatherModel:WeatherModel)
}