package com.jmr.amstradm4board.ui

import android.content.Context
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.jmr.amstradm4board.R


object Utils {
    val customFontFamily = FontFamily(
        Font(R.font.amstrad_cpc464, FontWeight.Normal)
    )

    fun getDskBackground(context: Context, drawableList: List<String>?, dskName: String): Int {
        val dskCleanedName = cleanName(dskName)

        val exactMatchId =
            context.resources.getIdentifier(dskCleanedName, "drawable", context.packageName)
        if (drawableList.isNullOrEmpty()) {
            return R.drawable.amstrad
        }

        if (exactMatchId != 0) {
            return exactMatchId
        }

        var bestMatchId = 0
        var bestScore = 0

        for (drawableName in drawableList) {
            val drawableNameCleaned = cleanName(drawableName)
            val score = similarityScore(dskCleanedName, drawableNameCleaned)
            if (score > bestScore) {
                bestScore = score
                bestMatchId =
                    context.resources.getIdentifier(drawableNameCleaned, "drawable", context.packageName)
            }
        }

        if (bestMatchId != 0) {
            return bestMatchId
        }

        return R.drawable.amstrad
    }

    private fun cleanName(fileName: String): String {
        return fileName
            .lowercase()
            .replace(".dsk", "")
            .replace("-", "_")
            .replace(" ", "_")
            .replace("__", "_")
            .replace("__", "_")
            .trim()
    }

    fun getDrawableResourceNames(): List<String> {
        val drawables = mutableListOf<String>()

        try {
            val drawableClass = R.drawable::class.java
            val fields = drawableClass.fields

            for (field in fields) {
                try {
                    val resourceName = field.name
                    drawables.add(resourceName)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return drawables
    }

    private fun similarityScore(str1: String, str2: String): Int {
        val words1 = str1.split("_", "(", ")", " ").filter { it.length > 2 } // Filtrar palabras cortas
        val words2 = str2.split("_", "(", ")", " ").filter { it.length > 2 }

        return words1.intersect(words2.toSet()).size
    }

}