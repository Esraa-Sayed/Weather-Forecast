package com.example.weatherforecast.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherforecast.Model.FavouriteModel

@Dao
interface FavouriteDAO {
    @get:Query("Select * from FavouriteModel")
    val getFavouriteModel: LiveData<List<FavouriteModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavouriteModel(weatherModel: FavouriteModel)

    @Delete
    fun deleteFavouriteModel(weatherModel: FavouriteModel)
}