package com.jmr.amstradm4board.ui.render.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.asFlow
import androidx.compose.foundation.lazy.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.jmr.amstradm4board.domain.model.DataFileType
import com.jmr.amstradm4board.ui.AmstradViewModel
import com.jmr.amstradm4board.ui.render.RenderDataFileItem

@Composable
fun RenderList(
    viewModel: AmstradViewModel,
    ip: String
) {
    val files by viewModel.dataFileList.asFlow().collectAsState(initial = emptyList())

    SwipeRefresh(
        state = SwipeRefreshState(viewModel.isRefreshing),
        onRefresh = {
            viewModel.toggleRefreshing(true)
            viewModel.refreshFileList(ip)
            viewModel.toggleRefreshing(false)
        }
    ) {
        LazyColumn {
            items(files) { file ->
                RenderDataFileItem(file) { clickedFile ->
                    val fullPath = clickedFile.path + "/" + clickedFile.name

                    when (clickedFile.type) {
                        DataFileType.DSK -> {
                            viewModel.openDSK(ip, fullPath) { dskContentFiles ->
                                viewModel.selectedDskName = clickedFile.name
                                viewModel.dskFiles = dskContentFiles
                                viewModel.showDskDialog = true
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