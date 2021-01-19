package com.supinfo.instabus.data.services

import com.supinfo.instabus.data.models.Station
import retrofit2.Call
import retrofit2.http.GET

interface StationService {
    /**
    Interface doing a "GET" on our API and having a function that sends a request to the web server
    and returns a response.
     **/
    @GET("/bus/nearstation/latlon/41.3985182/2.1917991/1.json")
    fun getStationList() : Call<Station>
}