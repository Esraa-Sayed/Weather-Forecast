package com.example.weatherforecast.FavouriteScreen.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Model.FavouriteModel
import com.example.weatherforecast.R

class FavouriteRecyclerViewAdapter(private var context: Context,private var onItemClickListener: OnItemClickListener, private var favPlaces:List<FavouriteModel>, private val language:String)
    :RecyclerView.Adapter<FavouriteRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.favouriate_row,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if ( language =="en")
                 holder.textCityName.text = favPlaces[position].addressEn
            else
                holder.textCityName.text = favPlaces[position].addressAr
            holder.itemView.setOnClickListener {
               onItemClickListener.onRowClicked(favPlaces[position])
            }
            holder.deleteThisRow.setOnClickListener {
                onItemClickListener.onDeleteIconClicked(favPlaces[position])
            }
    }
    fun newData(favPlaces:List<FavouriteModel>){
        this.favPlaces = favPlaces
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
       return favPlaces.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textCityName:TextView = itemView.findViewById(R.id.textCityFavName)
        var deleteThisRow:ImageButton = itemView.findViewById(R.id.deleteThisRow)
    }
}