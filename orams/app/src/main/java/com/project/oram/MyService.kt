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
import org.jetbrains.anko.longToast
import org.json.JSONObject

class MyService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d("Message Body", remoteMessage?.data?.get("body") + " " + remoteMessage?.data?.get("title"))

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

            if (accident.equals("A"))
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
}