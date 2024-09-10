package com.jmr.amstradm4board.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.jmr.amstradm4board.R
import com.jmr.amstradm4board.domain.model.DataFile
import dagger.hilt.android.AndroidEntryPoint


// Paleta de colores basada en el Amstrad CPC
val redKeyboard = Color(0xFFDD2222)
val darkGrayKeyboard = Color(0xFF282828)
val blackKeyboard = Color(0xFF222222)
val lightWhiteKeyboard = Color(0xFFD3D3D3)
val greenKeyboard = Color(0xFF4CAF50)
val blueKeyboard = Color(0xFF2222CC)

val pastelYellowScreen = Color(0xFFFFFF80)
val brightYellowScreen = Color(0xFFFFFF00)

val isGameBackground = blueKeyboard
val isDskBackground = blueKeyboard
val otherFilesBackground = blackKeyboard

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XferApp()
        }
    }
}

@Composable
fun XferApp() {
    val viewModel: AmstradViewModel = hiltViewModel()

    var ip by remember { mutableStateOf("192.168.1.39") }
    val path = "games/aaa%20JM"
    val files by viewModel.dataFileList.asFlow().collectAsState(initial = emptyList())
    var isRefreshing by remember { mutableStateOf(false) }
    val textFieldHeight = 56.dp

    viewModel.navigate(ip, path)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Amstrad M4 Board",
                        fontFamily = customFontFamily,
                        color = brightYellowScreen,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )
                },
                backgroundColor = blueKeyboard,
                contentColor = lightWhiteKeyboard,
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(darkGrayKeyboard)
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = ip,
                        label = {
                            Text(
                                text = "IP Address:",
                                color = pastelYellowScreen,
                                fontFamily = customFontFamily
                            )
                        },
                        onValueChange = { ip = it },
                        modifier = Modifier
                            .weight(1f)
                            .background(blackKeyboard, RoundedCornerShape(8.dp))
                            .heightIn(min = textFieldHeight),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = blackKeyboard,
                            textColor = brightYellowScreen,
                            cursorColor = brightYellowScreen,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            fontFamily = customFontFamily,
                            color = brightYellowScreen,
                            fontSize = 12.sp
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            viewModel.resetCPC()
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = redKeyboard),
                        modifier = Modifier
                            .width(76.dp)
                            .height(56.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Text(
                            text = "RESET CPC",
                            color = lightWhiteKeyboard,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            viewModel.resetM4()
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = redKeyboard),
                        modifier = Modifier
                            .width(76.dp)
                            .height(56.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Text(
                            text = "RESET M4",
                            color = lightWhiteKeyboard,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                    }
                }


                Spacer(modifier = Modifier.height(2.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    SwipeRefresh(
                        state = SwipeRefreshState(isRefreshing),
                        onRefresh = {
                            isRefreshing = true
                            viewModel.refreshFileList(ip)
                            isRefreshing = false
                        }
                    ) {
                        LazyColumn {
                            items(files) { file ->
                                FileItem(file) { clickedFile ->
                                    val fileName = clickedFile.name.lowercase()
                                    if (fileName.lowercase()
                                            .contains(".dsk") || !clickedFile.isGame
                                    ) {
                                        viewModel.navigate(
                                            ip,
                                            clickedFile.path + "/" + clickedFile.name
                                        )
                                    } else {
                                        viewModel.runGame(
                                            ip,
                                            clickedFile.path + "/" + clickedFile.name
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.goBack(ip)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = greenKeyboard
                    ),
                    modifier = Modifier
                        .height(60.dp)
                        .width(160.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = "DEL",
                        color = lightWhiteKeyboard,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .graphicsLayer {
                                shadowElevation = 8f
                                shape = RoundedCornerShape(12.dp)
                                clip = true
                            }
                    )
                }
            }
        }
    )
}

@Composable
fun FileItem(file: DataFile, onClick: (DataFile) -> Unit) {
    val backgroundColor = when {
        file.isGame -> isGameBackground
        file.name.lowercase().endsWith(".dsk") -> isDskBackground
        else -> otherFilesBackground
    }

    val isFolder = when {
        file.isGame -> false
        file.name.lowercase().endsWith(".dsk") -> false
        else -> true
    }

    Card(
        backgroundColor = backgroundColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick(file) },
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (!isFolder) file.name else file.name.uppercase(),
                fontSize = 14.sp,
                fontFamily = customFontFamily,
                style = MaterialTheme.typography.body1,
                color = brightYellowScreen,
                modifier = Modifier.weight(1f)
            )

            if (file.fileSize != "0") {
                Text(
                    text = file.fileSize,
                    fontFamily = customFontFamily,
                    style = MaterialTheme.typography.body2.copy(fontSize = 12.sp),
                    color = lightWhiteKeyboard
                )
            }
        }
    }
}

val customFontFamily = FontFamily(
    Font(R.font.amstrad_cpc464, FontWeight.Normal)
)