package com.jmr.amstradm4board.ui.render.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmr.amstradm4board.ui.AmstradViewModel
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.redKeyboard
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.resetCPCText
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.resetM4Text

@Composable
fun RenderResetButtonsRow(viewModel: AmstradViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StyledRetroButton(
            text = resetCPCText,
            fontSize = 13.sp,
            backgroundColor = redKeyboard,
            shadowColor = Color(0xFF990000),
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            onClick = { viewModel.resetCPC() }
        )

        StyledRetroButton(
            text = resetM4Text,
            fontSize = 13.sp,
            backgroundColor = redKeyboard,
            shadowColor = Color(0xFF990000),
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            onClick = { viewModel.resetM4() }
        )
    }
}
