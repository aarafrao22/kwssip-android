package com.aaraf.kwssip_android

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.aaraf.kwssip_android.Utils.FCM_TOKEN
import com.aaraf.kwssip_android.ui.theme.KWSSIPAndroidTheme
import com.aaraf.kwssip_android.views.SplashScreen
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    private var retryCount = 0

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KWSSIPAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    SplashScreen()

                    val context = LocalContext.current
                    FirebaseApp.initializeApp(context)

                    generateFCMToken(context)

                    Handler(Looper.getMainLooper()).postDelayed({
                        val nextActivity = if (getSavedAppId(context).isEmpty()) {
                            LoginActivity::class.java
                        } else {
                            HomeActivity::class.java
                        }
                        startActivity(Intent(this, nextActivity))
                        finish()
                    }, 4500)
                }
            }
        }
    }

    private fun saveUpdatedToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("RefreshedToken", token)
            apply()
        }
        Log.d(TAG, "Token saved: $token")
    }

    private fun getSavedAppId(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val appId = sharedPreferences.getString("appId", "").orEmpty()

        Log.d(TAG, if (appId.isNotEmpty()) "App ID retrieved: $appId" else "No App ID found")
        return appId
    }

    private fun generateFCMToken(context: Context) {
        if (retryCount > 3) {
            Log.e(TAG, "Exceeded maximum retry count for generating FCM token.")
            return
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val fcmToken = task.result
                if (fcmToken != "1" && fcmToken.isNotEmpty()) {
                    Log.d(TAG, "FCM Token: $fcmToken")
                    saveUpdatedToken(context, fcmToken)
                    FCM_TOKEN = fcmToken
                } else {
                    retryCount++
                    generateFCMToken(context)
                }
            } else {
                Log.e(TAG, "Error getting FCM token: ${task.exception}")
            }
        }
        Log.d(TAG, "generateFCMToken: $FCM_TOKEN")
    }
}
