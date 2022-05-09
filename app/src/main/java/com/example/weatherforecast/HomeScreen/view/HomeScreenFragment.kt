package com.example.weatherforecast.HomeScreen.view

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.HomeScreen.viewModel.HomeViewModel
import com.example.weatherforecast.HomeScreen.viewModel.HomeViewModelFactory
import com.example.weatherforecast.Network.WeatherClient
import com.example.weatherforecast.R
import com.example.weatherforecast.repo.RoomAPIrepo.WeatherDataRepo

class homeScreenFragment : Fragment() {

    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel
    private lateinit var preferences:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel(view.context)
        viewModel.observeOnSharedPref(view.context)
    }
    private fun setUpViewModel(context: Context){
        homeViewModelFactory = HomeViewModelFactory(
            WeatherDataRepo.getInstance(
                WeatherClient.getInstance(),context))
        viewModel = ViewModelProvider(this,homeViewModelFactory)[HomeViewModel::class.java]
        viewModel.weatherData.observe(viewLifecycleOwner, Observer {
            Log.e("data", "setUpViewModel: "+it.toString() )
        })

    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.unRegisterOnSharedPreferenceChangeListener()
    }

}