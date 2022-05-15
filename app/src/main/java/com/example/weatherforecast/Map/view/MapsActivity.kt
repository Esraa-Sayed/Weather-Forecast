package com.example.weatherforecast.Map.view

import android.Manifest
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
import java.util.*
import kotlin.collections.ArrayList
import androidx.appcompat.app.AlertDialog
import com.example.weatherforecast.Constants.IntentKeys


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapSearchEdittext: EditText
    private lateinit var googleMap: GoogleMap

    private lateinit var viewModel: ViewModelMainActivtyAndSetting
    private lateinit var viewModelFactory: ViewModelMainActivtyAndSettingFactory
    companion object{
      const val DEFAULT_ZOOM = 5f

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
        val geocoder = Geocoder(this, Locale(getAppLangauge()))
        var list = ArrayList<Address>()
        try {
             list = geocoder.getFromLocationName(textSearch,1) as ArrayList<Address>
        }catch (e:IOException){
            Log.e("TAG", "geolocate: IOException: ${e.message}" )
        }
        if (list.size > 0){
            var address = list[0]
            Log.e("TAG", "geolocate: address: $address" )
            moveCamera(address.latitude, address.longitude)
            addMarker(address.latitude, address.longitude, address.adminArea)
            showConfirmDialog(LatLng(address.latitude,address.longitude))
        }

    }
    private fun getLongitude():Float{

        return viewModel.readFloatFromSharedPreferences(SharedPrefrencesKeys.longitude)
    }
    private fun getLatitude():Float{

        return viewModel.readFloatFromSharedPreferences(SharedPrefrencesKeys.latitude)
    }
    fun moveCamera(latitude:Double,longitude:Double){
        val currentLocation = LatLng(latitude, longitude)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,DEFAULT_ZOOM))
    }
    fun addMarker(latitude:Double, longitude:Double, title:String){
        val currentLocation = LatLng(latitude, longitude)
        googleMap.clear();
        googleMap.addMarker(MarkerOptions().position(currentLocation).title(title))


    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        moveCamera(getLatitude().toDouble(), getLongitude().toDouble())
        addMarker(getLatitude().toDouble(), getLongitude().toDouble(), "")
        googleMap.setOnMapClickListener {
            addMarker(it.latitude, it.longitude, "new")
            showConfirmDialog(it)
        }
     //   addMyCurrentLocation(googleMap)

    }
    private fun getAppLangauge():String{
        return viewModel.readStringFromSharedPreferences(SharedPrefrencesKeys.language)
    }
    private fun showConfirmDialog(latLng: LatLng) {
        val builder: AlertDialog.Builder =  AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(getString(R.string.confirmation_message))
        builder.setMessage(getString(R.string.are_You_Sure))
        builder.setPositiveButton(getString(R.string.Confirm)) { dialog, which ->
            if ( whoOpenMapActivity() == IntentKeys.SETTING_ACTIVITY){
                Log.e("TAG", "geolocate: addressYYYYYYYYYYES" )
                saveLocationOnSharedPrefrences(latLng)
                finish()
            }
        }
        builder.setNegativeButton(android.R.string.cancel){ dialog, which -> }

        val dialog: AlertDialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
    private fun whoOpenMapActivity():String{
        return intent.getStringExtra(IntentKeys.COME_FROM).toString()
    }
    private fun saveLocationOnSharedPrefrences(latLng: LatLng){
        viewModel.changeSettingFloat(SharedPrefrencesKeys.latitude, latLng.latitude.toFloat())
        viewModel.changeSettingFloat(SharedPrefrencesKeys.latitude, latLng.longitude.toFloat())
        val cityNameAr = SharedPrefrencesKeys.getCityNameFromLatAndLong(this,"ar", latLng.latitude,latLng.longitude)
        val cityNameEn = SharedPrefrencesKeys.getCityNameFromLatAndLong(this,"en", latLng.latitude,latLng.longitude)
        viewModel.changeSettingStrings(SharedPrefrencesKeys.cityArabic,cityNameAr)
        viewModel.changeSettingStrings(SharedPrefrencesKeys.cityEnglish,cityNameEn)
    }
}