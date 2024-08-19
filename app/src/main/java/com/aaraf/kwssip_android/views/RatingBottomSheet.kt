package com.aaraf.kwssip_android.views

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaraf.kwssip_android.R
import com.aaraf.kwssip_android.model.LoginResponse
import com.aaraf.kwssip_android.network.RetrofitInterface
import com.aaraf.kwssip_android.network.ServiceBuilder
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingBottomSheet(onDismiss: () -> Unit, imageUris: List<Uri?>) {

    val name = rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val comment = rememberSaveable { mutableStateOf("") }
    val phone = rememberSaveable { mutableStateOf("") }
    val commentError = rememberSaveable { mutableStateOf<String?>(null) }
    val bottomSheetState = rememberModalBottomSheetState(false)
    val showAlertDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope() // For starting the activity

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 16.dp)
            ) {
                // First CustomTextFieldBottomSheet for Name
                CustomTextFieldBottomSheet(
                    value = name.value,
                    onValueChange = { name.value = it },
                    placeholder = "Name",
                    errorMessage = commentError.value,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                // Second CustomTextFieldBottomSheet for Phone
                CustomTextFieldBottomSheet(
                    value = phone.value,
                    onValueChange = { phone.value = it },
                    placeholder = "Phone",
                    errorMessage = commentError.value,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                        .padding(start = 8.dp)
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
                    .padding(start = 8.dp, end = 8.dp)
                    .height(160.dp),
            )

            Spacer(modifier = Modifier.height(24.dp)) // Adjusted spacing

            Button(
                onClick = {
                    val driverID = getSavedAppId(context)
                    // Simulate image upload and show the alert dialog
                    showAlertDialog.value = uploadedImages()
                    coroutineScope.launch {
                        upload(
                            name.value,
                            comment.value,
                            context,
                            phone.value,
                            5,
                            Integer.valueOf(driverID),
                            imageUris
                        )
                    }

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

                        Log.d(TAG, "RatingBottomSheet: $name $phone $comment")

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

private fun getSavedAppId(context: Context): String {
    val sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE)
    val appId = sharedPreferences.getString("appId", "").orEmpty()

    Log.d(TAG, if (appId.isNotEmpty()) "App ID retrieved: $appId" else "No App ID found")
    return appId
}

fun uploadedImages(): Boolean {
    Log.d(TAG, "uploadImages: ")

    return true
}
fun getPath(uri: Uri?, context: Context): String? {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor: Cursor =
        uri?.let { context.contentResolver.query(it, projection, null, null, null) }
            ?: return null
    val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    val s = cursor.getString(column_index)
    cursor.close()
    return s
}
suspend fun upload(
    customerName: String,
    customerFeedback: String,
    context: Context,
    customerContact: String,
    rating: Int,
    driverId: Int,
    imageUris: List<Uri?>
): Boolean {
    return suspendCancellableCoroutine { continuation ->

        // Create MultipartBody.Part for each image URI, ensuring you handle up to 5 images
        val parts = imageUris.mapIndexed { index, uri ->
            uri?.let {
                val requestFile: RequestBody =
                    RequestBody.create("*/*".toMediaTypeOrNull(), File(it.path!!))

                createFormData(
                    "img${index + 1}",
                    it.lastPathSegment ?: "image${index + 1}",
                    requestFile
                )
            }
        }.take(5) // Limit to 5 images as per the method signature

        // Extract the parts or set to null if not available
        val img1 = parts.getOrNull(0)
        val img2 = parts.getOrNull(1)
        val img3 = parts.getOrNull(2)
        val img4 = parts.getOrNull(3)
        val img5 = parts.getOrNull(4)

        ServiceBuilder.buildService(RetrofitInterface::class.java)
            .sendFeedback(
                driverId,
                customerName,
                customerFeedback,
                customerContact,
                rating,
                img1,
                img2,
                img3,
                img4,
                img5
            )
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>, response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful && response.body()!!.Success) {
                        Log.d(TAG, "onResponse: ${response.body()?.message}")
                        continuation.resume(true)
                    } else {
                        continuation.resume(false)
                        Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "onResponse: ${response.body()?.message}")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    continuation.resumeWithException(t)
                }
            })
    }
}



