package com.example.myapplication.networkmodel

data class LocationEcobici(

    val city: String,

    val country: String,

    override val latitude: Double,

    override val longitude: Double

) : GeographicPoint
