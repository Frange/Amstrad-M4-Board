package com.jmr.amstradm4board.ui.render.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmr.amstradm4board.ui.AmstradViewModel
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.delButtonText
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.greenKeyboard
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.lightWhiteKeyboard


@Composable
fun RenderDelButton(viewModel: AmstradViewModel, ip: String) {
    Button(
        onClick = {
            viewModel.goBack(ip)
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = greenKeyboard
        ),
        modifier = Modifier
            .height(60.dp)
            .width(160.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
    ) {
        Text(
            text = delButtonText,
            color = lightWhiteKeyboard,
            fontSize = 20.sp,
            modifier = Modifier
                .graphicsLayer {
                    shadowElevation = 8f
                    shape = RoundedCornerShape(12.dp)
                    clip = true
                }
        )
    }
}