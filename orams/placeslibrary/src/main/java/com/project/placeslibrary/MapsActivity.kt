package com.project.placeslibrary

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.project.placeslibrary.LocationEnabledUtil.OnLocationActionCallback
import com.project.placeslibrary.LocationUtil.MyLocationListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import org.jetbrains.anko.toast
import org.json.JSONException
import org.json.JSONObject


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
  
  private lateinit var mMap: GoogleMap
  val LOCATION_PERM_RC = 2
  val LOCATION_ENABLE_RC = 3
  @SuppressWarnings("deprecation")
  lateinit var progress: ProgressDialog
  lateinit var locationUtil: LocationUtil
  lateinit var currentLocation: Location
  lateinit var currentLatLng: LatLng
  lateinit var type: String
  
  private val locationActionCallback = object : OnLocationActionCallback {
    override fun onEnabled() {
      locationUtil = LocationUtil.getInstance(applicationContext,
          object : MyLocationListener {
            override fun onLocationUpdate(location: Location, isFirstUpdate: Boolean) {
              Handler().postDelayed(Runnable {
                currentLocation = location
                currentLatLng = LatLng(location.latitude, location.longitude)
                setup()
              }, 2000)
            }
          })
      if (locationUtil.isReady) {
        Handler().postDelayed(Runnable {
          currentLocation = locationUtil.location
          currentLatLng = LatLng(locationUtil.location.latitude, locationUtil.location.longitude)
          setup()
        }, 2000)
      }
    }
    
    override fun onCancelled() {
      Toast.makeText(this@MapsActivity, "Please enable GPS to continue using this app",
          Toast.LENGTH_SHORT).show()
      this@MapsActivity.finish()
    }
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_maps)
    type = intent.getStringExtra("type")
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    progress = ProgressDialog.show(this, "Please wait", "Loading map...")
    mapFragment.getMapAsync(this)
  }
  
  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap
    checkLocationPerm()
  }
  
  override fun onResume() {
    super.onResume()
    //checkLocationPerm()
  }
  
  fun checkLocationPerm() {
    if (ActivityCompat.checkSelfPermission(this@MapsActivity,
        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this@MapsActivity,
          arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERM_RC)
    } else {
      LocationEnabledUtil.checkAndAskLocation(this@MapsActivity,
          locationActionCallback,
          LOCATION_ENABLE_RC)
    }
  }
  
  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
      grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    when (requestCode) {
      LOCATION_PERM_RC ->
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          checkLocationPerm()
        } else {
          Toast.makeText(this@MapsActivity, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }
  }
  
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    when (requestCode) {
      LOCATION_ENABLE_RC ->
        if (resultCode == Activity.RESULT_OK) {
          locationActionCallback.onEnabled()
        } else {
          locationActionCallback.onCancelled()
        }
    }
  }
  
  fun setup() {
    try {
      progress.setMessage("Getting current location...")
      currentLatLng = locationUtil.latLng
      currentLocation = locationUtil.location
      mMap.isMyLocationEnabled = true
      my_location_button.setOnClickListener {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13f))
      }
      mMap.uiSettings.isMyLocationButtonEnabled = false
      mMap.uiSettings.setAllGesturesEnabled(true)
      mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13f))
      progress.setMessage("Loading nearby places...")
      VolleySingleton.getInstance(applicationContext).addToRequestQueue(StringRequest(
          Request.Method.POST, buildLink(type, currentLatLng), Response.Listener { response ->
        Log.e("nearby resp", response)
        parsePlaces(response)
      }, Response.ErrorListener { error ->
        error.printStackTrace()
        progress.dismiss()
      }), "nearby")
      //progress.dismiss()
    } catch (e: Exception) {
      e.printStackTrace()
      Toast.makeText(this, "An unkown error has occurred", Toast.LENGTH_SHORT).show()
      progress.dismiss()
    }
  }
  
  fun buildLink(type: String, latLng: LatLng): String {
    val link = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=${getString(
        R.string.api_key)}&location=${latLng.latitude},${latLng.longitude}&radius=10000&type=$type"
    Log.e("nearby link", link)
    return link
  }
  
  fun buildLinkWithPageToken(pageToken: String): String {
    val link = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=${getString(
        R.string.api_key)}&pagetoken=$pageToken"
    Log.e("nextPageToken link", link)
    return link
  }
  
  fun loadNextPlaces(pageToken: String): Unit {
    VolleySingleton.getInstance(applicationContext).addToRequestQueue(StringRequest(
        Request.Method.POST, buildLinkWithPageToken(pageToken), Response.Listener { response ->
      Log.e("pageToken resp", response)
      parsePlaces(response)
    }, Response.ErrorListener { error ->
      error.printStackTrace()
      progress.dismiss()
    }), "pageToken")
  }
  
  fun parsePlaces(response: String) {
    try {
      val mainResp = JSONObject(response)
      if (mainResp.getString("status") == "OK") {
        val resultsResp = mainResp.getJSONArray("results")
        for (i in 0 until resultsResp.length()) {
          val place = resultsResp.getJSONObject(i)
          val loc = place.getJSONObject("geometry").getJSONObject("location")
          val latLng = LatLng(loc.getDouble("lat"), loc.getDouble("lng"))
          val name = place.getString("name")
          mMap.addMarker(MarkerOptions().position(latLng).title(name).snippet("Get directions")).tag = place.getString("place_id") as String
        }
        if (mainResp.has("next_page_token")) {
          Handler().postDelayed({loadNextPlaces(mainResp.getString("next_page_token"))}, 2000)
        } else {
          mMap.setOnInfoWindowClickListener(this@MapsActivity)
          progress.dismiss()
        }
      } else {
        toast(mainResp.getString("status"))
        progress.dismiss()
      }
    } catch (e: JSONException) {
      e.printStackTrace()
      progress.dismiss()
    }
  }
  
  override fun onInfoWindowClick(marker: Marker?) {
    Log.e("tag", marker?.tag as String)
    val link = "https://www.google.com/maps/dir/?api=1&destination_place_id=${marker.tag as String}&destination=${marker.position.latitude},${marker.position.longitude}"
    Log.e("maps link", link)
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
  }
}
