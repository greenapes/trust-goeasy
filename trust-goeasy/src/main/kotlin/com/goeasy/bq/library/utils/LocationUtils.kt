package com.goeasy.bq.library.utils

import android.content.Context
import android.location.LocationManager

import com.goeasy.bq.library.utils.context.StaticContextProvider

internal object LocationUtils {

    fun isChecked() : Boolean {
        val lm = StaticContextProvider.getApplicationContext().getSystemService(
                Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled : Boolean = true
        var networkEnabled : Boolean = true
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }
        return gpsEnabled and networkEnabled
    }
}