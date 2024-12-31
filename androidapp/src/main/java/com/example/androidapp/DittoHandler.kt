package com.example.androidapp

import android.content.Context
import android.util.Log
import live.ditto.Ditto
import live.ditto.DittoIdentity
import live.ditto.DittoLogLevel
import live.ditto.DittoLogger
import live.ditto.android.DefaultAndroidDittoDependencies

class DittoHandler {
    companion object {
        lateinit var ditto: Ditto

        fun initializeDitto(
            applicationContext: Context,
            onInitialized: () -> Unit,
            onError: (error: Throwable) -> Unit,
        ) {
            if (::ditto.isInitialized) return onInitialized()

            try {
                DittoLogger.minimumLogLevel = DittoLogLevel.DEBUG

                val androidDependencies = DefaultAndroidDittoDependencies(applicationContext)
                val identity = DittoIdentity.OnlinePlayground(
                    androidDependencies,
                    appId = BuildConfig.DITTO_APP_ID,
                    token = BuildConfig.DITTO_PLAYGROUND_TOKEN,
                    enableDittoCloudSync = false,
                )

                ditto = Ditto(androidDependencies, identity).apply {
                    disableSyncWithV3()
                    startSync()
                }
            } catch (e: Throwable) {
                Log.e("Ditto error", onError(e).toString())
            }

            onInitialized()
        }
    }
}