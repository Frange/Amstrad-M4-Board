package com.jmr.amstradm4board.ui.render

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jmr.amstradm4board.ui.AmstradViewModel
import com.jmr.amstradm4board.ui.Utils
import com.jmr.amstradm4board.ui.Utils.customFontFamily
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.screenBackground
import com.jmr.amstradm4board.ui.drawableList
import com.jmr.amstradm4board.ui.isFirstTime
import com.jmr.amstradm4board.ui.render.component.RenderConnectionRow
import com.jmr.amstradm4board.ui.render.component.RenderBackButton
import com.jmr.amstradm4board.ui.render.list.RenderList
import com.jmr.amstradm4board.ui.render.component.RenderResetButtonsRow
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.brightYellowScreen
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.titleText
import com.jmr.amstradm4board.ui.render.dialog.RenderDskDialog


@Composable
fun RenderMainScreen() {
    val viewModel: AmstradViewModel = hiltViewModel()
    val path by remember { mutableStateOf(viewModel.lastPath) }

    if (isFirstTime) {
        isFirstTime = false
        drawableList = Utils.getDrawableResourceNames()
        viewModel.navigate(path)
    }

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(screenBackground)
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RenderHeaderRow()

                RenderResetButtonsRow(viewModel)

                RenderConnectionRow(viewModel)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    RenderList(viewModel)
                }

                Spacer(modifier = Modifier.height(8.dp))

                RenderBackButton(viewModel)
            }
        }
    )

    RenderDskDialog(
        viewModel,
        onDismiss = { viewModel.toggleDskDialog(false) },
        onFileClick = { file ->
            viewModel.runGame(file.path + "/" + file.name)
        }
    )
}

@Composable
fun RenderHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titleText,
            color = brightYellowScreen,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontFamily = customFontFamily,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
