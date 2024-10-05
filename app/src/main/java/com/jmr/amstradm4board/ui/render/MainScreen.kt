package com.jmr.amstradm4board.ui.render

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.jmr.amstradm4board.domain.model.DataFile
import com.jmr.amstradm4board.domain.model.DataFileType
import com.jmr.amstradm4board.ui.AmstradViewModel
import com.jmr.amstradm4board.ui.Utils
import com.jmr.amstradm4board.ui.Utils.customFontFamily
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.blueKeyboard
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.brightYellowScreen
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.delButtonText
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.enterButtonHeight
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.enterButtonWidth
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.enterCPCText
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.greenKeyboard
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.lightWhiteKeyboard
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.path
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.resetButtonFontSize
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.resetCPCText
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.resetM4Text
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.screenBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.titleText
import com.jmr.amstradm4board.ui.drawableList
import com.jmr.amstradm4board.ui.isFirstTime
import com.jmr.amstradm4board.ui.render.component.RenderEnterButton
import com.jmr.amstradm4board.ui.render.component.RenderIpField


@Composable
fun RenderMainScreen() {
    val viewModel: AmstradViewModel = hiltViewModel()
    val ip by remember { mutableStateOf(viewModel.ipAddress) }
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
                    .background(screenBackground)
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { viewModel.resetCPC() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text(text = resetM4Text, color = Color.White)
                    }

                    Button(
                        onClick = { viewModel.resetM4() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Text(text = resetCPCText, color = Color.White)
                    }
                }

                RenderIpField(viewModel, ip)

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
fun TrapezoidButton(viewModel: AmstradViewModel, ip: String) {
    val trapezoidShape = GenericShape { size, _ ->
        moveTo(0f, 0f)
        lineTo(size.width, 0f)
        lineTo(size.width * 0.8f, size.height)
        lineTo(size.width * 0.2f, size.height)
        close()
    }

    Button(
        onClick = {
            viewModel.navigate(ip, path)
        },
        modifier = Modifier
            .width(enterButtonWidth)
            .height(enterButtonHeight)
            .clip(trapezoidShape),
        colors = ButtonDefaults.buttonColors(backgroundColor = blueKeyboard),
    ) {
        Text(
            text = enterCPCText,
            color = lightWhiteKeyboard,
            textAlign = TextAlign.Center,
            fontSize = resetButtonFontSize
        )
    }
}
