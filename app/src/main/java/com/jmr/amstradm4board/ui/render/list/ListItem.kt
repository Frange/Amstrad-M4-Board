package com.jmr.amstradm4board.ui.render.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmr.amstradm4board.domain.model.DataFile
import com.jmr.amstradm4board.domain.model.DataFileType
import com.jmr.amstradm4board.ui.Utils.capitalizeFirstLetter
import com.jmr.amstradm4board.ui.Utils.customFontFamily
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.blackKeyboard
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.brightYellowScreen
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.folderBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.lightWhiteKeyboard
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.otherFilesBackground


@Composable
fun RenderDataFileItem(file: DataFile, onClick: (DataFile) -> Unit) {
    val backgroundColor = when (file.type) {
        DataFileType.FOLDER -> folderBackground
        DataFileType.DSK -> dskBackground
        DataFileType.GAME -> otherFilesBackground
        DataFileType.OTHER -> otherFilesBackground
    }

    Card(
        backgroundColor = backgroundColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick(file) },
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                painter = painterResource(
                    id = if (file.type == DataFileType.FOLDER) android.R.drawable.star_on
                    else android.R.drawable.star_off
                ),
                contentDescription = null,
                tint = if (file.type == DataFileType.FOLDER) blackKeyboard else brightYellowScreen,  // Color del icono
                modifier = Modifier
                    .size(24.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp)
            )

            Text(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                maxLines = 2,
                text = if (file.type != DataFileType.FOLDER) file.name.capitalizeFirstLetter()
                    .replace(".DSK", "")
                else file.name.uppercase().take(18),
                textAlign = TextAlign.Start,
                lineHeight = 20.sp,
                fontFamily = customFontFamily,
                fontSize = 13.sp,
                color = if (file.type == DataFileType.FOLDER) blackKeyboard else brightYellowScreen,
            )

            if (file.fileSize != "0") {
                Text(
                    text = file.fileSize,
                    fontFamily = customFontFamily,
                    style = MaterialTheme.typography.body2.copy(fontSize = 12.sp),
                    color = lightWhiteKeyboard,
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}
