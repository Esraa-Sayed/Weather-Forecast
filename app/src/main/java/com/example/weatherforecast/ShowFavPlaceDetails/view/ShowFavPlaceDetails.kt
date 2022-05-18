package com.example.weatherforecast.ShowFavPlaceDetails.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weatherforecast.Constants.APIRequest
import com.example.weatherforecast.Constants.IntentKeys
import com.example.weatherforecast.HomeScreen.view.DaysWeathersAdapter
import com.example.weatherforecast.HomeScreen.view.HourlyWeathersAdapter
import com.example.weatherforecast.Model.FavouriteModel
import com.example.weatherforecast.Model.WeatherModel
import com.example.weatherforecast.Network.WeatherClient
import com.example.weatherforecast.R
import com.example.weatherforecast.ShowFavPlaceDetails.viewModel.ShowFavPlaceViewModel
import com.example.weatherforecast.ShowFavPlaceDetails.viewModel.ShowFavPlaceViewModelFactory
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.repo.Repository
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView

class ShowFavPlaceDetails : AppCompatActivity() {
    private lateinit var showFavPlaceViewModelFactory: ShowFavPlaceViewModelFactory
    private lateinit var viewModel: ShowFavPlaceViewModel
    private lateinit var progressBar: ProgressBar
    //view
    private  lateinit var city: TextView
    private  lateinit var date: TextView
    private  lateinit var weatherDescription: TextView
    private  lateinit var tempTextView: TextView
    private  lateinit var tempTypeTextView: TextView //°C مثلا
    private  lateinit var tempIcon: CircleImageView
    private  lateinit var hourlyForTheCurrentDate: RecyclerView
    private  lateinit var daysForTheCurrentDate: RecyclerView
    private lateinit var cardView: CardView
    private lateinit var cardView2: CardView

    private lateinit var pressureTxt: TextView
    private lateinit var humidityTxt: TextView
    private lateinit var windTxt: TextView
    private lateinit var cloudTxt: TextView
    private lateinit var ultraVioletTxt: TextView
    private lateinit var visibilityTxt: TextView
    private lateinit var swipe: SwipeRefreshLayout

    private lateinit var  hourlyWeathersAdapter: HourlyWeathersAdapter
    private lateinit var  daysWeathersAdapter: DaysWeathersAdapter
    private lateinit var parentLayout:View
    private lateinit var favouriteModel: FavouriteModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home_screen)
        parentLayout = findViewById(android.R.id.content)
        setTitle(R.string.Favourite_Place_Details);
        val favPlaceIntent = intent
        favouriteModel = favPlaceIntent.getParcelableExtra(IntentKeys.FAVOURITE_Place)!!
        init()
        getData()
    }
    private fun getData(){
       viewModel.getFavWeatherFromNetwork(this,favouriteModel.latitude.toFloat(),favouriteModel.longitude.toFloat())
        viewModel.weatherData.observe(this, Observer {
            putDataOnView(it)
            swipe.setRefreshing(false)
        })
        viewModel.errorMsgResponse.observe(this, Observer {
            Snackbar.make(parentLayout,getString(R.string.No_internet_connection), Snackbar.LENGTH_LONG).show()
        })
    }
    private fun putDataOnView(weather: WeatherModel){
        if (viewModel.getAppLanguage(this) == "en")
            city.text = favouriteModel.addressEn
        else
            city.text = favouriteModel.addressAr
        var weatherCurrent = weather.current
        var weatherDesc = weatherCurrent.weather[0]
        date.text = APIRequest.getDateTime(weatherCurrent.dt,"EEE, d MMM ",viewModel.getAppLanguage(this))
        weatherDescription.text = weatherDesc.description
        tempTextView.text = weatherCurrent.temp.toString()
        tempTypeTextView.text = viewModel.getTempMeasuringUnit(this)
        APIRequest.setImageInView(this, weatherDesc.icon, tempIcon)

        pressureTxt.text = weatherCurrent.pressure.toString()
        humidityTxt.text = weatherCurrent.humidity.toString()
        cloudTxt.text = weatherCurrent.clouds.toString()
        visibilityTxt.text = weatherCurrent.clouds.toString()
        when( viewModel.getWindSpeedMeasuringUnit(this)){

            this.getString(R.string.meter_sec) -> windTxt.text = weatherCurrent.windSpeed.toString()

            else ->{
                windTxt.text = ((weatherCurrent.windSpeed * 2.237).toInt()).toString()
            }

        }
        daysWeathersAdapter.setDays(weather.daily)

        hourlyWeathersAdapter.setHours(weather.hourly)
        showView()
    }
    private fun init(){
        showFavPlaceViewModelFactory = ShowFavPlaceViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(), ConcreteLocalSource(this)))
        viewModel = ViewModelProvider(this,showFavPlaceViewModelFactory)[ShowFavPlaceViewModel::class.java]

        city = findViewById(R.id.city)
        date = findViewById(R.id.date)
        weatherDescription = findViewById(R.id.weatherDescription)
        tempTextView = findViewById(R.id.tempTextView)
        tempTypeTextView = findViewById(R.id.tempTypeTextView) //°C مثلا
        tempIcon = findViewById(R.id.tempIcon)
        hourlyForTheCurrentDate = findViewById(R.id.hourlyForTheCurrentDate)
        daysForTheCurrentDate = findViewById(R.id.DaysForTheCurrentDate)
        progressBar = findViewById(R.id.progressBar)
        cardView = findViewById(R.id.cardView)
        cardView2 = findViewById(R.id.cardView2)
        hourlyWeathersAdapter = HourlyWeathersAdapter(this, emptyList(),viewModel.getAppLanguage(this),viewModel.getTempMeasuringUnit(this))
        daysWeathersAdapter = DaysWeathersAdapter(this, emptyList(),viewModel.getAppLanguage(this),viewModel.getTempMeasuringUnit(this))
        var layoutM = LinearLayoutManager(this)
        hourlyForTheCurrentDate.apply {
            setHasFixedSize(true)
            layoutM.orientation = RecyclerView.HORIZONTAL
            layoutManager = layoutM
            adapter = hourlyWeathersAdapter
        }

        var layoutManagerDays = LinearLayoutManager(this)
        daysForTheCurrentDate .apply {
            setHasFixedSize(true)
            layoutManagerDays.orientation = RecyclerView.VERTICAL
            layoutManager = layoutManagerDays
            adapter = daysWeathersAdapter
        }

        pressureTxt = findViewById(R.id.pressureTxt)
        humidityTxt = findViewById(R.id.humidityTxt)
        windTxt = findViewById(R.id.windTxt)
        cloudTxt = findViewById(R.id.cloudTxt)
        ultraVioletTxt = findViewById(R.id.ultraVioletTxt)
        visibilityTxt = findViewById(R.id.visibilityTxt)

        swipe = findViewById(R.id.swipe)
        setSwipeListener()
        hideView()
    }
    private fun setSwipeListener(){
        swipe.setOnRefreshListener{
            viewModel.getFavWeatherFromNetwork(this,favouriteModel.latitude.toFloat(),favouriteModel.longitude.toFloat())
            Handler().postDelayed({ // Stop animation
                swipe.setRefreshing(false)
            }, 7000) // Delay in millis

        }
    }
    private fun hideView(){
        progressBar.visibility = View.VISIBLE
        city.visibility = View.GONE
        date.visibility = View.GONE
        cardView.visibility = View.GONE
        cardView2.visibility = View.GONE
        tempIcon.visibility = View.GONE
        hourlyForTheCurrentDate.visibility = View.GONE
        daysForTheCurrentDate.visibility = View.GONE
    }
    private fun showView(){
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