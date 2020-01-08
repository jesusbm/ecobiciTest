package com.example.myapplication

import android.content.Context
import org.json.JSONObject

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter


open class ApiManager(private val context: Context) {

    internal fun getStations():JSONObject {
        val input = context.resources.openRawResource(R.raw.ecobici)
        val writer = StringWriter()
        val buffer = CharArray(1024)
        try {
            val reader = BufferedReader(InputStreamReader(input, "UTF-8"))
            var n = reader.read(buffer)
            while (n != -1) {
                writer.write(buffer, 0, n)
                n = reader.read(buffer)
            }
        } finally {
            input.close()
        }

        return JSONObject(writer.toString())
    }
}