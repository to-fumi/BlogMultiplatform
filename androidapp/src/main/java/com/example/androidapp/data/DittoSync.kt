package com.example.androidapp.data

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.androidapp.DittoHandler.Companion.ditto
import com.example.androidapp.models.Post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray

object DittoSync: DittoSyncRepository, ComponentActivity() {

    override suspend fun setupDittoCollection(applicationContext: Context) {
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

            Log.i("Ditto Json", "All documents inserted successfully into the 'post' collection")

            val posts = ditto.store.collection("post").findAll().exec()
            posts.forEach { post ->
                Log.i("Ditto Json", post.value.keys.toString())
            }
        } catch (e: Exception) {
            Log.e("Ditto Error", "Failed to setup Ditto collection", e)
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
                                category = result.value["category"] as String,
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
