package com.aaraf.kwssip_android

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Style {

}

@Composable
fun LogoImage() {
    Image(
        painter = painterResource(id = R.drawable.blue_logo),
        contentDescription = "Splash Background",
        modifier = Modifier
            .height(120.dp) // Adjust the height as needed
            .width(120.dp)  // Adjust the width as needed
    )
}

@Composable
fun TitleText() {
    Text(
        text = "KWSSIP",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = colorResource(id = R.color.theme_blue),
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
fun SubTitleText() {
    Text(
        text = "Enter Credentials\nto Continue",
        fontSize = 16.sp,
        color = colorResource(id = R.color.theme_blue),
        textAlign = TextAlign.Center
    )
}

@Composable
fun LoginButton(onClick: () -> Unit) {
    Button(
        shape = RoundedCornerShape(32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.theme_blue),
            contentColor = Color.White
        ),
        onClick = onClick,
        modifier = Modifier
//            .align(Alignment.Center)
            .height(height = 50.dp)
            .fillMaxWidth(),
    ) {
        Text(text = "Login")
    }
}
