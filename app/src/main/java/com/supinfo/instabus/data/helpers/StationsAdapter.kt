package com.supinfo.instabus.data.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.supinfo.instabus.R
import com.supinfo.instabus.data.models.Station

class StationsAdapter(
        private val stationsList: Station,
        private val listener: OnItemClickListener):
        RecyclerView.Adapter<StationsAdapter.ViewHolder>(){
    /**
    Class to incorporate the data into stations_recyclerView,
    for that our data will take the form defined in the layout "stations_list".
    (we will visualize the name of the station and the number of the bus,
    with a button to access the associated photo screen).
     **/

    inner class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var stationName: TextView = itemView.findViewById(R.id.station_textName)
        var stationBuses: TextView = itemView.findViewById(R.id.station_textBusNumber)
        var buttonDetail: ImageButton = itemView.findViewById(R.id.buttonDetail)

        init{
            buttonDetail.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position, stationName)
            }
        }
    }

    override fun getItemCount(): Int = stationsList.data.nearstations.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stations_list, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val station = stationsList.data.nearstations[position]
        with(holder){
            stationName.let{
                it.text = station.street_name
                it.contentDescription = station.street_name
            }
            stationBuses.let{
                it.text = station.buses
                it.contentDescription = station.buses
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int, stationName: TextView)
    }
}