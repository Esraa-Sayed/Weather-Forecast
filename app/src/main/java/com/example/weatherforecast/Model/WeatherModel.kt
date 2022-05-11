package com.example.weatherforecast.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.Nullable

@Entity(tableName = "WeatherModel")
data class WeatherModel(
                        @PrimaryKey
                        @ColumnInfo(name = "timezone_offset")
                        val timezone_offset:Int,
                       @ColumnInfo(name = "lat")
                        val lat:Double,
                       @ColumnInfo(name = "lon")
                        val lon:Double,

                        @ColumnInfo(name = "alerts")
                        @Nullable
                        val alerts: List<Alert>? = emptyList(),
                       @ColumnInfo(name = "current")
                        val current: Current,
                       @ColumnInfo(name = "daily")
                        val daily: List<Daily>,
                       @ColumnInfo(name = "hourly")
                        val hourly: List<Hourly>,
                         )
