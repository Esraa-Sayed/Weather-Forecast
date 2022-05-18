package com.example.weatherforecast.AlertScreen.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.Model.UserAlerts
import com.example.weatherforecast.repo.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertsViewModel(private val _repo: RepositoryInterface): ViewModel() {
    fun insertUserAlerts(userAlerts: UserAlerts){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertUserAlert(userAlerts)
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
    fun getAppLanguage():String{
        return _repo.readStringFromSharedPreferences(SharedPrefrencesKeys.language)
    }
}