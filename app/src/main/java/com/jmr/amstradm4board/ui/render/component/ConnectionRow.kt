package com.jmr.amstradm4board.ui.render.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmr.amstradm4board.ui.AmstradViewModel
import com.jmr.amstradm4board.ui.Utils.customFontFamily
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.connectionRowHeight
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.enterButtonBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.enterButtonDarkBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.enterText
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.ipEditTextBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.ipEditTextColor
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.path
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.screenBackground

@Composable
fun RenderConnectionRow(viewModel: AmstradViewModel, ip: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(screenBackground),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier
                .padding(0.dp, 8.dp, 8.dp, 0.dp)
                .height(connectionRowHeight)
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            TextField(
                value = viewModel.ipAddress,
                onValueChange = { newIp ->
                    viewModel.ipAddress = newIp
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        color = ipEditTextBackground,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 8.dp),
                textStyle = TextStyle(
                    color = ipEditTextColor,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                ),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = ipEditTextBackground,
                    unfocusedContainerColor = ipEditTextBackground,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        StyledRetroButton(
            text = enterText,
            backgroundColor = enterButtonBackground,
            shadowColor = enterButtonDarkBackground,
            modifier = Modifier
                .padding(8.dp, 8.dp, 0.dp, 0.dp)
                .weight(1f)
                .height(connectionRowHeight),
            onClick = { viewModel.navigate(ip, path) }
        )
    }
}
