package com.example.weatherforecast.Model

import com.google.gson.annotations.SerializedName

data class Daily(
    val dt: Int,
    val temp: Temp,
    val weather: List<Weather>,
)