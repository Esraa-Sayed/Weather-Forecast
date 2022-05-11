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
import com.example.weatherforecast.R
import com.example.weatherforecast.repo.RepositoryInterface
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val _repo: RepositoryInterface): ViewModel()  {
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
         listener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == SharedPrefrencesKeys.latitude) {
                    if (_repo.isLocationSet()) {
                        getCurrentWeatherFromNetwork(context)
                    }
                }
            }
        preferences.registerOnSharedPreferenceChangeListener(listener)

    }
    fun unRegisterOnSharedPreferenceChangeListener(){
        if(this::listener.isInitialized)
            preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
    fun getCityName():String{
        val currrentAppLang = _repo.readStringFromSharedPreferences(SharedPrefrencesKeys.language)
        if(currrentAppLang == "en")
            return _repo.readStringFromSharedPreferences(SharedPrefrencesKeys.cityEnglish)
        else
            return _repo.readStringFromSharedPreferences(SharedPrefrencesKeys.cityArabic)
    }
    fun getAppLanguage():String{
         return  _repo.readStringFromSharedPreferences(SharedPrefrencesKeys.language)
    }
    fun  getTempMeasuringUnit(context: Context):String{
        val currentMeasuringUnit = _repo.readStringFromSharedPreferences(SharedPrefrencesKeys.temperature)
        return when(currentMeasuringUnit){
            context.getString(R.string.celsius) -> "°C"
            context.getString(R.string.fahrenheit)-> "°F"
            else-> "°K"
        }
    }
    fun getWindSpeedMeasuringUnit():String{
        return  _repo.readStringFromSharedPreferences(SharedPrefrencesKeys.windSpeed)
    }
    fun addWeatherModelInRoom(weatherModel: WeatherModel){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertWeatherModel(weatherModel)
        }
    }
    fun deleteWeatherModele(weatherModel: WeatherModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.deleteWeatherModel(weatherModel)
        }

    }

    fun getLocalWeatherModele(): LiveData<List<WeatherModel>> {
        return _repo.allStoredWeatherModel
    }
}