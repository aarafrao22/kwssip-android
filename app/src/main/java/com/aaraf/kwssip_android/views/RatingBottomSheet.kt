@file:Suppress("DEPRECATION")

package com.aaraf.kwssip_android.views

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaraf.kwssip_android.R
import com.aaraf.kwssip_android.Utils
import com.aaraf.kwssip_android.Utils.TASK_ID
import com.aaraf.kwssip_android.model.FeedbackResponse
import com.aaraf.kwssip_android.network.RetrofitInterface
import com.aaraf.kwssip_android.network.ServiceBuilder
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingBottomSheet(
    onDismiss: () -> Unit,
    afterImageUris: List<Uri?>,
    onSuccess: () -> Unit
) {
    val name = rememberSaveable { mutableStateOf("") }

    val beforeImageUris = Utils.BEFORE_LIST_URI

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
        modifier = Modifier
            .background(Color(0x884B4B4B))
            .imePadding(),
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

            EmotionRatingBar(
                emotions = listOf(
                    R.drawable.img1,
                    R.drawable.img2,
                    R.drawable.img3,
                    R.drawable.img4,
                    R.drawable.img5
                )
            ) { selectedRating ->
                rating.intValue = selectedRating + 1
                // Handle the selected rating here
                println("Selected rating: $selectedRating")
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
                    .height(100.dp),
            )

            Spacer(modifier = Modifier.height(24.dp)) // Adjusted spacing

            Button(
                onClick = {
                    val driverID = getSavedAppId(context)

                    showAlertDialog.value = false

                    coroutineScope.launch {

                        upload(name.value,
                            customerFeedback = comment.value,
                            context = context,
                            customerContact = phone.value,
                            rating = rating.intValue,
                            driverId = Integer.valueOf(driverID),
                            beforeImageUris = beforeImageUris,
                            afterImageUris = afterImageUris,
                            onFailure = {
                                showAlertDialog.value = false
                                Log.d(TAG, "RatingBottomSheet: ${beforeImageUris.size}")
                                Log.d(TAG, "RatingBottomSheet: ${afterImageUris.size}")
                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            },
                            onSuccess = {
                                showAlertDialog.value = true
                                Log.d(TAG, "RatingBottomSheet: $beforeImageUris")

                                Log.d(TAG, "RatingBottomSheet: $afterImageUris")
                                onSuccess()
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

                                ///HERE
                                phone.value = ""
                                name.value = ""
                                rating.intValue = 0
                                comment.value = ""

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

@Composable
fun EmotionRatingBar(
    emotions: List<Int>,
    onRatingSelected: (Int) -> Unit
) {
    var selectedRating by remember { mutableIntStateOf(-1) }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        emotions.forEachIndexed { index, emotionResId ->
            Image(
                painter = painterResource(id = emotionResId),
                contentDescription = "Emotion $index",
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(if (selectedRating == index) colorResource(id = R.color.dark_blue) else Color.Transparent) // Highlight selected icon
                    .clickable {
                        selectedRating = index
                        onRatingSelected(index)
                    },
                colorFilter = if (selectedRating == index) ColorFilter.tint(colorResource(id = R.color.white)) else null // Change color on selection
            )
        }
    }
}

fun getSavedAppId(context: Context): String {
    val sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE)
    val appId = sharedPreferences.getString("appId", "").orEmpty()

    Log.d(TAG, if (appId.isNotEmpty()) "App ID retrieved: $appId" else "No App ID found")
    return appId
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
    beforeImageUris: List<Uri?>,
    afterImageUris: List<Uri?>,
): Boolean {
    return suspendCancellableCoroutine { continuation ->

        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val taskId = TASK_ID ?: ""

        val customerNameM = createFormData("CustomerName", customerName)
        val customerContactM = createFormData("CustomerContact", customerContact)
        val customerFeedbackM = createFormData("CustomerFeedback", customerFeedback)
        val ratingM = createFormData("Rating", rating.toString())
        val complaintIdM = createFormData("complaintId", taskId)
        val driverIdM = createFormData("Driver_id", driverId.toString())

        fun createPartFromUri(name: String, uri: Uri?): MultipartBody.Part? {
            return uri?.let {
                val file = File(it.path!!)
                val requestFile = file.asRequestBody("*/*".toMediaTypeOrNull())
                createFormData(name, it.lastPathSegment ?: "image", requestFile)
            }
        }

        val beforeParts = beforeImageUris.mapIndexed { index, uri ->
            createPartFromUri("before_img${index + 1}", uri)
        }

        val afterParts = afterImageUris.mapIndexed { index, uri ->
            createPartFromUri("after_img${index + 1}", uri)
        }

        ServiceBuilder.buildService(RetrofitInterface::class.java).sendFeedback(
            Driver_id = driverIdM,
            CustomerName = customerNameM,
            CustomerFeedback = customerFeedbackM,
            CustomerContact = customerContactM,
            Rating = ratingM,
            before_img1 = beforeParts.getOrNull(0),
            before_img2 = beforeParts.getOrNull(1),
            before_img3 = beforeParts.getOrNull(2),
            before_img4 = beforeParts.getOrNull(3),
            before_img5 = beforeParts.getOrNull(4),
            after_img1 = afterParts.getOrNull(0),
            after_img2 = afterParts.getOrNull(1),
            after_img3 = afterParts.getOrNull(2),
            after_img4 = afterParts.getOrNull(3),
            after_img5 = afterParts.getOrNull(4),
            complaintId = complaintIdM
        ).enqueue(object : Callback<FeedbackResponse> {
            override fun onResponse(
                call: Call<FeedbackResponse>,
                response: Response<FeedbackResponse>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful && response.body()?.Success == true) {
                    response.body()?.message?.let {
                        Log.d(TAG, "onResponse: $it")
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        onSuccess(it)
                        continuation.resume(true)
                    }
                } else {
                    response.body()?.message?.let {
                        Log.d(TAG, "onResponse: $it")
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        onFailure(it)
                        continuation.resume(false)
                    }
                }
            }

            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                progressDialog.dismiss()
                Log.d(TAG, "onFailure: ${t.message}")
                onFailure(t.message ?: "Unknown error")
                continuation.resumeWithException(t)
            }
        })
    }
}




