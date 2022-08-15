package com.example.noter.utils


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import com.example.noter.R
import java.sql.Time

const val NOTIFICATION_INTENT_TITLE = "notification title"
const val CHANNEL_ID = "my_channel_id_1"
const val CHANNEL_NAME = "my_channel_name_1"
const val FIRST_NOTIFICATION_ID = 1

class NotificationCreator {

    private fun getBuilder(
        context: Context,
        title: String,
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
    }

    private fun createNotificationChannel(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText = context.resources.getString(R.string.notification_channel_description)
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = descriptionText
            val notificationManager: NotificationManager =
                context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            true
        } else false
    }

    fun sendNotification(context: Context, intent: Intent?): Boolean {
        val isChannelCreated = createNotificationChannel(context)
        return if (isChannelCreated) {
            val title = intent?.getStringExtra(NOTIFICATION_INTENT_TITLE) ?: "empty!"
            val builder = getBuilder(context, title)
            NotificationManagerCompat.from(context).notify(FIRST_NOTIFICATION_ID, builder.build())
            true
        } else false
    }
}