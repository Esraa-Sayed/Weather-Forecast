package com.example.weatherforecast.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.repo.RepositoryInterface
import com.example.weatherforecast.Model.SharedPrefrencesDataClass

class ViewModelMainActivtyAndSetting(private val _repo: RepositoryInterface): ViewModel() {
    fun setDataToSharedPrefInFirstTime(context: Context){
      if(!_repo.readBooleanFromSharedPreferences(SharedPrefrencesKeys.isNotFirstTime))
          _repo.writeSettingDataInPreferencesForFirstTime()
    }
    fun getDataFromSharedPrefrences():SharedPrefrencesDataClass{
        var data= _repo.getDataFromSharedPrefrences()
        return data
    }
    fun changeSettingStrings(key:String, value:String){
        _repo.setStringToSharedPrefrences(key,value)
    }
    fun changeSettingFloat(key:String, value:Float){
        _repo.setFloatToSharedPrefrences(key,value)
    }
    fun changeSettingBoolean(key:String, value:Boolean){
        _repo.setBoolToSharedPrefrences(key, value)
    }

}