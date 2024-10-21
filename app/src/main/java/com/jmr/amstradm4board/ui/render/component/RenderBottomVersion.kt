package com.jmr.amstradm4board.ui.render.component

import android.content.Context

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmr.amstradm4board.ui.Utils.customFontFamily
import com.jmr.amstradm4board.ui.Utils.getVersionName
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.bottomVersionText
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.lightGreenKeyboard


@Composable
fun RenderBottomVersion(context: Context) {

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$bottomVersionText ${getVersionName(context)}",
            color = lightGreenKeyboard,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            fontFamily = customFontFamily,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

