package com.jmr.amstradm4board.ui.render.config

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainScreenConfig {

    // Paleta de colores basada en el Amstrad CPC

    companion object {
        const val isMock = false
        const val defaultIp = "192.168.1.39"
        const val initPath = ""
//        const val initPath = "games/aaa%20JM"

        val black = Color(0xFF000000)

        val redKeyboard = Color(0xFFDD2222)
        val redDarkKeyboard = Color(0xFF990000)
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

        val folderBackground = pastelYellowScreen
        val dskBackground = blueKeyboard
        val gameBackground = darkGreenKeyboard
        val otherFilesBackground = blackKeyboard

        //TOP
        const val titleText = "Amstrad M4"
        const val enterText = "ENTER"
        const val resetCPCText = "RESET CPC"
        const val resetM4Text = "RESET M4"
        const val backButtonText = "BACK"

        val screenBackground = black

        val resetRowHeight = 70.dp
        val resetTextSize = 13.sp
        val resetBackground = redKeyboard
        val resetDarkBackground = redDarkKeyboard

        val connectionRowHeight = 70.dp

        val ipEditTextBackground = grayKeyboard
        val ipEditTextDarkBackground = darkGrayKeyboard
        val ipEditTextColor = pastelYellowScreen
        val ipFontSize = 18.sp

        val enterButtonBackground = blueKeyboard
        val enterButtonDarkBackground = darkBlueKeyboard
        val enterButtonFontSize = 16.sp

        val backButtonBackground = greenKeyboard
        val backButtonDarkBackground = darkGreenKeyboard
        val backButtonHeight = 60.dp

        // Configuración del diálogo DSK
        val dskDialogBackground = blackKeyboard
        val dskDialogCardBackground = black
        const val dskDialogMaxWidth = 0.95f
        const val dskDialogMaxHeight = 0.7f
        const val dskDialogAlpha = 0.9f
        val dskDialogTitleBackground = blueKeyboard
        val dskDialogTitleFontSize = 12.sp
        val dskDialogTitleFontColor = brightYellowScreen
//        const val dskDialogTitleLength = 25

        // Configuración de los elementos del diálogo DSK
        val dskItemPadding = 2.dp
        val dskItemFontSize = 11.sp
    }
}
