package com.supinfo.instabus.data.services

import com.supinfo.instabus.WEB_SERVICE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    /**
    Object allowing us to make the HTTP request (call the web service),
    we then make the link with the interface linked to the API data to create the service.
     **/
    private val okHttp = OkHttpClient.Builder()

    private val builder = Retrofit.Builder().baseUrl(WEB_SERVICE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())

    private val retrofit = builder.build()

    fun <T> buildService (serviceType :Class<T>):T{
        return retrofit.create(serviceType)
    }
}