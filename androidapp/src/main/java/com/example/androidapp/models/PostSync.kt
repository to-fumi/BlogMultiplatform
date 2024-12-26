package com.example.androidapp.models

import com.example.blogmultiplatform.models.Category

data class PostSync (
    val _id: String = "",
    val author: String = "",
    val date: Long = 0L,
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val category: Category,
)
