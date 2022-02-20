package com.example.forumproject.feature_post.data.model.response

data class CommentResponse(
    val cid: Int,
    val body: String,
    val author: String,
    val pub_date: String
)
