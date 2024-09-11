package com.aaraf.kwssip_android.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaraf.kwssip_android.R

@Preview(showBackground = true)
@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.image_bg_splash),
                contentScale = ContentScale.FillWidth
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(30.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Image(
                painter = painterResource(id = R.drawable.white),
                contentDescription = "Splash Background",
                modifier = Modifier
                    .height(120.dp) // Adjust the height as needed
                    .width(120.dp)  // Adjust the width as needed
            )
            Spacer(modifier = Modifier.height(8.dp)) // Space between image and text
            Text(
                text = "KWSC",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Karachi Water and Sewerage Services \nImprovement Project",
                fontSize = 9.sp,
                lineHeight = 10.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter) // Aligns this column to the bottom center
                .padding(bottom = 60.dp)
        ) {
            Text(
                text = "POWERED BY",
                fontSize = 11.sp,
                color = Color.White
            )
            Text(
                text = "iTecknologi",
                fontSize = 21.sp,
                color = Color.White
            )
        }
    }
}