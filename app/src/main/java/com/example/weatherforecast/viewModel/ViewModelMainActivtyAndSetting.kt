package com.example.weatherforecast.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.Model.FavouriteModel
import com.example.weatherforecast.repo.RepositoryInterface
import com.example.weatherforecast.Model.SharedPrefrencesDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    fun getLocationAndSaveItInSharedPref(){
        _repo.getCurrentLocation()
    }
    fun changeSettingBoolean(key:String, value:Boolean){
        _repo.setBoolToSharedPrefrences(key, value)
    }
    fun readFloatFromSharedPreferences(dataNeed: String):Float{
        return _repo.readFloatFromSharedPreferences(dataNeed)
    }
    fun readStringFromSharedPreferences(dataNeed: String):String{
        return _repo.readStringFromSharedPreferences(dataNeed)
    }
    fun insertFavouriatePlace(favouriteModel: FavouriteModel){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertFavouriateModel(favouriteModel)
        }
    }

}