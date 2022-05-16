package com.example.weatherforecast.ShowFavPlaceDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.HomeScreen.viewModel.HomeViewModel
import com.example.weatherforecast.repo.RepositoryInterface

class ShowFavPlaceViewModelFactory (private val _repo: RepositoryInterface): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ShowFavPlaceViewModel::class.java)) {
            ShowFavPlaceViewModel(_repo) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}