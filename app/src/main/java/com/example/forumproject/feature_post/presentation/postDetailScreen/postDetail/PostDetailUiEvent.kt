package com.example.forumproject.feature_post.presentation.postDetailScreen.postDetail

import com.example.forumproject.feature_post.data.model.response.PostResponse

sealed class PostDetailUiEvent {
    class OnAddComment(val post: PostResponse): PostDetailUiEvent()
    object OnLikeToggle: PostDetailUiEvent()
}
