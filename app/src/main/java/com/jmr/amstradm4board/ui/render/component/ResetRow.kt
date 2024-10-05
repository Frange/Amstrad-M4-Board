package com.jmr.amstradm4board.ui.render.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jmr.amstradm4board.ui.AmstradViewModel
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.resetCPCText
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.resetM4Text


@Composable
fun RenderResetButtonsRow(viewModel: AmstradViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { viewModel.resetCPC() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(text = resetM4Text, color = Color.White)
        }

        Button(
            onClick = { viewModel.resetM4() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(text = resetCPCText, color = Color.White)
        }
    }
}