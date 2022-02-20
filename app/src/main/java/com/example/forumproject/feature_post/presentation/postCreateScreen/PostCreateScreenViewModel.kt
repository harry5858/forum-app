package com.example.forumproject.feature_post.presentation.postCreateScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.core.util.Resource
import com.example.forumproject.core.util.UiEvent
import com.example.forumproject.feature_auth.domain.repository.AuthRepository
import com.example.forumproject.feature_dataStore.abstraction.DataStoreRepository
import com.example.forumproject.feature_post.data.model.requset.PostRequest
import com.example.forumproject.feature_post.domain.repository.PostRepository
import com.example.forumproject.feature_post.presentation.PostRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostCreateScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    var title = mutableStateOf("")
    private set

    var description = mutableStateOf("")
    private set

    var tag = mutableStateOf("")
    private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: PostCreateUiEvent) {
        when (event) {
            is PostCreateUiEvent.OnTitleChange -> {
                title.value = event.title
            }
            is PostCreateUiEvent.OnDescriptionChange -> {
                description.value = event.description
            }
            is PostCreateUiEvent.OnTagChange -> {
                tag.value = event.tag
            }
            is PostCreateUiEvent.OnDoneClick -> {
                viewModelScope.launch {
                    val accessToken = dataStoreRepository.getString("access")
                    if (title.value == "") {
                        sendUiEvent(UiEvent.ShowSnackbar(message = "Title is required."))
                    } else if (description.value == "") {
                        sendUiEvent(UiEvent.ShowSnackbar(message = "Description is required."))
                    } else {
                        postRepository.createPost(
                            accessToken = "Bearer $accessToken",
                            post = PostRequest(
                                title = title.value,
                                body = description.value,
                                tag = tag.value
                            )
                        ).collect { result ->
                            when (result) {
                                is Resource.Success -> {
                                     sendUiEvent(UiEvent.ShowSnackbar(message = "Post created."))
                                     sendUiEvent(UiEvent.PopBackStack)
                                 }
                                is Resource.Error -> {
                                    sendUiEvent(UiEvent.ShowSnackbar(message = "Error."))
                                }
                                is Resource.TokenExpired -> {
                                    sendUiEvent(UiEvent.ShowSnackbar(message = "Access token expired. Please reload page."))
                                    sendUiEvent(UiEvent.Navigate(PostRoutes.POST_LIST))
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