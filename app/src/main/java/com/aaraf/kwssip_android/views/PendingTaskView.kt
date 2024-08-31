package com.aaraf.kwssip_android.views

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaraf.kwssip_android.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun PendingTaskView() {
    Scaffold(
        topBar = {
            TopAppBar(colors = topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.White,
            ), title = {
                Text(
                    text = "Pending Tasks",
                    color = Color(0xFF919191),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            })
        },

        ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(color = Color.White),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.White)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    repeat(8) {
                        PendingItem()
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

            }
        }
    }

}

@Composable
fun PendingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .border(
                width = 0.2.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = Color.White, shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,

            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f), // Takes remaining width

            ) {

                Text(
                    text = "Pending Tasks",
                    color = Color(0xFF777777),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = "QM Building, QM Building @ BC 15, Block 7",
                    color = Color(0xFF777777),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .fillMaxWidth(0.84f)
                )

            }

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(painter = painterResource(id = R.drawable.checkbox_circle_line),
                    contentDescription = "Placeholder",
                    tint = Color(0XFF7AA76F),
                    modifier = Modifier
                        .padding(6.dp)
                        .size(34.dp)
                        .clickable {
                            Log.d(TAG, "PendingItem: CHECK")
                        })

                Icon(painter = painterResource(id = R.drawable.direction_fill),
                    contentDescription = "Placeholder",
                    tint = Color.Gray,
                    modifier = Modifier
                        .padding(6.dp)
                        .size(34.dp)
                        .clickable {
                            Log.d(TAG, "PendingItem: Direction")
                        })

            }
        }
    }
}
