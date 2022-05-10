package com.example.weatherforecast.Model

data class Hourly(
    val dt: Int,
    val temp: Double,
    val weather: List<Weather>,
)