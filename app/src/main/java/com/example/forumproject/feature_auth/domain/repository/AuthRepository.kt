package com.example.forumproject.feature_auth.domain.repository

import com.example.dictionary.core.util.Resource
import com.example.forumproject.feature_auth.data.model.request.GoogleUserRequest
import com.example.forumproject.feature_auth.data.model.response.LoginResponse
import com.example.forumproject.feature_auth.data.model.response.MessageResponse
import com.example.forumproject.feature_auth.data.model.response.RefreshResponse
import com.example.forumproject.feature_auth.data.model.request.UserRequest
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signUp(userRequest: UserRequest): Flow<Resource<MessageResponse>>

    fun login(userRequest: UserRequest): Flow<Resource<LoginResponse>>

    fun refreshToken(refreshToken: String): Flow<Resource<RefreshResponse>>

    fun googleOneTap(googleUserRequest: GoogleUserRequest): Flow<Resource<LoginResponse>>
}