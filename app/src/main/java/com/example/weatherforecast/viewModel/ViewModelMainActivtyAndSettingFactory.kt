package com.example.weatherforecast.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.repo.RepositoryInterface

class ViewModelMainActivtyAndSettingFactory (private val _repo: RepositoryInterface): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ViewModelMainActivtyAndSetting::class.java)) {
            ViewModelMainActivtyAndSetting(_repo) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}