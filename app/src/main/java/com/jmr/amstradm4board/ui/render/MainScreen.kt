package com.jmr.amstradm4board.ui.render

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jmr.amstradm4board.ui.AmstradViewModel
import com.jmr.amstradm4board.ui.Utils
import com.jmr.amstradm4board.ui.Utils.customFontFamily
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.blueKeyboard
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.brightYellowScreen
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.delButtonText
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.greenKeyboard
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.lightWhiteKeyboard
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.path
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.screenBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.titleText
import com.jmr.amstradm4board.ui.drawableList
import com.jmr.amstradm4board.ui.isFirstTime
import com.jmr.amstradm4board.ui.render.component.RenderConnectionRow
import com.jmr.amstradm4board.ui.render.component.RenderDelButton
import com.jmr.amstradm4board.ui.render.component.RenderList
import com.jmr.amstradm4board.ui.render.component.RenderResetButtonsRow


@Composable
fun RenderMainScreen() {
    val viewModel: AmstradViewModel = hiltViewModel()

    val showDskDialog by remember { mutableStateOf(viewModel.showDskDialog) }
    val selectedDskName by remember { mutableStateOf(viewModel.selectedDskName) }
    val ip by remember { mutableStateOf(viewModel.ipAddress) }

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

                RenderResetButtonsRow(viewModel)

                RenderConnectionRow(viewModel, ip)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    RenderList(viewModel, ip)
                }

                Spacer(modifier = Modifier.height(12.dp))

                RenderDelButton(viewModel, ip)
            }
        }
    )

    RenderDskDialog(
        showDskDialog,
        dskName = selectedDskName,
        files = viewModel.dskFiles,
        onDismiss = { viewModel.toggleDskDialog(false) },
        onFileClick = { file ->
            viewModel.runGame(ip, file.path + "/" + file.name)
            viewModel.toggleDskDialog(false)
        }
    )
}