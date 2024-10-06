package com.jmr.amstradm4board.ui.render.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jmr.amstradm4board.ui.AmstradViewModel
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.redKeyboard
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.resetCPCText
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.resetM4Text
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.resetRowHeight

@Composable
fun RenderResetButtonsRow(viewModel: AmstradViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp, 0.dp, 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StyledRetroButton(
            text = resetCPCText,
            backgroundColor = redKeyboard,
            shadowColor = Color(0xFF990000),
            modifier = Modifier
                .height(resetRowHeight)
                .weight(1f),
            onClick = { viewModel.resetCPC() }
        )

        StyledRetroButton(
            text = resetM4Text,
            backgroundColor = redKeyboard,
            shadowColor = Color(0xFF990000),
            modifier = Modifier
                .height(resetRowHeight)
                .weight(1f),
            onClick = { viewModel.resetM4() }
        )
    }
}
