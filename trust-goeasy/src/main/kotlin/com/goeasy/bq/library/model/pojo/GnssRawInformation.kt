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
package com.goeasy.bq.library.model.pojo

import android.location.GnssClock

class GnssRawInformation(
    var gnssClock: GnssClock,
    var gnssInfoList: MutableList<GnssInfo>
) {
    override fun toString(): String {
        return "GnssRawInformation(gnssClock=$gnssClock, gnssInfoList=$gnssInfoList)"
    }
}

class GnssInfo(
    var svid: Int,
    var constellation: Int,
    var pseudoRangeMetersPerSecond: Double,
    var accumulatedDeltaRangeMeters: Double
) {
    override fun toString(): String {
        return "GnssInfo(svid=$svid, constellation=$constellation, pseudoRangeMetersPerSecond=$pseudoRangeMetersPerSecond, accumulatedDeltaRangeMeters=$accumulatedDeltaRangeMeters)"
    }
}