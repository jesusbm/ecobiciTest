package com.example.myapplication.networkmodel

data class NetworkEcobici(

    val company: List<String>,

    val href: String,

    val id: String,

    val location: LocationEcobici,

    val name: String,

    val stations: List<StationEcobici>
)
