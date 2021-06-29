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

package com.goeasy.bq.library.model.api

import com.goeasy.bq.library.utils.Constants

import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServiceInterfaceLocation {

    @POST(Constants.DETAIL_ENDPOINT_LOCATION)
    fun getGeolocateCellTower(@Body request: RequestBody) : Call<GeolocateResponse>

    companion object Factory {
        fun create(): ApiServiceInterfaceLocation {
            val retrofit = retrofit2.Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.API_URL_LOCATION)
                .build()

            return retrofit.create(ApiServiceInterfaceLocation::class.java)
        }
    }
}