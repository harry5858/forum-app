package com.example.forumproject.feature_auth.presentation.signUpScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.forumproject.core.util.UiEvent
import kotlinx.coroutines.flow.collect

@ExperimentalComposeUiApi
@Composable
fun SignUpScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState
) {
    val passwordVisibility = remember { mutableStateOf(true) }

    val keyboardController = LocalSoftwareKeyboardController.current


    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is UiEvent.Navigate -> onNavigate(event)

                is UiEvent.PopBackStack -> onPopBackStack()
                else -> Unit
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = viewModel.username.value,
            onValueChange = { viewModel.onEvent(SignUpUiEvent.OnUsernameChange(it)) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = viewModel.password.value,
            onValueChange = { viewModel.onEvent(SignUpUiEvent.OnPasswordChange(it)) },
            trailingIcon = {
                IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                    if (passwordVisibility.value) {
                        Icon(Icons.Default.Warning, contentDescription = null)
                    } else {
                        Icon(Icons.Default.Star, contentDescription = null)
                    }
                }
            },
            visualTransformation = if (passwordVisibility.value) PasswordVisualTransformation()
            else VisualTransformation.None
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                viewModel.onEvent(SignUpUiEvent.OnSignUpClick)
                keyboardController?.hide()
            }
        ) {
            Text(text = "Sign Up")
        }
    }
}