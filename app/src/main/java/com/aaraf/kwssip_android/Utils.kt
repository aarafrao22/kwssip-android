package com.aaraf.kwssip_android

import android.net.Uri

object Utils {
    //    const val BASE_URL = "http://202.69.42.194:8080"
    const val BASE_URL = "http://fms.kwsc.gos.pk"
    var DRIVER_NAME = ""
    var FCM_TOKEN = ""
    var TASK_ID: String = ""
    var BEFORE_LIST_URI: List<Uri?> = List(5) { null }
}
