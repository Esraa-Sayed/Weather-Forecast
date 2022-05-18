package com.example.weatherforecast.repo

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.weatherforecast.Model.FavouriteModel
import com.example.weatherforecast.Model.SharedPrefrencesDataClass
import com.example.weatherforecast.Model.UserAlerts
import com.example.weatherforecast.Model.WeatherModel
import retrofit2.Response

interface RepositoryInterface {
    fun writeSettingDataInPreferencesForFirstTime(context: Context)
    fun readBooleanFromSharedPreferences(dataNeed: String,context: Context):Boolean
    fun readFloatFromSharedPreferences(dataNeed: String,context: Context):Float
    fun readStringFromSharedPreferences(dataNeed:String,context: Context): String
    fun setStringToSharedPrefrences(key:String,value:String,context: Context)
    fun setFloatToSharedPrefrences(key:String,value:Float,context: Context)
    fun setBoolToSharedPrefrences(key:String,value:Boolean,context: Context)
    fun getAppSharedPrefrences(context: Context): SharedPreferences
    fun isLocationSet(context: Context):Boolean

    suspend fun getCurrentWeatherOverNetwork(context: Context): Response<WeatherModel>
    suspend fun getFavWeatherOverNetwork(latitude:Float, longitude:Float,context: Context): Response<WeatherModel>

    val allStoredWeatherModel: LiveData<List<WeatherModel>>
    fun insertWeatherModel(weatherModel: WeatherModel)
    fun getCurrentLocation(context: Context)
    fun deleteWeatherModel(weatherModel: WeatherModel)

    val allStoredFavouriteModel: LiveData<List<FavouriteModel>>
    fun insertFavouriateModel(favouriteModel:FavouriteModel)
    fun deleteFavouriateModel(favouriteModel: FavouriteModel)

    val allStoredAlerts: LiveData<List<UserAlerts>>
    fun insertUserAlert(userAlerts:UserAlerts):Long
    fun deleteUserAlert(userAlert: UserAlerts)



}