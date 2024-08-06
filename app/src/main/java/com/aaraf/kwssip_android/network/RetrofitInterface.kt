package com.aaraf.kwssip_android.network


import com.aaraf.kwssip_android.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitInterface {

    @FormUrlEncoded
    @POST("userlogin.php")
    fun login(
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("device_type") device_type: String?,
        @Field("fcm") fcm: String?
    ): Call<LoginResponse>

}