package com.example.forumproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.rememberNavController
import com.example.forumproject.navigation.mainScreen.MainScreen
import com.example.forumproject.ui.theme.ForumProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContent {
            ForumProjectTheme {
                val navController = rememberNavController()

                MainScreen(
                    navController = navController,
                )
            }
        }
    }
}
