package com.example.forumproject.feature_auth.presentation.loginScreen.googleOneTap

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.forumproject.R
import com.example.forumproject.feature_auth.presentation.loginScreen.LoginUiEvent
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun GoogleOneTap(
    onEvent: (LoginUiEvent) -> Unit
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val oneTapClient = Identity.getSignInClient(context)
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                // send login or sign up post request to flask api here, call loginViewModel
                onEvent(LoginUiEvent.OnOneTapSuccess(idToken!!))

                Log.d("result ok", "$idToken")

                val username = credential.id
                Log.d("result ok", username)

//                val password = credential.password
//                Log.d("result ok", "$password")
            } catch (e: ApiException) {
                Log.d("Catch",e.toString())
            }
        } else {
            Log.d("LoginResultHandler", "UI cancelled")
            onEvent(LoginUiEvent.OnOneTapFail)
            // call login ViewModel method
        }
    }
    val scope =  rememberCoroutineScope()

    OutlinedButton(
        onClick = {
            scope.launch {
                signIn(
                    context, launcher
                )
            }
        },
    ) {
        Image(
            modifier = Modifier.size(16.dp),
            painter = painterResource(id = R.drawable.ic_google_icon),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Login/ Sign up via Google",
            color = Color.Black
        )
    }
}

suspend fun signIn(
    context: Context,
    launcher: ActivityResultLauncher<IntentSenderRequest>
) {
    Log.d("Sign In", "sign In called")
    val oneTapClient = Identity.getSignInClient(context)
    val clientId: String = context.getString(R.string.google_client_id)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(clientId)
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        .build()
    try {
        val result = oneTapClient.beginSignIn(signInRequest).await()

        val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
        launcher.launch(intentSenderRequest)

    } catch (e: Exception) {
        Log.d("LOG", e.message.toString())
//        Log.d("LOG", e.message ?:"test")
//        Log.d("LOG", e.toString())
//        Log.d("LOG", "${e.message.toString().contains("16:")}")
        if (e.message.toString().contains("16:")) {
            signUp(context, launcher)
        }
    }
}

suspend fun signUp(
    context: Context,
    launcher: ActivityResultLauncher<IntentSenderRequest>
) {
    Log.d("Sign Up", "sign up called")
    val oneTapClient = Identity.getSignInClient(context)
    val clientId: String = context.getString(R.string.google_client_id)
    val signUpRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(clientId)
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()
    try {
        val result = oneTapClient.beginSignIn(signUpRequest).await()

        val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
        launcher.launch(intentSenderRequest)
    } catch (e: Exception) {
        Log.d("LOG", e.message.toString())
    }
}