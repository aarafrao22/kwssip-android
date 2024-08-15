package com.aaraf.kwssip_android.views

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.aaraf.kwssip_android.R
import com.github.dhaval2404.imagepicker.ImagePicker

@Composable
fun HomeView() {
    var isSheetPresented by remember { mutableStateOf(false) }
    var selectedImageCount by remember { mutableIntStateOf(0) }

    var imageUris by remember { mutableStateOf(List(5) { null as Uri? }) }

    val context = LocalContext.current as Activity

    val imagePickers = List(5) { index ->
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUris = imageUris.toMutableList().apply { this[index] = result.data?.data }
                selectedImageCount++
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Welcome,",
            color = Color(0xFF21637D),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Ahmed",
            color = Color(0xFF43A5E4),
            fontSize = 32.sp,
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
                                selectedImage = imageUris[index],
                                onClick = {
                                    ImagePicker.with(context)
                                        .galleryOnly()
                                        .crop()
                                        .createIntent { intent ->
                                            imagePickers[index].launch(intent)
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
                    index = (index + 1).toString(),
                    isSelected = index < selectedImageCount
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = colorResource(id = R.color.dark_blue)
            ),
            onClick = { isSheetPresented = true },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            enabled = selectedImageCount == 5
        ) {
            Text("Upload Images")
        }

        if (isSheetPresented) {
            RatingBottomSheet(onDismiss = { isSheetPresented = false })
        }

        Spacer(modifier = Modifier.weight(1f))
    }
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
                imageVector = Icons.Default.Face,
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

@Composable
fun ImageCard(imageResId: Int) {
    // You can replace this with your actual image loading logic
    Box {
        // Placeholder for image
//        Text(text = path, color = Color.White, modifier = Modifier.align(Alignment.Center))
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Splash Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(60.dp) // Adjust the height as needed
                .width(60.dp)  // Adjust the width as needed
                .clip(CircleShape)

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
                color = Color.DarkGray,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            ),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = keyboardOptions,
            singleLine = true,
            onValueChange = onValueChange,
            modifier = modifier,
            placeholder = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 4.dp) // Adjust the padding as needed
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

fun uploadImages(onSuccess: () -> Unit) {
    // Your image upload logic here
    onSuccess()
}

@Composable
@Preview(showBackground = true)
fun HomeViewPreview() {
    HomeView()
}
