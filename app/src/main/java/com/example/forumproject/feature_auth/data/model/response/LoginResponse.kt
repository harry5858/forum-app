package com.example.forumproject.feature_auth.data.model.response

data class LoginResponse(
    val msg: String,
    val username: String,
    val access: String,
    val refresh: String
)
