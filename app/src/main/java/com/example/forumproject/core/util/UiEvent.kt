package com.example.forumproject.core.util

sealed class UiEvent {
    object PopBackStack: UiEvent()
    data class Navigate(val route: String): UiEvent()
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ): UiEvent()
    object OpenDrawer: UiEvent()
    object CloseDrawer: UiEvent()
    object ToggleSearchBar: UiEvent()
}
