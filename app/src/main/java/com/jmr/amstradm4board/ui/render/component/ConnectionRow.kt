package com.jmr.amstradm4board.ui.render.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmr.amstradm4board.ui.AmstradViewModel
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.enterButtonBackground
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.enterButtonFontSize
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.enterCPCText
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.lightWhiteKeyboard
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
                .padding(0.dp, 0.dp, 8.dp, 0.dp)
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "IP Address: \n${viewModel.ipAddress}",
                color = Color.Yellow,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray)
                    .padding(8.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(0.5f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Top
        ) {
            RenderEnterButton(viewModel, ip)
        }
    }
}


@Composable
fun RenderEnterButton(viewModel: AmstradViewModel, ip: String) {
    val space = 60f
    val spaceHeight = 145f
    val cornerRadius = 20f

    val trapezoidShape = GenericShape { size, _ ->
        moveTo(0f, 0f)
        lineTo(size.width - cornerRadius, 0f)
        quadraticTo(size.width, 0f, size.width, cornerRadius)
        lineTo(size.width, size.height - cornerRadius)
        quadraticTo(size.width, size.height, size.width - cornerRadius, size.height)
        lineTo(space + cornerRadius, size.height)
        quadraticTo(space, size.height, space, size.height - cornerRadius)
        lineTo(space, spaceHeight)
        quadraticTo(spaceHeight - cornerRadius, space, space + cornerRadius, spaceHeight)
        lineTo(cornerRadius, spaceHeight)
        quadraticTo(0f, spaceHeight, 0f, spaceHeight - cornerRadius)
        close()
    }

    Button(
        onClick = { viewModel.navigate(ip, path) },
        modifier = Modifier
            .width(130.dp)
            .height(120.dp)
            .clip(trapezoidShape),
        colors = ButtonDefaults.buttonColors(backgroundColor = enterButtonBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = enterCPCText,
                color = lightWhiteKeyboard,
                textAlign = TextAlign.Center,
                fontSize = enterButtonFontSize,
                modifier = Modifier.padding(
                    28.dp,
                    0.dp,
                    0.dp,
                    0.dp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

