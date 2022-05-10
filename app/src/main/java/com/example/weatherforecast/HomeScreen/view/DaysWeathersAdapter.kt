package com.example.weatherforecast.HomeScreen.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Model.Daily
import com.example.weatherforecast.Model.Hourly
import com.example.weatherforecast.R
import de.hdodenhof.circleimageview.CircleImageView

class DaysWeathersAdapter (private var context: Context, private var list: List<Daily>):
    RecyclerView.Adapter<DaysWeathersAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysWeathersAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.day_row_for_weather,parent,false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: DaysWeathersAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 7
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dayForCurrentWeather = itemView.findViewById<TextView>(R.id.dayForCurrentWeather)
        var weatherDescriptionDay = itemView.findViewById<TextView>(R.id.weatherDescriptionDay)
        var temperatureDay = itemView.findViewById<TextView>(R.id.temperatureDay)
        var tempIconDayRow= itemView.findViewById<CircleImageView>(R.id.tempIconDayRow)
    }
}