package com.aaraf.kwssip_android.views

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaraf.kwssip_android.HomeActivity
import com.aaraf.kwssip_android.R
import com.aaraf.kwssip_android.model.LoginResponse
import com.aaraf.kwssip_android.network.RetrofitInterface
import com.aaraf.kwssip_android.network.ServiceBuilder
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Preview(showBackground = true)
@Composable
fun LoginScreen() {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val emailError = rememberSaveable { mutableStateOf<String?>(null) }
    val passwordError = rememberSaveable { mutableStateOf<String?>(null) }
    val context = LocalContext.current // Get the current context
    val coroutineScope = rememberCoroutineScope() // For starting the activity

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

            CustomTextField(
                value = email.value,
                onValueChange = { email.value = it },
                placeholder = "Enter Email",
                errorMessage = emailError.value
            )

            CustomTextField(
                value = password.value,
                onValueChange = { password.value = it },
                placeholder = "Enter Password",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                errorMessage = passwordError.value
            )

            Spacer(modifier = Modifier.height(26.dp))

            Button(
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.theme_blue),
                    contentColor = Color.White
                ),
                onClick = {
                    if (validateInputs(
                            email.value,
                            password.value,
                            emailError,
                            passwordError
                        )
                    ) coroutineScope.launch {
                        callApi(email.value, password.value, context)
                    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    errorMessage: String?,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column {
        TextField(
            value = value,
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
            keyboardOptions = keyboardOptions,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            placeholder = {
                Text(text = placeholder, color = Color.Gray)
            },
            isError = errorMessage != null
        )
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

fun validateInputs(
    email: String,
    password: String,
    emailError: MutableState<String?>,
    passwordError: MutableState<String?>
): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    emailError.value = null
    passwordError.value = null

    when {
        email.trim().isEmpty() || password.trim().isEmpty() -> {
            if (email.trim().isEmpty()) emailError.value = "Enter Email"
            if (password.trim().isEmpty()) passwordError.value = "Enter Password"
            Log.d("TAG", "Enter Credentials First")
            return false
        }

        !email.matches(emailPattern) -> {
            emailError.value = "Invalid Email Address"
            Log.d("TAG", "Invalid Email Address")
            return false

        }

        else -> {
            Log.d("TAG", "validated Inputs xD xD")
            return true

        }
    }
}

suspend fun callApi(email: String, password: String, context: Context): Boolean {
    return suspendCancellableCoroutine { continuation ->
        ServiceBuilder.buildService(RetrofitInterface::class.java)
            .login(email, password, "1", "dmfklerfrjkhrjkth")
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>, response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful && response.body()?.Success == true) {
                        Log.d(TAG, "onResponse: ${response.body()?.message}")
                        context.startActivity(Intent(context, HomeActivity::class.java))
                        (context as? Activity)?.finish() // Finish the LoginActivity
                        continuation.resume(true)
                    } else {
                        continuation.resume(false)
                        Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    continuation.resumeWithException(t)
                }
            })
    }
}
