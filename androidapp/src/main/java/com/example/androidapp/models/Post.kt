package com.example.androidapp.models

import androidx.compose.runtime.Immutable

@Immutable
data class Post (
    val _id: String = "",
    val author: String = "",
    val date: Long = 0L,
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val category: String = Category.Programming.name,
) {
    constructor(item: Map<String, Any?>) : this (
        item["_id"] as String,
        item["author"] as String,
        item["date"] as Long,
        item["title"] as String,
        item["subtitle"] as String,
        item["thumbnail"] as String,
        item["category"] as String,
    )
}
