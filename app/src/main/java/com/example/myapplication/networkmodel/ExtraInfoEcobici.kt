package com.example.myapplication.networkmodel

data class ExtraInfoEcobici(

    val NearbyStationList: List<Int>,

    val address: String,

    val districtCode: String,

    val status: String,

    val uid: Int,

    val zip: String
)
