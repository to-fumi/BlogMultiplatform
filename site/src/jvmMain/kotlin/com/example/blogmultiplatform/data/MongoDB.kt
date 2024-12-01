package com.example.blogmultiplatform.data

import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.User
import com.example.blogmultiplatform.util.Constants.CONNECTION_STRING_URI_PLACEHOLDER
import com.example.blogmultiplatform.util.Constants.DATABASE_NAME
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.flow.firstOrNull

@InitApi
fun initMongoDB(context: InitApiContext) {
    context.data.add(MongoDB(context))
}

class MongoDB(
    private val context: InitApiContext,
): MongoRepository {
    private val mongoClient = MongoClient.create(CONNECTION_STRING_URI_PLACEHOLDER)
    private val database = mongoClient.getDatabase(DATABASE_NAME)
    private val userCollection = database.getCollection<User>("users")
    private val postCollection = database.getCollection<Post>("post")

    override suspend fun addPost(post: Post): Boolean {
        return postCollection.insertOne(post).wasAcknowledged()
    }

    override suspend fun checkUserExistence(user: User): User? {
        return try {
            userCollection
                .find(
                    and(
                        eq(User::username.name, user.username),
                        eq(User::password.name, user.password),
                    )
                ).firstOrNull()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            null
        }
    }

    override suspend fun checkUserId(id: String): Boolean {
        return try {
            val documentCount = userCollection.countDocuments(eq(User::_id.name, id))
            documentCount > 0
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            false
        }
    }
}
