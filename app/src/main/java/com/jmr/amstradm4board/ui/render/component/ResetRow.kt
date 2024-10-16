package com.jmr.amstradm4board.ui.render.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jmr.amstradm4board.ui.AmstradViewModel
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.redKeyboard
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.resetBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.resetCPCText
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.resetDarkBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.resetM4Text
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.resetRowHeight
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.resetTextSize

@Composable
fun RenderResetButtonsRow(viewModel: AmstradViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StyledRetroButton(
            text = resetCPCText,
            fontSize = resetTextSize,
            backgroundColor = resetBackground,
            shadowColor = resetDarkBackground,
            modifier = Modifier
                .weight(1f)
                .height(resetRowHeight)
                .padding(vertical = 8.dp),
            onClick = { viewModel.resetCPC() }
        )

        StyledRetroButton(
            text = resetM4Text,
            fontSize = resetTextSize,
            backgroundColor = redKeyboard,
            shadowColor = resetDarkBackground,
            modifier = Modifier
                .weight(1f)
                .height(resetRowHeight)
                .padding(vertical = 8.dp),
            onClick = { viewModel.resetM4() }
        )
    }
}
