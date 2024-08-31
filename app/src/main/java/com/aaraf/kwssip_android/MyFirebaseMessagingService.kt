package com.aaraf.kwssip_android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.aaraf.kwssip_android.model.UpdateFCMResponse
import com.aaraf.kwssip_android.network.RetrofitInterface
import com.aaraf.kwssip_android.network.ServiceBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        saveUpdatedToken(token, this)
        Log.d(TAG, "onNewToken: $token")
    }

    private fun getSavedAppId(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val appId = sharedPreferences.getString("appId", "").orEmpty()

        Log.d(TAG, if (appId.isNotEmpty()) "App ID retrieved: $appId" else "No App ID found")
        return appId
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.data["title"].toString()
        val body = message.data["body"].toString()
        val coordinates = message.data["coordinates"]

        customNotification(title, body, coordinates)
    }

    private fun customNotification(title: String, messageBody: String, coordinates: String?) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(applicationContext, "notify_001")
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.white)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOnlyAlertOnce(true)

        // Parse the coordinates and create an Intent to open Google Maps
        val intent = coordinates?.let {
            val (latitude, longitude) = it.split(",").map { coord -> coord.trim() }
            val uri = "geo:$latitude,$longitude?q=$latitude,$longitude"
            Intent(Intent.ACTION_VIEW, Uri.parse(uri)).apply {
                setPackage("com.google.android.apps.maps")
            }
        } ?: Intent(applicationContext, SplashActivity::class.java)

        val contentIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
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

    private fun saveUpdatedToken(
        token: String, context: Context
    ) {
        ServiceBuilder.buildService(RetrofitInterface::class.java)
            .updateFCM(getSavedAppId(context), token).enqueue(object : Callback<UpdateFCMResponse> {
                override fun onResponse(
                    call: Call<UpdateFCMResponse>, response: Response<UpdateFCMResponse>
                ) {
                    if (response.body()!!.Success) {
                        Log.d(TAG, "onResponse: SUCCESS")
                    }
                }

                override fun onFailure(call: Call<UpdateFCMResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })

        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putString("RefreshedToken", token)
        Log.d("MyFirebaseMessagingService", "saveUpdatedToken: tokenSaved $token")
        myEdit.apply()
    }
}
