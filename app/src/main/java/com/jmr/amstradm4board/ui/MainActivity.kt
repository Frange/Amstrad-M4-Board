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
    val viewModel: XferViewModel = hiltViewModel()
    val files by viewModel.files

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("XFER App") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp) // Añadir padding a los elementos
            ) {
                Button(
                    onClick = { viewModel.resetCPC() },
                    modifier = Modifier.padding(bottom = 8.dp) // Añadir separación entre botones
                ) {
                    Text("Reset CPC")
                }
                Button(
                    onClick = {
                        viewModel.listFiles("/")
                    },
                    modifier = Modifier.padding(bottom = 8.dp) // Añadir separación entre botones
                ) {
                    Text("List Files")
                }
                LazyColumn {
                    items(files) { file ->
                        Text("File: $file")
                    }
                }
            }
        }
    )
}