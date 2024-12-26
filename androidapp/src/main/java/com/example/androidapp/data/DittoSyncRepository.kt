package com.example.androidapp.data

import com.example.androidapp.models.PostSync
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.flow.Flow

interface DittoSyncRepository {
    fun configureTheRealm()
    suspend fun readAllPosts(): Flow<RequestState<List<PostSync>>>
}
