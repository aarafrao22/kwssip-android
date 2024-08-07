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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.aaraf.kwssip_android.R
import com.github.dhaval2404.imagepicker.ImagePicker

@Composable
fun HomeViewIos() {
    var isSheetPresented by remember { mutableStateOf(false) }
    var imageIndex by remember { mutableIntStateOf(0) }

    var image1 by remember { mutableStateOf<Uri?>(null) }
    var image2 by remember { mutableStateOf<Uri?>(null) }
    var image3 by remember { mutableStateOf<Uri?>(null) }
    var image4 by remember { mutableStateOf<Uri?>(null) }
    var image5 by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current as Activity

    val imagePickerLauncher1 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            image1 = result.data?.data
            imageIndex++
        }
    }

    val imagePickerLauncher2 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            image2 = result.data?.data
            imageIndex++
        }
    }

    val imagePickerLauncher3 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            image3 = result.data?.data
            imageIndex++
        }
    }

    val imagePickerLauncher4 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            image4 = result.data?.data
            imageIndex++
        }
    }

    val imagePickerLauncher5 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            image5 = result.data?.data
            imageIndex++
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Welcome,",
            color = Color(0xFF21637D),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Ahmed",
            color = Color(0xFF43A5E4),
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ImageCardView(selectedImage = image1, onClick = {
                    ImagePicker.with(context).galleryOnly().crop().createIntent { intent ->
                        imagePickerLauncher1.launch(intent)
                    }
                })
                ImageCardView(selectedImage = image2, onClick = {
                    ImagePicker.with(context).galleryOnly().crop().createIntent { intent ->
                        imagePickerLauncher2.launch(intent)
                    }
                })
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ImageCardView(selectedImage = image3, onClick = {
                    ImagePicker.with(context).galleryOnly().crop().createIntent { intent ->
                        imagePickerLauncher3.launch(intent)
                    }
                })
                ImageCardView(selectedImage = image4, onClick = {
                    ImagePicker.with(context).galleryOnly().crop().createIntent { intent ->
                        imagePickerLauncher4.launch(intent)
                    }
                })
                ImageCardView(selectedImage = image5, onClick = {
                    ImagePicker.with(context).galleryOnly().crop().createIntent { intent ->
                        imagePickerLauncher5.launch(intent)
                    }
                })
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            repeat(5) { index ->
                CardViewInd(index = (index + 1).toString(), isTrue = index < imageIndex)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { isSheetPresented = true },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
            /*.background(color = colorResource(id = R.color.theme_blue))*/,
            enabled = imageIndex == 5
        ) {
            Text("Upload Images")
        }

        if (isSheetPresented) {
            BottomSheetView(onDismiss = { isSheetPresented = false })
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@SuppressLint("MissingColorAlphaChannel")
@Composable
fun ImageCardView(selectedImage: Uri?, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(94.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF0F0F0))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        if (selectedImage != null) {
            Image(
                painter = rememberAsyncImagePainter(model = selectedImage),
                contentDescription = "Selected Image",
                modifier = Modifier.fillMaxSize()
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
fun CardViewInd(index: String, isTrue: Boolean) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (isTrue) Color(0xFF21637D) else Color(0xFFF0F0F0)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = index,
            color = if (isTrue) Color.White else Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun BottomSheetView(onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var isAlertShowing by remember { mutableStateOf(false) }
    val comment = rememberSaveable { mutableStateOf("") }
    val commentError = rememberSaveable { mutableStateOf<String?>(null) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12, 12, 0, 0))
            .background(Color(0xFF3EB3E0))
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Give Ahmed a Rating!",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            ImageCard(R.drawable.img1)
            ImageCard(R.drawable.img2)
            ImageCard(R.drawable.img3)
            ImageCard(R.drawable.img4)
            ImageCard(R.drawable.img5)

        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Leave a comment*",
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.weight(1f))

        CustomTextFieldBottomSheet(
            value = comment.value,
            onValueChange = { comment.value = it },
            placeholder = "Enter Email",
            errorMessage = commentError.value
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { uploadImages { isAlertShowing = true } },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.white),
                contentColor = colorResource(id = R.color.theme_blue)
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Upload")
        }

        Spacer(modifier = Modifier.weight(1f))
    }

    if (isAlertShowing) {
        AlertDialog(onDismissRequest = { isAlertShowing = false },
            title = { Text("Uploaded Successfully") },
            text = { Text("Thanks For Uploading Images\nAhmed") },
            confirmButton = {
                Button(
                    onClick = {
                        isAlertShowing = false
                        onDismiss()
                    },
                    modifier = Modifier.background(color = colorResource(id = R.color.theme_blue))
                ) {
                    Text("OK")
                }
            })
    }
}

@Composable
fun ImageCard(path: Int) {
    // You can replace this with your actual image loading logic
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(Color.Gray)
            .clip(RoundedCornerShape(30.dp))
    ) {
        // Placeholder for image
//        Text(text = path, color = Color.White, modifier = Modifier.align(Alignment.Center))
        Image(
            painter = painterResource(id = path),
            contentDescription = "Splash Background",
            modifier = Modifier
                .height(60.dp) // Adjust the height as needed
                .width(60.dp)  // Adjust the width as needed
                .clip(RoundedCornerShape(300.dp))
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
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            ),
            shape = RoundedCornerShape(32.dp),
            keyboardOptions = keyboardOptions,
            singleLine = true,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(16.dp),
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

fun uploadImages(onSuccess: () -> Unit) {
    // Your image upload logic here
    onSuccess()
}

@Composable
@Preview(showBackground = true)
fun HomeViewPreview() {
    HomeViewIos()
}
