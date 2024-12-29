package com.example.androidapp.data

import android.content.Context
import com.example.androidapp.models.Post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.flow.Flow

interface DittoSyncRepository {
    fun initializeDitto(
        applicationContext: Context,
        onInitialized: () -> Unit,
        onError: (error: Throwable) -> Unit,
    )
    suspend fun readAllPosts(): Flow<RequestState<List<Post>>>
}
