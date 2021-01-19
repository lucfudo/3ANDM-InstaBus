package com.supinfo.instabus.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.supinfo.instabus.R
import com.supinfo.instabus.activities.DetailActivity
import com.supinfo.instabus.data.helpers.StationsAdapter
import com.supinfo.instabus.data.models.Station
import com.supinfo.instabus.data.services.ServiceBuilder
import com.supinfo.instabus.data.services.StationService
import kotlinx.android.synthetic.main.fragment_stations_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class StationsListFragment : Fragment(), StationsAdapter.OnItemClickListener {
    /**
    Fragment for the list of stations.
     **/
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stations_list, container, false)
        // We call the function that generates the list with our data
        loadStationsList()
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // We restart the function that generates the list of our data when reloading the life cycle of the application
        loadStationsList()
    }

    private fun loadStationsList(){

        // We create our service using the interface linked to the API
        // and launch the request to get our data
        val destinationService = ServiceBuilder.buildService(StationService::class.java)
        val requestCall = destinationService.getStationList()
        // We asynchronously send the request
        requestCall.enqueue(object : Callback<Station> {
            override fun onResponse(call: Call<Station>, response: Response<Station>) {
                // If there are no problems, we incorporate our data into RecyclerView
                if (response.isSuccessful){
                    response.body()!!
                    stations_recyclerView.apply{
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(activity)
                        adapter = StationsAdapter(response.body()!!, this@StationsListFragment)
                    }
                }else{
                    Toast.makeText(activity, "Something went wrong ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Station>, t: Throwable) {
                Toast.makeText(activity, "Something went wrong $t", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick(position: Int, stationName: TextView) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra("stationName", stationName.text)
        startActivity(intent)
    }
}