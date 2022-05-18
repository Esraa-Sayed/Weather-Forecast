package com.example.weatherforecast.AlertScreen.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Constants.APIRequest
import com.example.weatherforecast.FavouriteScreen.view.FavouriteRecyclerViewAdapter
import com.example.weatherforecast.Model.Daily
import com.example.weatherforecast.Model.UserAlerts
import com.example.weatherforecast.R
import java.text.SimpleDateFormat
import java.util.*

class AlertsRecyclerViewAdapter(private val context: Context,
                                private val onButtonClickListener:OnButtonClickListener,
                                private var userAlerts:List<UserAlerts>,
                                private val language:String):RecyclerView.Adapter<AlertsRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.alerts_row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userAlert = userAlerts[position]
        holder.startAlertDateTextViewRow.text = APIRequest.convertTimeInMillisIntoString(userAlert.startLongDate,"dd/MM/yyyy",language)
        holder.startAlertTimeTextViewRow.text = APIRequest.convertTimeInMillisIntoString(userAlert.startLongDate,"HH:mm a",language)
        holder.endAlertDateTextViewRow.text = APIRequest.convertTimeInMillisIntoString(userAlert.endLongDate,"dd/MM/yyyy",language)
        holder.endAlertTimeTextViewRow.text = APIRequest.convertTimeInMillisIntoString(userAlert.endLongDate,"HH:mm a",language)
        holder.deleteThisAlertRow.setOnClickListener {
            onButtonClickListener.onDeleteButtonClickListener(userAlert)
        }
    }

    override fun getItemCount(): Int {
        return userAlerts.size
    }
    fun updateData(userAlerts:List<UserAlerts>){
        this.userAlerts = userAlerts
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val startAlertTimeTextViewRow:TextView = itemView.findViewById(R.id.startAlertTimeTextViewRow)
        val startAlertDateTextViewRow:TextView = itemView.findViewById(R.id.startAlertDateTextViewRow)
        val endAlertTimeTextViewRow:TextView = itemView.findViewById(R.id.endAlertTimeTextViewRow)
        val endAlertDateTextViewRow:TextView = itemView.findViewById(R.id.endAlertDateTextViewRow)
        val deleteThisAlertRow:ImageButton = itemView.findViewById(R.id.deleteThisAlertRow)
    }
}