package com.example.forumproject.feature_post.data.api

import com.example.forumproject.feature_post.data.model.requset.CommentRequest
import com.example.forumproject.feature_post.data.model.response.MessageResponse
import com.example.forumproject.feature_post.data.model.requset.PostRequest
import com.example.forumproject.feature_post.data.model.response.CommentResponse
import com.example.forumproject.feature_post.data.model.response.PostListResponse
import com.example.forumproject.feature_post.data.model.response.PostResponse
import retrofit2.Response
import retrofit2.http.*

interface PostApi {
    // need token
    @Headers("Accept-Encoding: identity")
    @POST("post/create")
    suspend fun createPost(
        @Header("Authorization") accessToken: String,
        @Body post: PostRequest
    ): Response<MessageResponse>

    @GET("post/{pid}")
    suspend fun getPostById(
        @Path("pid") pid: Int
    ): Response<PostResponse>

    @GET("post/")
    suspend fun getPostList(): Response<List<PostListResponse>>

    @GET("post/tag/{tag}")
    suspend fun getPostsByTag(
        @Path("tag") tag: String
    ): Response<List<PostListResponse>>

    // need token
    @Headers("Accept-Encoding: identity")
    @DELETE("post/{pid}")
    suspend fun deletePost(
        @Header("Authorization") accessToken: String,
        @Path("pid") pid: Int,
    ): Response<MessageResponse>

    // need token
    @Headers("Accept-Encoding: identity")
    @POST("post/{pid}/comment")
    suspend fun createComment(
        @Header("Authorization") accessToken: String,
        @Path("pid") pid: Int,
        @Body comment: CommentRequest
    ): Response<MessageResponse>

    @GET("post/{pid}/comment")
    suspend fun getCommentForPost(
        @Path("pid") pid: Int
    ): Response<List<CommentResponse>>

    @Headers("Accept-Encoding: identity")
    @POST("post/{pid}/like-dislike")
    suspend fun likePost(
        @Header("Authorization") accessToken: String,
        @Path("pid") pid: Int
    ): Response<MessageResponse>

    @Headers("Accept-Encoding: identity")
    @DELETE("post/{pid}/like-dislike")
    suspend fun dislikePost(
        @Header("Authorization") accessToken: String,
        @Path("pid") pid: Int
    ): Response<MessageResponse>

}