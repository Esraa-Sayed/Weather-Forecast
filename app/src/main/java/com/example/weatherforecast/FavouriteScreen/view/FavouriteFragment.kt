package com.example.weatherforecast.FavouriteScreen.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Constants.APIRequest.getFullAddress
import com.example.weatherforecast.Constants.IntentKeys
import com.example.weatherforecast.Constants.IntentKeys.REPLY_INTENT_KEY
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.Map.view.MapsActivity
import com.example.weatherforecast.Model.FavouriteModel
import com.example.weatherforecast.Network.WeatherClient
import com.example.weatherforecast.R
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.repo.Repository
import com.example.weatherforecast.viewModel.ViewModelMainActivtyAndSetting
import com.example.weatherforecast.viewModel.ViewModelMainActivtyAndSettingFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavouriteFragment : Fragment() {
    private  lateinit var favouriatePlaces: RecyclerView
    private  lateinit var favouritePlacesAdapter: FavouriteRecyclerViewAdapter
    private  lateinit var addFavFloatingActionButton: FloatingActionButton

    private lateinit var favouriteViewModelFactory:ViewModelMainActivtyAndSettingFactory
    private lateinit var favouriteViewModel: ViewModelMainActivtyAndSetting
    private lateinit var myView: View


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
        myView = view
        init(view)
    }
    private fun init(view: View) {

        favouriteViewModelFactory = ViewModelMainActivtyAndSettingFactory(
            Repository.getInstance(
                WeatherClient.getInstance(), ConcreteLocalSource(view.context),view.context))
        favouriteViewModel = ViewModelProvider(this,favouriteViewModelFactory)[ViewModelMainActivtyAndSetting::class.java]


        favouriatePlaces = view.findViewById(R.id.favRecyclerView)
        favouritePlacesAdapter = FavouriteRecyclerViewAdapter(view.context, emptyList(),favouriteViewModel.readStringFromSharedPreferences(SharedPrefrencesKeys.language))
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
            requestAddFavoritePlace.launch(intent)
        }
    }

    private val requestAddFavoritePlace =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val latLng = data?.getParcelableExtra<LatLng>(REPLY_INTENT_KEY)
                    if (latLng != null) {
                        Log.d("asdfg:", "Data ****** ${latLng.longitude}")
                        addPlaceInRoom(latLng)
                    }
                }
            }
    private fun addPlaceInRoom(latLng: LatLng) {
        val latitude = latLng.latitude
        val longitude = latLng.longitude
        val addressAr = getFullAddress(latitude,longitude,"ar",myView.context)
        val addressEn = getFullAddress(latitude,longitude,"en",myView.context)
        val favouriateModel = FavouriteModel(latitude,longitude, addressAr, addressEn)
        favouriteViewModel.insertFavouriatePlace(favouriateModel)
    }

    override fun onStart() {
        super.onStart()
        favouriteViewModel.getLocalFavouriate().observe(viewLifecycleOwner, Observer {
            sendDataToAdapter(it)
        })
    }
}