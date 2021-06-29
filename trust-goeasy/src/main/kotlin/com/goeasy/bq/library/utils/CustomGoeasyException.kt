package com.goeasy.bq.library.utils

import java.lang.Exception

class CustomGoeasyException(message:String) : Exception(message) {
    companion object {
        const val LOCATION_EXCEPTION = "The location services are not enabled"
        const val API_EXCEPTION = "Problems with Mozilla API Services"
        const val GNSS_NOT_SUPPORTED = "GNSS RAW DATA is not supported"

        const val KEYCLOAK_EXCEPTION = "Problems with Keycloak server"
    }
}