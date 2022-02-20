package com.example.forumproject.feature_auth.presentation.loginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.example.forumproject.R
import com.example.forumproject.core.util.UiEvent
import com.example.forumproject.feature_auth.presentation.loginScreen.googleOneTap.GoogleOneTap
import kotlinx.coroutines.flow.collect

@ExperimentalComposeUiApi
@Composable
fun LoginScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true) {
        loginViewModel.uiEvent.collect { event ->
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
            value = loginViewModel.username.value,
            onValueChange = { loginViewModel.onEvent(LoginUiEvent.OnUsernameChange(it)) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = loginViewModel.password.value,
            singleLine = true,
            onValueChange = { loginViewModel.onEvent(LoginUiEvent.OnPasswordChange(it)) },
            trailingIcon = {
                IconButton(onClick = { loginViewModel.onEvent(LoginUiEvent.OnPasswordVisibilityToggle) }) {
                    if (loginViewModel.passwordVisibility.value) {
                        Icon(Icons.Default.Warning, contentDescription = null)
                    } else {
                        Icon(Icons.Default.Star, contentDescription = null)
                    }
                }
            },
            visualTransformation = if (loginViewModel.passwordVisibility.value) PasswordVisualTransformation()
            else VisualTransformation.None
        )

        Spacer(modifier = Modifier.height(6.dp))

        Button(onClick = {
            loginViewModel.onEvent(LoginUiEvent.OnLoginClick)
            keyboardController?.hide()
        }) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            modifier = Modifier.clickable {
                loginViewModel.onEvent(LoginUiEvent.OnSignUpClick)
            },
            text = "Create an account."
        )

        Spacer(modifier = Modifier.height(6.dp))

        GoogleOneTap(
            onEvent = loginViewModel::onEvent
        )
    }
}


