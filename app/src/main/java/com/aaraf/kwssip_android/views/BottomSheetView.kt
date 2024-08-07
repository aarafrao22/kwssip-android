package com.aaraf.kwssip_android.views

//noinspection UsingMaterialAndMaterial3Libraries

//
//@Composable
//fun BottomSheetView2(onDismiss: () -> Unit) {
//    var name by remember { mutableStateOf("") }
//    var isAlertShowing by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(12.dp)
//            .background(Color(0xFF3EB3E0))
//    ) {
//        Spacer(modifier = Modifier.weight(1f))
//
//        Text(
//            text = "Give Ahmed a Rating!",
//            color = Color.White,
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.align(CenterHorizontally)
//        )
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
//            modifier = Modifier.align(CenterHorizontally)
//        ) {
//            ImageCard("img1")
//            ImageCard("img2")
//            ImageCard("img3")
//            ImageCard("img4")
//            ImageCard("img5")
//        }
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        Text(
//            text = "Leave a comment*",
//            color = Color.White,
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.align(CenterHorizontally)
//        )
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        BasicTextField(
//            value = name,
//            onValueChange = { name = it },
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.White, shape = RoundedCornerShape(22.dp))
//                .padding(16.dp),
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
//        )
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        Button(
//            onClick = { uploadImages { isAlertShowing = true } },
//            modifier = Modifier.align(CenterHorizontally)
//        ) {
//            Text("Upload")
//        }
//
//        Spacer(modifier = Modifier.weight(1f))
//    }
//
//    if (isAlertShowing) {
//        AlertDialog(
//            onDismissRequest = { isAlertShowing = false },
//            title = { Text("Uploaded Successfully") },
//            text = { Text("Thanks For Uploading Images\nAhmed") },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        isAlertShowing = false
//                        onDismiss()
//                    }
//                ) {
//                    Text("OK")
//                }
//            }
//        )
//    }
//}
//
//@Composable
//fun ImageCard(path: String) {
//    // You can replace this with your actual image loading logic
//    Box(
//        modifier = Modifier
//            .size(50.dp)
//            .background(Color.Gray)
//            .clip(RoundedCornerShape(30.dp))
//    ) {
//        // Placeholder for image
//        Text(text = path, color = Color.White, modifier = Modifier.align(Alignment.Center))
//    }
//}
//
//fun uploadImages(onSuccess: () -> Unit) {
//    // Your image upload logic here
//    onSuccess()
//}
