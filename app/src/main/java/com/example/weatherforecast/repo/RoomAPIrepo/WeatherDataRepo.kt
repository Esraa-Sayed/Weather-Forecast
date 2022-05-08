package com.example.weatherforecast.repo.RoomAPIrepo

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.Constants.APIRequest
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.Model.WeatherModel
import com.example.weatherforecast.Network.RemoteSource
import com.example.weatherforecast.R
import com.example.weatherforecast.repo.Repository
import retrofit2.Response

class WeatherDataRepo private constructor(var remoteSource: RemoteSource): WeatherDataRepoInterface{
    companion object {

        private var instance: WeatherDataRepo? = null
        fun getInstance(remoteSource: RemoteSource): WeatherDataRepo {
            return instance ?: WeatherDataRepo(remoteSource)
        }
    }
    override suspend fun getCurrentWeatherOverNetwork(context: Context): Response<WeatherModel> {

        var repo = Repository.getInstance(context)
        var latitude = repo.readFloatFromSharedPreferences(SharedPrefrencesKeys.latitude)
        var longitude = repo.readFloatFromSharedPreferences(SharedPrefrencesKeys.latitude)
        var language = repo.readStringFromSharedPreferences(SharedPrefrencesKeys.language)
        var measurementUnit = ""
        var tempreture = repo.readStringFromSharedPreferences(SharedPrefrencesKeys.temperature)
        when(tempreture){
            context.getString(R.string.celsius) -> measurementUnit = "metric"
            context.getString(R.string.fahrenheit)-> measurementUnit = "imperial"
            context.getString(R.string.kelvin)-> measurementUnit = "standard"
        }
        return remoteSource.getCurrentWeatherOverNetwork(latitude, longitude, language, measurementUnit)
    }
    override fun isNotFirstTime(context: Context):Boolean{
        var repo = Repository.getInstance(context)
       return repo.readBooleanFromSharedPreferences(SharedPrefrencesKeys.isNotFirstTime)

    }


}