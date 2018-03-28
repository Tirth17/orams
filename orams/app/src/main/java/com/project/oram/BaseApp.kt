package com.project.oram

import android.app.Application
import com.google.firebase.messaging.FirebaseMessaging


class BaseApp : Application()
{
    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().subscribeToTopic("default")

    }
}