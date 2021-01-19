package com.supinfo.instabus.data.models

data class Station(
    /**
     Data class associated with the Barcelona API.
     **/
    val code: Int,
    val `data`: Data
) {
    data class Data(
        val nearstations: List<Nearstation>,
        val transport: String
    ) {
        data class Nearstation(
            val buses: String,
            val city: String,
            val distance: String,
            val furniture: String,
            val id: String,
            val lat: String,
            val lon: String,
            val street_name: String,
            val utm_x: String,
            val utm_y: String
        )
    }
}