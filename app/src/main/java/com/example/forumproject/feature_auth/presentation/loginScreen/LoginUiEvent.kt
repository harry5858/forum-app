package com.example.forumproject.feature_auth.presentation.loginScreen

sealed class LoginUiEvent {
    data class OnUsernameChange(val username: String): LoginUiEvent()
    data class OnPasswordChange(val password: String): LoginUiEvent()
    object OnLoginClick: LoginUiEvent()
    object OnSignUpClick: LoginUiEvent()
    object OnPasswordVisibilityToggle: LoginUiEvent()
    // handle google one tap
    object OnGoogleOneTapClick: LoginUiEvent()
    data class OnOneTapSuccess(val idToken: String): LoginUiEvent()
    object OnOneTapFail: LoginUiEvent()
    object OnOneTapCancel: LoginUiEvent()
}