package com.example.androidapp.data

import com.example.androidapp.models.Post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.flow.Flow

interface DittoSyncRepository {
    suspend fun readAllPosts(): Flow<RequestState<List<Post>>>
}
