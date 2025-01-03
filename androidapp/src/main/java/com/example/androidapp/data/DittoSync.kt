package com.example.androidapp.data

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.androidapp.DittoHandler.Companion.ditto
import com.example.androidapp.models.Category
import com.example.androidapp.models.Post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray

object DittoSync: DittoSyncRepository, ComponentActivity() {

    override suspend fun setupDittoCollection(applicationContext: Context) {
        if (!isPostCollectionEmpty()) return
        try {
            val jsonString = applicationContext.assets.open("MyBlog.post.json").bufferedReader().use {
                it.readText()
            }
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val document = jsonObject.toString()

                ditto.store.execute(
                    """
                        INSERT INTO post
                        DOCUMENTS (deserialize_json(:document))
                    """.trimIndent(),
                    mapOf("document" to document)
                )
            }
            Log.i("Ditto Operation", "All documents inserted successfully into the 'post' collection")
        } catch (e: Exception) {
            Log.e("Ditto Error", "Failed to setup Ditto collection", e)
        }
    }

    override suspend fun readAllPosts(): Flow<RequestState<List<Post>>> {
        return flow {
            emit(RequestState.Loading)

            try {
                val postDocument = ditto.store.collection("post").findAll().exec()
                val posts = postDocument.map { post ->
                    Post(post.value.toMap())
                }
                emit(RequestState.Success(posts))
            } catch (e: Exception) {
                emit(RequestState.Error(Exception(e.message)))
            }
        }
    }

    override suspend fun searchPostsByTitle(query: String): Flow<RequestState<List<Post>>> {
        return flow {
            emit(RequestState.Loading)
            try {
                val allPosts = ditto.store.collection("post").findAll().exec()

                val filteredPosts = allPosts.filter { document ->
                    val title = document.value["title"] as String
                    title.contains(query, ignoreCase = true)
                }

                val posts = filteredPosts.map { post ->
                    Post(post.value.toMap())
                }
                emit(RequestState.Success(posts))
            } catch (e: Exception) {
                emit(RequestState.Error(Exception(e.message)))
            }
        }
    }

    override suspend fun searchPostsByCategory(category: Category): Flow<RequestState<List<Post>>> {
        return flow {
            emit(RequestState.Loading)
            try {
                val allPosts = ditto.store.collection("post").findAll().exec()

                val filteredPosts = allPosts.filter { document ->
                    val categoryDocument = document.value["category"] as String
                    categoryDocument == category.name
                }

                val posts = filteredPosts.map { post ->
                    Post(post.value.toMap())
                }
                emit(RequestState.Success(posts))
            } catch (e: Exception) {
                emit(RequestState.Error(Exception(e.message)))
            }
        }    }

    private fun isPostCollectionEmpty(): Boolean {
        return ditto.store.collection("post").findAll().exec().isEmpty()
    }
}
