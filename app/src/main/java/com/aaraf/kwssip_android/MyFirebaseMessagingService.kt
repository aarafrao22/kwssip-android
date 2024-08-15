package com.aaraf.kwssip_android

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        saveUpdatedToken(token)
    }

    private fun saveUpdatedToken(token: String) {
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()

        myEdit.putString("RefreshedToken", token)
        Log.d(TAG, "saveUpdatedToken: tokenSaved $token")
        myEdit.apply()
    }


}