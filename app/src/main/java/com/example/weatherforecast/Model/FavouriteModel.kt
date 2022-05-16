package com.example.weatherforecast.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavouriteModel")
data class FavouriteModel(
                            @ColumnInfo(name = "latitude")
                            var latitude:Double,
                            @ColumnInfo(name = "longitude")
                           var longitude:Double,
                            @ColumnInfo(name = "addressAr")
                           var addressAr:String,
                            @PrimaryKey
                            @ColumnInfo(name = "addressEn")
                           var addressEn:String)