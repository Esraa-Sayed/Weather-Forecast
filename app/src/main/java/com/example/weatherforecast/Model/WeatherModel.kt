package com.example.weatherforecast.Model

data class WeatherModel(
                        val lat:Double,
                        val lon:Double,
                        val alerts: List<Alert>,
                        val current: Current,
                        val daily: List<Daily>,
                        val hourly: List<Hourly>,
                         )
