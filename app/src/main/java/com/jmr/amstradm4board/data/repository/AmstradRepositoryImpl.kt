package com.jmr.amstradm4board.data.repository

import android.util.Log
import com.jmr.amstradm4board.data.service.AmstradApiService
import com.jmr.amstradm4board.domain.model.DataFile
import com.jmr.amstradm4board.domain.model.DataFileType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject


class AmstradRepositoryImpl @Inject constructor(
    private val apiService: AmstradApiService,
    private val client: OkHttpClient
) : AmstradRepository {

    override suspend fun navigate(ip: String, path: String): List<DataFile> {
        val url = "http://$ip/config.cgi?ls=//${encodeForUrl(path)}"

        val request = Request.Builder()
            .url(url)
            .build()

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute()
            getDataList(path)
        }
    }

    override suspend fun runGame(ip: String, path: String): Response {
        val url = "http://$ip/config.cgi?run2=//${encodeForUrl(path)}"
        val request = Request.Builder()
            .url(url)
            .build()

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }

    suspend fun getDataList(path: String): List<DataFile> {
        val response = apiService.downloadFolderFile()

        if (response.isSuccessful) {
            Log.v("MY_LOG", "")
            response.body()?.let { body ->
                val fileContent = body.string()
                return parseFileContent(path, fileContent)
            }
        }

        throw Exception("Error downloading file")
    }

    private fun parseFileContent(path: String, content: String): List<DataFile> {
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
        return gameList
    }

    private fun getTypeOfFile(name: String): DataFileType {
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

    private fun isGame(item: String): Boolean {
        return when {
            !item.contains(".") -> false // Si no tiene un ".", es una carpeta
            item.endsWith(
                ".dsk",
                ignoreCase = true
            ) -> false // Si termina en ".dsk", se trata como carpeta
            else -> true
        }
    }

    private fun encodeForUrl(input: String): String {
        return input.replace(" ", "%20")
    }
}