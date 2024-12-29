package com.example.androidapp.data

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.androidapp.BuildConfig
import com.example.androidapp.models.Category
import com.example.androidapp.models.Post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import live.ditto.Ditto
import live.ditto.DittoError
import live.ditto.DittoIdentity
import live.ditto.DittoLogLevel
import live.ditto.DittoLogger
import live.ditto.android.DefaultAndroidDittoDependencies
import live.ditto.transports.DittoSyncPermissions

object DittoSync: DittoSyncRepository, ComponentActivity() {

    private lateinit var ditto: Ditto

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            this.ditto.refreshPermissions()
        }

    private fun requestPermissions() {
        val missing = DittoSyncPermissions(this).missingPermissions()
        if (missing.isNotEmpty()) {
            requestPermissionLauncher.launch(missing)
        }
    }

    init {
        requestPermissions()
        initializeDitto()
    }

    override fun initializeDitto() {
        try {
            DittoLogger.minimumLogLevel = DittoLogLevel.DEBUG

            val androidDependencies = DefaultAndroidDittoDependencies(applicationContext)
            val identity = DittoIdentity.OnlinePlayground(
                androidDependencies,
                appId = BuildConfig.DITTO_APP_ID,
                token = BuildConfig.DITTO_PLAYGROUND_TOKEN,
            )

            ditto = Ditto(androidDependencies, identity)
            ditto.startSync()
            ditto.sync.registerSubscription(
                query = "SELECT * FROM COLLECTION post"
            )
        } catch (e: DittoError) {
            Log.e("Ditto error", e.message!!)
        }
    }

    override suspend fun readAllPosts(): Flow<RequestState<List<Post>>> {
        return try {
            flow {
                emit(
                    RequestState.Success(
                        ditto.store.execute(query = "SELECT * FROM post").items.map {
                            result ->
                            Post(
                                _id = result.value["_id"] as String,
                                author = result.value["author"] as String,
                                date = result.value["date"] as Long,
                                title = result.value["title"] as String,
                                subtitle = result.value["subtitle"] as String,
                                thumbnail = result.value["thumbnail"] as String,
                                category = result.value["category"] as Category,
                            )
                        }
                    )
                )
            }
        } catch (e: Exception) {
            flow { emit(RequestState.Error(Exception(e.message))) }
        }
    }
}
