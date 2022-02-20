package com.example.forumproject.navigation.mainScreen

sealed class MainScreenUiEvent {
    class OnQueryChange(val query: String): MainScreenUiEvent()
    class OnTagClick(val tag: String): MainScreenUiEvent()
    object OnDrawerClick: MainScreenUiEvent()
    object OnSearchToggle: MainScreenUiEvent()
    object OnSearchConfirm: MainScreenUiEvent()
    object OnFabClick: MainScreenUiEvent()
    object OnLoginSignUpClick: MainScreenUiEvent()
    object OnLogoutClick: MainScreenUiEvent()
}