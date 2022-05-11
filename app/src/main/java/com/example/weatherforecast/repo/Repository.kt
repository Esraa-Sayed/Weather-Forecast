package com.example.weatherforecast.repo

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.Model.SharedPrefrencesDataClass
import com.example.weatherforecast.Model.WeatherModel
import com.example.weatherforecast.Network.RemoteSource
import com.example.weatherforecast.R
import com.example.weatherforecast.db.LocalSource
import com.google.android.gms.location.*
import retrofit2.Response
import java.io.IOException
import java.util.*

class Repository private constructor(var remoteSource: RemoteSource?, var localSource: LocalSource?, var context: Context): RepositoryInterface {
    private lateinit var  fusedLocationProviderClient: FusedLocationProviderClient
    private var longitude:Float = 0.0f;
    private var latitude:Float = 0.0f;
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    companion object{
        private var instance: Repository? = null
        fun getInstance(remoteSource: RemoteSource?,localSource: LocalSource?, context: Context,): Repository {
            return instance ?: Repository(remoteSource,localSource,context)
        }
    }
    override fun writeSettingDataInPreferencesForFirstTime() {
        getCurrentLocation()
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(SharedPrefrencesKeys.windSpeed, "Meter/Sec")
        editor.putString(SharedPrefrencesKeys.temperature, "Celsius")
        editor.putString(SharedPrefrencesKeys.language,getCurrentDeviceLanguage())
        editor.putFloat(SharedPrefrencesKeys.longitude,longitude)
        editor.putFloat(SharedPrefrencesKeys.latitude,latitude)
        editor.putString(SharedPrefrencesKeys.locationState,"GPS")
        editor.putBoolean(SharedPrefrencesKeys.notification,true)
        editor.putBoolean(SharedPrefrencesKeys.isNotFirstTime,true)
        editor.commit()

    }
    override fun readBooleanFromSharedPreferences(dataNeed: String):Boolean{
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)

        return preferences.getBoolean(dataNeed, false)
    }
    override fun readFloatFromSharedPreferences(dataNeed: String):Float{
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)

        return preferences.getFloat(dataNeed, 0.0f)
    }
    override fun readStringFromSharedPreferences(dataNeed:String): String {
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)

        return preferences.getString(dataNeed, "notFound").toString()
    }
    override fun getDataFromSharedPrefrences(): SharedPrefrencesDataClass {
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        val windSpeed = preferences.getString(SharedPrefrencesKeys.windSpeed, "notFound").toString()
        val temp = preferences.getString(SharedPrefrencesKeys.temperature, "notFound").toString()
        val locationState = preferences.getString(SharedPrefrencesKeys.locationState, "notFound").toString()
        val lang = preferences.getString(SharedPrefrencesKeys.language,getCurrentDeviceLanguage()).toString()
        var notification = preferences.getBoolean(SharedPrefrencesKeys.notification,true)
        var data = SharedPrefrencesDataClass(locationState,windSpeed,temp,lang,notification)
        return data
    }
    override fun setStringToSharedPrefrences(key:String, value:String){
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.commit()
    }
    override fun setFloatToSharedPrefrences(key:String, value:Float){
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putFloat(key, value)
        editor.commit()
    }
    override fun setBoolToSharedPrefrences(key:String, value:Boolean){
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }
    override fun getAppSharedPrefrences():SharedPreferences{
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        return preferences
    }
    override fun isLocationSet():Boolean{
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        return preferences.getFloat(SharedPrefrencesKeys.latitude, 0.0f) != 0.0f
    }
    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient( context)
        locationRequest = LocationRequest.create().apply {
            interval = 100
            fastestInterval = 50
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

                getCityName()
                Log.e("Location222", "getCurrentLocation: ${location.longitude}  , ${location.latitude}" )
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,
            Looper.getMainLooper())
    }

    private fun getCityName(){

        val geocoderEn = Geocoder(context, Locale("en"))
        val geocoderAr = Geocoder(context, Locale("ar"))
        var cityNameEn = ""
        var cityNameAr = ""
        try {
            val addressesEn = geocoderEn.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1)
            val addressesAr = geocoderAr.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1)

            cityNameEn = addressesEn[0].adminArea
            cityNameAr = addressesAr[0].adminArea
            val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString(SharedPrefrencesKeys.cityEnglish,cityNameEn)
            editor.putString(SharedPrefrencesKeys.cityArabic,cityNameAr)
            editor.putFloat(SharedPrefrencesKeys.longitude,longitude)
            editor.putFloat(SharedPrefrencesKeys.latitude,latitude)
            editor.commit()

            Log.e("locationCity", "getCityName: $cityNameEn ar: $cityNameAr" )
        } catch (e: IOException) {
            Log.e("Error", "getCityName: ${e.localizedMessage}")
        }
    }
    private fun getCurrentDeviceLanguage():String{
        var currentlanguage = Locale.getDefault().getDisplayLanguage()
        if(currentlanguage.equals("العربية"))
        {
            currentlanguage = "ar"
        }
        else{
            currentlanguage = "en"
        }
        return currentlanguage
    }

    override suspend fun getCurrentWeatherOverNetwork(): Response<WeatherModel> {
        var latitude = readFloatFromSharedPreferences(SharedPrefrencesKeys.latitude)
        var longitude = readFloatFromSharedPreferences(SharedPrefrencesKeys.longitude)
        var language = readStringFromSharedPreferences(SharedPrefrencesKeys.language)
        var measurementUnit = ""
        var tempreture = readStringFromSharedPreferences(SharedPrefrencesKeys.temperature)
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
}