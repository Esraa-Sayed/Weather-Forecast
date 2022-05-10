package com.example.weatherforecast.Model

data class WeatherModel( val alerts: List<Alert>,
                         val current: Current,
                         val daily: List<Daily>,
                         val hourly: List<Hourly>,
                         )
