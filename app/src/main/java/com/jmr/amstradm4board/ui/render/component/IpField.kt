package com.jmr.amstradm4board.ui.render.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmr.amstradm4board.ui.AmstradViewModel


@Composable
fun RenderIpField(viewModel: AmstradViewModel, ip: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "IP Address: ${viewModel.ipAddress}",
            color = Color.Yellow,
            fontSize = 16.sp,
            modifier = Modifier
                .background(Color.DarkGray)
                .padding(8.dp)
        )

        RenderEnterButton(viewModel, ip)
    }
}