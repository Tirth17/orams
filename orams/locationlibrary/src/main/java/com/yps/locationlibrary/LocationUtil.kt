package com.yps.locationlibrary

import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.karumi.dexter.Dexter
import io.nlopez.smartlocation.OnLocationUpdatedListener
import io.nlopez.smartlocation.SmartLocation
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.READ_CONTACTS
import android.content.IntentSender
import android.location.Address
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.karumi.dexter.listener.PermissionRequest
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider
import android.location.Geocoder
import android.widget.VideoView
import java.util.*


class LocationUtil constructor(private val ctx: Context,private val act:Activity) {
    private var location: Location? = null
    private var lat: Double = 0.toDouble()
    private var lon: Double = 0.toDouble()
    private var city: String? = null
    private var country:String? = null
    private var latLng: LatLng? = null
    var isReady = false

    private var myLocationListener: MyLocationListener? = null

    init {
        Log.e(TAG, "Instantiated")

        displayLocationSettingsRequest(act)

        Dexter.withActivity(act)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {/* ... */
                if (report.areAllPermissionsGranted())
                {
                    SmartLocation.with(ctx).location().start(object : OnLocationUpdatedListener {
                        override fun onLocationUpdated(location: Location) {
                            Log.e(TAG, "Location updated: " + location.toString())
                            this@LocationUtil.location = location
                            this@LocationUtil.lat = location.getLatitude()
                            this@LocationUtil.lon = location.getLongitude()
                            latLng = LatLng(location.getLatitude(), location.getLongitude())
                            Log.d("lat",lat.toString())
                            this@LocationUtil.city = getCity(act,latLng!!)
                            this@LocationUtil.country = getCountry(act,latLng!!)
                            isReady = true
                        }
                    })

                }
                else
                {
                    Toast.makeText(ctx,"Please grant Location permission",Toast.LENGTH_LONG).show()
                    act.finish()
                }
            }

            override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {/* ... */
            }
        }).check()
    }

    fun getLocation(): Location? {
        return if (isReady) {
            location
        } else null
    }

    fun getLatLng(): LatLng? {
        return if (isReady) {
            latLng
        } else null
    }

    fun getLat(): Double {
        return if (isReady) {
            lat
        } else 0.0
    }

    fun getLon(): Double {
        return if (isReady) {
            lon
        } else 0.0
    }

    fun getCity(): String? {
        return if (isReady) {
            city
        } else "not ready yet"
    }

    fun getCountry(): String? {
        return if (isReady) {
            country
        } else "not ready yet"
    }

    fun distance(lat1: Double, lat2: Double, lon1: Double,
                 lon2: Double, el1: Double, el2: Double): Double {
        val R = 6371 // Radius of the earth
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + (Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        var distance = R.toDouble() * c * 1000.0 // convert to meters
        val height = el1 - el2
        distance = Math.pow(distance, 2.0) + Math.pow(height, 2.0)
        return Math.sqrt(distance)
    }

    fun displayLocationSettingsRequest(context: Activity) {
        val googleApiClient = GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build()
        googleApiClient.connect()

        val locationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(10000)
        locationRequest.setFastestInterval(10000 / 2)

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback(object : ResultCallback<LocationSettingsResult> {
            override fun onResult(result: LocationSettingsResult) {
                val status = result.getStatus()
                when (status.getStatusCode()) {
                    LocationSettingsStatusCodes.SUCCESS -> Log.i("Location", "All location settings are satisfied.")
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i("Location", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ")

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(context, LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS)
                        } catch (e: IntentSender.SendIntentException) {
                            Log.i("Location", "PendingIntent unable to execute request.")
                        }

                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.i("Location", "Location settings are inadequate, and cannot be fixed here. Dialog not created.")
                }
            }
        })
    }

    fun getCity(ctx: Context, latLng: LatLng): String {
        var city = ""
        if (Geocoder.isPresent()) {
            try {
                val geocoder = Geocoder(ctx, Locale.getDefault())
                var addresses: List<Address>? = null
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addresses != null && !addresses.isEmpty()) {
                    city = addresses[0].getLocality()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            Toast.makeText(ctx, "Failed to get city from location. Please restart your device and try again", Toast.LENGTH_SHORT).show()
        }
        return city
    }

    fun getCountry(ctx:Context, latLng:LatLng):String? {
        var country:String = ""
        if (Geocoder.isPresent()) {
            try {
                var geocoder:Geocoder = Geocoder(ctx, Locale.getDefault())
                var addresses:List<Address>? = null
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addresses != null && !addresses.isEmpty()) {
                    country = addresses.get(0).getCountryName()
                }
            } catch (e:Exception) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(ctx, "Failed to get city from location. Please restart your device and try again", Toast.LENGTH_SHORT).show();
        }
        return country
    }

    interface MyLocationListener {

        fun onLocationUpdate(location: Location, isFirstUpdate: Boolean)
    }

    companion object {

        private val TAG = "LocationUtil"
        private var mInstance: LocationUtil? = null

        fun with(ctx: Context,act:Activity, myLocationListener: MyLocationListener): LocationUtil {
            if (mInstance == null) {
                mInstance = LocationUtil(ctx,act)
            }
            mInstance!!.myLocationListener = myLocationListener
            return mInstance!!
        }

        fun with(ctx: Context,act:Activity): LocationUtil {
            if (mInstance == null) {
                mInstance = LocationUtil(ctx,act)
            }
            return mInstance!!
        }

        fun with(act: Activity): LocationUtil {
            if (mInstance == null) {
                mInstance = LocationUtil(act.applicationContext,act)
            }
            return mInstance!!
        }
    }
}
