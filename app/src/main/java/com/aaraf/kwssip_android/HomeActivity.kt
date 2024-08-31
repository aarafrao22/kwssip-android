package com.aaraf.kwssip_android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.aaraf.kwssip_android.ui.theme.KWSSIPAndroidTheme
import com.aaraf.kwssip_android.views.HomeView


class HomeActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KWSSIPAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    HomeView()
                }
            }
        }
    }

}