package com.example.weatherforecast.Model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserAlerts")
data class UserAlerts(
                @PrimaryKey(autoGenerate = true)
                val id: Long? = null,
                @ColumnInfo(name = "startLongDate")
                var startLongDate:Long,
                @ColumnInfo(name = "endLongDate")
                var endLongDate:Long,
                @ColumnInfo(name = "alertOption")
                var alertOption:String
                )
