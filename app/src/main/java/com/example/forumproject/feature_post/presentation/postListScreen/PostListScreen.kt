package com.example.forumproject.feature_post.presentation.postListScreen


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.forumproject.core.util.UiEvent
import kotlinx.coroutines.flow.collect

@Composable
fun PostListScreen(
    scaffoldState: ScaffoldState,
    onNavigate: (UiEvent.Navigate) -> Unit,
    postListViewModel: PostListViewModel = hiltViewModel()
) {

//    val posts =
//        postListViewModel.posts.collectAsState(initial = emptyList())

//    Log.d("Posts", posts.toString())

    LaunchedEffect(key1 = true) {
        postListViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

//    if (posts.value.isEmpty()) {
//        Text(
//            text = "No posts",
//            fontSize = 25.sp,
//        )
//    } else {
//        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
//            contentPadding = PaddingValues(all = 12.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            items(posts.value) { post ->
//                PostItem(
//                    post = post,
//                    onEvent = postListViewModel::onEvent
//                )
//            }
//        }
//    }

    if (postListViewModel.posts.value.isEmpty()) {
        Text(
            text = "No posts",
            fontSize = 25.sp,
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(all = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(postListViewModel.posts.value) { post ->
                PostItem(
                    post = post,
                    onEvent = postListViewModel::onEvent
                )
            }
        }
    }

}