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


package com.goeasy.bq.library.utils;

import kotlin.reflect.full.functions

internal object SystemPropertyProxy {

    const val SYSTEMPROPERTIES_PACKAGE = "android.os.SystemProperties";
    const val TAG = "SystemPropertyProxy"

    fun get(key : String) : String {
        try{
            val systemPropertyJava =  Class.forName(SYSTEMPROPERTIES_PACKAGE)
            val systemPropertyKotlin = systemPropertyJava.kotlin
            var functionToInvoke  = systemPropertyKotlin.functions.find { it.name == "get" }
            return functionToInvoke?.call(key) as String
        } catch (e : Exception) {
            LogcatProxy.e(e.toString())
            return ""
        }
    }
}