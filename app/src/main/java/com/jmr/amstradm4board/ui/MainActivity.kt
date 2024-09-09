package com.jmr.amstradm4board.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import com.jmr.amstradm4board.domain.model.DataFile
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
                        viewModel.goBack()
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text("List Files")
                }
                LazyColumn {
                    items(files) { file ->
                        FileItem(file) { clickedFile ->
                            val file = clickedFile.name.lowercase()
                            if (file.contains(".dsk") && clickedFile.isGame) {
                                viewModel.navigate(clickedFile.path + "/" + clickedFile.name)
                            } else {
                                viewModel.runGame(clickedFile.path + "/" + clickedFile.name)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun FileItem(file: DataFile, onClick: (DataFile) -> Unit) {
    // Usamos Row para mostrar el nombre y el tamaño del archivo en una línea
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(file) }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween // Alinear elementos
    ) {
        // Mostrar el nombre del archivo
        Text(
            text = file.name,
            modifier = Modifier.weight(1f), // Ocupa el espacio restante
            style = MaterialTheme.typography.body1 // Estilo de texto principal
        )

        // Mostrar el tamaño del archivo, alineado a la derecha y más pequeño
        Text(
            text = file.fileSize,
            style = MaterialTheme.typography.body2.copy(fontSize = 12.sp), // Tamaño más pequeño
            modifier = Modifier.alignByBaseline() // Alinear a la misma línea base que el nombre
        )
    }
}
