package com.aaraf.kwssip_android.model

data class LoginResponse(
    val Success: Boolean,
    val message: String,
    val driver_name: String,
    val app_id: Int
)
