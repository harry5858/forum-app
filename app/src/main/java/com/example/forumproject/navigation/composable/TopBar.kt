package com.example.forumproject.navigation.composable

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import com.example.forumproject.ui.theme.Purple500

@Composable
fun TopBar(
    currentDestination: String,
    onNavIconClick: () -> Unit,
    onSearchButtonClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = currentDestination)
        },
        navigationIcon = {
            IconButton(
                onClick = onNavIconClick
            ) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu Button")
            }
        },
        actions = {
            IconButton(
                onClick = onSearchButtonClick
            ) {
                Icon(Icons.Filled.Search, contentDescription = "Search Button")
            }
        },
        backgroundColor = Purple500,
        elevation = AppBarDefaults.TopAppBarElevation
    )
}
