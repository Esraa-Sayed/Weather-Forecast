package com.example.weatherforecast.FavouriteScreen.view

import com.example.weatherforecast.Model.FavouriteModel

interface OnItemClickListener {
    fun onRowClicked(favouriteModel: FavouriteModel)
    fun onDeleteIconClicked(favouriteModel: FavouriteModel)
}