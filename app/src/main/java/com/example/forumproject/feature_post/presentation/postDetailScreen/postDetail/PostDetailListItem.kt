package com.example.forumproject.feature_post.presentation.postDetailScreen.postDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forumproject.feature_post.data.model.response.CommentResponse
import com.example.forumproject.feature_post.data.model.response.PostResponse

@Composable
fun PostBodyAndComments (
    item: Any?
) {
    if (item is CommentResponse){
        Column(
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.author
                )
                Text(
                    text = item.pub_date
                )
            }
            Divider(color = Color.White, thickness = 1.dp)
            Text(
                text = item.body
            )
        }
    } else if (item is String) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = item
            )
        }
        Divider(color = Color.Black, thickness = 1.dp)
        Text(
            text = "Comments",
            fontSize = 24.sp
        )
    } else {
        Unit
    }
}