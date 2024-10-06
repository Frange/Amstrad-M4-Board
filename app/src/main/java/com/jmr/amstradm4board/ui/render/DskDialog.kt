package com.jmr.amstradm4board.ui.render

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jmr.amstradm4board.domain.model.DataFile
import com.jmr.amstradm4board.ui.Utils.customFontFamily
import com.jmr.amstradm4board.ui.Utils.getDskBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.brightYellowScreen
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogAlpha
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogCardBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogImageAlpha
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogImagePadding
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogMaxHeight
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogMaxWidth
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogTitleBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogTitleFontColor
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogTitleFontSize
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogTitleLength
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskItemAlpha
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskItemBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskItemFontSize
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskItemKSizeFontSize
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskItemPadding
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.lightWhiteKeyboard
import com.jmr.amstradm4board.ui.drawableList


@Composable
fun RenderDskDialog(
    showDskDialog: Boolean,
    dskName: String,
    files: List<DataFile>,
    onDismiss: () -> Unit,
    onFileClick: (DataFile) -> Unit
) {
    if (showDskDialog) {
        val backgroundResId =
            getDskBackground(LocalContext.current, drawableList, dskName.lowercase())

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
                                    RenderDskItem(
                                        file,
                                        onClick = { onFileClick(file) })
                                }
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
