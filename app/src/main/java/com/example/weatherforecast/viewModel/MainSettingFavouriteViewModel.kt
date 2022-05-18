package com.example.weatherforecast.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.Model.FavouriteModel
import com.example.weatherforecast.repo.RepositoryInterface
import com.example.weatherforecast.Model.SharedPrefrencesDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainSettingFavouriteViewModel(private val _repo: RepositoryInterface): ViewModel() {
    fun setDataToSharedPrefInFirstTime(context: Context){
      if(!_repo.readBooleanFromSharedPreferences(SharedPrefrencesKeys.isNotFirstTime,context))
          _repo.writeSettingDataInPreferencesForFirstTime(context)
    }
    fun changeSettingStrings(key:String, value:String,context: Context){
        _repo.setStringToSharedPrefrences(key,value,context)
    }
    fun changeSettingFloat(key:String, value:Float,context: Context){
        _repo.setFloatToSharedPrefrences(key,value,context)
    }
    fun getLocationAndSaveItInSharedPref(context: Context){
        _repo.getCurrentLocation(context)
    }
    fun changeSettingBoolean(key:String, value:Boolean,context: Context){
        _repo.setBoolToSharedPrefrences(key, value,context)
    }
    fun readFloatFromSharedPreferences(dataNeed: String,context: Context):Float{
        return _repo.readFloatFromSharedPreferences(dataNeed,context)
    }
    fun readBooleanFromSharedPreferences(dataNeed: String,context: Context):Boolean{
        return _repo.readBooleanFromSharedPreferences(dataNeed,context)
    }
    fun readStringFromSharedPreferences(dataNeed: String,context: Context):String{
        return _repo.readStringFromSharedPreferences(dataNeed,context)
    }
    fun insertFavouriatePlace(favouriteModel: FavouriteModel){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertFavouriateModel(favouriteModel)
        }
    }
    fun getLocalFavouriate(): LiveData<List<FavouriteModel>> {
        return _repo.allStoredFavouriteModel
    }
    fun deleteFavouriateModel(favouriteModel: FavouriteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.deleteFavouriateModel(favouriteModel)
        }
    }

}