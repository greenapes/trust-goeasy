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

import android.util.Log

internal object LogcatProxy {

    private const val DBG = true;
    private const val TAG = "Trust-GOEASY"

    fun e(message: String) {
        if (DBG)
            Log.e(TAG, message);
    }

    fun d(message: String) {
        if (DBG)
            Log.d(TAG, message);
    }

    fun v(message: String) {
        if (DBG)
            Log.v(TAG, message);
    }

    fun i(message: String) {
        if (DBG)
            Log.i(TAG, message);
    }

    fun w(message: String) {
        if (DBG)
            Log.w(TAG, message);
    }
}