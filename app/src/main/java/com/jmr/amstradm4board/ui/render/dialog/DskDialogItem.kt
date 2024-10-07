package com.jmr.amstradm4board.ui.render.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jmr.amstradm4board.domain.model.DataFile
import com.jmr.amstradm4board.domain.model.DataFileType
import com.jmr.amstradm4board.ui.Utils.customFontFamily
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.brightYellowScreen
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskItemFontSize
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.dskItemPadding
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.gameBackground


@Composable
fun RenderDskItem(file: DataFile, onClick: (DataFile) -> Unit) {
    Card(
        backgroundColor = if (file.type == DataFileType.GAME) gameBackground else dskBackground,
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
                .padding(dskItemPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = file.name.uppercase(),
                textAlign = TextAlign.Center,
                fontSize = dskItemFontSize,
                fontFamily = customFontFamily,
                style = MaterialTheme.typography.body1,
                color = brightYellowScreen,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
