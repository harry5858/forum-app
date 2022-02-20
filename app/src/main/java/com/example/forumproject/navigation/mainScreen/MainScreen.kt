package com.example.forumproject.navigation.mainScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.forumproject.core.internetConnectionTracker.ConnectionState
import com.example.forumproject.core.internetConnectionTracker.ConnectivityStatus
import com.example.forumproject.core.internetConnectionTracker.ConnectivityStatusText
import com.example.forumproject.core.internetConnectionTracker.connectivityState
import com.example.forumproject.core.util.UiEvent
import com.example.forumproject.feature_post.presentation.PostRoutes
import com.example.forumproject.navigation.MainNavigation
import com.example.forumproject.navigation.composable.Fab
import com.example.forumproject.navigation.composable.SearchBar
import com.example.forumproject.navigation.composable.TopBar
import com.example.forumproject.navigation.composable.Drawer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect


@ExperimentalComposeUiApi
@Composable
fun MainScreen(
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState(
        rememberDrawerState(initialValue = DrawerValue.Closed)
    )

    val currentRoute = navController.currentBackStackEntryAsState()

    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available

    var visibility by remember { mutableStateOf(false) }

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            visibility = true
        } else {
            delay(2000)
            visibility = false
        }
    }

    LaunchedEffect(key1 = true) {
        mainScreenViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is UiEvent.Navigate -> navController.navigate(event.route)
                is UiEvent.OpenDrawer -> scaffoldState.drawerState.open()
                is UiEvent.CloseDrawer -> {
                    scaffoldState.drawerState.close()
                }
                is UiEvent.ToggleSearchBar -> {
                    mainScreenViewModel.topBarState.value = !mainScreenViewModel.topBarState.value
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (!mainScreenViewModel.topBarState.value) {
                TopBar(
                    currentDestination = "Forum App",
                    onNavIconClick = { mainScreenViewModel.onEvent(MainScreenUiEvent.OnDrawerClick) },
                    onSearchButtonClick = { mainScreenViewModel.onEvent(MainScreenUiEvent.OnSearchToggle) }
                )
            } else {
                SearchBar(
                    query = mainScreenViewModel.query.value,
                    onTextChange = { mainScreenViewModel.onEvent(MainScreenUiEvent.OnQueryChange(it)) } ,
                    onCloseClicked = { mainScreenViewModel.onEvent(MainScreenUiEvent.OnSearchToggle) },
                    onSearchClicked = {  }
                )
            }
        },
        drawerContent = {
            Drawer(
                loginState = mainScreenViewModel.loginState.value,
                username = mainScreenViewModel.username.value,
                onEvent = mainScreenViewModel::onEvent
            )
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        floatingActionButton = {
            if (currentRoute.value?.destination?.route?.contains(PostRoutes.POST_LIST) == true) {
                Fab(
                    onFabClick = {
                        mainScreenViewModel.onEvent(MainScreenUiEvent.OnFabClick)
                    }
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (visibility) {
                ConnectivityStatusText(isConnected = isConnected)
            }
            MainNavigation(
                navController = navController,
                scaffoldState = scaffoldState
            )
        }
    }
}