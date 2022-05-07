package com.example.weatherforecast.Model

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Looper
import android.util.Log
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.google.android.gms.location.*
import java.io.IOException
import java.util.*

class Repository private constructor(var context: Context):RepositoryInterface{
    private lateinit var  fusedLocationProviderClient: FusedLocationProviderClient
    private var longitude:Float = 0.0f;
    private var latitude:Float = 0.0f;
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private  var cityName = ""
    companion object{
        private var instance: Repository? = null
        fun getInstance(context: Context): Repository {
            return instance ?: Repository(context)
        }
    }
    override fun writeSettingDataInPreferencesForFirstTime() {
        getCurrentLocation()
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(SharedPrefrencesKeys.windSpeed, "Meter/Sec")
        editor.putString(SharedPrefrencesKeys.temperature, "Celsius")
        editor.putString(SharedPrefrencesKeys.language,getCurrentLanguage())
        editor.putString(SharedPrefrencesKeys.city,cityName)
        editor.putFloat(SharedPrefrencesKeys.longitude,longitude)
        editor.putFloat(SharedPrefrencesKeys.latitude,latitude)
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
    override fun getDataFromSharedPrefrences():SharedPrefrencesDataClass{
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        val windSpeed = preferences.getString(SharedPrefrencesKeys.windSpeed, "Meter/Sec").toString()
        val temp = preferences.getString(SharedPrefrencesKeys.temperature, "Celsius").toString()
        val lang = preferences.getString(SharedPrefrencesKeys.language,getCurrentLanguage()).toString()
        var notification = preferences.getBoolean(SharedPrefrencesKeys.notification,true)
        var data = SharedPrefrencesDataClass(windSpeed,temp,lang,notification)
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

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(){
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

        val geocoder = Geocoder(context, Locale("en"))

        try {
            val addresses = geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1)

            cityName = addresses[0].adminArea
            Log.e("locationCity", "getCityName: $cityName" )
        } catch (e: IOException) {
            Log.e("Error", "getCityName: ${e.localizedMessage}")
        }
    }
    private fun getCurrentLanguage():String{
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
}