package com.example.weatherforecast

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.LocaleHelperChangeLanguage.LocaleHelper
import com.example.weatherforecast.repo.Repository
import com.example.weatherforecast.viewModel.MainSettingFavouriteViewModel
import com.example.weatherforecast.viewModel.MainSettingFavouriteViewModelFactory

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        var viewModelFactory =  MainSettingFavouriteViewModelFactory(
            Repository.getInstance(null, null)
        )
        var viewModel = ViewModelProvider(this, viewModelFactory)[MainSettingFavouriteViewModel::class.java]
        var lang = viewModel.readStringFromSharedPreferences(SharedPrefrencesKeys.language,this)
        val locationState = viewModel.readStringFromSharedPreferences(SharedPrefrencesKeys.locationState,this)
        if(locationState == getString(R.string.gps))
            viewModel.getLocationAndSaveItInSharedPref(this)
        LocaleHelper.setAppLocale(this, lang);
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }
}