package com.example.forumproject.feature_post.presentation.postCreateScreen

sealed class PostCreateUiEvent {
    class OnTitleChange(val title: String): PostCreateUiEvent()
    class OnDescriptionChange(val description: String): PostCreateUiEvent()
    class OnTagChange(val tag: String): PostCreateUiEvent()
    object OnDoneClick: PostCreateUiEvent()
}