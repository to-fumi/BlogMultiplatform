package com.example.androidapp.data

import android.content.Context
import com.example.androidapp.models.Category
import com.example.androidapp.models.Post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.flow.Flow

interface DittoSyncRepository {
    suspend fun setupDittoCollection(applicationContext: Context)
    suspend fun readAllPosts(): Flow<RequestState<List<Post>>>
    suspend fun searchPostsByTitle(query: String): Flow<RequestState<List<Post>>>
    suspend fun searchPostsByCategory(category: Category): Flow<RequestState<List<Post>>>
}
