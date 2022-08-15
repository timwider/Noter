package com.example.noter.presentation.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.noter.utils.NotificationCreator

class NotificationService: Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        NotificationCreator().sendNotification(this, intent)
        stopSelf()
        return super.onStartCommand(intent, flags, startId)
    }
}