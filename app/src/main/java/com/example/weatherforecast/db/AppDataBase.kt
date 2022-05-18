package com.example.weatherforecast.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecast.Model.FavouriteModel
import com.example.weatherforecast.Model.UserAlerts

import com.example.weatherforecast.Model.WeatherModel

@Database(entities = [WeatherModel::class,FavouriteModel::class,UserAlerts::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDataBase : RoomDatabase(){

    abstract fun weatherModelDAO(): WeatherModelDAO?
    abstract fun favouriateModelDAO(): FavouriteDAO?
    abstract fun alertsUserDAO(): AlertsUserDAO?

    companion object {
        private var instance: AppDataBase? = null

        @Synchronized
        fun getInstance(context: Context): AppDataBase {
            return instance ?: Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, "Room")
                .build()
        }
    }
}