package com.example.forumproject.feature_post.presentation.postDetailScreen.postDetail

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dictionary.core.util.Resource
import com.example.forumproject.core.util.UiEvent
import com.example.forumproject.feature_auth.domain.repository.AuthRepository
import com.example.forumproject.feature_dataStore.abstraction.DataStoreRepository
import com.example.forumproject.feature_post.data.model.response.CommentResponse
import com.example.forumproject.feature_post.data.model.response.PostResponse
import com.example.forumproject.feature_post.domain.repository.PostRepository
import com.example.forumproject.feature_post.presentation.PostRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    // get post clicked

    var post = mutableStateOf<PostResponse?>(null)
    private set

    private var postId = mutableStateOf(-1)

    var likeStatus = mutableStateOf(false)
    private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val pid = savedStateHandle.get<Int>("pid")!!
        postId.value = pid
        viewModelScope.launch {
            postRepository.getPostById(pid).collect {
//                post.value = it
                this@PostDetailViewModel.post.value = it
                likeStatus.value = post.value!!.like
            }

        }
    }

    val comments = postRepository.getPostComment(postId.value)

    fun onEvent(event: PostDetailUiEvent) {
        when (event) {
            is PostDetailUiEvent.OnAddComment -> {
                viewModelScope.launch {
                    val refreshToken = dataStoreRepository.getString("refresh")
                    if (refreshToken == null) {
                        sendUiEvent(UiEvent.ShowSnackbar(message = "Please login."))
                    } else {
                        authRepository.refreshToken("Bearer $refreshToken").collect { result ->
                            when (result) {
                                is Resource.Success -> {
                                    dataStoreRepository.putString("access", result.data!!.access)
                                    sendUiEvent(UiEvent.Navigate(
                                        PostRoutes.COMMENT_CREATE + "?pid=${event.post.pid}")
                                    )
                                }
                                is Resource.Error -> {
                                    sendUiEvent(
                                        UiEvent.ShowSnackbar(
                                            message = "Error."
                                        )
                                    )
                                }
                                is Resource.TokenExpired -> {
                                    sendUiEvent(
                                        UiEvent.ShowSnackbar(
                                            message = "Token expired, please login again."
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
            is PostDetailUiEvent.OnLikeToggle -> {
                Log.d("post", post.value?.like.toString())
                Log.d("like", likeStatus.value.toString())
                viewModelScope.launch {
                    var accessToken = dataStoreRepository.getString("access")
                    if (accessToken == null) {
                        sendUiEvent(UiEvent.ShowSnackbar(message = "Please login."))
                    } else {
                        when (likeStatus.value) {
                            false -> {
                                postRepository.likePost(
                                    pid = postId.value,
                                    accessToken = "Bearer $accessToken"
                                ).collect { result ->
                                    when (result) {
                                        is Resource.Success -> {
                                            likeStatus.value = true
                                            sendUiEvent(UiEvent.ShowSnackbar(
                                                message = result.data?.msg ?: "Post liked.")
                                            )
                                        }
                                        is Resource.Error -> {
                                            sendUiEvent(UiEvent.ShowSnackbar(
                                                message = result.data?.msg ?: "Error.")
                                            )
                                        }
                                        is Resource.TokenExpired -> {
                                            val refreshToken = dataStoreRepository.getString("refresh")
                                            authRepository.refreshToken("Bearer $refreshToken").collect { result ->
                                                when (result) {
                                                    is Resource.Success -> {
                                                        accessToken = result.data!!.access
                                                        sendUiEvent(UiEvent.ShowSnackbar(
                                                            message = "Token refreshed, please like the post again.")
                                                        )
                                                    }
                                                    is Resource.Error -> {
                                                        sendUiEvent(UiEvent.ShowSnackbar(
                                                            message = result.data?.msg ?: "Error.")
                                                        )
                                                    }
                                                    is Resource.TokenExpired -> {
                                                        sendUiEvent(UiEvent.ShowSnackbar(
                                                            message = "Token expired, please login again.")
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            true -> {
                                postRepository.dislikePost(
                                    pid = postId.value,
                                    accessToken = "Bearer $accessToken"
                                ).collect { result ->
                                    when (result) {
                                        is Resource.Success -> {
                                            likeStatus.value = false
                                            sendUiEvent(UiEvent.ShowSnackbar(
                                                message = result.data?.msg ?: "Post disliked.")
                                            )
                                        }
                                        is Resource.Error -> {
                                            sendUiEvent(UiEvent.ShowSnackbar(
                                                message = result.data?.msg ?: "Error.")
                                            )
                                        }
                                        is Resource.TokenExpired -> {
                                            val refreshToken = dataStoreRepository.getString("refresh")
                                            authRepository.refreshToken(refreshToken!!).collect { result ->
                                                when (result) {
                                                    is Resource.Success -> {
                                                        accessToken = result.data!!.access
                                                        sendUiEvent(UiEvent.ShowSnackbar(
                                                            message = "Token refreshed, please dislike the post again.")
                                                        )
                                                    }
                                                    is Resource.Error -> {
                                                        sendUiEvent(UiEvent.ShowSnackbar(
                                                            message = result.data?.msg ?: "Error.")
                                                        )
                                                    }
                                                    is Resource.TokenExpired -> {
                                                        sendUiEvent(UiEvent.ShowSnackbar(
                                                            message = "Token expired, please login again.")
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}