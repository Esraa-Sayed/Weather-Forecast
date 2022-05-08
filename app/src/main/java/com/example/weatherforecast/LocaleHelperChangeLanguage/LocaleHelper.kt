package com.example.weatherforecast.LocaleHelperChangeLanguage

import android.content.Context
import android.os.Build
import android.annotation.TargetApi
import android.content.res.Configuration
import android.content.res.Resources
import java.util.*


object LocaleHelper {
    fun setAppLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}