package com.example.forumproject.feature_post.data.repository

import android.util.Log
import com.example.dictionary.core.util.Resource
import com.example.forumproject.feature_post.data.api.PostApi
import com.example.forumproject.feature_post.data.model.requset.CommentRequest
import com.example.forumproject.feature_post.data.model.requset.PostRequest
import com.example.forumproject.feature_post.data.model.response.CommentResponse
import com.example.forumproject.feature_post.data.model.response.MessageResponse
import com.example.forumproject.feature_post.data.model.response.PostListResponse
import com.example.forumproject.feature_post.data.model.response.PostResponse
import com.example.forumproject.feature_post.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.internal.http2.ConnectionShutdownException
import retrofit2.HttpException
import java.net.ConnectException
import java.nio.channels.ConnectionPendingException

class PostRepositoryImpl(
    private val postApi: PostApi
): PostRepository {
    override fun createPost(accessToken: String, post: PostRequest): Flow<Resource<MessageResponse>> = flow {
        try {
            val response = postApi.createPost(accessToken, post)

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()))
                400 -> emit(Resource.Error(message=response.body().toString(),data = response.body()))
                500 -> emit(Resource.Error(message=response.body().toString(),data = response.body()))
                401 -> emit(Resource.TokenExpired(message=response.body().toString(),data = response.body()))
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

    override fun getPostById(pid: Int): Flow<PostResponse> = flow {
        try {
            val response = postApi.getPostById(pid)

            when (response.code()) {
                200 -> emit(response.body()!!)
                500 -> emit(response.body()!!)
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

    override fun getAllPost(): Flow<List<PostListResponse>> = flow {
        try {
            Log.d("posts list", "called")
            val response = postApi.getPostList()

            when (response.code()) {
                200 -> emit(response.body()!!)
                500 -> emit(response.body()!!)
            }
        } catch (e: Exception) {
            when (e) {
                is ConnectException -> {
                    Log.d("connection", "exception")
                    emit(emptyList<PostListResponse>())
                }
                is HttpException -> {
                    Log.d("Http", "exception")
                    emit(emptyList<PostListResponse>())
                }
            }
        }
    }

    override fun filterPostByTag(tag: String): Flow<List<PostListResponse>> = flow {
        try {
            val response = postApi.getPostsByTag(tag)

            when (response.code()) {
                200 -> emit(response.body()!!)
                500 -> emit(response.body()!!)
            }
        } catch (e: Exception) {
            when (e) {
                is ConnectException -> {
                    Log.d("connection", "exception")
                    emit(emptyList<PostListResponse>())
                }
                is HttpException -> {
                    Log.d("Http", "exception")
                    emit(emptyList<PostListResponse>())
                }
            }
        }

    }

    override fun deletePost(id: Int): Flow<Resource<MessageResponse>> {
        TODO("Not yet implemented")
    }

    override fun postComment(pid: Int, accessToken: String, comment : CommentRequest): Flow<Resource<MessageResponse>> = flow {
        try {
            val response = postApi.createComment(
                pid = pid,
                accessToken = accessToken,
                comment = comment
            )

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()))
                500 -> emit(Resource.Error(message=response.body().toString(),data = response.body()))
                401 -> emit(Resource.TokenExpired(message=response.body().toString(),data = response.body()))
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

    override fun getPostComment(pid: Int): Flow<List<CommentResponse>> = flow {
        try {
            val response = postApi.getCommentForPost(pid = pid)

            when (response.code()) {
                200 -> emit(response.body()!!)
                500 -> emit(response.body()!!)
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

    override fun likePost(pid: Int, accessToken: String): Flow<Resource<MessageResponse>> = flow {
        try {
            val response = postApi.likePost(pid = pid, accessToken = accessToken)

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()))
                500 -> emit(Resource.Error(message=response.body().toString(),data = response.body()))
                401 -> emit(Resource.TokenExpired(message=response.body().toString(),data = response.body()))
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

    override fun dislikePost(pid: Int, accessToken: String): Flow<Resource<MessageResponse>> = flow {
        try {
            val response = postApi.dislikePost(pid = pid, accessToken = accessToken)

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()))
                400 -> emit(Resource.Error(message=response.body().toString(),data = response.body()))
                500 -> emit(Resource.Error(message=response.body().toString(),data = response.body()))
                401 -> emit(Resource.TokenExpired(message=response.body().toString(),data = response.body()))
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