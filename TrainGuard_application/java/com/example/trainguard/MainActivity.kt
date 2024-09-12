package com.example.trainguard

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.trainguard.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mainBinding.personButton.setOnClickListener {
            getLocation()
        }
        mainBinding.trainButton.setOnClickListener {
            trainActivitySet()
        }
        val policy = ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)
    }

    @SuppressLint("MissingPermission")
    private fun getJustLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    if (task.result != null) {
                        val location: Location = task.result

                        Client.personLocationLatitude = location.latitude
                        Client.personLocationLongtitude = location.longitude
                    }
                }
            } else {
                //Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getRangeAndLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location = task.result
                    val res = floatArrayOf(.1f)

                    Client.personLocationLatitude = location.latitude
                    Client.personLocationLongtitude = location.longitude

                    Client.getArrayOfTrains()
                    Client.getRangeFromServer()
                    /*if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)!!
                        mainBinding.apply {
                            tvLatitude.text = "${res[0]}"//"Latitude\n${list[0]?.latitude}"
                            //tvLongitude.text = "Longitude\n${list[0]?.longitude}"
                            //tvCountryName.text = "Country Name\n${list[0].countryName}"
                            //tvLocality.text = "Locality\n${list[0].locality}"
                            //tvAddress.text = "Address\n${list[0].getAddressLine(0)}"
                        }
                    }*/
                }
            } else {
                //Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                val scope = CoroutineScope(Dispatchers.Default)
                scope.launch {
                    //Looper.prepare()
                    while (true) {
                        getJustLocation()
                        delay(1000)
                    }
                }

                val myIntent = Intent(this, TrainActivity::class.java)
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(myIntent)
                finish()
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }

    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun trainActivitySet() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                val scope = CoroutineScope(Dispatchers.Default)
                scope.launch {
                    //Looper.prepare()
                    while (true) {
                        getJustLocation()
                        delay(1000)
                    }
                }

                val myIntent = Intent(this, MashinistActivity::class.java)
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent)
                finish()
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Геолокация включена! Выберете роль!", Toast.LENGTH_LONG).show()
            }
        }
    }
}