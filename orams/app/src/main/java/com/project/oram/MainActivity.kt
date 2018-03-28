package com.project.oram

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.project.AppConstants.ORAM.AppManager
import com.project.medbox.AppConstants
import com.project.oram.R.string.google_maps_key
import com.project.placeslibrary.MapsActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.android.PolyUtil
import com.google.maps.model.TrafficModel
import com.google.maps.model.TravelMode
import com.yps.locationlibrary.LocationUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
  override fun onMapReady(p0: GoogleMap?) {
    googlemap = p0

    when (accident) {
      "A" -> {
        val sourcelatlong = LatLng(sourcelat!!.toDouble(), sourcelong!!.toDouble())
        addMarker(sourcelatlong, "My location", "source")

        val destinationlatlong = LatLng(destinationlat!!.toDouble(), destinationlong!!.toDouble())
        addMarker(destinationlatlong, "Car Location", "destination")

        googlemap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationlatlong, 16F))

        val results = DirectionsApi
            .newRequest(getGeoContext())
            .mode(TravelMode.DRIVING)
            .origin(com.google.maps.model.LatLng(sourcelatlong.latitude, sourcelatlong.longitude))
            .destination(com.google.maps.model.LatLng(destinationlat!!.toDouble(), destinationlong!!.toDouble()))
            .optimizeWaypoints(true)
            .departureTime(DateTime())
            .trafficModel(TrafficModel.OPTIMISTIC)
            .await()

        if (results.routes.isNotEmpty()) {
          val decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath())
          googlemap?.addPolyline(PolylineOptions().addAll(decodedPath).color(resources.getColor(R.color.colorAccent)).width(20f).zIndex(Float.MAX_VALUE))
        } else {
          toast("No routes found")
        }
      }

      "B" -> {
        val destinationlatlong = LatLng(destinationlat!!.toDouble(), destinationlong!!.toDouble())
        addMarker(destinationlatlong, "Car Location", "destination")

        googlemap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationlatlong, 16F))
      }
    }
  }

  fun getGeoContext(): GeoApiContext {
    val geoApiContext = GeoApiContext.Builder()
        .apiKey(getString(google_maps_key))
        .connectTimeout(30, TimeUnit.SECONDS)
        .maxRetries(10)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)

    return geoApiContext.build()
  }

  var requestQueue: RequestQueue? = null
  var phone: String? = ""
  var googlemap: GoogleMap? = null
  var sourcelat: String? = ""
  var sourcelong: String? = ""
  var destinationlat: String? = ""
  var destinationlong: String? = ""
  var l: LocationUtil? = null
  var accident: String? = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    showPlacesFAM()
    phone = AppManager.spGetString(this@MainActivity, AppConstants.PHONE)

    if (phone.equals("")) {
      AppManager.logout(this@MainActivity)
    }

    l = LocationUtil.with(this@MainActivity)

    requestQueue = Volley.newRequestQueue(this@MainActivity)

    accident = AppManager.spGetString(this@MainActivity, AppConstants.ACCIDENT)
    if (!accident.equals("")) {

      when (accident) {
        "A" -> {
          sourcelat = l!!.getLat().toString()
          sourcelong = l!!.getLon().toString()
          destinationlat = AppManager.spGetString(this@MainActivity, AppConstants.DESTINATIONLAT)
          destinationlong = AppManager.spGetString(this@MainActivity, AppConstants.DESTINATIONLON)
        }

        "B" -> {
          destinationlat = AppManager.spGetString(this@MainActivity, AppConstants.DESTINATIONLAT)
          destinationlong = AppManager.spGetString(this@MainActivity, AppConstants.DESTINATIONLON)
        }
      }
      val mapFragment: MapFragment = fragmentManager.findFragmentById(R.id.map_view) as MapFragment
      mapFragment.getMapAsync(this@MainActivity)
    }

    /*Handler().postDelayed(Runnable {
        accident = AppManager.spGetString(this@MainActivity,AppConstants.ACCIDENT)
        if (!accident.equals(""))
        {
            when(accident)
            {
                "A" ->
                {
                    sourcelat = l!!.getLat().toString()
                    sourcelong = l!!.getLon().toString()
                    destinationlat = AppManager.spGetString(this@MainActivity,AppConstants.DESTINATIONLAT)
                    destinationlong = AppManager.spGetString(this@MainActivity,AppConstants.DESTINATIONLON)
                }

                "B" ->
                {
                    destinationlat = AppManager.spGetString(this@MainActivity,AppConstants.DESTINATIONLAT)
                    destinationlong = AppManager.spGetString(this@MainActivity,AppConstants.DESTINATIONLON)
                }
            }
            val mapFragment:MapFragment = fragmentManager.findFragmentById(R.id.map_view) as MapFragment
            mapFragment.getMapAsync(this@MainActivity)
        }
    },60000)*/
  }

  fun addMarker(latLng: LatLng, title: String, type: String) {
    var options: MarkerOptions = MarkerOptions()
    options.position(latLng)
    options.title(title)

    if (type.equals("source"))
      options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
    else if (type.equals("destination"))
      options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
    else
      options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))

    googlemap!!.addMarker(options)
  }

  protected fun showPlacesFAM() {
    fan_places.visibility = View.VISIBLE
    fab_bus_stop.setOnClickListener {
      sendType("bus_station")
    }
    fab_atm.setOnClickListener {
      sendType("atm")
    }
    fab_hospital.setOnClickListener {
      sendType("hospital")
    }
    fab_restaurant.setOnClickListener {
      sendType("restaurant")
    }
    fab_airport.setOnClickListener {
      sendType("airport")
    }
    fab_police_station.setOnClickListener {
      sendType("police")
    }
  }

  private fun sendType(type: String) {
    startActivity<MapsActivity>("type" to type)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.logout -> {
        AppManager.logout(this@MainActivity)
        finish()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

}
