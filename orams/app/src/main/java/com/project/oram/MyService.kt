package com.project.oram

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationManager
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Context
import android.support.v4.app.NotificationCompat
import com.project.AppConstants.ORAM.AppManager
import com.project.medbox.AppConstants
import com.project.placeslibrary.LocationUtil
import org.jetbrains.anko.longToast
import org.json.JSONObject

class MyService : FirebaseMessagingService() {

    lateinit var location : LocationUtil

    override fun onCreate() {
        super.onCreate()
        location = LocationUtil.getInstance(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        sendNotification(remoteMessage?.data?.get("message"))
    }

    private fun sendNotification(messageBody: String?) {
        Log.e("messagebody", messageBody)

        val data = JSONObject(messageBody)

        var phone = AppManager.spGetString(this, AppConstants.PHONE)
        if (phone.equals("")) {
            longToast("You are not logged in")
        } else {
            var lat = data.getString("lat")
            var long = data.getString("lon")
            var accident = data.getString("accident")
            AppManager.spPutString(applicationContext,AppConstants.DESTINATIONLAT,lat)
            AppManager.spPutString(applicationContext,AppConstants.DESTINATIONLON,long)
            AppManager.spPutString(applicationContext,AppConstants.ACCIDENT,accident)

            if (accident.equals("A") && distance(lat.toDouble(), long.toDouble(), location.lat, location.lon) < 5000)
            {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent,
                        PendingIntent.FLAG_ONE_SHOT)

                val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val notificationBuilder = NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("ORAM")
                        .setContentText("An Accident has Occured")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
                        .setContentIntent(pendingIntent)


                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                notificationManager.notify(0, notificationBuilder.build())
            }
        }
    }


    fun distance(lat1: Double, lon1: Double, lat2: Double,
                 lon2: Double, el1: Double = 0.0, el2: Double = 0.0): Double {
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
}
