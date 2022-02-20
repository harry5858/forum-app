package com.example.forumproject.feature_post.presentation.postDetailScreen.postDetail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.forumproject.core.util.UiEvent
import com.example.forumproject.feature_post.data.model.response.CommentResponse
import com.example.forumproject.feature_post.data.model.response.PostResponse
import kotlinx.coroutines.flow.collect

@Composable
fun PostDetailScreen(
    scaffoldState: ScaffoldState,
    onNavigate: (UiEvent.Navigate) -> Unit,
    postDetailViewModel: PostDetailViewModel = hiltViewModel()
) {
    
    val listItemHolder = mutableListOf<Any>(postDetailViewModel.post.value?.body ?: "")

    val comments = postDetailViewModel.comments.collectAsState(initial = emptyList())

    Log.d("comments", comments.toString())

    comments.value.forEach { comment ->
        listItemHolder.add(comment)
    }

    LaunchedEffect(key1 = true) {
        postDetailViewModel.uiEvent.collect { event ->
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
    Column(
        modifier = Modifier.fillMaxSize(),

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(8f),
                text = postDetailViewModel.post.value?.title ?: "",
                fontSize = 24.sp,
                overflow = TextOverflow.Ellipsis
            )
            FavButton(
                state = postDetailViewModel.likeStatus.value,
                onEvent = postDetailViewModel::onEvent
            )
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    postDetailViewModel.onEvent(
                        PostDetailUiEvent.OnAddComment(postDetailViewModel.post.value!!)
                    )
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add comment")
            }
        }
        Divider(color = Color.Black, thickness = 1.dp)
        LazyColumn(
            contentPadding = PaddingValues(all = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listItemHolder) { item ->
                PostBodyAndComments(item = item)
            }
        }
    }
}

@Composable
fun FavButton(
    state: Boolean,
    onEvent: (PostDetailUiEvent) -> Unit
) {
    if (state) {
        IconButton(onClick = { onEvent(PostDetailUiEvent.OnLikeToggle) }) {
            Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
        }
    } else {
        IconButton(onClick = { onEvent(PostDetailUiEvent.OnLikeToggle) }) {
            Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = null)
        }
    }
}

