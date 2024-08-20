package com.aaraf.kwssip_android.network

import com.aaraf.kwssip_android.Utils
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {

    private var interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val gson = GsonBuilder()
        .setLenient()  // Allows the parser to be lenient when reading JSON
        .create()


    private val okHttpClient = OkHttpClient().newBuilder().connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS).addInterceptor(interceptor)
        .writeTimeout(15, TimeUnit.SECONDS).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Utils.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}