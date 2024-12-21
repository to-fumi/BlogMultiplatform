package com.example.blogmultiplatform.pages

import androidx.compose.runtime.*
import com.example.blogmultiplatform.components.CategoryNavigationItems
import com.example.blogmultiplatform.components.OverflowSidePanel
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.sections.HeaderSection
import com.example.blogmultiplatform.sections.MainSection
import com.example.blogmultiplatform.util.fetchLatestPosts
import com.example.blogmultiplatform.util.fetchMainPosts
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.worker.rememberWorker
import com.example.blogmultiplatform.worker.EchoWorker
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint

@Page
@Composable
fun HomePage() {
    val worker = rememberWorker { EchoWorker { output -> console.log("Echoed: $output") } }
    LaunchedEffect(Unit) {
        worker.postInput("Hello, worker!")
    }
    // TODO: Replace the following with your own content
    val breakpoint = rememberBreakpoint()
    var overflowOpened by remember { mutableStateOf(false) }
    var mainPosts by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }
    var latestPosts by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }
    var latestPostsToSkip by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        fetchMainPosts(
            onSuccess = { mainPosts = it },
            onError = { },
        )
        fetchLatestPosts(
            skip = latestPostsToSkip,
            onSuccess = { latestPosts = it },
            onError = { },
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (overflowOpened) {
            OverflowSidePanel(
                onMenuClosed = {
                    overflowOpened = false
                },
                content = {
                    CategoryNavigationItems(vertical = true)
                }
            )
        }
        HeaderSection(
            breakpoint = breakpoint,
            onMenuOpen = { overflowOpened = true }
        )
        MainSection(
            breakpoint = breakpoint,
            posts = mainPosts,
        )
    }
}
