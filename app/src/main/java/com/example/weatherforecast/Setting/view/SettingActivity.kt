
package com.example.weatherforecast.Setting.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.LocaleHelperChangeLanguage.LocaleHelper
import com.example.weatherforecast.repo.Repository

import com.example.weatherforecast.R
import com.example.weatherforecast.viewModel.ViewModelMainActivtyAndSetting
import com.example.weatherforecast.viewModel.ViewModelMainActivtyAndSettingFactory
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingActivity : AppCompatActivity() {
    lateinit var viewModel: ViewModelMainActivtyAndSetting
    lateinit var viewModelFactory: ViewModelMainActivtyAndSettingFactory
     lateinit var notification: SwitchMaterial
     lateinit var location:RadioGroup
     lateinit var windSpeed:RadioGroup
     lateinit var temperature:RadioGroup
     lateinit var language:RadioGroup
    lateinit var radioButton:RadioButton
     var locationText = "GPS"
     var windSpeedText = "Meter/Sec"
     var temperatureText = "Celsius"
     var languageText = "English"
     var notificationMode = "On"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initComponents()
    }
    fun initComponents() {
        setTitle(R.string.settingLabel);
        notification = findViewById(R.id.notification)
        location = findViewById(R.id.location)
        windSpeed = findViewById(R.id.windSpeed)
        temperature = findViewById(R.id.temperature)
        language = findViewById(R.id.language)
        viewModelFactory =  ViewModelMainActivtyAndSettingFactory(
            Repository.getInstance(null,this)
        )
        viewModel = ViewModelProvider(this, viewModelFactory)[ViewModelMainActivtyAndSetting::class.java]
        addRadioGroupListener()
        addNotificationLisetener()
        setConfigrationData()

    }
    fun addNotificationLisetener(){
        notification.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked) {
                viewModel.changeSettingBoolean(SharedPrefrencesKeys.notification,isChecked)
            } else {
                viewModel.changeSettingBoolean(SharedPrefrencesKeys.notification,false)
            }
        }
    }
    fun addRadioGroupListener(){

        location.setOnCheckedChangeListener{radioGroup, checkedId ->
            radioButton = findViewById(checkedId)
            locationText = radioButton.getText().toString()
            viewModel.changeSettingStrings(SharedPrefrencesKeys.locationState,locationText)
        }
        windSpeed.setOnCheckedChangeListener{radioGroup,checkedId->
            radioButton = findViewById(checkedId)
            windSpeedText = radioButton.getText().toString()
            viewModel.changeSettingStrings(SharedPrefrencesKeys.windSpeed,windSpeedText)
        }
        temperature.setOnCheckedChangeListener{_,checkedId->
            radioButton = findViewById(checkedId)
            temperatureText = radioButton.getText().toString()
            viewModel.changeSettingStrings(SharedPrefrencesKeys.temperature,temperatureText)
        }
        language.setOnCheckedChangeListener{_,checkedId->
            radioButton = findViewById(checkedId)
            languageText = radioButton.getText().toString()
            if (languageText == getString(R.string.english)) {
                languageText = "en"
                LocaleHelper.setAppLocale(this, "en")
            }
            else {
                languageText = "ar"
                LocaleHelper.setAppLocale(this, "ar")
            }
            viewModel.changeSettingStrings(SharedPrefrencesKeys.language,languageText)
            Log.e("Tag", "addRadioGroupListener: ${languageText}" )
        }
    }
   private fun setConfigrationData(){
        var data  = viewModel.getDataFromSharedPrefrences()
        setLocationState(data.locationState)
        setWindSpeed(data.windSpeed)
        setTemperature(data.temperature)
        setLanguage(data.language)
        setNotification(data.notification)
    }
    private fun setLocationState(locationState:String) {
        if (locationState == "GPS") {
            radioButton = findViewById(R.id.GPS)
            radioButton.isChecked = true
        }else{
            radioButton = findViewById(R.id.Map)
            radioButton.isChecked = true
        }
    }
    private fun setWindSpeed(windSpeed:String){
        if(windSpeed == "Meter/Sec"){
            radioButton = findViewById(R.id.MeterSec)
            radioButton.isChecked = true
        }else {
            radioButton = findViewById(R.id.MileHour)
            radioButton.isChecked = true
        }

    }
   private fun  setTemperature(temperature:String){
        if(temperature == "Celsius"){
            radioButton = findViewById(R.id.Celsius)
            radioButton.isChecked = true
        }else if(temperature == "Kelvin"){
            radioButton = findViewById(R.id.Kelvin)
            radioButton.isChecked = true
        }
        else {
            radioButton = findViewById(R.id.Fahrenheit)
            radioButton.isChecked = true
        }
    }
    private fun  setLanguage(language:String){
        if(language == "en"){
            radioButton = findViewById(R.id.English)
            radioButton.isChecked = true
        }
        else{
            radioButton = findViewById(R.id.Arabic)
            radioButton.isChecked = true
        }
    }
    private fun setNotification(notificationState: Boolean){
        notification.isChecked = notificationState
    }
}