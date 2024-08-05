package com.aaraf.kwssip_android.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaraf.kwssip_android.R
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun LoginScreen() {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                onClick = {
                    validateInputs(email, password)
                },
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

fun validateInputs(email: MutableState<String>, password: MutableState<String>) {
    val emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    val error = null // Adjust error object according to your actual implementation

    if (email.value.trim().isNotEmpty() || password.value.trim().isNotEmpty()) {
        if (isEmailValid(email.value, emailPattern)) {
            // Valid email, proceed with contact validation
            if (password.value.length >= 11) {
                if (isEmailValid(email.value, emailPattern)) {
                    // "before login event"
                    loginEvent()
                    Log.d("TAG", "validated Inputs xD xD ")
                    disable()
                } else {
                    showInvalidEmailError()
                }
            } else {
                showInvalidContactError()
            }
        } else {
            showInvalidEmailError()
        }
    } else {
//        Toast.makeText(ctx, "Enter Credentials First", Toast.LENGTH_SHORT).show()
        Log.d("TAG", "Enter Credentials First")
        disable()
    }
}

fun isEmailValid(email: String, pattern: Pattern): Boolean {
    val result = email.trim().matches(pattern.toRegex())
    Log.d("TAG", "isEmailValid: $result")
    return result
}

fun showInvalidEmailError() {
//    edEmail.setError("Invalid Email Address", error)
    disable()
}

fun showInvalidContactError() {
//    edContact.setError("Invalid Contact: 11 digits Required", error)
    disable()
}

fun disable() {
    // Implement disable logic here
}

fun loginEvent() {
    // Implement login event logic here
}