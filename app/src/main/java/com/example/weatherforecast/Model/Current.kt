package com.example.weatherforecast.Model

import com.example.weatherforecast.R
import com.google.gson.annotations.SerializedName

data class Current(
    val clouds: Int,
    val visibility: Int,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    val uvi: Double,
    val humidity: Int,
    val pressure: Int,

    val dt: Int,
    val temp: Double,
    val weather: List<Weather>,
)