/*
 *  Copyright (C) 2019 The bq Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.goeasy.bq.library

import android.annotation.SuppressLint
import android.content.Context
import android.location.GnssMeasurementsEvent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

import com.goeasy.bq.library.callback.GnssMeasurementsCallback
import com.goeasy.bq.library.callback.GnssNavigationCallback
import com.goeasy.bq.library.listener.GnssMeasurementMessageListener
import com.goeasy.bq.library.listener.GnssNavigationMessageListener
import com.goeasy.bq.library.utils.context.ContextProvider
import com.goeasy.bq.library.utils.context.StaticContextProvider
import com.goeasy.bq.library.utils.CustomGoeasyException
import com.goeasy.bq.library.utils.LocationUtils

class GNSSInformation (private val contextProvider : ContextProvider = StaticContextProvider) {

    /**
     * Constructor if you don't have to use the singleton context
     */
    constructor(context: Context) : this(object : ContextProvider {
        override fun getApplicationContext(): Context {
            return context.applicationContext
        }
    })

    private val mContext: Context
        get() = contextProvider.getApplicationContext()

    companion object {
        private var gnssNavigationMessageListener = GnssNavigationMessageListener()
        private var gnssMeasurementsListener = GnssMeasurementMessageListener()
        const val EARTH_RADIUS_METERS: Float = 6371000F

    }

    /**
     * Register callback to obtain GnssNavigationMessage
     * @param callback @see GnssNavigationCallback
     */
    @SuppressLint("MissingPermission")
    fun registerGnssNavigation (callback : GnssNavigationCallback){
        if (LocationUtils.isChecked()) {
            gnssNavigationMessageListener.callback = callback
            val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.registerGnssNavigationMessageCallback(gnssNavigationMessageListener)
        } else {
            callback.onFailure(CustomGoeasyException(CustomGoeasyException.LOCATION_EXCEPTION))
        }
    }

    /**
     * Unregister Callback of GNSS Navigation
     */
    fun unregisterGnssNavigation() {
        if (LocationUtils.isChecked()) {
            val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.unregisterGnssNavigationMessageCallback(gnssNavigationMessageListener)
        }
    }

    /**
     * Register callback to obtain GnssClockInformation
     * @param callback @see GnssMeasurementsCallback
     */
    @SuppressLint("MissingPermission")
    fun registerGnssMeasurements(callback: GnssMeasurementsCallback) {
        if (LocationUtils.isChecked()) {
            gnssMeasurementsListener.callback = callback
            val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.registerGnssMeasurementsCallback(gnssMeasurementsListener)
        } else {
            callback.onFailure(CustomGoeasyException(CustomGoeasyException.LOCATION_EXCEPTION))
        }
    }

    /**
     * Unregister Callback of Gnss Measurements
     */
    fun unregisterGnssMeasurements() {
        if (LocationUtils.isChecked()) {
            val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.unregisterGnssMeasurementsCallback(gnssMeasurementsListener)
        }
    }

}
