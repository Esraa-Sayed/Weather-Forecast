package com.example.weatherforecast.ShowFavPlaceDetails.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Constants.APIRequest
import com.example.weatherforecast.Model.Daily
import com.example.weatherforecast.R
import de.hdodenhof.circleimageview.CircleImageView

class DaysWeathersFavAdapter (private var context: Context, private var days: List<Daily>, private val language:String, private val tempUnit:String):
    RecyclerView.Adapter<DaysWeathersFavAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.day_row_for_weather,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder:ViewHolder, position: Int) {

        if(days.isNotEmpty()){
            val day = days[position+1]
            val time = APIRequest.getDateTime(day.dt,"MMM d",language)
            holder.dayForCurrentWeather.text = time
            holder.weatherDescriptionDay.text = day.weather[0].description
            holder.temperatureDay.text = "${day.temp.max.toInt()}/ ${day.temp.min.toInt()} $tempUnit"
            APIRequest.setImageInView(context, day.weather[0].icon, holder.tempIconDayRow)
        }

    }

    override fun getItemCount(): Int {
        return 7
    }
    fun setDays(days: List<Daily>){
        this.days = days
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dayForCurrentWeather = itemView.findViewById<TextView>(R.id.dayForCurrentWeather)
        var weatherDescriptionDay = itemView.findViewById<TextView>(R.id.weatherDescriptionDay)
        var temperatureDay = itemView.findViewById<TextView>(R.id.temperatureDay)
        var tempIconDayRow= itemView.findViewById<CircleImageView>(R.id.tempIconDayRow)
    }
}