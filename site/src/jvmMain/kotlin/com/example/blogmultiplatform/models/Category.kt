package com.example.blogmultiplatform.models

import com.example.blogmultiplatform.CategoryCommon
import kotlinx.serialization.Serializable

@Serializable
enum class Category: CategoryCommon {
    Technology,
    Programming,
    Design,
    ;

    override val color: String
        get() =
            when (this) {
                Technology -> Theme.Green.hex
                Programming -> Theme.Yellow.hex
                Design -> Theme.Purple.hex
            }
}
