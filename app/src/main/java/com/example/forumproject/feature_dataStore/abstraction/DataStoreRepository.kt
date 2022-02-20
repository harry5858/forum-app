package com.example.forumproject.feature_dataStore.abstraction

interface DataStoreRepository {

    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String): String?

    suspend fun putInt(key: String, value: Int)
    suspend fun getInt(key: String): Int?

    suspend fun deleteKey(key: String)
}