package com.example.myapplication

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.networkmodel.FullDataEcobici
import com.google.gson.GsonBuilder
import java.io.InputStreamReader
import java.io.Reader
import android.location.Location
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.StationsAdapter
import com.example.myapplication.mapper.StationEcobiciMapper
import com.example.myapplication.model.StationEcobiciModel
import com.example.myapplication.networkmodel.GeographicPoint

class MainActivity : Activity() {

    private val sTAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val myAdapter = StationsAdapter(this, buildDistanceComparator()).also {
            loadDataInRecyclerViewAdapter(it)
        }
        recyclerView.adapter = myAdapter
    }

    private fun loadDataInRecyclerViewAdapter(adapter: StationsAdapter) {
        val fullData = loadData()
        val myLocation = fullData.network.location
        val models = fullData.network.stations.map { station ->
            val distanceFromOrigin = calculateDistanceBetweenPoints(myLocation, station)
            StationEcobiciMapper.fromNetworkModel(station, resources).apply {
                distanceFromUser = distanceFromOrigin
            }
        }
        val filteredModels = models.filter { it.free_bikes > 0 }
        adapter.edit().replaceAll(filteredModels).commit()
    }

    private fun loadData(): FullDataEcobici {
        val jsonReader = getFileReader()
        val dataEcobici = GsonBuilder().create().run {
            fromJson(jsonReader, FullDataEcobici::class.java)
        }
        Log.e(sTAG, "$dataEcobici")
        return dataEcobici
    }

    private fun getFileReader(): Reader {
        return with(resources.openRawResource(R.raw.ecobici)) {
            InputStreamReader(this)
        }
    }

    private fun calculateDistanceBetweenPoints(or: GeographicPoint, dst: GeographicPoint): Float {
        val array = FloatArray(1)
        Location.distanceBetween(or.latitude, or.longitude, dst.latitude, dst.longitude, array)
        return array[0]
    }

    private fun buildDistanceComparator(): Comparator<StationEcobiciModel> {
        return Comparator<StationEcobiciModel> { p0, p1 ->
            if (null != p0?.distanceFromUser && null != p1?.distanceFromUser) {
                p0.distanceFromUser!!.compareTo(p1.distanceFromUser!!)
            } else {
                0
            }
        }
    }
}
