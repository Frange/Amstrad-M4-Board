package com.jmr.amstradm4board.data.repository

import com.jmr.amstradm4board.domain.model.DataFile
import com.jmr.amstradm4board.domain.model.DataFileType

object UtilsRepository {

    fun encodeForUrl(input: String): String {
        return input.replace(" ", "%20")
    }

    fun parseFileContent(path: String, content: String): List<DataFile> {
        val gameList = mutableListOf<DataFile>()
        val lines = content.lines()

        lines.forEachIndexed { _, line ->
            val parts = line.split(",")
            if (parts.size == 3) {
                val name = parts[0].trim()
                val size = parts[2].trim()
                val type = getTypeOfFile(name)
                gameList.add(DataFile(path, name, type, size))
            }
        }

        return gameList.sortedWith(compareBy<DataFile> { it.type }.thenBy { it.name })

//        return gameList
    }

    fun getTypeOfFile(name: String): DataFileType {
        return when {
            isFolder(name) -> DataFileType.FOLDER
            isGame(name) -> DataFileType.GAME
            isDskFile(name) -> DataFileType.DSK
            else -> DataFileType.OTHER
        }
    }

    private fun isDskFile(name: String): Boolean {
        return name.lowercase().endsWith(".dsk")
    }

    private fun isFolder(name: String): Boolean {
        return !name.contains(".")
    }

    private fun isGame(name: String): Boolean {
        return when {
            !name.contains(".") -> false
            name.lowercase().endsWith(
                ".dsk",
                ignoreCase = true
            ) -> false
            name.lowercase().endsWith(
                ".bin",
                ignoreCase = true
            ) -> true
            name.lowercase().endsWith(
                ".bas",
                ignoreCase = true
            ) -> true
            else -> false
        }
    }

}