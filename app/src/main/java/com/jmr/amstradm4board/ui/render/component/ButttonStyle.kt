package com.jmr.amstradm4board.ui.render.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmr.amstradm4board.ui.Utils.customFontFamily

@Composable
fun StyledRetroButton(
    text: String,
    fontSize: TextUnit,
    backgroundColor: Color,
    shadowColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                shadowColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(end = 14.dp, bottom = 14.dp)
            .background(
                backgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                0.2.dp,
                Color(1f, 1f, 1f, 0.2f),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    backgroundColor,
                    shape = RoundedCornerShape(10.dp)
                )
                .align(Alignment.Center)
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                fontFamily = customFontFamily,
                lineHeight = 28.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}