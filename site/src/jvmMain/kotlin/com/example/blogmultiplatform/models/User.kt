package com.example.blogmultiplatform.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.ObjectIdGenerator

@Serializable
data class User(
    @SerialName(value = "_id")
    val id: String = ObjectIdGenerator().generate().toString(),
    val username: String = "",
    val password: String = "",
)
