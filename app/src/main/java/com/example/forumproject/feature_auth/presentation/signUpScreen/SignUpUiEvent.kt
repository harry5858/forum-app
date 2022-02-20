package com.example.forumproject.feature_auth.presentation.signUpScreen

sealed class SignUpUiEvent {
    data class OnUsernameChange(val username: String): SignUpUiEvent()
    data class OnPasswordChange(val password: String): SignUpUiEvent()
    object OnSignUpClick: SignUpUiEvent()
}