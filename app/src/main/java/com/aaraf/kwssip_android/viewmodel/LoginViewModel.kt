package com.aaraf.kwssip_android.viewmodel


import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aaraf.kwssip_android.HomeActivity

class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)

    fun validateInputs(): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        emailError = null
        passwordError = null

        return when {
            email.trim().isEmpty() || password.trim().isEmpty() -> {
                if (email.trim().isEmpty()) emailError = "Enter Email"
                if (password.trim().isEmpty()) passwordError = "Enter Password"
                false
            }

            !email.matches(emailPattern) -> {
                emailError = "Invalid Email Address"
                false
            }

            else -> true
        }
    }

    fun onLoginClick(context: Context) {
        if (validateInputs()) {
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
            (context as? Activity)?.finish()
        }
    }
}
