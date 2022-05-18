package com.example.weatherforecast.AlertScreen.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.Model.UserAlerts
import com.example.weatherforecast.Model.WeatherModel
import com.example.weatherforecast.repo.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertsViewModel(private val _repo: RepositoryInterface): ViewModel() {
    private var _id = MutableLiveData<Long>()
    val id: LiveData<Long> = _id

    fun insertUserAlerts(userAlerts: UserAlerts){
        viewModelScope.launch(Dispatchers.IO) {
            _id.postValue(_repo.insertUserAlert(userAlerts))

        }
    }
    fun getUserAlerts(): LiveData<List<UserAlerts>> {
        return _repo.allStoredAlerts
    }
    fun deleteUserAlerts(userAlerts: UserAlerts) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.deleteUserAlert(userAlerts)
        }
    }
    fun getAppLanguage(context:Context):String{
        return _repo.readStringFromSharedPreferences(SharedPrefrencesKeys.language, context)
    }
    fun getLocalWeatherModele(): LiveData<List<WeatherModel>> {
        return _repo.allStoredWeatherModel
    }
}