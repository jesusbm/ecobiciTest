package com.example.myapplication.mapper

import android.content.res.Resources
import com.example.myapplication.R
import com.example.myapplication.model.StationEcobiciModel
import com.example.myapplication.networkmodel.StationEcobici

class StationEcobiciMapper {

    companion object {

        fun fromNetworkModel(station: StationEcobici, resorces: Resources): StationEcobiciModel {

            return StationEcobiciModel(

                name = station.name,

                address = station.extra.address,

                occupied_slots = resorces.getQuantityString(R.plurals.occupied_slots, station.free_bikes).format(station.free_bikes),

                empty_slots = resorces.getQuantityString(R.plurals.empty_slots, station.empty_slots).format(station.empty_slots),

                free_bikes = station.free_bikes

            )
        }

    }

}
