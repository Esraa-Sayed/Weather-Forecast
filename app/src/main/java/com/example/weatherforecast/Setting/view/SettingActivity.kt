
package com.example.weatherforecast.Setting.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.RadioGroup

import com.example.weatherforecast.R
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlin.math.log

class SettingActivity : AppCompatActivity() {
     lateinit var notification: SwitchMaterial
     lateinit var location:RadioGroup
     lateinit var windSpeed:RadioGroup
     lateinit var temperature:RadioGroup
     lateinit var language:RadioGroup
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
        notification = findViewById(R.id.notification)
        location = findViewById(R.id.location)
        windSpeed = findViewById(R.id.windSpeed)
        temperature = findViewById(R.id.temperature)
        language = findViewById(R.id.language)
        addRadioGroupListener()
        notification.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked) {
                Log.e("TAG", "initComponents: Ischecked true" )
                notificationMode = "On"
            } else {
                Log.e("TAG", "initComponents: Ischecked false" )
                notificationMode = "Off"
            }
        }
    }
    fun addRadioGroupListener(){
        lateinit var radioButton:RadioButton
        location.setOnCheckedChangeListener{radioGroup, checkedId ->
            radioButton = findViewById(checkedId)
            locationText = radioButton.getText().toString()
            Log.e("Tag", "addRadioGroupListener: $locationText" )
        }
        windSpeed.setOnCheckedChangeListener{radioGroup,checkedId->
            radioButton = findViewById(checkedId)
            windSpeedText = radioButton.getText().toString()
            Log.e("Tag", "addRadioGroupListener: $windSpeedText" )
        }
        temperature.setOnCheckedChangeListener{_,checkedId->
            radioButton = findViewById(checkedId)
            temperatureText = radioButton.getText().toString()
            Log.e("Tag", "addRadioGroupListener: $temperatureText" )
        }
        language.setOnCheckedChangeListener{_,checkedId->
            radioButton = findViewById(checkedId)
            languageText = radioButton.getText().toString()
            Log.e("Tag", "addRadioGroupListener: ${languageText}" )
        }
    }
}