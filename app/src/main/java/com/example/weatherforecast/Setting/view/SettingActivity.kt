
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
import com.example.weatherforecast.viewModel.MainSettingFavouriteViewModel
import com.example.weatherforecast.viewModel.MainSettingFavouriteViewModelFactory
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingActivity : AppCompatActivity() {
    private lateinit var favouriteViewModel: MainSettingFavouriteViewModel
    private lateinit var favouriteViewModelFactory: MainSettingFavouriteViewModelFactory
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
        favouriteViewModelFactory =  MainSettingFavouriteViewModelFactory(
            Repository.getInstance(null, null)
        )
        favouriteViewModel = ViewModelProvider(this, favouriteViewModelFactory)[MainSettingFavouriteViewModel::class.java]


        addRadioGroupListener()
        addNotificationLisetener()
        setConfigrationData()
    }
    private fun addNotificationLisetener(){
        notification.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked) {
                favouriteViewModel.changeSettingBoolean(SharedPrefrencesKeys.notification,isChecked,this)
            } else {
                favouriteViewModel.changeSettingBoolean(SharedPrefrencesKeys.notification,false,this)
            }
        }
    }
   private fun addRadioGroupListener(){

       mapRadioButton.setOnClickListener {
           openMap()
           favouriteViewModel.changeSettingStrings(SharedPrefrencesKeys.locationState, mapRadioButton.getText().toString(),this)
       }
        location.setOnCheckedChangeListener{radioGroup, checkedId ->
            if (!flagOnClickListenerBecauseConfig){
                radioButton = findViewById(checkedId)
                 locationText = radioButton.getText().toString()
                if(locationText == getString(R.string.gps))
                {
                    favouriteViewModel.getLocationAndSaveItInSharedPref(this)
                    favouriteViewModel.changeSettingStrings(SharedPrefrencesKeys.locationState,locationText,this)
                }

            }
        }
        windSpeed.setOnCheckedChangeListener { radioGroup, checkedId ->
            if (!flagOnClickListenerBecauseConfig) {
                radioButton = findViewById(checkedId)
                windSpeedText = radioButton.getText().toString()
                favouriteViewModel.changeSettingStrings(SharedPrefrencesKeys.windSpeed, windSpeedText,this)
          }
        }
        temperature.setOnCheckedChangeListener{_,checkedId->
            if (!flagOnClickListenerBecauseConfig){
                radioButton = findViewById(checkedId)
                temperatureText = radioButton.getText().toString()
                favouriteViewModel.changeSettingStrings(SharedPrefrencesKeys.temperature,temperatureText,this)
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
                favouriteViewModel.changeSettingStrings(SharedPrefrencesKeys.language,languageText,this)
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
        val locationState  = favouriteViewModel.readStringFromSharedPreferences(SharedPrefrencesKeys.locationState,this)
        val windSpeed  = favouriteViewModel.readStringFromSharedPreferences(SharedPrefrencesKeys.windSpeed,this)
        val temperature  = favouriteViewModel.readStringFromSharedPreferences(SharedPrefrencesKeys.temperature,this)
        val language  = favouriteViewModel.readStringFromSharedPreferences(SharedPrefrencesKeys.language,this)
        val notification  = favouriteViewModel.readBooleanFromSharedPreferences(SharedPrefrencesKeys.notification,this)
        setLocationState(locationState)
        setWindSpeed(windSpeed)
        setTemperature(temperature)
        setLanguage(language)
        setNotification(notification)
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
        favouriteViewModel.changeSettingStrings(SharedPrefrencesKeys.temperature,getTextOnCheckedRadioButton(temperature),this)
        favouriteViewModel.changeSettingStrings(SharedPrefrencesKeys.windSpeed,getTextOnCheckedRadioButton(windSpeed),this)
        favouriteViewModel.changeSettingStrings(SharedPrefrencesKeys.locationState,getTextOnCheckedRadioButton(location),this)
    }
    private fun getTextOnCheckedRadioButton(radioButtonGroup:RadioGroup):String{
        val radioButtonID = radioButtonGroup.checkedRadioButtonId
        radioButton = radioButtonGroup.findViewById(radioButtonID)
         val s = radioButton.text.toString()
        return s

    }
}