package com.aaraf.kwssip_android.views

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaraf.kwssip_android.R
import com.aaraf.kwssip_android.Utils.TASK_ID
import com.aaraf.kwssip_android.model.Complaint
import com.aaraf.kwssip_android.model.ComplaintsListModel
import com.aaraf.kwssip_android.network.RetrofitInterface
import com.aaraf.kwssip_android.network.ServiceBuilder
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
@Preview(showBackground = true)
fun PendingTaskView() {
//    val itemList = remember { mutableStateListOf<Complaint>() }
    val itemListCompleted = remember { mutableStateListOf<Complaint>() }
    val itemListPending = remember { mutableStateListOf<Complaint>() }
    val showList = remember { mutableStateOf(true) }
    val taskId = remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                ServiceBuilder.buildService(RetrofitInterface::class.java)
                    .getComplaints(/*getSavedAppId(context)*/"54"
                    ).enqueue(object : Callback<ComplaintsListModel> {
                        override fun onResponse(
                            call: Call<ComplaintsListModel>, response: Response<ComplaintsListModel>
                        ) {
                            val tasks = response.body()!!.complaints

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
    if (taskId.value != "") {
        Log.d(TAG, "PendingTaskView: ${taskId.value}")
    }

    if (showList.value) {
        TaskListView(
            itemListPending = itemListPending,
            itemListCompleted = itemListCompleted,
            onClick = {
                taskId.value = it
                showList.value = false
                TASK_ID = it
            })
    } else {
        HomeView(onSuccess = {
            showList.value = true
        })
    }
}

@Composable
fun TaskListView(
    itemListPending: List<Complaint>,
    itemListCompleted: List<Complaint>,
    onClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(68.dp))
            Text(
                text = "Welcome,",
                color = Color(0xFF21637D),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Text(
                text = getDriverName(LocalContext.current),
                color = Color(0xFF43A5E4),
                fontSize = 32.sp,
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

    Box(
        modifier = Modifier
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
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
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
    Icon(
        painter = painterResource(id = id),
        contentDescription = contentDescription,
        tint = tint,
        modifier = Modifier
            .padding(6.dp)
            .size(34.dp)
            .clickable { onClick() }
    )
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
