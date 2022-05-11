package com.example.weatherforecast.HomeScreen.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Constants.APIRequest
import com.example.weatherforecast.Constants.APIRequest.roundTheNumber
import com.example.weatherforecast.Model.Daily
import com.example.weatherforecast.Model.Hourly
import com.example.weatherforecast.R
import de.hdodenhof.circleimageview.CircleImageView

class HourlyWeathersAdapter(private var context: Context, private var hours: List<Hourly>,private val language:String,private val tempUnit:String):
    RecyclerView.Adapter<HourlyWeathersAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeathersAdapter.ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.hourly_row_for_weather,parent,false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: HourlyWeathersAdapter.ViewHolder, position: Int) {
        if(hours.isNotEmpty()){
            val hour = hours[position+1]
            holder.hourTime.text = APIRequest.getDateTime(hour.dt,"hh:mm a",language)
            holder.tempHour.text = "${roundTheNumber(hour.temp)}${tempUnit}"
            APIRequest.setImageInView(context,hour.weather[0].icon,holder.tempIconHourRow)
        }

    }
    fun setHours(hours: List<Hourly>){
        this.hours = hours
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return 24
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var hourTime = itemView.findViewById<TextView>(R.id.hourTime)
        var tempHour = itemView.findViewById<TextView>(R.id.tempHour)
        var tempIconHourRow = itemView.findViewById<CircleImageView>(R.id.tempIconHourRow)
    }
}