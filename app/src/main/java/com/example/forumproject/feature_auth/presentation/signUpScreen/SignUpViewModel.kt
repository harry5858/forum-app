package com.example.forumproject.feature_auth.presentation.signUpScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.core.util.Resource
import com.example.forumproject.core.util.UiEvent
import com.example.forumproject.feature_auth.data.model.request.UserRequest
import com.example.forumproject.feature_auth.domain.repository.AuthRepository
import com.example.forumproject.feature_auth.presentation.AuthRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(
    private val repository: AuthRepository
) : ViewModel(){
    var username = mutableStateOf("")
        private set

    var password = mutableStateOf("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.OnUsernameChange -> {
                username.value = event.username
            }
            is SignUpUiEvent.OnPasswordChange -> {
                password.value = event.password
            }
            is SignUpUiEvent.OnSignUpClick -> {
                viewModelScope.launch {
                    repository.signUp(
                        UserRequest(
                            username = username.value,
                            password = password.value
                        )
                    ).collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                sendUiEvent(UiEvent.ShowSnackbar(message = result.data?.msg ?: ""))
                                sendUiEvent(UiEvent.PopBackStack)
//                                sendUiEvent(UiEvent.Navigate(AuthRoutes.LOGIN))
                            }
                            is Resource.Error -> {
                                sendUiEvent(UiEvent.ShowSnackbar(message = result.data?.msg ?: "Error."))
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