package com.example.forumproject.feature_auth.data.repository

import android.util.Log
import com.example.dictionary.core.util.Resource
import com.example.forumproject.feature_auth.data.api.AuthApi
import com.example.forumproject.feature_auth.data.model.request.GoogleUserRequest
import com.example.forumproject.feature_auth.data.model.response.LoginResponse
import com.example.forumproject.feature_auth.data.model.response.MessageResponse
import com.example.forumproject.feature_auth.data.model.response.RefreshResponse
import com.example.forumproject.feature_auth.data.model.request.UserRequest
import com.example.forumproject.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.ConnectException

class AuthRepositoryImpl(
    private val api: AuthApi
): AuthRepository {
    override fun signUp(userRequest: UserRequest): Flow<Resource<MessageResponse>> = flow {
        try {
            val response = api.signUp(userRequest)

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()))
                400 -> emit(Resource.Error(message=response.body().toString(),data = response.body()))
                else -> emit(Resource.Error(message=response.body().toString(),data = response.body()))
            }
        } catch (e: Exception) {
            when (e) {
                is ConnectException -> {
                    Log.d("connection", "exception")
                }
                is HttpException -> {
                    Log.d("Http", "exception")
                }
            }
        }
    }

    override fun login(userRequest: UserRequest): Flow<Resource<LoginResponse>> = flow {
        try {
            val response = api.login(userRequest)

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()))
                400 -> emit(Resource.Error(message=response.body().toString(),data = response.body()))
                else -> emit(Resource.Error(message=response.body().toString(),data = response.body()))
            }
        } catch (e: Exception) {
            when (e) {
                is ConnectException -> {
                    Log.d("connection", "exception")
                }
                is HttpException -> {
                    Log.d("Http", "exception")
                }
            }
        }
    }

    override fun refreshToken(refreshToken: String): Flow<Resource<RefreshResponse>> = flow {
        try {
            val response = api.refreshToken(refreshToken)

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()))
                400 -> emit(Resource.Error(message=response.body().toString(),data = response.body()))
                else -> emit(Resource.Error(message=response.body().toString(),data = response.body()))
            }
        } catch (e: Exception) {
            when (e) {
                is ConnectException -> {
                    Log.d("connection", "exception")
                }
                is HttpException -> {
                    Log.d("Http", "exception")
                }
            }
        }
    }

    override fun googleOneTap(googleUserRequest: GoogleUserRequest): Flow<Resource<LoginResponse>> = flow {
        try {
            val response = api.googleOneTap(googleUserRequest)

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()))
                400 -> emit(Resource.Error(message=response.body().toString(),data = response.body()))
                else -> emit(Resource.Error(message=response.body().toString(),data = response.body()))
            }

        } catch (e: Exception) {
            when (e) {
                is ConnectException -> {
                    Log.d("connection", "exception")
                }
                is HttpException -> {
                    Log.d("Http", "exception")
                }
            }
        }
    }
}