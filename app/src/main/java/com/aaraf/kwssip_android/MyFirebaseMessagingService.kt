package com.aaraf.kwssip_android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        saveUpdatedToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d(TAG, "onMessageReceived: ${message.notification?.title}")


        sendNotification(
            message.notification?.title.toString(),
            message.notification?.body.toString()
        )


    }

    private fun sendNotification(title: String, messageBody: String) {
        customNotification(title, messageBody)
    }

    private fun customNotification(title: String, messageBody: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(applicationContext, "notify_001")
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.white)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOnlyAlertOnce(true)

        val notificationIntent = Intent(applicationContext, SplashActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        mBuilder.setContentIntent(contentIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "channel_id"
            val channel = NotificationChannel(
                channelId, "Miscellaneous", NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            }
            notificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }

        val notification = mBuilder.build()
        notificationManager.notify(Random().nextInt(5000) + 1, notification)
    }

    private fun saveUpdatedToken(token: String) {
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putString("RefreshedToken", token)
        Log.d("MyFirebaseMessagingService", "saveUpdatedToken: tokenSaved $token")
        myEdit.apply()
    }
}
