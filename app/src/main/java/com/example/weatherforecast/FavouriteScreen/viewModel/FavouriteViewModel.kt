package com.example.weatherforecast.FavouriteScreen.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.Model.FavouriteModel
import com.example.weatherforecast.repo.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteViewModel(private val _repo: RepositoryInterface): ViewModel()  {



    fun getLocalFavouriate(): LiveData<List<FavouriteModel>> {
        return _repo.allStoredFavouriteModel
    }
    fun deleteFavouriateModel(favouriteModel: FavouriteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.deleteFavouriateModel(favouriteModel)
        }
    }
    fun getAppLanguage():String{
        return  _repo.readStringFromSharedPreferences(SharedPrefrencesKeys.language)
    }
}