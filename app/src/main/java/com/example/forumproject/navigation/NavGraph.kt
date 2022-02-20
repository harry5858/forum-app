package com.example.forumproject.navigation

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.example.forumproject.feature_auth.presentation.AuthRoutes
import com.example.forumproject.feature_auth.presentation.loginScreen.LoginScreen
import com.example.forumproject.feature_auth.presentation.signUpScreen.SignUpScreen
import com.example.forumproject.feature_post.presentation.PostRoutes
import com.example.forumproject.feature_post.presentation.commentCreateDialog.CommentCreateDialog
import com.example.forumproject.feature_post.presentation.postCreateScreen.PostCreateScreen
import com.example.forumproject.feature_post.presentation.postDetailScreen.postDetail.PostDetailScreen
import com.example.forumproject.feature_post.presentation.postListScreen.PostListScreen

@ExperimentalComposeUiApi
@Composable
fun MainNavigation(
    navController: NavHostController,
    scaffoldState: ScaffoldState
){
    NavHost(
        navController = navController,
       startDestination = PostRoutes.POST_LIST + "?tag={tag}"
    ) {
        composable(AuthRoutes.LOGIN) {
            LoginScreen(
                onPopBackStack = {
                    navController.popBackStack(route = PostRoutes.POST_LIST+"?tag={tag}", inclusive = false)
                },
                onNavigate = {
                    navController.navigate(it.route)
                 },
                scaffoldState = scaffoldState)
        }
        composable(AuthRoutes.SIGN_UP) {
            SignUpScreen(
                onPopBackStack = {
                    navController.popBackStack(route = AuthRoutes.LOGIN, inclusive = false)
                },
                onNavigate = {
                    navController.navigate(it.route)
                },
                scaffoldState = scaffoldState
            )
        }
        composable(
            PostRoutes.POST_LIST + "?tag={tag}",
            arguments = listOf(
                navArgument(name = "tag") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            PostListScreen(
                scaffoldState = scaffoldState,
                onNavigate = {
                    navController.navigate(it.route)
                }
            )
        }
        composable(
            PostRoutes.POST_DETAIL + "?pid={pid}",
            arguments = listOf(
                navArgument(name = "pid") {
                    type = NavType.IntType
                }
            )
        ) {
            PostDetailScreen(
                scaffoldState = scaffoldState,
                onNavigate = {
                    navController.navigate(it.route)
                }
            )
        }
        composable(PostRoutes.POST_CREATE){
            PostCreateScreen(
                onPopBackStack = {
                     navController.popBackStack(route = PostRoutes.POST_LIST+"?tag={tag}", inclusive = false)
                },
                scaffoldState = scaffoldState
            )
        }
        dialog(
            PostRoutes.COMMENT_CREATE + "?pid={pid}",
            arguments = listOf(
                navArgument(name = "pid"){
                    type = NavType.IntType
                }
            )
        ) {
            CommentCreateDialog(
                onPopBackStack = {
                    navController.popBackStack()
                },
                scaffoldState = scaffoldState
            )
        }
    }
}
