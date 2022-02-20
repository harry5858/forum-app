package com.example.forumproject.feature_post.presentation.commentCreateDialog

sealed class CommentCreateUiEvent{
    class OnDescriptionChange(val description: String): CommentCreateUiEvent()
    object OnDismissClick: CommentCreateUiEvent()
    object OnDoneClick: CommentCreateUiEvent()
}
