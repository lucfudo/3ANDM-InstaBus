package com.supinfo.instabus.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.supinfo.instabus.R
import com.supinfo.instabus.activities.DetailActivity
import com.supinfo.instabus.data.models.Station
import com.supinfo.instabus.data.services.ServiceBuilder
import com.supinfo.instabus.data.services.StationService
import retrofit2.Call
import retrofit2.Callback

class StationsMapsFragment : Fragment() {
    /**
    Fragment for the map view of stations.
     **/
    private lateinit var mMap : GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stations_maps, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        // When the Google Map instance is ready to be used, we start loading our data
        mapFragment.getMapAsync {
                googleMap -> mMap = googleMap
                loadData() }
        return view
    }

    private fun loadData(){
        // We create our service using the interface linked to the API
        // and launch the request to get our data
        val destinationService = ServiceBuilder.buildService(StationService::class.java)
        val requestCall = destinationService.getStationList()
        // We asynchronously send the request
        requestCall.enqueue(object: Callback<Station> {
            override fun onResponse(call: Call<Station>, response: retrofit2.Response<Station>) {
                // We define the marker
                val size = response.body()!!.data.nearstations.size-1
                val actual = response.body()!!.data.nearstations[0]
                val marker = LatLng(actual.lat.toDouble(), actual.lon.toDouble())
                // We add all the data
                for(i in 0..size){
                    val current = response.body()!!.data.nearstations[i]
                    val street = LatLng(current.lat.toDouble(), current.lon.toDouble())
                    mMap.addMarker(MarkerOptions().position(street).title(current.street_name))

                    mMap.setOnInfoWindowClickListener {
                        val intent = Intent(activity, DetailActivity::class.java)
                        intent.putExtra("stationName", it.title)
                        activity?.finish()
                        startActivity(intent)
                    }
                }
                // We zoom on the location of our stations
                val zoomLevel = 15.0f
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, zoomLevel))
            }
            override fun onFailure(call: Call<Station>, t: Throwable) {
                Toast.makeText(activity, "Something went wrong $t", Toast.LENGTH_SHORT).show()
            }
        })
    }

}