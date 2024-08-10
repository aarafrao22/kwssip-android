package com.aaraf.kwssip_android.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
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
@Preview(showBackground = true)
fun HomeScreen() {
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            imageUri.value = uri
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Welcome,", style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = colorResource(id = R.color.dark_blue)
                )
            )
            Text(
                text = "Ahmed",
                fontSize = 32.sp,
                fontWeight = FontWeight.W800,
                color = colorResource(id = R.color.theme_blue)
            )
            Spacer(modifier = Modifier.height(height = 80.dp))

            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CardItem(context, launcher, imageUri)
                CardItem(context, launcher, imageUri)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CardItem(context, launcher, imageUri)
                CardItem(context, launcher, imageUri)
                CardItem(context, launcher, imageUri)
            }
            Spacer(modifier = Modifier.height(height = 80.dp))
            Button(
                onClick = { /*TODO*/ }, colors = ButtonColors(
                    containerColor = colorResource(id = R.color.theme_blue),
                    contentColor = Color.White,
                    disabledContentColor = colorResource(id = R.color.dark_grey),
                    disabledContainerColor = colorResource(id = R.color.dark_grey)
                )
            ) {
                Text(text = "Take Pictures")
            }
            Spacer(modifier = Modifier.height(height = 40.dp))
            NumberRow()
            Spacer(modifier = Modifier.height(height = 40.dp))
            Button(
                onClick = { /*TODO*/ }, colors = ButtonColors(
                    containerColor = colorResource(id = R.color.theme_blue),
                    contentColor = Color.White,
                    disabledContentColor = colorResource(id = R.color.dark_grey),
                    disabledContainerColor = colorResource(id = R.color.dark_grey)
                )
            ) {
                Text(text = "Upload")
            }
        }

    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun CardItem(
    context: Context,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    imageUri: MutableState<Uri?>
) {
    val activity = context as? Activity
        ?: // Log error or handle null activity case
        return

    Image(
        modifier = Modifier
            .background(
                color = colorResource(id = R.color.dark_grey),
                shape = RoundedCornerShape(12.dp)
            )
            .height(80.dp)
            .width(80.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = {
                    ImagePicker
                        .with(activity)
                        .galleryOnly()
                        .crop() // Optional crop option
                        .createIntent { intent ->
                            launcher.launch(intent)
                        }
                }
            ),
        painter = rememberAsyncImagePainter(model = imageUri.value),
        contentDescription = "Selected Image"
    )
}

@Composable
fun NumberRow() {
    val numbers = (1..5).map { it.toString() }

    LazyRow(
        modifier = Modifier.padding(16.dp)
    ) {
        items(numbers) { number ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(
                        color = colorResource(id = R.color.theme_blue),
                        shape = RoundedCornerShape(34.dp)
                    )
                    .height(40.dp)
                    .width(40.dp)
                    .padding(8.dp)
            ) {
                Text(
                    text = number,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp)) // Add margin between items
        }
    }
}

