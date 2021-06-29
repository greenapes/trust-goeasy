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
package com.goeasy.bq.library.listener

import android.location.GnssMeasurementsEvent
import com.goeasy.bq.library.callback.GnssMeasurementsCallback
import com.goeasy.bq.library.model.pojo.GnssInfo
import com.goeasy.bq.library.model.pojo.GnssRawInformation
import com.goeasy.bq.library.utils.CustomGoeasyException
import com.goeasy.bq.library.utils.LogcatProxy

class GnssMeasurementMessageListener : GnssMeasurementsEvent.Callback() {

    lateinit var callback : GnssMeasurementsCallback
    private var STATUS_NOT_SUPPORTED : Int = 0
    private var STATUS_LOCATION_DISABLED : Int = 2
    private var STATUS_NOT_ALLOWED : Int = 0

    override fun onGnssMeasurementsReceived(eventArgs: GnssMeasurementsEvent?) {
        LogcatProxy.d("[GnssMeasurementMessageListener] [onGnssMeasurementsReceived] -> ${eventArgs.toString()}")

        eventArgs?.let {
            val gnssInfoList : MutableList<GnssInfo> = mutableListOf()
            it.measurements.forEach { gnssMeasurement ->
                val gnssInfo = GnssInfo(
                        gnssMeasurement.svid,
                        gnssMeasurement.constellationType,
                        gnssMeasurement.pseudorangeRateMetersPerSecond,
                        gnssMeasurement.accumulatedDeltaRangeMeters)
                gnssInfoList.add(gnssInfo)
            }
            val gnssRawInformation = GnssRawInformation(it.clock, gnssInfoList)
            callback.onGnssMesurementResponse(gnssRawInformation)
        }
    }

    override fun onStatusChanged(status: Int) {
        LogcatProxy.d("[GnssMeasurementMessageListener] [onStatusChanged] -> ${status}")

        if (status == STATUS_NOT_SUPPORTED) {
            callback.onFailure(CustomGoeasyException(CustomGoeasyException.GNSS_NOT_SUPPORTED))
        }
        else if (status == STATUS_LOCATION_DISABLED) {
            callback.onFailure(CustomGoeasyException(CustomGoeasyException.LOCATION_EXCEPTION))
        }
        super.onStatusChanged(status)
    }
}