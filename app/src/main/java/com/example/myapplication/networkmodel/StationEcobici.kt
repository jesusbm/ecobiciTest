package com.example.myapplication.networkmodel

data class StationEcobici(

    val empty_slots: Int,

    val extra: ExtraInfoEcobici,

    val free_bikes: Int,

    val id: String,

    override val latitude: Double,

    override val longitude: Double,

    val name: String,

    val timestamp: String

) : GeographicPoint
