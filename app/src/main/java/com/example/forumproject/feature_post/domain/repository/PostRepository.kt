package com.example.forumproject.feature_post.domain.repository

import com.example.dictionary.core.util.Resource
import com.example.forumproject.feature_post.data.model.requset.CommentRequest
import com.example.forumproject.feature_post.data.model.requset.PostRequest
import com.example.forumproject.feature_post.data.model.response.CommentResponse
import com.example.forumproject.feature_post.data.model.response.MessageResponse
import com.example.forumproject.feature_post.data.model.response.PostListResponse
import com.example.forumproject.feature_post.data.model.response.PostResponse
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    fun createPost(accessToken: String, post: PostRequest): Flow<Resource<MessageResponse>>

    fun getPostById(pid: Int): Flow<PostResponse>

    fun getAllPost(): Flow<List<PostListResponse>>

    fun filterPostByTag(tag: String): Flow<List<PostListResponse>>

    fun deletePost(id: Int): Flow<Resource<MessageResponse>>

    fun postComment(pid: Int, accessToken: String, comment: CommentRequest): Flow<Resource<MessageResponse>>

    fun getPostComment(pid: Int): Flow<List<CommentResponse>>

    fun likePost(pid: Int, accessToken: String): Flow<Resource<MessageResponse>>

    fun dislikePost(pid: Int, accessToken: String): Flow<Resource<MessageResponse>>

}