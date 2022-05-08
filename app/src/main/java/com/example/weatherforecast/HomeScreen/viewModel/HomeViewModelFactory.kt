package com.example.weatherforecast.HomeScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.repo.RepositoryInterface
import com.example.weatherforecast.repo.RoomAPIrepo.WeatherDataRepo
import com.example.weatherforecast.repo.RoomAPIrepo.WeatherDataRepoInterface
import com.example.weatherforecast.viewModel.ViewModelMainActivtyAndSetting

class HomeViewModelFactory (private val _repo: WeatherDataRepoInterface): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(_repo) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}
