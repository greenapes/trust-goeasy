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

package com.goeasy.bq.library;

import android.content.Context

import com.goeasy.bq.library.utils.context.StaticContextProvider

/**
 * GoeasyLib: Library for project Goeasy in Kotlin
 *
 * This object is necessary to instantiate and pass the context of the application
 * If you don't want to use singleton context, you could use the constructor of each Class
 *  
 */
object GoeasyLib {

    /**
     * Initialize GoeasyLib singleton object.
     *
     * @param context Application context
     */
    fun init(context: Context) {
        StaticContextProvider.setContext(context.applicationContext)
    }
}

