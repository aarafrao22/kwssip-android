package com.aaraf.kwssip_android

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    private var count = 0
    private var updatedFCM = ""

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


                    generate_fcm(context)

                    Handler().postDelayed({
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }, 4500)
                }
            }
        }
    }

    private fun saveUpdatedToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()

        myEdit.putString("RefreshedToken", token)
        Log.d(TAG, "saveUpdatedToken: tokenSaved $token")
        myEdit.apply()
    }

    private fun generate_fcm(context: Context) {
        if (count <= 3) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
                if (task.isSuccessful) {
                    val fcmToken = task.result
                    if ("1" != fcmToken) {
                        Log.d(TAG, "FCM Token: $updatedFCM")
                        if (updatedFCM != "") {
                            FCM_TOKEN = updatedFCM
                            saveUpdatedToken(context, updatedFCM)
                        }
                    } else {
                        count++
                        generate_fcm(context) // Retry if token is "1"
                    }
                } else {
                    Log.e(
                        TAG,
                        "Error getting FCM token: " + task.exception
                    )
                }
            }
        } else {
            Log.e(TAG, "Exceeded maximum retry count for generating FCM token.")
        }
    }
}


