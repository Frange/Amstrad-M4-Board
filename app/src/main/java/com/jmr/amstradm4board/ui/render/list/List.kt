package com.jmr.amstradm4board.ui.render.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.asFlow
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.jmr.amstradm4board.domain.model.DataFileType
import com.jmr.amstradm4board.ui.AmstradViewModel
import com.jmr.amstradm4board.ui.Utils.customFontFamily
import com.jmr.amstradm4board.ui.render.component.StyledRetroButton
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.brightYellowScreen
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.noListButtonText
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.noListMessageText

@Composable
fun RenderListWithErrorState(
    viewModel: AmstradViewModel,
    onRetryClick: () -> Unit
) {
    when (viewModel.listState) {
        is AmstradViewModel.ListState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp),
                    color = brightYellowScreen
                )
            }
        }

        is AmstradViewModel.ListState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .width(320.dp),
                    text = noListMessageText,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = customFontFamily,
                    color = brightYellowScreen,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                StyledRetroButton(
                    text = noListButtonText,
                    fontSize = 16.sp,
                    backgroundColor = Color.Gray,
                    shadowColor = Color.DarkGray,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(70.dp)
                        .width(300.dp),
                    onClick = { onRetryClick() }
                )
            }
        }

        is AmstradViewModel.ListState.Ready,
        is AmstradViewModel.ListState.Loaded -> {
            RenderList(viewModel)
        }
    }
}

@Composable
fun RenderList(viewModel: AmstradViewModel) {
    val files by viewModel.dataFileList.collectAsState(initial = emptyList()) // Cambiar a StateFlow
    val keyboardController = LocalSoftwareKeyboardController.current

    SwipeRefresh(
        modifier = Modifier
            .padding(0.dp, 8.dp, 0.dp, 0.dp),
        state = SwipeRefreshState(viewModel.isRefreshing),
        onRefresh = {
            keyboardController?.hide()

            viewModel.toggleRefreshing(true)
            viewModel.refreshFileList()
            viewModel.toggleRefreshing(false)
        }
    ) {
        LazyColumn {
            items(files) { file ->
                RenderDataFileItem(file) { clickedFile ->
                    val fullPath = clickedFile.path + "/" + clickedFile.name

                    when (clickedFile.type) {
                        DataFileType.DSK -> {
                            viewModel.openDSK(fullPath) { dskContentFiles ->
                                viewModel.selectedDskName = clickedFile.name
                                viewModel.dskFiles = dskContentFiles
                                viewModel.showDskDialog = true
                            }
                        }

                        DataFileType.FOLDER -> {
                            viewModel.navigate(fullPath)
                        }

                        else -> {
                            // Nothing to do
                        }
                    }
                }
            }
        }
    }
}
