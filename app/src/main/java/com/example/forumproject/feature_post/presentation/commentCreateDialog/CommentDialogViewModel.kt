package com.example.forumproject.feature_post.presentation.commentCreateDialog

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.core.util.Resource
import com.example.forumproject.core.util.UiEvent
import com.example.forumproject.feature_auth.domain.repository.AuthRepository
import com.example.forumproject.feature_dataStore.abstraction.DataStoreRepository
import com.example.forumproject.feature_post.data.model.requset.CommentRequest
import com.example.forumproject.feature_post.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentDialogViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    var description = mutableStateOf("")
    private set

    var postId = mutableStateOf(-1)
    private set

    init{
        val pid = savedStateHandle.get<Int>("pid") ?: 0
        Log.d("pid", pid.toString())
        postId.value = pid
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CommentCreateUiEvent) {
        when (event) {
            is CommentCreateUiEvent.OnDescriptionChange -> {
                description.value = event.description
            }
            is CommentCreateUiEvent.OnDismissClick -> {
                description.value = ""
                sendUiEvent(UiEvent.PopBackStack)
            }
            is CommentCreateUiEvent.OnDoneClick -> {
                Log.d("pid", postId.value.toString())
                if (description.value == "") {
                    sendUiEvent(UiEvent.ShowSnackbar(message = "Please input comment."))
                } else {
                    // Add login status check
                    viewModelScope.launch {
                        var accessToken = dataStoreRepository.getString("access")
                        postRepository.postComment(
                            pid = postId.value,
                            accessToken = "Bearer $accessToken",
                            comment = CommentRequest(
                                body = description.value
                            )
                        ).collect { result ->
                            when (result) {
                                is Resource.Success -> {
                                    sendUiEvent(UiEvent.ShowSnackbar(message = "Comment posted."))
                                    sendUiEvent(UiEvent.PopBackStack)
                                }
                                is Resource.Error -> {
                                    sendUiEvent(UiEvent.ShowSnackbar(message = "Error."))
                                }
                                is Resource.TokenExpired -> {
                                    sendUiEvent(
                                        UiEvent.ShowSnackbar(
                                            message = "Token expired, please re-open comment dialog.")
                                    )
                                    sendUiEvent(UiEvent.PopBackStack)
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