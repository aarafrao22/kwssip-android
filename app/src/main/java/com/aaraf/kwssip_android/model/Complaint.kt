package com.aaraf.kwssip_android.model

data class Complaint(
    val Veh_no: String,
    val action: String,
    val complaintID: String,
    val coordinates: String,
    val date: String,
    val end_date: String,
    val driver_name: String,
    val location: String
)