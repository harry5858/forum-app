package com.example.forumproject.feature_post.presentation.postListScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forumproject.feature_post.data.model.response.PostListResponse

@Composable
fun PostItem(
    post: PostListResponse,
    onEvent: (PostListUiEvent) -> Unit
){
    Box(
        modifier = Modifier.clip(CircleShape)
    ) {
        Column(
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth()
                .padding(24.dp)
                .clickable(
                    onClick = { onEvent(PostListUiEvent.OnPostClick(post)) }
                ),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = post.author
                )
                Spacer(modifier = Modifier.width(48.dp))
                Text(
                    text = post.tag
                )
            }
            Divider(color = Color.White, thickness = 1.dp)
            Text(
                text = post.title,
                fontSize = 24.sp
            )
        }
    }
}