package com.example.androidapp.data

import androidx.activity.ComponentActivity
import com.example.androidapp.DittoHandler.Companion.ditto
import com.example.androidapp.models.Category
import com.example.androidapp.models.Post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object DittoSync: DittoSyncRepository, ComponentActivity() {
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
