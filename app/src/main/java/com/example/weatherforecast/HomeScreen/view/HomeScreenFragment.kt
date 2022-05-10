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
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.HomeScreen.viewModel.HomeViewModel
import com.example.weatherforecast.HomeScreen.viewModel.HomeViewModelFactory
import com.example.weatherforecast.Network.WeatherClient
import com.example.weatherforecast.R
import com.example.weatherforecast.repo.RoomAPIrepo.WeatherDataRepo
import de.hdodenhof.circleimageview.CircleImageView

class HomeScreenFragment : Fragment() {

    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel

    //view
    private  lateinit var city:TextView
    private  lateinit var date:TextView
    private  lateinit var weatherDescription:TextView
    private  lateinit var tempTextView:TextView
    private  lateinit var tempTypeTextView:TextView //°C مثلا
    private  lateinit var tempIcon:CircleImageView
    private  lateinit var hourlyForTheCurrentDate: RecyclerView
    private  lateinit var DaysForTheCurrentDate: RecyclerView

    private lateinit var pressureTxt:TextView
    private lateinit var humidityTxt:TextView
    private lateinit var windTxt:TextView
    private lateinit var cloudTxt:TextView
    private lateinit var ultraVioletTxt:TextView
    private lateinit var visibilityTxt:TextView
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
        init(view)
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
    private fun init(view: View){
        city = view.findViewById(R.id.city)
        date = view.findViewById(R.id.date)
        weatherDescription = view.findViewById(R.id.weatherDescription)
        tempTextView = view.findViewById(R.id.tempTextView)
        tempTypeTextView = view.findViewById(R.id.tempTypeTextView) //°C مثلا
        tempIcon = view.findViewById(R.id.tempIcon)
        hourlyForTheCurrentDate = view.findViewById(R.id.hourlyForTheCurrentDate)
        DaysForTheCurrentDate = view.findViewById(R.id.DaysForTheCurrentDate)

        val hourlyWeathersAdapter = HourlyWeathersAdapter(view.context, emptyList())
        val DaysWeathersAdapter = DaysWeathersAdapter(view.context, emptyList())
        var layoutM = LinearLayoutManager(activity)
        hourlyForTheCurrentDate.apply {
            setHasFixedSize(true)
            layoutM.orientation = RecyclerView.HORIZONTAL
            layoutManager = layoutM
            adapter = hourlyWeathersAdapter
        }

        var layoutManagerDays = LinearLayoutManager(activity)
        DaysForTheCurrentDate .apply {
            setHasFixedSize(true)
            layoutManagerDays.orientation = RecyclerView.VERTICAL
            layoutManager = layoutManagerDays
            adapter = DaysWeathersAdapter
        }

        pressureTxt = view.findViewById(R.id.pressureTxt)
        humidityTxt = view.findViewById(R.id.humidityTxt)
        windTxt = view.findViewById(R.id.windTxt)
        cloudTxt = view.findViewById(R.id.cloudTxt)
        ultraVioletTxt = view.findViewById(R.id.ultraVioletTxt)
        visibilityTxt = view.findViewById(R.id.visibilityTxt)

    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.unRegisterOnSharedPreferenceChangeListener()
    }

}