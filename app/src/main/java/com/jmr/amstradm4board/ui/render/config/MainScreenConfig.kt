package com.jmr.amstradm4board.ui.render.config

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainScreenConfig {

    // Paleta de colores basada en el Amstrad CPC

    companion object {
        val black = Color(0xFF000000)

        val redKeyboard = Color(0xFFDD2222)
        val grayKeyboard = Color(0xFF282828)
        val darkGrayKeyboard = Color(0xFF171717)
        val blackKeyboard = Color(0xFF222222)
        val lightWhiteKeyboard = Color(0xFFD3D3D3)
        val greenKeyboard = Color(0xFF4CAF50)
        val darkGreenKeyboard = Color(0xFF226600)
        val blueKeyboard = Color(0xFF2222CC)
        val darkBlueKeyboard = Color(0xFF222266)

        val pastelYellowScreen = Color(0xFFFFFF80)
        val brightYellowScreen = Color(0xFFFFFF00)

        val isGameBackground = blueKeyboard
        val isDskBackground = blueKeyboard
        val folderBackground = pastelYellowScreen
        val otherFilesBackground = blackKeyboard

        // Configuración de la pantalla
        const val path = "games/aaa%20JM"

        //TOP
        const val titleText = "Amstrad M4"
        const val enterText = "ENTER"
        const val resetCPCText = "RESET CPC"
        const val resetM4Text = "RESET M4"
        const val ipLabelText = "IP:"
        const val defaultIp = "192.168.1.39"
        const val delButtonText = "DEL"

        val screenBackground = black

        val ipEditTextBackground = grayKeyboard
        val ipEditTextDarkBackground = darkGrayKeyboard
        val ipEditTextColor = pastelYellowScreen
        val ipTextFieldHeight = 56.dp
        val ipFontSize = 18.sp

        val resetRowHeight = 60.dp
        val connectionRowHeight = 70.dp
        val enterButtonBackground = blueKeyboard
        val enterButtonDarkBackground = darkBlueKeyboard
        val delButtonBackground = greenKeyboard
        val delButtonDarkBackground = darkGreenKeyboard
        val delButtonHeight = 60.dp

        val enterButtonFontSize = 16.sp
        val enterButtonWidth = 130.dp
        val enterButtonHeight = 56.dp

        val resetButtonFontSize = 12.sp
        val resetButtonWidth = 76.dp
        val resetButtonHeight = 56.dp

        // Configuración del diálogo DSK
        val dskDialogBackground = blackKeyboard
        val dskDialogCardBackground = black
        const val dskDialogMaxWidth = 0.95f
        const val dskDialogMaxHeight = 0.7f
        const val dskDialogAlpha = 0.9f
        const val dskDialogImageAlpha = 0.3f
        val dskDialogImagePadding = PaddingValues(0.dp, 70.dp, 0.dp, 30.dp)
        val dskDialogTitleBackground = blueKeyboard
        val dskDialogTitleFontSize = 16.sp
        val dskDialogTitleFontColor = brightYellowScreen
        const val dskDialogTitleLength = 16

        // Configuración de los elementos del diálogo DSK
        val dskItemBackground = blueKeyboard
        const val dskItemAlpha = 0.8f
        val dskItemPadding = 6.dp
        val dskItemFontSize = 14.sp
        val dskItemKSizeFontSize = 12.sp
    }
}
