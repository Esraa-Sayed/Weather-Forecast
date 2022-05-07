package com.example.weatherforecast.DataStorage

import android.content.Context

class SharedPrefrencesModel private constructor(var context: Context) {
    companion object{
        const val preferenceFile = "SettingData"
        private var instance: SharedPrefrencesModel? = null
        fun getInstance(context: Context): SharedPrefrencesModel{
            return instance ?: SharedPrefrencesModel(context)
        }
    }
    fun writeSettingDataInPreferences(windSpeed: String,temperature: String, language: String) {
        val preferences = context.getSharedPreferences(preferenceFile, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("windSpeed", windSpeed)
        editor.putString("temperature", temperature)
        editor.putString("language",language)
        editor.commit()
    }
    fun readWindSpeedFromSharedPreferences(): String {
        val preferences = context.getSharedPreferences(preferenceFile, Context.MODE_PRIVATE)

        return preferences.getString("windSpeed", "N/A").toString()
    }
    fun readtTmperatureFromSharedPreferences(): String {
        val preferences = context.getSharedPreferences(preferenceFile, Context.MODE_PRIVATE)

        return preferences.getString("temperature", "N/A").toString()
    }
    fun readLanguageFromSharedPreferences():String{
        val preferences = context.getSharedPreferences(preferenceFile,Context.MODE_PRIVATE)
        return preferences.getString("language", "N/A").toString()
    }

}