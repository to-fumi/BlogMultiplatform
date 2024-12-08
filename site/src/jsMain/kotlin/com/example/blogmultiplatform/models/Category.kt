package com.example.blogmultiplatform.models

import kotlinx.serialization.Serializable

@Serializable
actual enum class Category {
    Technology,
    Programming,
    Design;

    actual val color: String
        get() = when (this) {
            Technology -> Theme.Green.hex
            Programming -> Theme.Yellow.hex
            Design -> Theme.Purple.hex
        }
}
