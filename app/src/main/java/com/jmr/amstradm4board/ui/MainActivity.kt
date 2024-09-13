package com.jmr.amstradm4board.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.jmr.amstradm4board.domain.model.DataFile
import com.jmr.amstradm4board.domain.model.DataFileType
import dagger.hilt.android.AndroidEntryPoint

import com.jmr.amstradm4board.ui.Utils.customFontFamily
import com.jmr.amstradm4board.ui.Utils.getDskBackground

// Paleta de colores basada en el Amstrad CPC
val black = Color(0xFF000000)

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

// Config screen
const val path = "games/aaa%20JM"

//TOP
const val titleText = "Amstrad M4 Board"
const val resetCPCText = "RESET CPC"
const val resetM4Text = "RESET M4"
const val ipLabelText = "IP Address:"
const val defaultIp = "192.168.1.39"
const val delButtonText = "DEL"

val ipTextFieldHeight = 56.dp
val ipFontSize = 12.sp

val resetButtonFontSize = 12.sp
val resetButtonWidth = 76.dp
val resetButtonHeight = 56.dp

val dskDialogBackground = blackKeyboard
val dskDialogCardBackground = black
const val dskDialogMaxWidth = 0.95f
const val dskDialogMaxHeight = 0.7f
const val dskDialogAlpha = 0.9f
const val dskDialogImageAlpha = 0.3f
val dskDialogImagePadding = PaddingValues(0.dp, 70.dp, 0.dp, 30.dp)
val dskDialogTitleBackground = blueKeyboard
val dskDialogTitleFontSize = 16.sp
val dskDialogTitleFontColor = brightYellowScreen
const val dskDialogTitleLength = 16

val dskItemBackground = blueKeyboard
const val dskItemAlpha = 0.8f
val dskItemPadding = 6.dp
val dskItemFontSize = 14.sp
val dskItemKSizeFontSize = 12.sp

var isFirstTime = true
var drawableList: List<String>? = null

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RenderMainScreen()
        }
    }
}

@Composable
fun RenderMainScreen() {
    val viewModel: AmstradViewModel = hiltViewModel()

    var ip by remember { mutableStateOf(defaultIp) }
    val files by viewModel.dataFileList.asFlow().collectAsState(initial = emptyList())
    var isRefreshing by remember { mutableStateOf(false) }

    var showDskDialog by remember { mutableStateOf(false) }
    var selectedDskName by remember { mutableStateOf("") }
    var dskFiles by remember { mutableStateOf<List<DataFile>>(emptyList()) }

    if (isFirstTime) {
        isFirstTime = false
        drawableList = Utils.getDrawableResourceNames()
        viewModel.navigate(ip, path)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = titleText,
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
                                text = ipLabelText,
                                color = pastelYellowScreen,
                                fontFamily = customFontFamily
                            )
                        },
                        onValueChange = { ip = it },
                        modifier = Modifier
                            .weight(1f)
                            .background(blackKeyboard, RoundedCornerShape(8.dp))
                            .heightIn(min = ipTextFieldHeight),
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
                            fontSize = ipFontSize
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
                            .width(resetButtonWidth)
                            .height(resetButtonHeight)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Text(
                            text = resetCPCText,
                            color = lightWhiteKeyboard,
                            textAlign = TextAlign.Center,
                            fontSize = resetButtonFontSize
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            viewModel.resetM4()
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = redKeyboard),
                        modifier = Modifier
                            .width(resetButtonWidth)
                            .height(resetButtonHeight)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Text(
                            text = resetM4Text,
                            color = lightWhiteKeyboard,
                            textAlign = TextAlign.Center,
                            fontSize = resetButtonFontSize
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
                                RenderDataFileItem(file) { clickedFile ->
                                    val fullPath = clickedFile.path + "/" + clickedFile.name

                                    when (clickedFile.type) {
                                        DataFileType.DSK -> {
                                            viewModel.openDSK(ip, fullPath) { dskContentFiles ->
                                                selectedDskName = clickedFile.name
                                                dskFiles = dskContentFiles
                                                showDskDialog = true
                                            }
                                        }

                                        DataFileType.GAME -> {
                                            viewModel.runGame(
                                                ip,
                                                fullPath
                                            )
                                        }

                                        DataFileType.FOLDER -> {
                                            viewModel.navigate(
                                                ip,
                                                fullPath
                                            )
                                        }

                                        DataFileType.OTHER -> {

                                        }
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
                        text = delButtonText,
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

    if (showDskDialog) {
        RenderDskDialog(
            dskName = selectedDskName,
            files = dskFiles,
            onDismiss = { showDskDialog = false },
            onFileClick = { file ->
                viewModel.runGame(ip, file.path + "/" + file.name)
                showDskDialog = false
            }
        )
    }
}

@Composable
fun RenderDataFileItem(file: DataFile, onClick: (DataFile) -> Unit) {
    val backgroundColor = when (file.type) {
        DataFileType.DSK -> isDskBackground
        DataFileType.GAME -> isGameBackground
        DataFileType.FOLDER -> otherFilesBackground
        DataFileType.OTHER -> otherFilesBackground
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
                text = if (file.type != DataFileType.FOLDER) file.name
                    .replace(".dsk", "")
                    .replace(".DSK", "")
                    .take(20)
                else file.name.uppercase().take(18),
                fontSize = 13.sp,
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

@Composable
fun RenderDskDialog(
    dskName: String,
    files: List<DataFile>,
    onDismiss: () -> Unit,
    onFileClick: (DataFile) -> Unit
) {
    val backgroundResId = getDskBackground(LocalContext.current, drawableList, dskName.lowercase())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                dskDialogBackground.copy(alpha = dskDialogAlpha)
            )
            .clickable(
                onClick = { onDismiss() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth(dskDialogMaxWidth)
                .fillMaxHeight(dskDialogMaxHeight)
                .align(Alignment.Center)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    /* Do nothing, prevent dismissal on clicking inside */
                },
            backgroundColor = dskDialogCardBackground,
            elevation = 16.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    alpha = dskDialogImageAlpha,
                    painter = painterResource(
                        id = backgroundResId,
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dskDialogImagePadding)
                )

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(dskDialogTitleBackground, RoundedCornerShape(8.dp)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp, 0.dp),
                            maxLines = 1,
                            text = dskName
                                .replace(".dsk", "")
                                .replace(".DSK", "")
                                .take(dskDialogTitleLength),
                            fontFamily = customFontFamily,
                            fontSize = dskDialogTitleFontSize,
                            color = dskDialogTitleFontColor
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            items(files) { file ->
                                RenderDskItem(file, onClick = { onFileClick(file) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RenderDskItem(file: DataFile, onClick: (DataFile) -> Unit) {
    Card(
        backgroundColor = dskItemBackground,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(dskItemAlpha)
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick(file) },
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dskItemPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = file.name.uppercase(),
                fontSize = dskItemFontSize,
                fontFamily = customFontFamily,
                style = MaterialTheme.typography.body1,
                color = brightYellowScreen,
                modifier = Modifier.weight(1f)
            )

            if (file.fileSize != "0") {
                Text(
                    text = file.fileSize,
                    fontFamily = customFontFamily,
                    style = MaterialTheme.typography.body2.copy(fontSize = dskItemKSizeFontSize),
                    color = lightWhiteKeyboard
                )
            }
        }
    }
}