package com.example.forumproject.navigation.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forumproject.navigation.mainScreen.MainScreenUiEvent
import com.example.forumproject.ui.theme.Purple500

@Composable
fun Drawer(
    loginState: Boolean,
    username: String,
    onEvent: (MainScreenUiEvent) -> Unit
) {

    Column(
        modifier = Modifier
            .background(Purple500)
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (loginState) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                    ,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text (
                        text = "Welcome",
                        fontSize = 24.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.size(width = 156.dp, height = 72.dp),
                            text = username,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            overflow = TextOverflow.Ellipsis
                        )
                        Button(
                            modifier = Modifier.size(width = 96.dp, height = 54.dp),
                            onClick = { onEvent(MainScreenUiEvent.OnLogoutClick) },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan)
                        ) {
                            Text(
                                text = "Logout",
                                color = Color.Black
                            )
                        }
                    }

                }
            } else {
                Text(
                    modifier = Modifier
                        .padding(20.dp)
                        .clickable {
                            onEvent(MainScreenUiEvent.OnLoginSignUpClick)
                        },
                    text = "Login/ Sign Up",
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        Divider(color = Color.Black, thickness = 1.dp)

        Text(
            modifier = Modifier
                .padding(20.dp)
                .clickable {
                    onEvent(MainScreenUiEvent.OnTagClick("General"))
                },
            text = "General",
            fontSize = 25.sp,
            color = Color.White
        )
        Text(
            modifier = Modifier
                .padding(20.dp)
                .clickable {
                    onEvent(MainScreenUiEvent.OnTagClick("Music"))
                },
            text = "Music",
            fontSize = 25.sp,
            color = Color.White
        )
        Text(
            modifier = Modifier
                .padding(20.dp)
                .clickable {
                    onEvent(MainScreenUiEvent.OnTagClick("Sport"))
                },
            text = "Sport",
            fontSize = 25.sp,
            color = Color.White
        )
        Text(
            modifier = Modifier
                .padding(20.dp)
                .clickable {
                    onEvent(MainScreenUiEvent.OnTagClick("Anime"))
                },
            text = "Anime",
            fontSize = 25.sp,
            color = Color.White
        )

    }
}