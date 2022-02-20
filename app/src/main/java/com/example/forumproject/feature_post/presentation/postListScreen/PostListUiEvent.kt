package com.example.forumproject.feature_post.presentation.postListScreen

import com.example.forumproject.feature_post.data.model.response.PostListResponse

sealed class PostListUiEvent {
    class OnPostClick(val post: PostListResponse): PostListUiEvent()
}