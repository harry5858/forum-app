package com.example.forumproject.feature_post.data.model.response

data class PostResponse (
    val pid: Int,
    val title: String,
    val body: String,
    val author: String,
    val pub_date: String,
    val like: Boolean
)