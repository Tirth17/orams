package com.project.placeslibrary;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shripal17 on 19-08-2017.
 */

public class LocationUtil {

  private static final String TAG = "LocationUtil";
  private static LocationUtil mInstance = null;
  private Location location;
  private double lat;
  private double lon;
  private String city, country;
  private Context ctx;
  private LatLng latLng;
  private boolean ready = false;
  private MyLocationListener myLocationListener;

  private LocationUtil(Context ctx) {
    this.ctx = ctx;
    Log.e(TAG, "Instantiated");
    SmartLocation.with(ctx).location().start(new OnLocationUpdatedListener() {
      @Override
      public void onLocationUpdated(Location location) {
        Log.e(TAG, "Location updated: " + location.toString());
        LocationUtil.this.location = location;
        LocationUtil.this.lat = location.getLatitude();
        LocationUtil.this.lon = location.getLongitude();
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //updateCity(location);
        myLocationListener.onLocationUpdate(location, !ready);
        ready = true;
      }
    });
  }

  public static LocationUtil getInstance(Context ctx, final MyLocationListener myLocationListener) {
    if (mInstance == null) {
      mInstance = new LocationUtil(ctx);
    }
    mInstance.myLocationListener = myLocationListener;
    return mInstance;
  }

  public static LocationUtil getInstance(Context ctx) {
    if (mInstance == null) {
      mInstance = new LocationUtil(ctx);
    }
    return mInstance;
  }

  public Location getLocation() {
    if (ready) {
      return location;
    }
    return null;
  }

  public LatLng getLatLng() {
    if (ready) {
      return latLng;
    }
    return null;
  }

  public double getLat() {
    if (ready) {
      return lat;
    }
    return 0;
  }

  public double getLon() {
    if (ready) {
      return lon;
    }
    return 0;
  }

  public String getCity() {
    if (ready) {
      city = getCity(ctx, latLng);
      return city;
    }
    return "not ready yet";
  }

  public String getCountry() {
    if (isReady()) {
      country = getCountry(ctx, latLng);
      return country;
    } else {
      return "not ready";
    }
  }

  public static String getCity(Context ctx, LatLng latLng) {
    String city = "";
    if (Geocoder.isPresent()) {
      try {
        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        List<Address> addresses = null;
        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        if (addresses != null && !addresses.isEmpty()) {
          city = addresses.get(0).getLocality();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      Toast.makeText(ctx, "Failed to get city from location. Please restart your device and try again", Toast.LENGTH_SHORT).show();
    }
    return city;
  }



  public static String getCountry(Context ctx, LatLng latLng) {
    String country = "";
    if (Geocoder.isPresent()) {
      try {
        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        List<Address> addresses = null;
        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        if (addresses != null && !addresses.isEmpty()) {
          country = addresses.get(0).getCountryName();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      Toast.makeText(ctx, "Failed to get city from location. Please restart your device and try again", Toast.LENGTH_SHORT).show();
    }
    return country;
  }

  public boolean isReady() {
    return ready;
  }

  public interface MyLocationListener {

    void onLocationUpdate(Location location, boolean isFirstUpdate);
  }
}
