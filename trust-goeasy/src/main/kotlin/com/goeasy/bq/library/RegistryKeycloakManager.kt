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

import com.goeasy.bq.library.callback.KeycloakCallback
import com.goeasy.bq.library.model.api.ApiServiceInterfaceKeycloak
import com.goeasy.bq.library.model.api.KeycloakRequest
import com.goeasy.bq.library.model.api.KeycloakResponse
import com.goeasy.bq.library.utils.Constants
import com.goeasy.bq.library.utils.CustomGoeasyException
import com.goeasy.bq.library.utils.LogcatProxy
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response

/**
 *
 */
object RegistryKeycloakManager {

    fun obtainToken (keycloakCallback: KeycloakCallback) {
        ApiServiceInterfaceKeycloak.create().getKeycloakToken(Constants.CLIENT_ID,
                Constants.USERNAME,  Constants.PASSWORD, Constants.GRANT_TYPE, Constants.CLIENT_SECRET)
                .enqueue(object : Callback<KeycloakResponse> {
            override fun onFailure(call: Call<KeycloakResponse>, t: Throwable) {
                LogcatProxy.e("[onFailure] -> ${t.localizedMessage}")
                keycloakCallback.onFailure(CustomGoeasyException(CustomGoeasyException.KEYCLOAK_EXCEPTION))
            }

            override fun onResponse(call: Call<KeycloakResponse>, response: Response<KeycloakResponse>) {
                if(response.isSuccessful){
                    LogcatProxy.d("[OnResponse] ${response.body()?.access_token}")
                    response.body()?.access_token?.let { keycloakCallback.onKeycloakTokenResponse(it) }
                } else {
                    LogcatProxy.d("[OnResponse] ${response.code()}")
                    LogcatProxy.d("[OnResponse] ${response.errorBody()!!.source()}")
                    keycloakCallback.onFailure(CustomGoeasyException(CustomGoeasyException.KEYCLOAK_EXCEPTION))
                }
            }
        })
    }
}