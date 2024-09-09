package com.jmr.amstradm4board.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import dagger.hilt.android.AndroidEntryPoint


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
    val files by viewModel.dataFileList.asFlow().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("XFER App") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
//                        viewModel.resetCPC()
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text("Reset CPC")
                }
                Button(
                    onClick = {
//                        viewModel.downloadAndParseFile()
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text("List Files")
                }
                LazyColumn {
                    items(files) { file ->
                        Text(
                            text = "${file.name} - Size: ${file.fileSize}",
                        )
                    }
                }
            }
        }
    )
}