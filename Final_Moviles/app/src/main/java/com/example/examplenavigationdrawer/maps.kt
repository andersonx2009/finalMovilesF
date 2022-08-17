package com.example.examplenavigationdrawer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.example.examplenavigationdrawer.databinding.ActivityMapsBinding
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class maps : AppCompatActivity(), OnMapReadyCallback {

    var latitud : Double= 0.0
    var longitud : Double=0.0
    private val firebaseDatabase = FirebaseFirestore.getInstance()
private lateinit var tmpRealTimeMarkers : ArrayList<Marker>
    private lateinit var RealTimeMarkers : ArrayList<Marker>
    private lateinit var mMap : GoogleMap
    private lateinit var binding: ActivityMapsBinding
    val mCurrentLocation = MutableLiveData<LocationModel>()
    private val SECOND = 1000L
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    val locationCallbackRequest = LocationRequest.create().apply {
        interval = 10 * SECOND
        fastestInterval = 5 * SECOND
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private val logTAG = maps::class.java.simpleName
    lateinit var locationCallback: LocationCallback

    fun startLocationTracking(context: Context){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationCallback = object: LocationCallback(){
            override fun onLocationResult(locations: LocationResult) {
                super.onLocationResult(locations)
                for(location in locations.locations){
                    mCurrentLocation.postValue(LocationModel(location, Calendar.getInstance().timeInMillis))
                }

            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            PermissionHelper.askForLocationPermission(this)
            return
        }
        mFusedLocationClient.requestLocationUpdates(locationCallbackRequest, locationCallback, null)
    }

    fun stopLocationTracking(context: Context){
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val botonRegresarIniciarSesion = findViewById<ImageButton>(R.id.ButtonBackToLogin)

        botonRegresarIniciarSesion.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }

    }

    override fun onPause() {
        super.onPause()
        stopLocationTracking(this)
    }

    override fun onResume(){
        super.onResume()
        checkPermissionAndSetView()
    }

    private fun checkPermissionAndSetView() {
        if(PermissionHelper.arePermissionGranted(this)) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
            startLocationTracking(this)
        } else {
            PermissionHelper.askForLocationPermission(this)

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PermissionHelper.REQUEST_CODE_FOR_PERMISSION) {
            checkPermissionAndSetView()
        }
    }


    /**o
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    // Ubicaciones
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        tmpRealTimeMarkers= ArrayList<Marker>()
        RealTimeMarkers= ArrayList<Marker>()

        firebaseDatabase.collection("tiendas").get().addOnSuccessListener { results->
            for(document in results){
                if(document!=null){
                    val location = LatLng( document.getString("latitud").toString().toDouble(), document.getString("longitud").toString().toDouble())
                        val markerOptions:MarkerOptions = MarkerOptions()
                    markerOptions.position(location)
                    tmpRealTimeMarkers.add(mMap.addMarker(markerOptions))

                }
                latitud =document.getString("latitud").toString().toDouble()
                longitud =document.getString("longitud").toString().toDouble()
                RealTimeMarkers.clear()
                RealTimeMarkers.addAll(tmpRealTimeMarkers)

            }
        }


        mCurrentLocation.observe(this, androidx.lifecycle.Observer {
            Log.e(logTAG, "${it.location.latitude}, ${it.location.longitude}, ${it.timeStamp}")


            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng( latitud, longitud)))
        })

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.setMinZoomPreference(10f)
        mMap.setMaxZoomPreference(18f)

        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(-34.0, 151.0)
        //mMap.addMarker(MarkerOptions().position(location).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }


}
