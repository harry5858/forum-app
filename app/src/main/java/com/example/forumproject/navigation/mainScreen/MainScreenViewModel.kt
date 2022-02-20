package com.example.forumproject.navigation.mainScreen

import android.util.Log
import androidx.compose.material.DrawerValue

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.core.util.Resource
import com.example.forumproject.core.util.UiEvent
import com.example.forumproject.feature_auth.domain.repository.AuthRepository
import com.example.forumproject.feature_auth.presentation.AuthRoutes
import com.example.forumproject.feature_dataStore.abstraction.DataStoreRepository
import com.example.forumproject.feature_post.presentation.PostRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val authRepository: AuthRepository
): ViewModel(){

    var username = mutableStateOf("")
    private set

    var loginState = mutableStateOf(false)
    private set

    var topBarState = mutableStateOf(false)
    private set

    var query = mutableStateOf("")
    private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: MainScreenUiEvent) {
        when (event) {
            is MainScreenUiEvent.OnQueryChange -> {
                query.value = event.query
            }
            is MainScreenUiEvent.OnDrawerClick -> {
                viewModelScope.launch {
                    val refreshToken = dataStoreRepository.getString("refresh")
                    Log.d("token", refreshToken ?: "No refresh token")
                    if (refreshToken != null) {
                        authRepository.refreshToken("Bearer $refreshToken").collect { result ->
                            when (result) {
                                is Resource.Success -> {
                                    loginState.value = true
                                    dataStoreRepository.putString("access", result.data!!.access)
                                    username.value = dataStoreRepository.getString("username")!!
                                }
                                is Resource.Error -> {

                                    loginState.value = false
                                    sendUiEvent(UiEvent.ShowSnackbar(
                                        message = "Token expired, Please Login again")
                                    )
                                }
                                else -> {
                                    sendUiEvent(UiEvent.ShowSnackbar(
                                        message = "Error")
                                    )
                                }
                            }
                        }
                    }
                }
                sendUiEvent(UiEvent.OpenDrawer)
            }
            is MainScreenUiEvent.OnSearchToggle -> {
                sendUiEvent(UiEvent.ToggleSearchBar)
            }
            is MainScreenUiEvent.OnSearchConfirm -> {
                // send a post request to post api
            }
            is MainScreenUiEvent.OnLoginSignUpClick -> {
                sendUiEvent(UiEvent.CloseDrawer)
                sendUiEvent(UiEvent.Navigate(AuthRoutes.LOGIN))
            }
            is MainScreenUiEvent.OnFabClick -> {
                viewModelScope.launch {
                    val refreshToken = dataStoreRepository.getString("refresh")
                    if (refreshToken == null) {
                        sendUiEvent(UiEvent.ShowSnackbar(message = "Login required."))
                    } else {
                        authRepository.refreshToken("Bearer $refreshToken").collect { result ->
                            when (result) {
                                is Resource.Success -> {
                                    loginState.value = true
                                    dataStoreRepository.putString("access", result.data!!.access)
                                    sendUiEvent(UiEvent.Navigate(PostRoutes.POST_CREATE))
                                }
                                is Resource.Error -> {
                                    loginState.value = false
                                    sendUiEvent(
                                        UiEvent.ShowSnackbar(
                                            message = "Token expired, Please Login again"
                                        )
                                    )
                                }
                                else -> Unit
                            }
                        }
                    }
                }
            }
            is MainScreenUiEvent.OnLogoutClick -> {
                viewModelScope.launch {
                    dataStoreRepository.deleteKey("access")
                    dataStoreRepository.deleteKey("refresh")
                    dataStoreRepository.deleteKey("username")
                    Log.d("token", dataStoreRepository.getString("refresh") ?:"Still")
                }
                loginState.value = false
                sendUiEvent(UiEvent.CloseDrawer)
            }

            is MainScreenUiEvent.OnTagClick -> {
                Log.d("tag", "?tag=${event.tag}")
                sendUiEvent(UiEvent.Navigate(PostRoutes.POST_LIST + "?tag=${event.tag}"))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}