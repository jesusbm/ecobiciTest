package com.example.myapplication.model

import com.example.myapplication.adapter.SortedListRecyclerViewAdapter

data class StationEcobiciModel(

    val name: String,

    val address: String,

    val empty_slots: String,

    val occupied_slots: String,

    val free_bikes: Int,

    var distanceFromUser: Float? = null,

    var distanceFromUserLabel: String? = null

) : SortedListRecyclerViewAdapter.ViewModel {

    fun isEmpty(): Boolean {

        return 0 >= free_bikes

    }

}
