package com.example.androidapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.rememberNavController
import com.example.androidapp.DittoHandler.Companion.initializeDitto
import com.example.androidapp.navigation.SetupNavGraph
import com.example.androidapp.ui.theme.BlogmultiplatformTheme
import live.ditto.transports.DittoSyncPermissions

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val installSplashScreen = installSplashScreen()
        var isDittoInitialized = false

        installSplashScreen.setKeepOnScreenCondition {
            return@setKeepOnScreenCondition !isDittoInitialized
        }

        initializeDitto(
            applicationContext = applicationContext,
            onInitialized = { isDittoInitialized = true },
            onError = { error ->
                throw error
            }
        )

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RequestPermission()

            BlogmultiplatformTheme {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }

    @Composable
    private fun RequestPermission() {
        val lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle

        val lifecycleObserver = remember {
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    val missing = DittoSyncPermissions(this).missingPermissions()
                    if (missing.isNotEmpty()) {
                        Log.d("Missing", missing.toString())
                        this.requestPermissions(missing, 0)
                    }
                }
            }
        }
        DisposableEffect(lifecycle, lifecycleObserver) {
            lifecycle.addObserver(lifecycleObserver)
            onDispose {
                lifecycle.removeObserver(lifecycleObserver)
            }
        }
    }
}
