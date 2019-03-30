package com.edu.uabc.appm.mymapa

import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.contracts.Returns

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient  //variable de inicializacion tardia, no se requiere inicializacion
    private lateinit var  lastLocation: Location  //conocer su ultima localizacion

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

    }

    override fun onMarkerClick(p0: Marker?) = false//lo decimos que no trabaje con el metodo sino con toda la clase


    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient=LocationServices.getFusedLocationProviderClient(this)//para ver la ultima localizacion posible

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
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        mMap.setOnMarkerClickListener(this)
        mMap.uiSettings.isZoomControlsEnabled = true //abilitar zoom
        setUpMap()//llamado al metodo
    }
    private fun PonerIcono(location: LatLng){
        val opcionmarcada=MarkerOptions().position(location)
        mMap.addMarker(opcionmarcada)
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) //valida que tenga permiso en el archivo manifes
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION
                ), LOCATION_PERMISSION_REQUEST_CODE
            )//en caso de que no se tenga se pedora el permiso de la hubicacion
            return
        }
        mMap.isMyLocationEnabled= true //para ver el punto azul del usuario
        mMap.mapType=GoogleMap.MAP_TYPE_TERRAIN//cambiar el tipo de mapas
        fusedLocationClient.lastLocation.addOnSuccessListener (this){ location ->
            //ver mi hubicacion
            if (location!=null){
                  lastLocation=location
                val currentLatLong= LatLng(location.latitude, location.longitude)
                PonerIcono(currentLatLong)//enviar posicion del marcador
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong,10f))

            }
        }

        }
    }

