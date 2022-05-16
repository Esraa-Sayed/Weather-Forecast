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
import androidx.appcompat.app.AlertDialog
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
import com.example.weatherforecast.ShowFavPlaceDetails.view.ShowFavPlaceDetails
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.repo.Repository
import com.example.weatherforecast.viewModel.MainSettingFavouriteViewModel
import com.example.weatherforecast.viewModel.MainSettingFavouriteViewModelFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavouriteFragment : Fragment(),OnItemClickListener {
    private  lateinit var favouriatePlaces: RecyclerView
    private  lateinit var favouritePlacesAdapter: FavouriteRecyclerViewAdapter
    private  lateinit var addFavFloatingActionButton: FloatingActionButton

    private lateinit var favouriteFavouriteViewModelFactory:MainSettingFavouriteViewModelFactory
    private lateinit var favouriteFavouriteViewModel: MainSettingFavouriteViewModel
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

        favouriteFavouriteViewModelFactory = MainSettingFavouriteViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(), ConcreteLocalSource(view.context),view.context))
        favouriteFavouriteViewModel = ViewModelProvider(this,favouriteFavouriteViewModelFactory)[MainSettingFavouriteViewModel::class.java]


        favouriatePlaces = view.findViewById(R.id.favRecyclerView)
        favouritePlacesAdapter = FavouriteRecyclerViewAdapter(view.context,this, emptyList(),favouriteFavouriteViewModel.readStringFromSharedPreferences(SharedPrefrencesKeys.language))
        var layoutMan = LinearLayoutManager(activity)
        favouriatePlaces.apply {
            setHasFixedSize(true)
            layoutMan.orientation = RecyclerView.VERTICAL
            layoutManager = layoutMan
            adapter = favouritePlacesAdapter
        }

        addFavFloatingActionButton = view.findViewById(R.id.addFavFloatingActionButton)
        addListenerTOFloatingActionButton()

        favouriteFavouriteViewModel.getLocalFavouriate().observe(viewLifecycleOwner, Observer {
            sendDataToAdapter(it)
        })
    }

    private fun sendDataToAdapter(places: List<FavouriteModel>) {
            favouritePlacesAdapter.newData(places)
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
        favouriteFavouriteViewModel.insertFavouriatePlace(favouriateModel)
    }

    override fun onStart() {
        super.onStart()
        favouriteFavouriteViewModel.getLocalFavouriate().observe(viewLifecycleOwner, Observer {
            sendDataToAdapter(it)
        })
    }

    override fun onRowClicked(favouriteModel: FavouriteModel) {
       val intent = Intent(activity, ShowFavPlaceDetails::class.java)
        intent.putExtra(IntentKeys.FAVOURITE_Place,favouriteModel)
        startActivity(intent)
    }

    override fun onDeleteIconClicked(favouriteModel: FavouriteModel) {
        showConfirmDialog(favouriteModel)
    }
    private fun showConfirmDialog(favouriteModel: FavouriteModel) {
        val builder: AlertDialog.Builder =  AlertDialog.Builder(myView.context)
        builder.setCancelable(true)
        builder.setTitle(getString( R.string.are_You_Sure))
        builder.setMessage(getString(R.string.You_want_to_delete_this_palce))
        builder.setPositiveButton(getString(R.string.Confirm)) { _, _ ->
                deleteThisRowFromRoom(favouriteModel)
        }
        builder.setNegativeButton(android.R.string.cancel){ _, _ -> }

        val dialog: AlertDialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun deleteThisRowFromRoom(favouriteModel: FavouriteModel) {
            favouriteFavouriteViewModel.deleteFavouriateModel(favouriteModel)
    }
}