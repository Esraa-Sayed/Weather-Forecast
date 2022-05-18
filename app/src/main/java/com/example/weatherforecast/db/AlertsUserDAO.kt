package com.example.weatherforecast.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherforecast.Model.FavouriteModel
import com.example.weatherforecast.Model.UserAlerts

@Dao
interface AlertsUserDAO {
    @get:Query("Select * from UserAlerts")
    val getUserAlerts: LiveData<List<UserAlerts>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserAlerts(userAlerts: UserAlerts):Long

    @Delete
    fun deleteUserAlerts(userAlerts: UserAlerts)
}