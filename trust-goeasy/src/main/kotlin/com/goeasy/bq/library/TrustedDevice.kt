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

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.location.Location
import android.provider.Settings
import android.util.Log

import com.goeasy.bq.library.utils.LogcatProxy
import com.goeasy.bq.library.utils.SystemPropertyProxy
import com.goeasy.bq.library.utils.context.ContextProvider
import com.goeasy.bq.library.utils.context.StaticContextProvider

import com.scottyab.rootbeer.RootBeer

/**
 *
 */
class TrustedDevice (private val contextProvider : ContextProvider = StaticContextProvider) {

    /**
     * Constructor if you don't have to use the singleton context
     */
    constructor(context: Context) : this(object : ContextProvider {
        override fun getApplicationContext(): Context {
            return context.applicationContext
        }
    })

    val context: Context 
        get() = contextProvider.getApplicationContext()

    companion object {

        /**
         *
         */
        const val BOOTLOADER_PROP = "ro.boot.verifiedbootstate"

        /*
         *
         */
        const val FINGERPRINT_PROP = "ro.build.fingerprint"

        /*
         *
         */
        const val VALUE_RELEASE_KEY = "release-keys"

        /**
         *
         */
        const val VALUE_BOOTLOADER_LOCK = "green";

        /**
         *
         */
        const val MOCK_PERMISSION = "android.permission.ACCESS_MOCK_LOCATION";


        const val TAG = "TrustedDevice"
    }

    /**
     * Returns if the device has the bootloader locked or unlocked
     *
     * @return True if the device has the bootloader locked or unlocked
     */
    fun isBootloaderUnlocked () : Boolean {
        return !VALUE_BOOTLOADER_LOCK.equals(SystemPropertyProxy.get(BOOTLOADER_PROP))
    }

    /**
     * Returns if the device has selected a fake app location
     *
     * @return True if the device has selected a fake app location
     */
    fun isFakeLocationEnabled() : Boolean {
        if (android.os.Build.VERSION.SDK_INT >= 18) {
            return areThereMockPermissionApps()
        } else {
            return isMockSettingsON() || areThereMockPermissionApps()
        }
    }

    /**
     * Returns if the location is provided by Mock Provider
     *
     * @param location that the app wants to verify
     *
     * @return True if the location is provided by Mock Provider
     */
    fun isThisLocationFromMock(location: Location) : Boolean {
        return location.isFromMockProvider()
    }

    /**
     * Returns if the device is rooted
     *
     * @return True if the device is rooted
     */
    fun isDeviceRooted() : Boolean {
        var rootBeer : RootBeer = RootBeer(context);
        return rootBeer.isRooted() && rootBeer.isRootedWithoutBusyBoxCheck()
    }

    /**
     * Returns if the fingerprint has the release-key
     *
     * @return True if the fingerprint has the release-key
     */
    fun isFingerprintRelease() : Boolean {
        return SystemPropertyProxy.get(FINGERPRINT_PROP).contains(VALUE_RELEASE_KEY, ignoreCase = true)
    }
    /**
     * Returns if the device is trusted:
     *  - is not a root device
     *  - is not enable fake location
     *  - is not the bootloader unlocked
     *  - the fingerprint has release-key
     *
     * @return True if the device is trusted
     */
    fun isTrustedDevice() : Boolean {
        return (!isDeviceRooted() && !isFakeLocationEnabled() && !isBootloaderUnlocked()
                    && isFingerprintRelease())
    }

    private fun isMockSettingsON() : Boolean {
        return Settings.Secure.getString(context.getContentResolver(),
                            Settings.Secure.ALLOW_MOCK_LOCATION).equals("1")
    }

    private fun areThereMockPermissionApps() : Boolean {
        var count = 0;
        val pm : PackageManager = context.getPackageManager()
        val packages: List<ApplicationInfo> = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        packages.forEach {
            try {
                var packageInfo : PackageInfo = pm.getPackageInfo(it.packageName, PackageManager.GET_PERMISSIONS)
                var requestedPermissions : Array<String>? = packageInfo?.requestedPermissions
                if (requestedPermissions != null)
                    count += requestedPermissions.filter{permission -> permission == MOCK_PERMISSION}.size
            } catch (e : NameNotFoundException) {
            }
        }
        return (count > 0)
    }
}