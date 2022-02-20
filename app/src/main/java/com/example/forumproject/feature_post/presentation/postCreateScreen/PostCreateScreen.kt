package com.example.forumproject.feature_post.presentation.postCreateScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.forumproject.core.util.UiEvent
import kotlinx.coroutines.flow.collect

@Composable
fun PostCreateScreen(
    onPopBackStack: () -> Unit,
    postCreateScreenViewModel: PostCreateScreenViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState
) {
    LaunchedEffect(key1 = true) {
        postCreateScreenViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is UiEvent.PopBackStack -> onPopBackStack()
                else -> Unit
            }
        }
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFEDEAE0))
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = postCreateScreenViewModel.title.value,
            onValueChange = { postCreateScreenViewModel.onEvent(PostCreateUiEvent.OnTitleChange(it)) },
            label = {
                Text(
                    text = "Title"
                )
            },
            placeholder = {
                Text(
                    text = "Input title here..."
                )
            },
            singleLine = true
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically),
            value = postCreateScreenViewModel.description.value,
            onValueChange = {
                postCreateScreenViewModel.onEvent(PostCreateUiEvent.OnDescriptionChange(it))
            },
            label = {
              Text(text = "Description")
            },
            placeholder = {
                Text(text = "Input description here...")
            },
            maxLines = 20
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Tag (optional)"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = Tag.GENERAL)
                RadioButton(
                    selected = postCreateScreenViewModel.tag.value == Tag.GENERAL,
                    onClick = {
                        postCreateScreenViewModel.onEvent(PostCreateUiEvent.OnTagChange(Tag.GENERAL))
                    }
                )
                Text(text = Tag.MUSIC)
                RadioButton(
                    selected = postCreateScreenViewModel.tag.value == Tag.MUSIC,
                    onClick = {
                        postCreateScreenViewModel.onEvent(PostCreateUiEvent.OnTagChange(Tag.MUSIC))
                    }
                )
                Text(text = Tag.SPORT)
                RadioButton(
                    selected = postCreateScreenViewModel.tag.value == Tag.SPORT,
                    onClick = {
                        postCreateScreenViewModel.onEvent(PostCreateUiEvent.OnTagChange(Tag.SPORT))
                    }
                )
                Text(text = Tag.ANIME)
                RadioButton(
                    selected = postCreateScreenViewModel.tag.value == Tag.ANIME,
                    onClick = {
                        postCreateScreenViewModel.onEvent(PostCreateUiEvent.OnTagChange(Tag.ANIME))
                    }
                )
            }
        }

        Divider(color = Color.Black, thickness = 1.dp)

        Button(onClick = { postCreateScreenViewModel.onEvent(PostCreateUiEvent.OnDoneClick) }) {
            Text(
                text = "Done"
            )
        }
    }
}

object Tag {
    const val GENERAL = "General"
    const val MUSIC = "Music"
    const val SPORT = "Sport"
    const val ANIME = "Anime"
}