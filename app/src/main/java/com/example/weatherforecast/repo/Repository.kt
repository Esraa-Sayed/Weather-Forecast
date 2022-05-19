package com.example.weatherforecast.repo

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.Constants.SharedPrefrencesKeys.getCityNameFromLatAndLong
import com.example.weatherforecast.Model.FavouriteModel
import com.example.weatherforecast.Model.SharedPrefrencesDataClass
import com.example.weatherforecast.Model.UserAlerts
import com.example.weatherforecast.Model.WeatherModel
import com.example.weatherforecast.Network.RemoteSource
import com.example.weatherforecast.R
import com.example.weatherforecast.SharedPref.SharedPref
import com.example.weatherforecast.db.LocalSource
import com.google.android.gms.location.*
import retrofit2.Response
import java.io.IOException
import java.util.*

class Repository private constructor(var remoteSource: RemoteSource?, var localSource: LocalSource?): RepositoryInterface {
    private lateinit var  fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var longitude: Float = 0.0f
    private var latitude: Float = 0.0f
    companion object{
        private var instance: Repository? = null
        fun getInstance(remoteSource: RemoteSource?,localSource: LocalSource?): Repository {
            return instance ?: Repository(remoteSource,localSource)
        }
    }
    override fun writeSettingDataInPreferencesForFirstTime(context: Context) {
        getCurrentLocation(context)
        SharedPref.getInstance(context).writeSettingDataInPreferencesForFirstTime()

    }
    override fun readBooleanFromSharedPreferences(dataNeed: String,context: Context):Boolean{

        return SharedPref.getInstance(context).readBooleanFromSharedPreferences(dataNeed)
    }
    override fun readFloatFromSharedPreferences(dataNeed: String, context: Context): Float {

        return SharedPref.getInstance(context).readFloatFromSharedPreferences(dataNeed)
    }
    override fun readStringFromSharedPreferences(dataNeed: String, context: Context): String {

        return SharedPref.getInstance(context).readStringFromSharedPreferences(dataNeed)
    }

    override fun setStringToSharedPrefrences(key: String, value: String, context: Context) {
        SharedPref.getInstance(context).setStringToSharedPrefrences(key,value)
    }
    override fun setFloatToSharedPrefrences(key: String, value: Float, context: Context) {
        SharedPref.getInstance(context).setFloatToSharedPrefrences(key,value)
    }
    override fun setBoolToSharedPrefrences(key: String, value: Boolean, context: Context) {
       SharedPref.getInstance(context).setBoolToSharedPrefrences(key,value)
    }
    override fun getAppSharedPrefrences(context: Context): SharedPreferences {
        return SharedPref.getInstance(context).getAppSharedPrefrences()
    }
    override fun isLocationSet(context: Context): Boolean {
        return SharedPref.getInstance(context).isLocationSet()
    }
    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(context: Context){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient( context)
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 5000
        }
        //setUplocationCallback
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                longitude = location.longitude.toFloat()
                latitude = location.latitude.toFloat()

                getCityName(context)
                Log.e("Location222", "getCurrentLocation: ${location.longitude}  , ${location.latitude}" )
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,
            Looper.getMainLooper())
    }

    private fun getCityName(context: Context){
        var cityNameEn = ""
        var cityNameAr = ""
        try {
            cityNameEn = getCityNameFromLatAndLong(context, "en", latitude.toDouble(), longitude.toDouble())
            cityNameAr = getCityNameFromLatAndLong(context, "ar", latitude.toDouble(), longitude.toDouble())
            saveCityNamesArabicAndEnglishInSharedPrefrences(cityNameEn,cityNameAr,context)

        } catch (e: IOException) {
            Log.e("Error", "getCityName: ${e.localizedMessage}")
        }
    }
    private fun saveCityNamesArabicAndEnglishInSharedPrefrences(cityNameEn: String, cityNameAr: String,context: Context) {
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(SharedPrefrencesKeys.cityEnglish,cityNameEn)
        editor.putString(SharedPrefrencesKeys.cityArabic,cityNameAr)
        editor.putFloat(SharedPrefrencesKeys.longitude,longitude)
        editor.putFloat(SharedPrefrencesKeys.latitude,latitude)
        editor.commit()
        Log.e("locationCity", "getCityName: $cityNameEn ar: $cityNameAr" )
    }


    override suspend fun getCurrentWeatherOverNetwork(context: Context): Response<WeatherModel> {
        var latitude = readFloatFromSharedPreferences(SharedPrefrencesKeys.latitude,context)
        var longitude = readFloatFromSharedPreferences(SharedPrefrencesKeys.longitude,context)
        var language = readStringFromSharedPreferences(SharedPrefrencesKeys.language,context)
        var measurementUnit = ""
        var tempreture = readStringFromSharedPreferences(SharedPrefrencesKeys.temperature,context)
        when(tempreture){
            context.getString(R.string.celsius) -> measurementUnit = "metric"
            context.getString(R.string.fahrenheit)-> measurementUnit = "imperial"
            context.getString(R.string.kelvin)-> measurementUnit = "standard"
        }
        return remoteSource!!.getCurrentWeatherOverNetwork(latitude, longitude, language, measurementUnit)
    }
    override suspend fun getFavWeatherOverNetwork(
        latitude: Float,
        longitude: Float,
        context: Context
    ): Response<WeatherModel> {
        var language = readStringFromSharedPreferences(SharedPrefrencesKeys.language,context)
        var measurementUnit = ""
        var tempreture = readStringFromSharedPreferences(SharedPrefrencesKeys.temperature,context)
        when(tempreture){
            context.getString(R.string.celsius) -> measurementUnit = "metric"
            context.getString(R.string.fahrenheit)-> measurementUnit = "imperial"
            context.getString(R.string.kelvin)-> measurementUnit = "standard"
        }
        return remoteSource!!.getCurrentWeatherOverNetwork(latitude, longitude, language, measurementUnit)
    }
    override val allStoredWeatherModel: LiveData<List<WeatherModel>>
        get() = localSource!!.allStoredWeatherModel

    override fun insertWeatherModel(weatherModel: WeatherModel) {
        localSource!!.insertWeatherModel(weatherModel)
    }

    override fun deleteWeatherModel(weatherModel: WeatherModel) {
        localSource!!.deleteWeatherModel(weatherModel)
    }

    override val allStoredFavouriteModel: LiveData<List<FavouriteModel>>
        get() = localSource!!.allStoredFavouriteModel

    override fun insertFavouriateModel(favouriteModel: FavouriteModel){
        localSource!!.insertFavouriateModel(favouriteModel)
    }
    override fun deleteFavouriateModel(favouriteModel: FavouriteModel){
        localSource!!.deleteFavouriateModelmovie(favouriteModel)
    }

    override val allStoredAlerts: LiveData<List<UserAlerts>>
        get() = localSource!!.allStoredUserAlerts

    override fun insertUserAlert(userAlert: UserAlerts):Long{
        return localSource!!.insertUserAlerts(userAlert)

    }

    override fun deleteUserAlert(userAlert: UserAlerts) {
        localSource!!.deleteUserAlerts(userAlert)
    }
}