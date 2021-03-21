package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.cache.DatabaseService
import com.example.weatherapp.util.PermissionUtils
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var database: DatabaseService

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }

    //weather url to get JSON
    var weather_url1 = ""
    var lat = ""
    var lng = ""

    //api id for url
    var api_id1 = "c1a50c737f54fc0017c6e655ee224a42"
    private lateinit var textViewTemp: TextView
    private lateinit var textViewDate: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewTemp = findViewById(R.id.textViewTemp)
        textViewDate = findViewById(R.id.textViewDate)

        Log.e("lat", weather_url1)

        btVar1.setOnClickListener {
            Log.e("lat", "onClick")
            obtainLocation()
        }

    }

    @SuppressLint("MissingPermission")
    private fun obtainLocation() {
        Log.e("lat", "function")
        weather_url1 = "http://api.openweathermap.org/data/2.5/weather?"+"lat=" + lat +"&lon="+ lng +"&appid="+ api_id1
        getTemp()
    }

    fun getTemp() {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url: String = weather_url1
        Log.e("lat", url)
        // Request a string response from the provided URL.
        val stringReq = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    val obj = JSONObject(response)
                    val updatedAt: Long = obj.getLong("dt")
                    val updatedAtText = "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt * 1000))
                    val arr = obj.getJSONObject("main")
                    val obj1 =  "Temperature: " + arr.getString("temp")
                    val obj2 = "City: " + obj.getString("name")
                    val arr1 = obj.getJSONObject("sys")
                    val obj3 = "country:" + arr1.getString("country")
                    textViewTemp.text = obj1
                    textViewDate.text = updatedAtText
                    TextViewCity.text = obj2
                    TextViewCountry.text = obj3
                },
                //In case of any error
                Response.ErrorListener { textViewTemp!!.text = "That didn't work!" })
        queue.add(stringReq)
    }

    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {
                            //  latTextView.text = location.latitude.toString()
                            // lngTextView.text = location.longitude.toString()
                            lat = location.latitude.toString()
                            lng = location.longitude.toString()
                        }
                        // Few more things we can do here:
                        // For example: Update the location of user on server
                    }
                },
                Looper.myLooper()
        )
    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                        this,
                        LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                            this,
                            getString(R.string.location_permission_not_granted),
                            Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

