package com.example.weatherforecast.ShowFavPlaceDetails.viewModel

import android.content.Context
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

class ShowFavPlaceViewModel (private val _repo: RepositoryInterface): ViewModel()  {
    private var _weatherData = MutableLiveData<WeatherModel>()
    private var _errorMsgResponse = MutableLiveData<String>()
    private var _flagAPIFinishedDorNot = MutableLiveData<Boolean>()
    val weatherData: LiveData<WeatherModel> = _weatherData
    val errorMsgResponse: LiveData<String> = _errorMsgResponse
    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, t ->
        run {
            t.printStackTrace()
            Log.d("errorMsg:", t.message.toString())
            _errorMsgResponse.postValue(t.message)
        }
    }
    fun getFavWeatherFromNetwork(context: Context,latitude:Float, longitude:Float) {
        _flagAPIFinishedDorNot.postValue(false)
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val currentWeatherResponse = _repo.getFavWeatherOverNetwork(latitude,longitude)
            if (currentWeatherResponse.isSuccessful) {
                _weatherData.postValue(currentWeatherResponse.body())
                _flagAPIFinishedDorNot.postValue(true)
            }
        }
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
}