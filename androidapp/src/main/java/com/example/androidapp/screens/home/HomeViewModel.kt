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

    private val _searchPosts: MutableState<RequestState<List<Post>>> = mutableStateOf(RequestState.Idle)
    val searchPosts: State<RequestState<List<Post>>> = _searchPosts

    init {
        viewModelScope.launch {
            fetchAllPosts()
        }
    }

    private fun fetchAllPosts() {
        _allPosts.value = RequestState.Loading
        viewModelScope.launch {
            DittoSync.readAllPosts().collectLatest { _allPosts.value = it }
        }
    }

    fun searchPostsByTitle(query: String) {
        _searchPosts.value = RequestState.Loading
        viewModelScope.launch {
            DittoSync.searchPostsByTitle(query).collectLatest { _searchPosts.value = it }
        }
    }

    fun resetSearchedPosts() {
        _searchPosts.value = RequestState.Idle
    }
}
