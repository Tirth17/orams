package com.project.oram

import android.app.Application
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Created by yashshah2014 on 24/1/18.
 */
class BaseApp : Application()
{
    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().subscribeToTopic("default")

    }
}