package com.example.forumproject.feature_post.presentation.postListScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forumproject.core.util.UiEvent
import com.example.forumproject.feature_post.data.model.response.PostListResponse
import com.example.forumproject.feature_post.domain.repository.PostRepository
import com.example.forumproject.feature_post.presentation.PostRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val postRepository: PostRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val tag = savedStateHandle.get<String>("tag") ?: ""

//    var posts: Flow<List<PostListResponse>> = emptyFlow()

    var posts = mutableStateOf(emptyList<PostListResponse>())
    private set

    init {
        viewModelScope.launch {
            if (tag == "") {
                postRepository.getAllPost().collect {
                    posts.value = it
                }
            } else {
                postRepository.filterPostByTag(tag).collect {
                    posts.value = it
                }
            }
        }
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: PostListUiEvent) {
        when (event) {
            is PostListUiEvent.OnPostClick -> {
                sendUiEvent(UiEvent.Navigate(
                    PostRoutes.POST_DETAIL + "?pid=${event.post.pid}")
                )
            }
        }
    }
    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}