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
import android.location.Location
import android.telephony.*
import android.telephony.gsm.GsmCellLocation

import com.goeasy.bq.library.callback.CellTowerCallback
import com.goeasy.bq.library.model.api.ApiServiceInterfaceLocation
import com.goeasy.bq.library.model.api.CellTowerInfo
import com.goeasy.bq.library.model.api.GeolocateRequest
import com.goeasy.bq.library.model.api.GeolocateResponse
import com.goeasy.bq.library.utils.CustomGoeasyException
import com.goeasy.bq.library.utils.LocationUtils
import com.goeasy.bq.library.utils.LogcatProxy
import com.goeasy.bq.library.utils.context.StaticContextProvider

import com.google.gson.GsonBuilder

import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response

/**
 *
 */
object CellTowerValidation {

    /**
     *
     */
    @SuppressLint("MissingPermission")
    fun validateCellTower (location : Location, callback: CellTowerCallback) {
        if (LocationUtils.isChecked()) {
//            val connBuilder: NetworkRequest.Builder = NetworkRequest.Builder()
//            connBuilder.addTransportType(android.net.NetworkCapabilities.TRANSPORT_CELLULAR)
//            val connManager = StaticContextProvider.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as CONNECTIVITY_SERVICE
            val gson = GsonBuilder().setPrettyPrinting().create()
            val cellTowers: MutableList<CellTowerInfo> = mutableListOf()

            val telephonyManager = StaticContextProvider.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val cellInfoList: List<CellInfo>? = telephonyManager.allCellInfo

            cellInfoList?.forEach {
                LogcatProxy.d("cellInfoList?.forEach -> $it")
                when (it) {
                    is CellInfoLte -> {
                        val cellLte: CellIdentityLte = it.cellIdentity
                        val cellTowerInfo = CellTowerInfo(
                                "lte",
                                cellLte.mccString?.toInt(),
                                cellLte.mncString?.toInt(),
                                cellLte.tac,
                                cellLte.ci,
                                null
                        )

                        cellTowers.add(cellTowerInfo)
                    }
                    is CellInfoWcdma -> {
                        val cellWcdma: CellIdentityWcdma = it.cellIdentity;
                        val cellTowerInfo = CellTowerInfo(
                                "wcdma",
                                cellWcdma.mccString?.toInt(),
                                cellWcdma.mncString?.toInt(),
                                cellWcdma.lac,
                                cellWcdma.cid,
                                cellWcdma.psc
                        )

                        cellTowers.add(cellTowerInfo)
                    }
                    is CellInfoGsm -> {
                        val cellGsm: CellIdentityGsm = it.cellIdentity
                        val cellSignal = it.cellSignalStrength
                        val cellTowerInfo = CellTowerInfo(
                                "gsm",
                                cellGsm.mccString?.toInt(),
                                cellGsm.mncString?.toInt(),
                                cellGsm.lac,
                                cellGsm.cid,
                                null
                        )

                        cellTowers.add(cellTowerInfo)
                    }
                }
            }

            if (cellTowers.size == 0) {
                val cellLocation = telephonyManager.getCellLocation()
                if (cellLocation is GsmCellLocation) {
                    val cellTowerInfo = CellTowerInfo(
                            if (cellLocation.psc == -1) "gsm" else "umts",
                            telephonyManager.networkOperator.substring(0, 3).toInt(),
                            telephonyManager.networkOperator.substring(3).toInt(),
                            cellLocation.lac,
                            cellLocation.cid,
                            null
                    )
                    cellTowers.add(cellTowerInfo)
                }
            }

            val geolocateRequest = GeolocateRequest(cellTowers)
            val jsonGeolocateRequest: String = gson.toJson(geolocateRequest)
            LogcatProxy.d(jsonGeolocateRequest)
            val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonGeolocateRequest)

            ApiServiceInterfaceLocation.create().getGeolocateCellTower(requestBody).enqueue(object : Callback<GeolocateResponse> {
                override fun onFailure(call: Call<GeolocateResponse>, t: Throwable) {
                    LogcatProxy.e("[onFailure] -> ${t.localizedMessage}")
                    callback.onFailure(CustomGoeasyException(CustomGoeasyException.API_EXCEPTION))
                }

                override fun onResponse(call: Call<GeolocateResponse>, response: Response<GeolocateResponse>) {
                    if (response.code() == 200) {
                        val body = response.body()
                        LogcatProxy.d("[OnResponse] ${response.message()}")
                        body!!.let {
                            LogcatProxy.d("[OnResponse] lat ->  ${it.location.lat}, lng -> ${it.location.lng}")
                            val locationAPI: Location = Location("LocationAPI")
                            locationAPI.latitude = it.location.lat
                            locationAPI.longitude = it.location.lng
                            if (locationAPI.distanceTo(location) > it.accuracy) {
                                LogcatProxy.d("[OnResponse] The distance between the location is > than the accuracy")
                                callback.onCellTowerValidationResponse(false)
                                return
                            }
                        }
                        callback.onCellTowerValidationResponse(true)
                    } else {
                        LogcatProxy.d("[cellTowerInvalidResponse] ${response.code()}")
                        callback.onFailure(CustomGoeasyException(CustomGoeasyException.API_EXCEPTION))
                    }
                }

            })
        } else {
            callback.onFailure(CustomGoeasyException(CustomGoeasyException.LOCATION_EXCEPTION))
        }
    }


}