package com.aaraf.kwssip_android.views

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.net.Uri
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
    val rating = rememberSaveable { mutableIntStateOf(0) }
    val commentError = rememberSaveable { mutableStateOf<String?>(null) }
    val bottomSheetState = rememberModalBottomSheetState(false)
    val showAlertDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope() // For starting the activity

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = bottomSheetState,
        modifier = Modifier.background(Color(0x884B4B4B)),
        containerColor = Color(0xFF3EB3E0),
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState(), reverseScrolling = true)
        ) {
//            Spacer(modifier = Modifier.height(16.dp)) // Reduced space at the top

            Text(
                text = "Give a Rating!",
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
                    ImageCard(
                        imageResId = R.drawable.img1 + index,
                        onClick = {
                            rating.intValue = index + 1
                            when (rating.intValue) {
                                1 -> {
                                    Toast.makeText(context, "Worst", Toast.LENGTH_SHORT).show()
                                }

                                2 -> {
                                    Toast.makeText(context, "Bad", Toast.LENGTH_SHORT).show()
                                }

                                3 -> {
                                    Toast.makeText(context, "Neutral", Toast.LENGTH_SHORT).show()
                                }

                                4 -> {
                                    Toast.makeText(context, "Good", Toast.LENGTH_SHORT).show()
                                }

                                5 -> {
                                    Toast.makeText(context, "Excellent", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    )
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp)
                    .height(160.dp),
            )

            Spacer(modifier = Modifier.height(24.dp)) // Adjusted spacing

            Button(
                onClick = {
                    val driverID = getSavedAppId(context)

                    showAlertDialog.value = uploadedImages()
                    coroutineScope.launch {
                        upload(name.value,
                            comment.value,
                            context,
                            phone.value,
                            rating = 5,
                            Integer.valueOf(driverID),
                            imageUris = imageUris,
                            onFailure = {
                                showAlertDialog.value = false
                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            },
                            onSuccess = {
                                showAlertDialog.value = true
                            })


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
            if (showAlertDialog.value) {
                AlertDialog(onDismissRequest = {
                    showAlertDialog.value = false
                    onDismiss()
                },
                    title = { Text("Uploaded Successfully") },

                    text = { Text("Thanks For Uploading Images") },
                    confirmButton = {
                        Button(
                            onClick = {

                                Log.d(
                                    TAG,
                                    "RatingBottomSheet: name: $name phone: $phone connect: $comment"
                                )

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

suspend fun upload(
    customerName: String,
    customerFeedback: String,
    context: Context,
    customerContact: String,
    rating: Int,
    driverId: Int,
    onSuccess: (String) -> Unit,
    onFailure: (String) -> Unit,
    imageUris: List<Uri?>
): Boolean {
    return suspendCancellableCoroutine { continuation ->


        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false) // Optional: Prevent the user from canceling the dialog
        progressDialog.show()


        val customerNameM = createFormData("CustomerName", customerName)
        val customerContactM = createFormData("CustomerContact", customerContact)
        val customerFeedbackM = createFormData("CustomerFeedback", customerFeedback)
        val ratingM = createFormData("Rating", rating.toString())
        val driverIdM = createFormData("Driver_id", driverId.toString())


        val parts = imageUris.mapIndexed { index, uri ->
            uri?.let {
                val requestFile: RequestBody =
                    RequestBody.create("*/*".toMediaTypeOrNull(), File(it.path!!))

                createFormData(
                    "img${index + 1}", it.lastPathSegment ?: "image${index + 1}", requestFile
                )
            }
        }.take(5)

        val img1 = parts.getOrNull(0)
        val img2 = parts.getOrNull(1)
        val img3 = parts.getOrNull(2)
        val img4 = parts.getOrNull(3)
        val img5 = parts.getOrNull(4)

        ServiceBuilder.buildService(RetrofitInterface::class.java).sendFeedback(
            Driver_id = driverIdM,
            CustomerName = customerNameM,
            CustomerFeedback = customerFeedbackM,
            CustomerContact = customerContactM,
            Rating = ratingM,
            img1 = img1,
            img2 = img2,
            img3 = img3,
            img4 = img4,
            img5 = img5
        ).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>, response: Response<LoginResponse>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful && response.body()!!.Success) {
                    Log.d(TAG, "onResponse: ${response.body()?.message}")
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                    onSuccess(response.body()?.message!!)
                    continuation.resume(true)
                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                    onFailure(response.body()?.message!!)
                    Log.d(TAG, "onResponse: ${response.body()?.message}")
                    continuation.resume(false)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                progressDialog.dismiss()
                onFailure(t.message!!)
                Log.d(TAG, "onFailure: ${t.message}")
                continuation.resumeWithException(t)
            }
        })
    }
}



