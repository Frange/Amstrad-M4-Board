package com.jmr.amstradm4board.ui.render.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmr.amstradm4board.ui.AmstradViewModel
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.backButtonBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.backButtonDarkBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.backButtonHeight
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.backButtonText


@Composable
fun RenderBackButton(viewModel: AmstradViewModel) {
    if (viewModel.lastPath != "") {
        StyledRetroButton(
            text = backButtonText,
            fontSize = 14.sp,
            backgroundColor = backButtonBackground,
            shadowColor = backButtonDarkBackground,
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
                .width(200.dp)
                .height(backButtonHeight),
            onClick = { viewModel.goBack() }
        )
    }
}