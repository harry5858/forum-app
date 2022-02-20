package com.example.forumproject.feature_auth.presentation.loginScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dictionary.core.util.Resource
import com.example.forumproject.core.util.UiEvent
import com.example.forumproject.feature_auth.data.model.request.GoogleUserRequest
import com.example.forumproject.feature_auth.data.model.request.UserRequest
import com.example.forumproject.feature_auth.domain.repository.AuthRepository
import com.example.forumproject.feature_auth.presentation.AuthRoutes
import com.example.forumproject.feature_dataStore.abstraction.DataStoreRepository
import com.example.forumproject.feature_post.presentation.PostRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val dataRepository: DataStoreRepository
) : ViewModel() {

    var username = mutableStateOf("")
        private set

    var password = mutableStateOf("")
        private set

    var loginStatus = mutableStateOf(false)
        private set

    var passwordVisibility = mutableStateOf(true)
        private set

    var googleOneTap = mutableStateOf(false)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnUsernameChange -> {
                username.value = event.username
            }
            is LoginUiEvent.OnPasswordChange -> {
                password.value = event.password
            }
            is LoginUiEvent.OnSignUpClick -> {
                sendUiEvent(UiEvent.Navigate(AuthRoutes.SIGN_UP))
            }
            is LoginUiEvent.OnLoginClick -> {
                viewModelScope.launch {
                    repository.login(
                        UserRequest(
                            username = username.value,
                            password = password.value
                        )
                    ).collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                dataRepository.putString("username", result.data?.username ?: "")
                                dataRepository.putString("access", result.data?.access ?: "")
                                Log.d("access", result.data?.access ?: "")
                                dataRepository.putString("refresh", result.data?.refresh ?: "")
                                sendUiEvent(UiEvent.ShowSnackbar(message = result.data?.msg ?: ""))
                                sendUiEvent(UiEvent.PopBackStack)
                                sendUiEvent(UiEvent.Navigate(route = PostRoutes.POST_LIST))
                                loginStatus.value = true
                            }
                            is Resource.Error -> {
                                sendUiEvent(UiEvent.ShowSnackbar(message = result.data?.msg ?: "Error."))
                            }
                        }
                    }
                }
            }
            is LoginUiEvent.OnPasswordVisibilityToggle -> {
                passwordVisibility.value = !passwordVisibility.value
            }
            is LoginUiEvent.OnGoogleOneTapClick -> {
                googleOneTap.value  = !googleOneTap.value
            }
            is LoginUiEvent.OnOneTapSuccess -> {
                // send post request to flask app
                Log.d("One Tap success", "one tap success")
                Log.d("One Tap success", event.idToken)
                viewModelScope.launch {
                    repository.googleOneTap(
                        GoogleUserRequest(
                            idToken = event.idToken
                        )
                    ).collect{ result ->
                        when (result) {
                            is Resource.Success -> {
                                dataRepository.putString("username", result.data?.username ?: "")
                                dataRepository.putString("access", result.data?.access ?: "")
                                Log.d("access", result.data?.access ?: "")
                                dataRepository.putString("refresh", result.data?.refresh ?: "")
                                sendUiEvent(UiEvent.ShowSnackbar(message = result.data?.msg ?: ""))
                                sendUiEvent(UiEvent.PopBackStack)
                                sendUiEvent(UiEvent.Navigate(route = PostRoutes.POST_LIST))
                                loginStatus.value = true
                            }
                            is Resource.Error -> {
                                sendUiEvent(UiEvent.ShowSnackbar(message = result.data?.msg ?: "Error."))
                            }
                            else -> Unit
                        }
                    }
                }
            }
            is LoginUiEvent.OnOneTapFail -> {
                sendUiEvent(UiEvent.ShowSnackbar(message = "Failed to login."))
            }
            is LoginUiEvent.OnOneTapCancel -> {
                googleOneTap.value = false
                sendUiEvent(UiEvent.ShowSnackbar(message = "Failed to login."))
            }
        }
    }
    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}