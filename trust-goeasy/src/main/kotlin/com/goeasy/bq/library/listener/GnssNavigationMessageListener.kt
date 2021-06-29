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

import android.location.GnssNavigationMessage

import com.goeasy.bq.library.callback.GnssNavigationCallback
//import com.goeasy.bq.library.pseudorange.GpsNavigationMessageStore
import com.goeasy.bq.library.utils.CustomGoeasyException
import com.goeasy.bq.library.utils.LogcatProxy

/**
 *
 */
class GnssNavigationMessageListener : GnssNavigationMessage.Callback() {

    lateinit var callback : GnssNavigationCallback
    //private var mGpsNavigationMessageStore : GpsNavigationMessageStore = GpsNavigationMessageStore()
    private var STATUS_NOT_SUPPORTED : Int = 0
    private var STATUS_LOCATION_DISABLED : Int = 2

    override fun onGnssNavigationMessageReceived(event: GnssNavigationMessage?) {
        LogcatProxy.d("[GnssNavigationMessageListener] [onGnssNavigationMessageReceived] -> ${event.toString()}")

        event?.let {
            parseHwNavigationMessageUpdates(event)
            callback.onGnssNavigationResponse(it)
        }
    }

    override fun onStatusChanged(status: Int) {
        LogcatProxy.d("[GnssNavigationMessageListener] [onStatusChanged] -> $status")
        if (status == STATUS_NOT_SUPPORTED) {
            callback.onFailure(CustomGoeasyException(CustomGoeasyException.GNSS_NOT_SUPPORTED))
        }
        else if (status == STATUS_LOCATION_DISABLED) {
            callback.onFailure(CustomGoeasyException(CustomGoeasyException.LOCATION_EXCEPTION))
        }
        super.onStatusChanged(status)
    }

    fun parseHwNavigationMessageUpdates(navigationMessage: GnssNavigationMessage) {
        val messagePrn = navigationMessage.svid.toByte()
        val messageType = (navigationMessage.type shr 8).toByte()
        val subMessageId = navigationMessage.submessageId

        val messageRawData = navigationMessage.data
        // parse only GPS navigation messages for now
        if (messageType.toInt() == 1) {
 //           mGpsNavigationMessageStore.onNavMessageReported(
 //                   messagePrn, messageType, subMessageId.toShort(), messageRawData)
 //           LogcatProxy.d(mGpsNavigationMessageStore.createDecodedNavMessage().toString())
        }

    }
}