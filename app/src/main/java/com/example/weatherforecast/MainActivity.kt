package com.example.weatherforecast

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.weatherforecast.repo.Repository
import com.example.weatherforecast.Setting.view.SettingActivity
import com.example.weatherforecast.viewModel.MainSettingFavouriteViewModel
import com.example.weatherforecast.viewModel.MainSettingFavouriteViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView;
    lateinit var favouriteViewModel: MainSettingFavouriteViewModel
    lateinit var favouriteViewModelFactory: MainSettingFavouriteViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents(applicationContext)
    }
    override fun onStart() {
        super.onStart()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                12
            )
        } else {
            getCurrentLocation()
        }
    }
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(){

        var locationManager:LocationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            favouriteViewModel.setDataToSharedPrefInFirstTime(this)
        }
        else {
            //when location servies is not enabled
            //open location setting
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 12 && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else if (requestCode == 12 && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
           Toast.makeText(
               getBaseContext(),
                "Permission denied",
                Toast.LENGTH_LONG,

            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.setting_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
               startActivity(Intent(this, SettingActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun initComponents(context:Context){
        setTitle(R.string.app_name);
        bottomNavigationView = findViewById(R.id.bottomnavigation)
        val navController2 = findNavController(this, R.id.nav_host_fragment_activity_main)
        favouriteViewModelFactory =  MainSettingFavouriteViewModelFactory(
            Repository.getInstance(null, null)
        )
        favouriteViewModel = ViewModelProvider(this, favouriteViewModelFactory)[MainSettingFavouriteViewModel::class.java]
        setupWithNavController(bottomNavigationView, navController2)

    }
}