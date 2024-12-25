package com.example.blogmultiplatform.data

import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.Constants.POSTS_PER_PAGE
import com.example.blogmultiplatform.models.Newsletter
import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.models.User
import com.example.blogmultiplatform.util.Constants.CONNECTION_STRING_URI_PLACEHOLDER
import com.example.blogmultiplatform.util.Constants.DATABASE_NAME
import com.example.blogmultiplatform.util.Constants.MAIN_POSTS_LIMIT
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.`in`
import com.mongodb.client.model.Filters.regex
import com.mongodb.client.model.Sorts.descending
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

@InitApi
fun initMongoDB(context: InitApiContext) {
    context.data.add(MongoDB(context))
}

class MongoDB(
    private val context: InitApiContext,
) : MongoRepository {
    private val mongoClient = MongoClient.create(CONNECTION_STRING_URI_PLACEHOLDER)
    private val database = mongoClient.getDatabase(DATABASE_NAME)
    private val userCollection = database.getCollection<User>("users")
    private val postCollection = database.getCollection<Post>("post")
    private val newsletterCollection = database.getCollection<Newsletter>("newsletter")

    override suspend fun addPost(post: Post): Boolean {
        return postCollection.insertOne(post).wasAcknowledged()
    }

    override suspend fun updatePost(post: Post): Boolean {
        return postCollection
            .updateOne(
                eq(Post::id.name, post.id),
                Updates.combine(
                    Updates.set(Post::title.name, post.title),
                    Updates.set(Post::subtitle.name, post.subtitle),
                    Updates.set(Post::category.name, post.category),
                    Updates.set(Post::thumbnail.name, post.thumbnail),
                    Updates.set(Post::content.name, post.content),
                    Updates.set(Post::main.name, post.main),
                    Updates.set(Post::popular.name, post.popular),
                    Updates.set(Post::sponsored.name, post.sponsored),
                ),
            )
            .wasAcknowledged()
    }

    override suspend fun readMyPosts(
        skip: Int,
        author: String,
    ): List<PostWithoutDetails> {
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(eq(PostWithoutDetails::author.name, author))
            .sort(descending(PostWithoutDetails::date.name))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun readMainPosts(): List<PostWithoutDetails> {
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(eq(PostWithoutDetails::main.name, true))
            .sort(descending(PostWithoutDetails::date.name))
            .limit(MAIN_POSTS_LIMIT)
            .toList()
    }

    override suspend fun readLatestPosts(skip: Int): List<PostWithoutDetails> {
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(
                and(
                    eq(PostWithoutDetails::popular.name, false),
                    eq(PostWithoutDetails::main.name, false),
                    eq(PostWithoutDetails::sponsored.name, false),
                ),
            )
            .sort(descending(PostWithoutDetails::date.name))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun readSponsoredPosts(): List<PostWithoutDetails> {
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(eq(PostWithoutDetails::sponsored.name, true))
            .sort(descending(PostWithoutDetails::date.name))
            .limit(2)
            .toList()
    }

    override suspend fun readPopularPosts(skip: Int): List<PostWithoutDetails> {
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(eq(PostWithoutDetails::popular.name, true))
            .sort(descending(PostWithoutDetails::date.name))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun deleteSelectedPosts(ids: List<String>): Boolean {
        return postCollection
            .deleteMany(`in`(Post::id.name, ids))
            .wasAcknowledged()
    }

    override suspend fun searchPostsByTitle(
        query: String,
        skip: Int,
    ): List<PostWithoutDetails> {
        val regexQuery = query.toRegex(RegexOption.IGNORE_CASE).toPattern()
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(regex(PostWithoutDetails::title.name, regexQuery))
            .sort(descending(PostWithoutDetails::date.name))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun searchPostsByCategory(
        category: Category,
        skip: Int,
    ): List<PostWithoutDetails> {
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(eq(PostWithoutDetails::category.name, category))
            .sort(descending(PostWithoutDetails::date.name))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun readSelectedPost(id: String): Post {
        return postCollection
            .find(eq(Post::id.name, id))
            .toList()
            .first()
    }

    override suspend fun checkUserExistence(user: User): User? {
        return try {
            userCollection
                .find(
                    and(
                        eq(User::username.name, user.username),
                        eq(User::password.name, user.password),
                    ),
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

    override suspend fun subscribe(newsletter: Newsletter): String {
        val result =
            newsletterCollection
                .find(eq(Newsletter::email.name, newsletter.email))
                .toList()
        return if (result.isNotEmpty()) {
            "You're already subscribed."
        } else {
            val newEmail =
                newsletterCollection
                    .insertOne(newsletter)
                    .wasAcknowledged()
            if (newEmail) {
                "Successfully Subscribed!"
            } else {
                "Something went wrong. Please try again later."
            }
        }
    }
}
