package com.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import com.example.blogmultiplatform.components.AdminPageLayout
import com.example.blogmultiplatform.util.isUserLoggedIn
import com.varabyte.kobweb.core.Page

@Page
@Composable
fun MyPostsPage() {
    isUserLoggedIn {
        MyPostsScreen()
    }
}

@Composable
fun MyPostsScreen() {
    AdminPageLayout {

    }
}
