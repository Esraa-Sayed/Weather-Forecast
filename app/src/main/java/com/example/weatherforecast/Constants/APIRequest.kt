package com.example.weatherforecast.Constants

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

object APIRequest {
    const val API_ID = "05a9a9b5ac16ee922e76bd1ca539e793"
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
}