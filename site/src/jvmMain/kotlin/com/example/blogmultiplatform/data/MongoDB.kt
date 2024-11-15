package com.example.blogmultiplatform.data

import com.example.blogmultiplatform.models.User
import com.example.blogmultiplatform.util.Constants.DATABASE_NAME
import com.mongodb.MongoClientSettings
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider

@InitApi
fun initMongoDB(context: InitApiContext) {
    val pojoCodecRegistry: CodecRegistry = fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

    val settings = MongoClientSettings.builder()
        .codecRegistry(pojoCodecRegistry)
        .build()

    context.data.add(MongoDB(context, settings))
}

class MongoDB(
    private val context: InitApiContext,
    settings: MongoClientSettings,
): MongoRepository {
    private val mongoClient: MongoClient = MongoClients.create(settings)
    private val database = mongoClient.getDatabase(DATABASE_NAME)
    private val userCollection = database.getCollection("users", User::class.java)

    override suspend fun userCheckExistence(user: User): User? {
        return try {
            userCollection
                .find(
                    and(
                        eq(User::username.name, user.username),
                        eq(User::password.name, user.password),
                    )
                ).awaitFirstOrNull()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            null
        }
    }
}
