package com.aaraf.kwssip_android.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.aaraf.kwssip_android.LoginActivity
import com.aaraf.kwssip_android.R
import com.aaraf.kwssip_android.Utils
import com.aaraf.kwssip_android.model.UpdateFCMResponse
import com.aaraf.kwssip_android.network.RetrofitInterface
import com.aaraf.kwssip_android.network.ServiceBuilder
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun HomeView(
    isBefore: Boolean = true,
    onSuccess: () -> Unit,
    onPostClick: () -> Unit,
) {
    var isSheetPresented by remember { mutableStateOf(false) }
    val showAlertDialog = remember { mutableStateOf(false) }
    var selectedImageCount by remember { mutableIntStateOf(0) }

    val beforeImageUris = remember { mutableStateListOf<Uri?>(null, null, null, null, null) }
    val afterImageUris = remember { mutableStateListOf<Uri?>(null, null, null, null, null) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current as Activity

    val imagePickers = List(5) { index ->
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val pickedUri = result.data?.data
                if (pickedUri != null) {
                    if (isBefore) {
                        beforeImageUris[index] = pickedUri
                    } else {
                        afterImageUris[index] = pickedUri
                    }
                    selectedImageCount++
                }
            }
        }
    }

    Scaffold { paddingValues: PaddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFFFFF)),
            ) {
                Spacer(modifier = Modifier.weight(0.2f))
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .align(Alignment.TopEnd)
                            .clip(CircleShape)
                            .background(colorResource(id = R.color.dark_blue))
                            .clickable(onClick = {
                                showAlertDialog.value = true
                            })
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Placeholder",
                            tint = Color.White,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        )
                    }
                }

                Text(
                    text = "Welcome, ${getDriverName(LocalContext.current)}",
                    color = Color(0xFF43A5E4),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = if (isBefore) "Capture Pre-Installation" else "Capture Post-Installation",
                    color = Color(0xFF21637D),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(2) { rowIndex ->
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            repeat(3) { colIndex ->
                                val index = rowIndex * 3 + colIndex
                                if (index < 5) {
                                    ImagePickerView(
                                        selectedImage = if (isBefore) beforeImageUris[index] else afterImageUris[index],
                                        onClick = {
                                            coroutineScope.launch {
                                                ImagePicker.with(context).cameraOnly()
                                                    .crop(512f, 512f)
                                                    .maxResultSize(512, 512)
                                                    .createIntent { intent ->
                                                        imagePickers[index].launch(intent)
                                                    }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    repeat(5) { index ->
                        IndicatorView(
                            index = (index + 1).toString(), isSelected = index < selectedImageCount
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (!isBefore) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = colorResource(id = R.color.dark_blue)
                        ),
                        onClick = { isSheetPresented = true },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        enabled = selectedImageCount == 2
                    ) {
                        Text("Upload Images")
                    }

                } else {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = colorResource(id = R.color.dark_blue)
                        ),
                        onClick = {
                            onPostClick()
                            Utils.BEFORE_LIST_URI = beforeImageUris
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        enabled = selectedImageCount == 2
                    ) {
                        Text("Capture Post Installation")
                    }
                }

                if (isSheetPresented) {
                    Log.d(TAG, "HomeView before: ${beforeImageUris.size}")
                    for (i in beforeImageUris) {
                        Log.d(TAG, "HomeView before: $i")
                    }

                    Log.d(TAG, "HomeView before: ${afterImageUris.size}")
                    for (i in afterImageUris) {
                        Log.d(TAG, "HomeView after: $i")
                    }
                    RatingBottomSheet(
                        onDismiss = {
                            isSheetPresented = false
                            selectedImageCount = 0
//                            beforeUriList.clear()
//                            beforeUriList.addAll(List(5) { null })
                        },
//                        beforeImageUris = beforeImageUris,
                        afterImageUris = afterImageUris,
                        onSuccess = {
                            onSuccess()
                            selectedImageCount = 0
                            beforeImageUris.clear()
                            beforeImageUris.addAll(List(5) { null })
                            isSheetPresented = false
                        })
                }

                Spacer(modifier = Modifier.weight(1f))

                if (showAlertDialog.value) {
                    AlertDialog(
                        onDismissRequest = {
                            showAlertDialog.value = false
                        },
                        title = { Text("Logout?") },
                        text = { Text("Are you sure, do you want to log out?") },
                        dismissButton = {
                            Button(
                                onClick = {
                                    showAlertDialog.value = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Gray
                                )
                            ) {
                                Text("Cancel")
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    ServiceBuilder.buildService(RetrofitInterface::class.java)
                                        .logout(getSavedAppId(context))
                                        .enqueue(object : Callback<UpdateFCMResponse> {
                                            override fun onResponse(
                                                call: Call<UpdateFCMResponse>,
                                                response: Response<UpdateFCMResponse>
                                            ) {
                                                if (response.body()?.Success == true) {
                                                    clearAppId(context)
                                                    context.startActivity(
                                                        Intent(context, LoginActivity::class.java)
                                                    )
                                                    context.finish()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        response.body()?.message
                                                            ?: "Logout failed!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }

                                            override fun onFailure(
                                                call: Call<UpdateFCMResponse>,
                                                t: Throwable
                                            ) {
                                                Toast.makeText(
                                                    context,
                                                    "Logout failed!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        })
                                    showAlertDialog.value = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.dark_blue)
                                )
                            ) {
                                Text("Logout")
                            }
                        }
                    )
                }
            }
        }
    }
}


fun onDismiss() {
    Log.d(TAG, "onDismiss: ")
}

fun clearAppId(context: Context) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MySharedPref", MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()

    if (sharedPreferences.contains("appId")) {
        editor.remove("appId").apply()
        Log.d(TAG, "clearAppId: appId has been cleared")
    } else {
        Log.d(TAG, "clearAppId: No appId to clear")
    }
}


fun getDriverName(context: Context): String {
    val sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE)
    val driverName = sharedPreferences.getString("driver_name", "").orEmpty()
    return driverName
}

@SuppressLint("MissingColorAlphaChannel")
@Composable
fun ImagePickerView(selectedImage: Uri?, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(94.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF0F0F0))
            .clickable(onClick = onClick)
    ) {
        if (selectedImage != null) {
            Image(
                painter = rememberAsyncImagePainter(model = selectedImage),
                contentDescription = "Selected Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.upload_cloud_2_line),
                contentDescription = "Placeholder",
                tint = Color.Gray,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun IndicatorView(index: String, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (isSelected) Color(0xFF21637D) else Color(0xFFF0F0F0)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = index,
            color = if (isSelected) Color.White else Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextFieldBottomSheet(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    errorMessage: String?,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier,
) {
    Column(modifier = modifier) {
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
                color = Color.DarkGray, fontSize = 18.sp, textAlign = TextAlign.Start
            ),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = keyboardOptions,
            singleLine = true,
            onValueChange = onValueChange,
            modifier = modifier,
            placeholder = {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = placeholder,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.TopStart)
                    )
                }
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

