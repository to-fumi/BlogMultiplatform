package com.example.androidapp.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidapp.data.DittoSync
import com.example.androidapp.models.Post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _allPosts: MutableState<RequestState<List<Post>>> = mutableStateOf(RequestState.Idle)
    val allPosts: State<RequestState<List<Post>>> = _allPosts

    init {
        viewModelScope.launch {
//            fetchAllPosts()
        }
    }

    private fun fetchAllPosts() {
        viewModelScope.launch {
            DittoSync.readAllPosts().collectLatest { _allPosts.value = it }
        }
    }
}
