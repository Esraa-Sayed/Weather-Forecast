package com.example.weatherforecast.repo

import android.content.Context
import android.content.SharedPreferences
import com.example.weatherforecast.Model.SharedPrefrencesDataClass
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
}