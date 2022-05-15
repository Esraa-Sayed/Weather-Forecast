package com.example.weatherforecast.HomeScreen.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weatherforecast.Constants.APIRequest
import com.example.weatherforecast.Constants.APIRequest.setImageInView

import com.example.weatherforecast.HomeScreen.viewModel.HomeViewModel
import com.example.weatherforecast.HomeScreen.viewModel.HomeViewModelFactory
import com.example.weatherforecast.Model.WeatherModel
import com.example.weatherforecast.Network.WeatherClient
import com.example.weatherforecast.R
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.repo.Repository
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener




class HomeScreenFragment : Fragment() {

    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel
    private lateinit var progressBar:ProgressBar
    //view
    private  lateinit var city:TextView
    private  lateinit var date:TextView
    private  lateinit var weatherDescription:TextView
    private  lateinit var tempTextView:TextView
    private  lateinit var tempTypeTextView:TextView //°C مثلا
    private  lateinit var tempIcon:CircleImageView
    private  lateinit var hourlyForTheCurrentDate: RecyclerView
    private  lateinit var daysForTheCurrentDate: RecyclerView
    private lateinit var cardView:CardView
    private lateinit var cardView2:CardView

    private lateinit var pressureTxt:TextView
    private lateinit var humidityTxt:TextView
    private lateinit var windTxt:TextView
    private lateinit var cloudTxt:TextView
    private lateinit var ultraVioletTxt:TextView
    private lateinit var visibilityTxt:TextView
    private lateinit var swipe: SwipeRefreshLayout

    private lateinit var  hourlyWeathersAdapter:HourlyWeathersAdapter
    private lateinit var  daysWeathersAdapter:DaysWeathersAdapter
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
        setUpViewModel(view)
    }

    private fun setUpViewModel(view: View){
        viewModel.getWeatherView(viewLifecycleOwner,view)
        viewModel.getLocalWeatherModele().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                putDataOnView(it[0], view.context)
                swipe.setRefreshing(false)
            }
        })

    }
    private fun init(view: View){
        homeViewModelFactory = HomeViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(), ConcreteLocalSource(view.context),view.context))
        viewModel = ViewModelProvider(this,homeViewModelFactory)[HomeViewModel::class.java]

        city = view.findViewById(R.id.city)
        date = view.findViewById(R.id.date)
        weatherDescription = view.findViewById(R.id.weatherDescription)
        tempTextView = view.findViewById(R.id.tempTextView)
        tempTypeTextView = view.findViewById(R.id.tempTypeTextView) //°C مثلا
        tempIcon = view.findViewById(R.id.tempIcon)
        hourlyForTheCurrentDate = view.findViewById(R.id.hourlyForTheCurrentDate)
        daysForTheCurrentDate = view.findViewById(R.id.DaysForTheCurrentDate)
        progressBar = view.findViewById(R.id.progressBar)
        cardView = view.findViewById(R.id.cardView)
        cardView2 = view.findViewById(R.id.cardView2)
        hourlyWeathersAdapter = HourlyWeathersAdapter(view.context, emptyList(),viewModel.getAppLanguage(),viewModel.getTempMeasuringUnit(view.context))
        daysWeathersAdapter = DaysWeathersAdapter(view.context, emptyList(),viewModel.getAppLanguage(),viewModel.getTempMeasuringUnit(view.context))
        var layoutM = LinearLayoutManager(activity)
        hourlyForTheCurrentDate.apply {
            setHasFixedSize(true)
            layoutM.orientation = RecyclerView.HORIZONTAL
            layoutManager = layoutM
            adapter = hourlyWeathersAdapter
        }

        var layoutManagerDays = LinearLayoutManager(activity)
        daysForTheCurrentDate .apply {
            setHasFixedSize(true)
            layoutManagerDays.orientation = RecyclerView.VERTICAL
            layoutManager = layoutManagerDays
            adapter = daysWeathersAdapter
        }

        pressureTxt = view.findViewById(R.id.pressureTxt)
        humidityTxt = view.findViewById(R.id.humidityTxt)
        windTxt = view.findViewById(R.id.windTxt)
        cloudTxt = view.findViewById(R.id.cloudTxt)
        ultraVioletTxt = view.findViewById(R.id.ultraVioletTxt)
        visibilityTxt = view.findViewById(R.id.visibilityTxt)

        swipe = view.findViewById(R.id.swipe)
        setSwipeListener(view)
        hideView()

    }
    private fun setSwipeListener(view: View){
        swipe.setOnRefreshListener{
            viewModel.getWeatherView(viewLifecycleOwner, view)
            Handler().postDelayed({ // Stop animation (This will be after 3 seconds)
                swipe.setRefreshing(false)
            }, 7000) // Delay in millis

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.unRegisterOnSharedPreferenceChangeListener()
    }
    private fun putDataOnView(weather:WeatherModel,context: Context){
        city.text = viewModel.getCityName()
        var weatherCurrent = weather.current
        var weatherDesc = weatherCurrent.weather[0]
        date.text = APIRequest.getDateTime(weatherCurrent.dt,"EEE, d MMM ",viewModel.getAppLanguage())
        weatherDescription.text = weatherDesc.description
        tempTextView.text = weatherCurrent.temp.toString()
        tempTypeTextView.text = viewModel.getTempMeasuringUnit(context)
        setImageInView(context,weatherDesc.icon,tempIcon)

        pressureTxt.text = weatherCurrent.pressure.toString()
        humidityTxt.text = weatherCurrent.humidity.toString()
        cloudTxt.text = weatherCurrent.clouds.toString()
        visibilityTxt.text = weatherCurrent.clouds.toString()
        when( viewModel.getWindSpeedMeasuringUnit()){

            context.getString(R.string.meter_sec) -> windTxt.text = weatherCurrent.windSpeed.toString()

            else ->{
                windTxt.text = ((weatherCurrent.windSpeed * 2.237).toInt()).toString()
            }

        }
        daysWeathersAdapter.setDays(weather.daily)

        hourlyWeathersAdapter.setHours(weather.hourly)
        showView()
    }
    fun hideView(){
        progressBar.visibility = View.VISIBLE
        city.visibility = View.GONE
        date.visibility = View.GONE
        cardView.visibility = View.GONE
        cardView2.visibility = View.GONE
        tempIcon.visibility = View.GONE
        hourlyForTheCurrentDate.visibility = View.GONE
        daysForTheCurrentDate.visibility = View.GONE
    }
    fun showView(){
        progressBar.visibility = View.GONE
        city.visibility = View.VISIBLE
        date.visibility = View.VISIBLE
        cardView.visibility = View.VISIBLE
        cardView2.visibility = View.VISIBLE
        tempIcon.visibility = View.VISIBLE
        hourlyForTheCurrentDate.visibility = View.VISIBLE
        daysForTheCurrentDate.visibility = View.VISIBLE
    }

}