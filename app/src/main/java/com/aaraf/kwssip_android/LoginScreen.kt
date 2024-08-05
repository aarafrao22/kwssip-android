package com.aaraf.kwssip_android

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun LoginScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0)),
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
                painter = painterResource(id = R.drawable.blue_logo),
                contentDescription = "Splash Background",
                modifier = Modifier
                    .height(120.dp) // Adjust the height as needed
                    .width(120.dp)  // Adjust the width as needed
            )
            Spacer(modifier = Modifier.height(8.dp)) // Space between image and text
            Text(
                text = "KWSSIP",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = colorResource(id = R.color.theme_blue),
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Enter Credentials\nto Continue",
                fontSize = 16.sp,
                color = colorResource(id = R.color.theme_blue),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))

            val email = remember { mutableStateOf("") }
            val password = remember { mutableStateOf("") }

            TextField(
                value = email.value,
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    color = Color.DarkGray,
                ),
                shape = RoundedCornerShape(32.dp),
                onValueChange = { email.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = {
                    Text(text = "Enter Email", color = Color.Gray)
                },
            )

            TextField(
                value = password.value,
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    color = Color.DarkGray,
                ),
                shape = RoundedCornerShape(32.dp),
                onValueChange = { password.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = {
                    Text(text = "Enter Password", color = Color.Gray)
                },
            )

            Spacer(modifier = Modifier.height(26.dp))

            Button(
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.theme_blue),
                    contentColor = Color.White
                ),
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(height = 50.dp)
                    .fillMaxWidth(),

                ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
