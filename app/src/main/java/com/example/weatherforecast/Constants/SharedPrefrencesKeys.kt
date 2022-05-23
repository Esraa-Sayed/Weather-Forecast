package com.example.weatherforecast.Constants

import android.content.Context
import android.location.Geocoder
import com.example.weatherforecast.R
import java.util.*

object SharedPrefrencesKeys {
    const val windSpeed = "windSpeed"
    const val temperature = "temperature"
    const val language = "language"
    const val cityEnglish = "cityEnglish"
    const val cityArabic = "cityArabic"
    const val longitude = "longitude"
    const val latitude = "latitude"
    const val notification = "notification"
    const val isNotFirstTime = "isNotFirstTime"
    const val preferenceFile = "SettingData"
    const val locationState = "locationState"
   fun getCityNameFromLatAndLong(context: Context, language:String, latitude:Double, longitude:Double):String{
       val geocoder = Geocoder(context, Locale(language))
       val addresses = geocoder.getFromLocation(latitude, longitude, 1)
       if (!addresses.isNullOrEmpty() && !addresses[0].adminArea.isNullOrEmpty()){

           return addresses[0].adminArea
       }
       return context.getString(R.string.Unknown_city)
   }
}