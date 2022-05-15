
package com.example.weatherforecast.Setting.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.Constants.IntentKeys
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.LocaleHelperChangeLanguage.LocaleHelper
import com.example.weatherforecast.Map.view.MapsActivity
import com.example.weatherforecast.repo.Repository

import com.example.weatherforecast.R
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.viewModel.ViewModelMainActivtyAndSetting
import com.example.weatherforecast.viewModel.ViewModelMainActivtyAndSettingFactory
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingActivity : AppCompatActivity() {
    private lateinit var viewModel: ViewModelMainActivtyAndSetting
    private lateinit var viewModelFactory: ViewModelMainActivtyAndSettingFactory
    private lateinit var notification: SwitchMaterial
    private lateinit var location:RadioGroup
    private lateinit var windSpeed:RadioGroup
    private lateinit var temperature:RadioGroup
    private lateinit var language:RadioGroup
    private lateinit var radioButton:RadioButton
    private lateinit var mapRadioButton:RadioButton
    private lateinit var locationText:String
    private lateinit var windSpeedText:String
    private lateinit var temperatureText:String
    private var flagOnClickListenerBecauseConfig = true
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
        mapRadioButton = findViewById(R.id.mapRadioButton)
        viewModelFactory =  ViewModelMainActivtyAndSettingFactory(
            Repository.getInstance(null, null,this)
        )
        viewModel = ViewModelProvider(this, viewModelFactory)[ViewModelMainActivtyAndSetting::class.java]


        addRadioGroupListener()
        addNotificationLisetener()
        setConfigrationData()
    }
    private fun addNotificationLisetener(){
        notification.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked) {
                viewModel.changeSettingBoolean(SharedPrefrencesKeys.notification,isChecked)
            } else {
                viewModel.changeSettingBoolean(SharedPrefrencesKeys.notification,false)
            }
        }
    }
   private fun addRadioGroupListener(){

       mapRadioButton.setOnClickListener {
           openMap()
           viewModel.changeSettingStrings(SharedPrefrencesKeys.locationState, mapRadioButton.getText().toString())
       }
        location.setOnCheckedChangeListener{radioGroup, checkedId ->
            if (!flagOnClickListenerBecauseConfig){
                radioButton = findViewById(checkedId)
                 locationText = radioButton.getText().toString()
                if(locationText == getString(R.string.gps))
                {
                    viewModel.getLocationAndSaveItInSharedPref()
                    viewModel.changeSettingStrings(SharedPrefrencesKeys.locationState,locationText)
                }

            }
        }
        windSpeed.setOnCheckedChangeListener { radioGroup, checkedId ->
            if (!flagOnClickListenerBecauseConfig) {
                radioButton = findViewById(checkedId)
                windSpeedText = radioButton.getText().toString()
                viewModel.changeSettingStrings(SharedPrefrencesKeys.windSpeed, windSpeedText)
          }
        }
        temperature.setOnCheckedChangeListener{_,checkedId->
            if (!flagOnClickListenerBecauseConfig){
                radioButton = findViewById(checkedId)
                temperatureText = radioButton.getText().toString()
                viewModel.changeSettingStrings(SharedPrefrencesKeys.temperature,temperatureText)
            }
        }
        language.setOnCheckedChangeListener{_,checkedId->
            if (!flagOnClickListenerBecauseConfig){
                radioButton = findViewById(checkedId)
                var languageText = radioButton.getText().toString()
                if (languageText == getString(R.string.english)) {
                    languageText = "en"
                    LocaleHelper.setAppLocale(this, "en")
                }
                else {
                    languageText = "ar"
                    LocaleHelper.setAppLocale(this, "ar")
                }
                finish();
                startActivity(intent);
                viewModel.changeSettingStrings(SharedPrefrencesKeys.language,languageText)
                Log.e("Tag", "addRadioGroupListener: ${languageText}" )
            }
        }
    }
    private fun  openMap(){
        var intent = Intent(this,MapsActivity::class.java)
        intent.putExtra(IntentKeys.COME_FROM, IntentKeys.SETTING_ACTIVITY)
        startActivity(intent)
    }
   private fun setConfigrationData(){
        var data  = viewModel.getDataFromSharedPrefrences()
        setLocationState(data.locationState)
        setWindSpeed(data.windSpeed)
        setTemperature(data.temperature)
        setLanguage(data.language)
        setNotification(data.notification)
       flagOnClickListenerBecauseConfig = false



   }
    private fun setLocationState(locationState:String) {
        if (locationState == "GPS" || locationState == "الحالي") {
            radioButton = findViewById(R.id.GPS)
            radioButton.isChecked = true
        }else{
            radioButton = findViewById(R.id.mapRadioButton)
            radioButton.isChecked = true
        }
    }
    private fun setWindSpeed(windSpeed:String){
        if(windSpeed == "Meter/Sec" || windSpeed == "متر/ث"){
            radioButton = findViewById(R.id.MeterSec)
            radioButton.isChecked = true
        }else {
            radioButton = findViewById(R.id.MileHour)
            radioButton.isChecked = true
        }

    }
   private fun  setTemperature(temperature:String){
        if(temperature == "Celsius" || temperature == "سيليزوس"){
            radioButton = findViewById(R.id.Celsius)
            radioButton.isChecked = true
        }else if(temperature == "Kelvin" || temperature == "كلفن"){
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
        //to use it when change language
        viewModel.changeSettingStrings(SharedPrefrencesKeys.temperature,getTextOnCheckedRadioButton(temperature))
        viewModel.changeSettingStrings(SharedPrefrencesKeys.windSpeed,getTextOnCheckedRadioButton(windSpeed))
        viewModel.changeSettingStrings(SharedPrefrencesKeys.locationState,getTextOnCheckedRadioButton(location))
    }
    private fun getTextOnCheckedRadioButton(radioButtonGroup:RadioGroup):String{
        val radioButtonID = radioButtonGroup.checkedRadioButtonId
        radioButton = radioButtonGroup.findViewById(radioButtonID)
         val s = radioButton.text.toString()
        return s

    }
}