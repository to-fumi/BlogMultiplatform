package com.example.blogmultiplatform.navigation

import com.example.blogmultiplatform.models.Constants.POST_ID_PARAM
import com.example.blogmultiplatform.models.Constants.QUERY_PARAM
import com.example.blogmultiplatform.models.Constants.UPDATED_PARAM

sealed class Screen (val route: String) {
    object AdminHome: Screen(route = "/admin/")
    object AdminLogin: Screen(route = "/admin/login")
    object AdminCreate: Screen(route = "/admin/create") {
        fun passPostId(id: String) = "/admin/create?${POST_ID_PARAM}=$id"
    }
    object AdminMyPosts: Screen(route = "/admin/my-posts") {
        fun searchByTitle(query: String) = "/admin/my-posts?${QUERY_PARAM}=$query"
    }

    object AdminSuccess: Screen(route = "/admin/success") {
        fun postUpdated() = "/admin/success?${UPDATED_PARAM}=true"
    }
}
