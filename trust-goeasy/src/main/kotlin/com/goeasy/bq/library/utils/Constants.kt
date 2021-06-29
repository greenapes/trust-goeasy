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

package com.goeasy.bq.library.utils

object Constants {
    const val API_KEY = "test"

    const val API_URL_LOCATION = "https://location.services.mozilla.com/v1/"
    const val DETAIL_ENDPOINT_LOCATION = "geolocate?key=${API_KEY}"

    const val API_URL_KEYCLOAK = "https://galileocloud.goeasyproject.eu/auth/realms/GOEASY/protocol/openid-connect/"
    const val DETAIL_ENDPOINT_TOKEN = "token"
    const val CLIENT_ID = "get_token_client"
    const val USERNAME = "goeasy_bq_library"
    const val PASSWORD = "password"
    const val GRANT_TYPE = "password"
    const val CLIENT_SECRET = "8e83581a-9ff5-4dc5-8072-5214c11011eb"
}