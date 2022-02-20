package com.example.forumproject.feature_dataStore.implementation

import android.content.Context
import android.system.Os.remove
import androidx.compose.ui.input.key.Key
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.forumproject.feature_dataStore.abstraction.DataStoreRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val context: Context
) : DataStoreRepository {

    private val Context.dataStore: DataStore<Preferences> by
    preferencesDataStore(name = "Tokens_Data_Store")


    override suspend fun putString(key: String, value: String) {
        val preferenceKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferenceKey] = value
        }
    }

    override suspend fun getString(key: String): String? {
        return try {
            val preferencesKey = stringPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            preferences[preferencesKey]
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun putInt(key: String, value: Int) {
        val preferenceKey = intPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferenceKey] = value
        }
    }

    override suspend fun getInt(key: String): Int? {
        return try {
            val preferencesKey = intPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            preferences[preferencesKey]
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun deleteKey(key: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit {
            it.remove(preferencesKey)
        }
    }

}
