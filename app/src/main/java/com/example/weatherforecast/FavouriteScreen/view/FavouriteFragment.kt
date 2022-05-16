package com.example.weatherforecast.FavouriteScreen.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Constants.IntentKeys
import com.example.weatherforecast.FavouriteScreen.viewModel.FavouriteViewModel
import com.example.weatherforecast.Map.view.MapsActivity
import com.example.weatherforecast.Map.viewModel.FavouriteViewModelFactory
import com.example.weatherforecast.Model.FavouriteModel
import com.example.weatherforecast.Network.WeatherClient
import com.example.weatherforecast.R
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.repo.Repository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavouriteFragment : Fragment() {
    private  lateinit var favouriatePlaces: RecyclerView
    private  lateinit var favouritePlacesAdapter: FavouriteRecyclerViewAdapter
    private  lateinit var addFavFloatingActionButton: FloatingActionButton

    private lateinit var favouriteViewModelFactory:FavouriteViewModelFactory
    private lateinit var favouriteViewModel: FavouriteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favouriate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }
    private fun init(view: View) {

        favouriteViewModelFactory = FavouriteViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(), ConcreteLocalSource(view.context),view.context))
        favouriteViewModel = ViewModelProvider(this,favouriteViewModelFactory)[FavouriteViewModel::class.java]


        favouriatePlaces = view.findViewById(R.id.favRecyclerView)
        favouritePlacesAdapter = FavouriteRecyclerViewAdapter(view.context, emptyList(),favouriteViewModel.getAppLanguage())
        var layoutMan = LinearLayoutManager(activity)
        favouriatePlaces.apply {
            setHasFixedSize(true)
            layoutMan.orientation = RecyclerView.VERTICAL
            layoutManager = layoutMan
            adapter = favouritePlacesAdapter
        }

        addFavFloatingActionButton = view.findViewById(R.id.addFavFloatingActionButton)
        addListenerTOFloatingActionButton()

        favouriteViewModel.getLocalFavouriate().observe(viewLifecycleOwner, Observer {
            sendDataToAdapter(it)
        })
    }

    private fun sendDataToAdapter(places: List<FavouriteModel>) {
        if(!places.isNullOrEmpty()){
            favouritePlacesAdapter.newData(places)
        }
    }

    private fun addListenerTOFloatingActionButton(){
        addFavFloatingActionButton.setOnClickListener {
            val intent = Intent(activity,MapsActivity::class.java)
            intent.putExtra(IntentKeys.COME_FROM,IntentKeys.FAV_ACTIVITY)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        favouriteViewModel.getLocalFavouriate().observe(viewLifecycleOwner, Observer {
            sendDataToAdapter(it)
        })
    }
}