package com.example.forumproject.feature_auth.data.api

import com.example.forumproject.feature_auth.data.model.request.GoogleUserRequest
import com.example.forumproject.feature_auth.data.model.response.LoginResponse
import com.example.forumproject.feature_auth.data.model.response.MessageResponse
import com.example.forumproject.feature_auth.data.model.response.RefreshResponse
import com.example.forumproject.feature_auth.data.model.request.UserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/register")
    suspend fun signUp(
        @Body user: UserRequest
    ): Response<MessageResponse>

    @POST("auth/login")
    suspend fun login(
        @Body user: UserRequest
    ): Response<LoginResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") refreshToken: String
    ): Response<RefreshResponse>

    @POST("auth/googleOneTap")
    suspend fun googleOneTap(
        @Body googleUser: GoogleUserRequest
    ): Response<LoginResponse>
}