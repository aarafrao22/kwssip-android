package com.aaraf.kwssip_android.views

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaraf.kwssip_android.LoginActivity
import com.aaraf.kwssip_android.R
import com.aaraf.kwssip_android.Utils.TASK_ID
import com.aaraf.kwssip_android.model.Complaint
import com.aaraf.kwssip_android.model.ComplaintsListModel
import com.aaraf.kwssip_android.model.UpdateFCMResponse
import com.aaraf.kwssip_android.network.RetrofitInterface
import com.aaraf.kwssip_android.network.ServiceBuilder
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//todo:  /*getSavedAppId(context)*/ as 54
@Composable
@Preview(showBackground = true)
fun PendingTaskView() {
    val itemListCompleted = remember { mutableStateListOf<Complaint>() }
    val itemListPending = remember { mutableStateListOf<Complaint>() }
    val showList = remember { mutableStateOf(true) }
    val isBefore = remember { mutableStateOf(true) }
    val taskId = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current


    if (taskId.value != "") {
        Log.d(TAG, "PendingTaskView: ${taskId.value}")
    }

    if (showList.value) {
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                try {
                    ServiceBuilder.buildService(RetrofitInterface::class.java)
                        .getComplaints(/*getSavedAppId(context)*/"54"
                        ).enqueue(object : Callback<ComplaintsListModel> {
                            override fun onResponse(
                                call: Call<ComplaintsListModel>,
                                response: Response<ComplaintsListModel>
                            ) {
                                val tasks = response.body()!!.complaints

                                itemListPending.clear()
                                itemListCompleted.clear()

                                for (i in tasks) {
                                    if (i.action == "Completed") {
                                        itemListCompleted.add(i)
                                    } else {
                                        itemListPending.add(i)
                                    }
                                }
//                            itemList.addAll(tasks)
                            }

                            override fun onFailure(call: Call<ComplaintsListModel>, t: Throwable) {
                                Log.d(TAG, "onFailure: ${t.message}")
                            }
                        })
                } catch (e: Exception) {
                    Log.e("PendingTaskView", "Error fetching tasks", e)
                }
            }
        }

        TaskListView(
            itemListPending = itemListPending,
            itemListCompleted = itemListCompleted,
            onClick = {
                taskId.value = it
                showList.value = false
                TASK_ID = it
            })

    } else {

        //pre installation
        HomeView(
            onSuccess = {
                showList.value = true
            },
            onPostClick = {
                isBefore.value = false
            })
    }

    if (!isBefore.value) {

        //post installation
        HomeView(
            onSuccess = {
                showList.value = true
            }, isBefore = false,
            onPostClick = {}
        )
    }


    BackHandler {
        if (!showList.value) {
            showList.value = true
        } else {
            val activity = context as Activity
            activity.finish()
        }
    }
}

@Composable
fun TaskListView(
    itemListPending: List<Complaint>, itemListCompleted: List<Complaint>, onClick: (String) -> Unit
) {
    val showAlertDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current


    Scaffold { paddingValues: PaddingValues ->

        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.TopEnd,
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
                    Spacer(modifier = Modifier.height(16.dp))
                    if (showAlertDialog.value) {
                        AlertDialog(onDismissRequest = {
                            showAlertDialog.value = false
                            onDismiss()
                        },
                            title = { Text("Logout?") },
                            text = { Text("Are you sure, do you wanna log out?") },
                            dismissButton = {
                                Button(
                                    onClick = {
                                        showAlertDialog.value = false
                                        onDismiss()
                                    }, colors = ButtonDefaults.buttonColors(
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
                                            .logout(
                                                getSavedAppId(context)
                                            ).enqueue(object : Callback<UpdateFCMResponse> {
                                                override fun onResponse(
                                                    call: Call<UpdateFCMResponse>,
                                                    response: Response<UpdateFCMResponse>
                                                ) {
                                                    if (response.body()!!.Success) {

                                                        clearAppId(context)

                                                        context.startActivity(
                                                            Intent(
                                                                context, LoginActivity::class.java
                                                            )
                                                        )
                                                        val activity = (context as? Activity)
                                                        activity?.finish()


                                                    } else Toast.makeText(
                                                        context,
                                                        response.body()!!.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }

                                                override fun onFailure(
                                                    call: Call<UpdateFCMResponse>, t: Throwable
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Logout failed!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }

                                            })


                                        showAlertDialog.value = false


                                    }, colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(id = R.color.dark_blue)
                                    )
                                ) {
                                    Text("Logout")
                                }
                            })
                    }

                    Text(
                        text = "Welcome, ${getDriverName(LocalContext.current)}",
                        color = Color(0xFF43A5E4),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                    Text(
                        text = "Pending Tasks",
                        color = Color(0xFF919191),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                }

                items(itemListPending) { item ->
                    PendingItem(item = item) { onClick(item.complaintID) }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Completed Tasks",
                        color = Color(0xFF919191),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(18.dp))


                }

                items(itemListCompleted) { item ->
                    PendingItem(item = item) { onClick(item.complaintID) }
                    Spacer(modifier = Modifier.height(8.dp))
                }

            }
        }

    }


}

@Composable
fun ItemList(items: List<Complaint>, onClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        if (items.isNotEmpty()) {
            items(items.size) { index ->
                PendingItem(item = items[index]) { onClick(items[index].complaintID) }
            }
        } else {
            item {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No Task Yet",
                        color = Color(0xFF777777),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun PendingItem(item: Complaint, onClick: () -> Unit) {
    val context = LocalContext.current
    val backgroundColor = Color.White

    Box(modifier = Modifier
        .fillMaxWidth()
        .border(width = 0.2.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp))
        .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
        .padding(16.dp)
        .clickable {
            if (item.action == "Completed") {
                Toast
                    .makeText(context, "Completed Task", Toast.LENGTH_SHORT)
                    .show()
            }
        }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = item.Veh_no,
                    color = Color(0xFF777777),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.location,
                    color = Color(0xFF777777),
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
                Text(
                    text = "Assigned on: ${item.date}",
                    color = Color(0xFF777777),
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                Text(
                    text = if (item.end_date != null) "Completed on: ${item.end_date}"
                    else "",
                    color = Color(0xFF777777),
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (item.action != "Completed") {
                    ActionIcon(
                        id = R.drawable.checkbox_circle_line,
                        contentDescription = "Checkbox",
                        tint = Color(0XFF7AA76F),
                        onClick = onClick
                    )

                    ActionIcon(
                        id = R.drawable.direction_fill,
                        contentDescription = "Directions",
                        tint = Color.Gray
                    ) {
                        openMapActivity(context, item.coordinates)
                    }
                }


            }
        }
    }
}

@Composable
fun ActionIcon(id: Int, contentDescription: String, tint: Color, onClick: () -> Unit = {}) {
    Icon(painter = painterResource(id = id),
        contentDescription = contentDescription,
        tint = tint,
        modifier = Modifier
            .padding(6.dp)
            .size(34.dp)
            .clickable { onClick() })
}


fun openMapActivity(context: android.content.Context, coordinates: String) {
    val intent = coordinates.takeIf { it.contains(",") }?.let {
        val (latitude, longitude) = it.split(",").map { coord -> coord.trim() }
        val uri = "geo:$latitude,$longitude?q=$latitude,$longitude"
        Intent(Intent.ACTION_VIEW, Uri.parse(uri)).apply {
            setPackage("com.google.android.apps.maps")
        }
    }
    if (intent != null) {
        context.startActivity(intent)
    } else {
        Log.d("openMapActivity", "Invalid or missing coordinates")
    }
}
