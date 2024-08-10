package com.aaraf.kwssip_android.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaraf.kwssip_android.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingBottomSheet(onDismiss: () -> Unit) {
    val name by remember { mutableStateOf("") }
    val placeholder by remember { mutableStateOf("") }
    val comment = rememberSaveable { mutableStateOf("") }
    val commentError = rememberSaveable { mutableStateOf<String?>(null) }
    val nameError = rememberSaveable { mutableStateOf<String?>(null) }
    val bottomSheetState = rememberModalBottomSheetState(false)
    val showAlertDialog = remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = bottomSheetState,
        modifier = Modifier.background(Color(0x884B4B4B)),
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp))
                .background(Color(0xFF3EB3E0))
        ) {
            Spacer(modifier = Modifier.height(16.dp)) // Reduced space at the top

            Text(
                text = "Give Ahmed a Rating!",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 30.dp, bottom = 16.dp) // Adjust padding
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                repeat(5) { index ->
                    ImageCard(imageResId = R.drawable.img1 + index)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Leave a comment*",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 24.dp, bottom = 16.dp) // Adjust padding
            )

            Spacer(modifier = Modifier.height(16.dp))


//            TextField(
//                placeholder = { Text(text = "placeholder")},
//                value = name,
//                colors = TextFieldDefaults.textFieldColors(
//                    disabledTextColor = Color.Transparent,
//                    containerColor = Color.White,
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                    disabledIndicatorColor = Color.Transparent
//                ),
//                textStyle = TextStyle(
//                    color = Color.DarkGray,
//                    fontSize = 18.sp,
//                ),
//                shape = RoundedCornerShape(16.dp),
//                onValueChange = name,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//            )
            Column(modifier = Modifier.fillMaxWidth()) {

                CustomTextFieldBottomSheet(
                    value = comment.value,
                    onValueChange = { comment.value = it },
                    placeholder = "Name",
                    errorMessage = commentError.value,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                )
                CustomTextFieldBottomSheet(
                    value = comment.value,
                    onValueChange = { comment.value = it },
                    placeholder = "Phone",
                    errorMessage = commentError.value,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextFieldBottomSheet(
                value = comment.value,
                onValueChange = { comment.value = it },
                placeholder = "Write Your Feedback",
                errorMessage = commentError.value,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(220.dp),
            )

            Spacer(modifier = Modifier.height(24.dp)) // Adjusted spacing

            Button(
                onClick = {
                    // Simulate image upload and show the alert dialog
                    showAlertDialog.value = true
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = colorResource(id = R.color.theme_blue)
                ), modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                Text("Upload")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Show AlertDialog separately from the ModalBottomSheet
    if (showAlertDialog.value) {
        AlertDialog(onDismissRequest = {
            showAlertDialog.value = false
            onDismiss()
        },
            title = { Text("Uploaded Successfully") },
            text = { Text("Thanks For Uploading Images, Ahmed") },
            confirmButton = {
                Button(
                    onClick = {
                        showAlertDialog.value = false
                        onDismiss()
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.theme_blue)
                    )
                ) {
                    Text("OK")
                }
            })
    }
}

@Composable
fun textFieldNew() {

}

