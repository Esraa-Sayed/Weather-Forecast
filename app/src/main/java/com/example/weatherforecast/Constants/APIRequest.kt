package com.example.weatherforecast.Constants

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object APIRequest {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private const val IMG_URL: String = "https://openweathermap.org/img/wn/"
    @SuppressLint("SimpleDateFormat")
    fun getDateTime(dt: Int, pattern: String,language:String): String {
        val format = SimpleDateFormat(pattern,Locale(
                language))
        format.timeZone = TimeZone.getTimeZone("GMT+2")
        return format.format(Date(dt * 1000L))
    }
    fun setImageInView(context: Context,iconURL:String,imgViewCurrentWeatherIcon:ImageView){
        Glide
            .with(context)
            .load("$IMG_URL${iconURL}@4x.png")
            .into(imgViewCurrentWeatherIcon)
    }
    fun getFullAddress(latitude: Double, longitude: Double, language: String,context: Context): String{
        var geocoder = Geocoder(context, Locale(language))
        var allAddress = "Unknown"
        try{
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()){
                var city = addresses[0].adminArea
                var country = addresses[0].countryName
                allAddress = "$city,$country"
            }
        }catch (e: IOException){
            Log.e("TAG", "getFullAddress: ${e.message}" )
        }
        return allAddress
    }
}