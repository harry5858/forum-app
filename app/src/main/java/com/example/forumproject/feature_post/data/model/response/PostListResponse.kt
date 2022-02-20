package com.example.forumproject.feature_post.data.model.response

data class PostListResponse(
    val pid: Int,
    val title: String,
    val author: String,
    val pub_data: String,
    val tag: String
)
