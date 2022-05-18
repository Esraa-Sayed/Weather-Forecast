package com.example.weatherforecast.Constants

import android.content.Context
import androidx.room.TypeConverter
import com.example.weatherforecast.Model.UserAlerts
import com.example.weatherforecast.R
import com.example.weatherforecast.WorkManager.Notification
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object WorkManagerConstants {
    const val ALERT: String = "my alert"
    const val DESCRIPTION: String = "description"
    const val ICON: String = "icon"
    const val FROM_TIME_IN_MILLIS: String = "fromTimeInMillis"
    @TypeConverter
    fun convertToUserAlert(value: String): UserAlerts {
        val type: Type = object : TypeToken<UserAlerts>() {}.type
        return Gson().fromJson(value, type)
    }
    @TypeConverter
    fun convertUserAlertToString(myAlert: UserAlerts): String = Gson().toJson(myAlert)
    fun openNotification(context: Context, myAlert:UserAlerts, description: String, icon: String, title: String) {
        val notificationHelper = Notification(context, description, icon, title)
        val nb = notificationHelper.getChannelNotification()
        notificationHelper.getManager()!!.notify(myAlert.id.hashCode(), nb.build())
    }
    fun getIcon(imageString: String): Int {
        val imageInInteger: Int
        when (imageString) {
            "01d" -> imageInInteger = R.drawable.icon_01d
            "01n" -> imageInInteger = R.drawable.icon_01n
            "02d" -> imageInInteger = R.drawable.icon_02d
            "02n" -> imageInInteger = R.drawable.icon_02n
            "03n" -> imageInInteger = R.drawable.icon_03n
            "03d" -> imageInInteger = R.drawable.icon_03d
            "04d" -> imageInInteger = R.drawable.icon_04d
            "04n" -> imageInInteger = R.drawable.icon_04n
            "09d" -> imageInInteger = R.drawable.icon_09d
            "09n" -> imageInInteger = R.drawable.icon_09n
            "10d" -> imageInInteger = R.drawable.icon_10d
            "10n" -> imageInInteger = R.drawable.icon_10n
            "11d" -> imageInInteger = R.drawable.icon_11d
            "11n" -> imageInInteger = R.drawable.icon_11n
            "13d" -> imageInInteger = R.drawable.icon_13d
            "13n" -> imageInInteger = R.drawable.icon_13n
            "50d" -> imageInInteger = R.drawable.icon_50d
            "50n" -> imageInInteger = R.drawable.icon_50n
            else -> imageInInteger = R.drawable.icon_50n
        }
        return imageInInteger
    }

}