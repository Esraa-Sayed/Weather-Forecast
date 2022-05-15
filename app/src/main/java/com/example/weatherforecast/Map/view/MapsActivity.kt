package com.example.weatherforecast.Map.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.weatherforecast.databinding.ActivityMapsBinding
import com.example.weatherforecast.repo.Repository
import com.example.weatherforecast.viewModel.ViewModelMainActivtyAndSetting
import com.example.weatherforecast.viewModel.ViewModelMainActivtyAndSettingFactory
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapSearchEdittext: EditText

    private lateinit var viewModel: ViewModelMainActivtyAndSetting
    private lateinit var viewModelFactory: ViewModelMainActivtyAndSettingFactory
    companion object{
      const val DEFAULT_ZOOM = 8f

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        initComponents()
    }
    private fun  initComponents(){
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModelFactory =  ViewModelMainActivtyAndSettingFactory(
            Repository.getInstance(null, null,this)
        )
        viewModel = ViewModelProvider(this, viewModelFactory)[ViewModelMainActivtyAndSetting::class.java]
        mapSearchEdittext = binding.mapSearchEdittext
        initSearch()

    }
    private fun initSearch(){
        mapSearchEdittext.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH ||
                event.action == KeyEvent.ACTION_DOWN ||
                event.action == KeyEvent.KEYCODE_ENTER ){
                geoLocate()
                true
            } else {
                false
            }
        }
    }
    private fun geoLocate(){
        val textSearch = mapSearchEdittext.text.toString()
        val geocoder = Geocoder(this)
        var list = ArrayList<Address>()
        try {
             list = geocoder.getFromLocationName(textSearch,1) as ArrayList<Address>
        }catch (e:IOException){
            Log.e("TAG", "geolocate: IOException: ${e.message}" )
        }
        if (list.size > 0){
            var address = list[0]
            Log.e("TAG", "geolocate: address: $address" )
        }

    }
    private fun getLongitude():Float{

        return viewModel.readFloatFromSharedPreferences(SharedPrefrencesKeys.longitude)
    }
    private fun getLatitude():Float{

        return viewModel.readFloatFromSharedPreferences(SharedPrefrencesKeys.latitude)
    }
    fun moveCamera(googleMap: GoogleMap){
        val currentLocation = LatLng(getLatitude().toDouble(), getLongitude().toDouble())
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,DEFAULT_ZOOM))
    }
    fun addMarker(googleMap: GoogleMap){
        val currentLocation = LatLng(getLatitude().toDouble(), getLongitude().toDouble())
        googleMap.addMarker(MarkerOptions().position(currentLocation))
    }

    fun addMyCurrentLocation(googleMap: GoogleMap){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                12
            )
        }
        googleMap.isMyLocationEnabled = true
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        moveCamera(googleMap)
        addMarker(googleMap)
     //   addMyCurrentLocation(googleMap)

    }
}