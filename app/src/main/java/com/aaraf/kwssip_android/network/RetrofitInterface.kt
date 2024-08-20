package com.aaraf.kwssip_android.network


import com.aaraf.kwssip_android.model.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RetrofitInterface {

    @FormUrlEncoded
    @POST("userlogin.php")
    fun login(
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("device_type") device_type: String?,
        @Field("fcm") fcm: String?
    ): Call<LoginResponse>


    @Multipart
    @POST("feedback.php")
    fun sendFeedback(
        @Part Driver_id: MultipartBody.Part?,
        @Part CustomerName: MultipartBody.Part?,
        @Part CustomerFeedback: MultipartBody.Part?,
        @Part CustomerContact: MultipartBody.Part?,
        @Part Rating: MultipartBody.Part?,
        @Part img1: MultipartBody.Part?,
        @Part img2: MultipartBody.Part?,
        @Part img3: MultipartBody.Part?,
        @Part img4: MultipartBody.Part?,
        @Part img5: MultipartBody.Part?,

        ): Call<LoginResponse>

}