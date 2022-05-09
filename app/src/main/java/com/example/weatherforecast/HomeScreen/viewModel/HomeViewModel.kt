package com.example.weatherforecast.HomeScreen.viewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.Model.WeatherModel
import com.example.weatherforecast.repo.RoomAPIrepo.WeatherDataRepoInterface
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val _repo: WeatherDataRepoInterface): ViewModel()  {
    private var _weatherData = MutableLiveData<WeatherModel>()
    private var _errorMsgResponse = MutableLiveData<String>()
    val weatherData:LiveData<WeatherModel> = _weatherData
    private lateinit var preferences:SharedPreferences
    private lateinit var listener:SharedPreferences.OnSharedPreferenceChangeListener
    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, t ->
        run {
            t.printStackTrace()
            Log.d("errorMsg:", t.message.toString())
            _errorMsgResponse.postValue(t.message)
        }
    }
    private fun getCurrentWeatherFromNetwork(context:Context) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val currentWeatherResponse = _repo.getCurrentWeatherOverNetwork()
            if (currentWeatherResponse.isSuccessful) {
                _weatherData.postValue(currentWeatherResponse.body())
            } else {
                _errorMsgResponse.postValue(currentWeatherResponse.message())
            }
        }
    }
    fun observeOnSharedPref(context: Context){
         preferences = _repo.getAppSharedPrefrences()
        if (_repo.isLocationSet())
        {
            getCurrentWeatherFromNetwork(context)
        }
        else{
            val listener =
                SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                    if (key == SharedPrefrencesKeys.latitude) {
                        if (_repo.isLocationSet()) {
                            getCurrentWeatherFromNetwork(context)
                        }
                    }
                }
            preferences.registerOnSharedPreferenceChangeListener(listener)
        }
    }
    fun unRegisterOnSharedPreferenceChangeListener(){
        preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}