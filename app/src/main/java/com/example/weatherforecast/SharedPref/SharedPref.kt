package com.example.weatherforecast.SharedPref

import android.content.Context
import android.content.SharedPreferences
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.Model.SharedPrefrencesDataClass
import com.example.weatherforecast.Network.RemoteSource
import com.example.weatherforecast.db.LocalSource
import com.example.weatherforecast.repo.Repository
import java.util.*

class SharedPref private constructor(private var context: Context) {
    private var longitude: Float = 0.0f;
    private var latitude: Float = 0.0f;
    companion object{
        private var instance: SharedPref? = null
        fun getInstance(context: Context): SharedPref {
            return instance ?: SharedPref(context)
        }
    }
    fun writeSettingDataInPreferencesForFirstTime() {
        val preferences =
            context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(SharedPrefrencesKeys.windSpeed, "Meter/Sec")
        editor.putString(SharedPrefrencesKeys.temperature, "Celsius")
       // editor.putString(SharedPrefrencesKeys.language, getCurrentDeviceLanguage())
        editor.putFloat(SharedPrefrencesKeys.longitude, longitude)
        editor.putFloat(SharedPrefrencesKeys.latitude, latitude)
        editor.putString(SharedPrefrencesKeys.locationState, "GPS")
        editor.putBoolean(SharedPrefrencesKeys.notification, true)
        editor.putBoolean(SharedPrefrencesKeys.isNotFirstTime, true)
        editor.commit()

    }
   fun readBooleanFromSharedPreferences(dataNeed: String):Boolean{
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)

        return preferences.getBoolean(dataNeed, false)
    }
    fun readFloatFromSharedPreferences(dataNeed: String): Float {
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)

        return preferences.getFloat(dataNeed, 0.0f)
    }
    fun readStringFromSharedPreferences(dataNeed: String): String {
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        if(dataNeed == SharedPrefrencesKeys.language)
            return preferences.getString(dataNeed, getCurrentDeviceLanguage()).toString()
        return preferences.getString(dataNeed, "notFound").toString()
    }
    fun setStringToSharedPrefrences(key: String, value: String) {
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.commit()
    }
    fun setFloatToSharedPrefrences(key: String, value: Float) {
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putFloat(key, value)
        editor.commit()
    }
    fun setBoolToSharedPrefrences(key: String, value: Boolean) {
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }
    fun getAppSharedPrefrences(): SharedPreferences {
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        return preferences
    }
    fun isLocationSet(): Boolean {
        val preferences = context.getSharedPreferences(SharedPrefrencesKeys.preferenceFile, Context.MODE_PRIVATE)
        return preferences.getFloat(SharedPrefrencesKeys.latitude, 0.0f) != 0.0f
    }
    private fun getCurrentDeviceLanguage(): String {
        var currentlanguage = Locale.getDefault().getDisplayLanguage()
        if (currentlanguage.equals("العربية")) {
            currentlanguage = "ar"
        } else {
            currentlanguage = "en"
        }
        return currentlanguage
    }
}