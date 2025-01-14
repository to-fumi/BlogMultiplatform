package com.example.androidapp.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.androidapp.navigation.Screen
import com.example.androidapp.screens.details.DetailsScreen
import com.example.androidapp.util.Constants.POST_ID_ARGUMENT
import com.example.androidapp.util.Constants.SHOW_SECTIONS_PARAM

fun NavGraphBuilder.detailsRoute(
    onBackPress: () -> Unit,
) {
    composable(
        route = Screen.Details.route,
        arguments = listOf(navArgument(name = POST_ID_ARGUMENT) {
            type = NavType.StringType
        })
    ) {
        val postId = it.arguments?.getString(POST_ID_ARGUMENT)
        DetailsScreen(
            url = "http://10.0.2.2:8080/posts/post?${POST_ID_ARGUMENT}=$postId&$SHOW_SECTIONS_PARAM=false",
            onBackPress = onBackPress,
        )
    }
}
