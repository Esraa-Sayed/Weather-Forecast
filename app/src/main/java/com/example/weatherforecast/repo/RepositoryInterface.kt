package com.example.weatherforecast.repo

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.weatherforecast.Model.FavouriteModel
import com.example.weatherforecast.Model.SharedPrefrencesDataClass
import com.example.weatherforecast.Model.UserAlerts
import com.example.weatherforecast.Model.WeatherModel
import retrofit2.Response

interface RepositoryInterface {
    fun writeSettingDataInPreferencesForFirstTime()
    fun readBooleanFromSharedPreferences(dataNeed: String):Boolean
    fun readFloatFromSharedPreferences(dataNeed: String):Float
    fun readStringFromSharedPreferences(dataNeed:String): String
    fun setStringToSharedPrefrences(key:String,value:String)
    fun setFloatToSharedPrefrences(key:String,value:Float)
    fun setBoolToSharedPrefrences(key:String,value:Boolean)
    fun getDataFromSharedPrefrences(): SharedPrefrencesDataClass
    fun getAppSharedPrefrences(): SharedPreferences
    fun isLocationSet():Boolean

    suspend fun getCurrentWeatherOverNetwork(): Response<WeatherModel>
    suspend fun getFavWeatherOverNetwork(latitude:Float, longitude:Float): Response<WeatherModel>

    val allStoredWeatherModel: LiveData<List<WeatherModel>>
    fun insertWeatherModel(weatherModel: WeatherModel)
    fun getCurrentLocation()
    fun deleteWeatherModel(weatherModel: WeatherModel)

    val allStoredFavouriteModel: LiveData<List<FavouriteModel>>
    fun insertFavouriateModel(favouriteModel:FavouriteModel)
    fun deleteFavouriateModel(favouriteModel: FavouriteModel)

    val allStoredAlerts: LiveData<List<UserAlerts>>
    fun insertUserAlert(userAlerts:UserAlerts)
    fun deleteUserAlert(userAlert: UserAlerts)


}