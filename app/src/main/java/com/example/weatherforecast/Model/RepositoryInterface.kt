package com.example.weatherforecast.Model

interface RepositoryInterface {
    fun writeSettingDataInPreferencesForFirstTime()
    fun readBooleanFromSharedPreferences(dataNeed: String):Boolean
    fun readFloatFromSharedPreferences(dataNeed: String):Float
    fun readStringFromSharedPreferences(dataNeed:String): String
    fun setStringToSharedPrefrences(key:String,value:String)
    fun setFloatToSharedPrefrences(key:String,value:Float)
    fun setBoolToSharedPrefrences(key:String,value:Boolean)
    fun getDataFromSharedPrefrences():SharedPrefrencesDataClass
}