package com.example.androidapp.data

import android.content.Context
import com.example.androidapp.models.Post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.flow.Flow

interface DittoSyncRepository {
    suspend fun setupDittoCollection(applicationContext: Context)
    suspend fun readAllPosts(): Flow<RequestState<List<Post>>>
}
