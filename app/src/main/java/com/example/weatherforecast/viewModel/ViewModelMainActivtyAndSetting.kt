package com.example.weatherforecast.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.Model.RepositoryInterface
import com.example.weatherforecast.Model.SharedPrefrencesDataClass
import java.util.*

class ViewModelMainActivtyAndSetting(private val _repo: RepositoryInterface): ViewModel() {
    fun setDataToSharedPrefInFirstTime(context: Context){
        _repo.writeSettingDataInPreferencesForFirstTime()
    }
    fun getDataFromSharedPrefrences():SharedPrefrencesDataClass{
        var data= _repo.getDataFromSharedPrefrences()
        return data
    }

}