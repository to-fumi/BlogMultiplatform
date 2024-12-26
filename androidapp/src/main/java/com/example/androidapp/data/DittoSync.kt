package com.example.androidapp.data

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.androidapp.models.PostSync
import com.example.androidapp.util.Constants.APP_ID
import com.example.androidapp.util.Constants.PLAYGROUND_AUTHENTICATION_TOKEN
import com.example.androidapp.util.RequestState
import com.example.blogmultiplatform.models.Category
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

    private fun initializeDitto(): Ditto? {
        return try {
            DittoLogger.minimumLogLevel = DittoLogLevel.DEBUG

            val androidDependencies = DefaultAndroidDittoDependencies(applicationContext)
            val identity = DittoIdentity.OnlinePlayground(
                androidDependencies,
                appId = APP_ID,
                token = PLAYGROUND_AUTHENTICATION_TOKEN
            )

            Ditto(androidDependencies, identity).apply {
                startSync()
            }
        } catch (e: DittoError) {
            println(e.message ?: "Unknown error")
            null
        }
    }

    init {
        requestPermissions()
        initializeDitto()
    }

    override fun configureTheRealm() {
        val postSubscription = ditto.sync.registerSubscription(
            query = "SELECT * FROM COLLECTION post"
        )
    }

    override suspend fun readAllPosts(): Flow<RequestState<List<PostSync>>> {
        return try {
            flow {
                emit(
                    RequestState.Success(
                        ditto.store.execute(query = "SELECT * FROM post").items.map {
                            result ->
                            PostSync(
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
