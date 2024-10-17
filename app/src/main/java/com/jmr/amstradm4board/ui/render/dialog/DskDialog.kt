package com.jmr.amstradm4board.ui.render.dialog

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmr.amstradm4board.domain.model.DataFile
import com.jmr.amstradm4board.ui.AmstradViewModel
import com.jmr.amstradm4board.ui.Utils.customFontFamily
import com.jmr.amstradm4board.ui.Utils.getDskBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogAlpha
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogCardBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogMaxHeight
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogMaxWidth
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogTitleBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogTitleFontColor
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskDialogTitleFontSize
import com.jmr.amstradm4board.ui.drawableList


@Composable
fun RenderDskDialog(
    viewModel: AmstradViewModel,
    onDismiss: () -> Unit,
    onFileClick: (DataFile) -> Unit
) {
    if (viewModel.showDskDialog) {
        val context = LocalContext.current

        val backgroundResId =
            getDskBackground(context, drawableList, viewModel.selectedDskName.lowercase())

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
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(dskDialogTitleBackground, RoundedCornerShape(8.dp)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(18.dp, 0.dp)
                                .weight(1f),
                            maxLines = 2,
                            text = viewModel.selectedDskName.uppercase()
                                .replace(".dsk", "")
                                .replace(".DSK", ""),
                            lineHeight = 20.sp,
                            fontFamily = customFontFamily,
                            fontSize = dskDialogTitleFontSize,
                            color = dskDialogTitleFontColor
                        )
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .width(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Image(
                            painter = painterResource(id = backgroundResId),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(300.dp)
                                .width(170.dp)
                                .clip(RectangleShape)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            LazyColumn(
                                modifier = Modifier.align(Alignment.TopCenter)
                            ) {
                                items(viewModel.dskFiles) { file ->
                                    RenderDskItem(
                                        file,
                                        onClick = { onFileClick(file) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}