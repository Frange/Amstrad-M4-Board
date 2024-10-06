package com.jmr.amstradm4board.ui.render.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jmr.amstradm4board.ui.AmstradViewModel
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.connectionRowHeight
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.delButtonBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.delButtonDarkBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.delButtonText
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.path


@Composable
fun RenderDelButton(viewModel: AmstradViewModel, ip: String) {
    StyledRetroButton(
        text = delButtonText,
        backgroundColor = delButtonBackground,
        shadowColor = delButtonDarkBackground,
        modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, 0.dp)
            .width(200.dp)
            .height(connectionRowHeight),
        onClick = { viewModel.navigate(ip, path) }
    )
}