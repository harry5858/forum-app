package com.example.forumproject.feature_post.di

import com.example.forumproject.core.util.Constants
import com.example.forumproject.feature_post.data.api.PostApi
import com.example.forumproject.feature_post.data.repository.PostRepositoryImpl
import com.example.forumproject.feature_post.domain.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostModule {

    @Provides
    @Singleton
    fun providePostRepository(
        api: PostApi
    ): PostRepository {
        return PostRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun providePostApi(): PostApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostApi::class.java)
    }
}