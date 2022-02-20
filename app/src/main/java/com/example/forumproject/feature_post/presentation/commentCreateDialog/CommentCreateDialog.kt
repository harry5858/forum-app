package com.example.forumproject.feature_post.presentation.commentCreateDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.forumproject.core.util.UiEvent
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommentCreateDialog(
    scaffoldState: ScaffoldState,
    onPopBackStack: () -> Unit,
    commentDialogViewModel: CommentDialogViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = true) {
        commentDialogViewModel.uiEvent.collect { event ->
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

    val state = remember { mutableStateOf(true) }

    val keyboardController = LocalSoftwareKeyboardController.current

    if (state.value) {
        Dialog(
            onDismissRequest = { state.value = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                securePolicy = SecureFlagPolicy.SecureOff)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFEDEAE0)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                commentDialogViewModel.onEvent(CommentCreateUiEvent.OnDismissClick)
                            }
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Dismiss")
                        }
                        Text(
                            modifier = Modifier.weight(3f),
                            text = "Add comment",
                            fontSize = 24.sp
                        )
                        IconButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                commentDialogViewModel.onEvent(CommentCreateUiEvent.OnDoneClick)
                                keyboardController?.hide()
                            }
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Dismiss")
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(color = Color.Black, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(4.dp))
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        value = commentDialogViewModel.description.value,
                        onValueChange = {
                            commentDialogViewModel.onEvent(CommentCreateUiEvent.OnDescriptionChange(it))
                        },
                        placeholder = {
                              Text(
                                  text = "Input description here..."
                              )
                        },
                        label = {
                                Text(
                                    text = "Comment description"
                                )
                        },
                        maxLines = 12,

                    )
                }
            }
        }
    }
}