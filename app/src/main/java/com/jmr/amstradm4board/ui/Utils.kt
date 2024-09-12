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

    fun similarityScore(str1: String, str2: String): Int {
        val words1 = str1.split("_")
        val words2 = str2.split("_")

        return words1.intersect(words2.toSet()).size
    }

    fun getDskBackground(context: Context, dskName: String): Int {
        val cleanedName = dskName
            .lowercase()
            .replace(".dsk", "")
            .replace("-", "_")
            .replace(" ", "_")
            .trim()

        val drawableNames = getDrawableResourceNames(context)

        val exactMatchId = context.resources.getIdentifier(cleanedName, "drawable", context.packageName)
        if (exactMatchId != 0) {
            return exactMatchId
        }

        var bestMatchId = 0
        var bestScore = 0

        for (drawableName in drawableNames) {
            val score = similarityScore(cleanedName, drawableName)
            if (score > bestScore) {
                bestScore = score
                bestMatchId = context.resources.getIdentifier(drawableName, "drawable", context.packageName)
            }
        }

        if (bestMatchId != 0) {
            return bestMatchId
        }

        return R.drawable.amstrad
    }

    private fun getDrawableResourceNames(context: Context): List<String> {
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

}