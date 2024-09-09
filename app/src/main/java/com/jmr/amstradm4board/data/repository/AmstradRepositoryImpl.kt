package com.jmr.amstradm4board.data.repository

import android.util.Log
import com.jmr.amstradm4board.data.service.AmstradApiService
import com.jmr.amstradm4board.domain.model.DataFile
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
    private val apiService: AmstradApiService
) : AmstradRepository {

    override suspend fun runGame(path: String): Response? {
//      http://192.168.1.39/config.cgi?run2=%2F%2Fgames%2Faaa%20JM%2FAbadia%20(1987).dsk%2FABADIA64.BAS
        val client = OkHttpClient()

        val url = "http://192.168.1.39/config.cgi?run2=//${encodeForUrl(path)}"
        val request = Request.Builder()
            .url(url)
            .build()

        return withContext(Dispatchers.IO) {
            try {
                client.newCall(request).execute()
            } catch (e: IOException) {
                null
            }
        }
    }

    override suspend fun navigate(path: String): Response? {
        val client = OkHttpClient()

        val web = "http://192.168.1.39/config.cgi?ls=%2F%2Fgames%2Faaa%20JM"
        val url = "http://192.168.1.39/config.cgi?ls=//${encodeForUrl(path)}"

        val request = Request.Builder()
            .url(url)
            .build()

        return withContext(Dispatchers.IO) {
            try {
                client.newCall(request).execute()
            } catch (e: IOException) {
                null
            }
        }
    }

    override suspend fun updateList(path: String): List<DataFile> {
        return try {
            val response = navigate(path)
//            val response = apiService.updateList("//$path")
            if (response?.isSuccessful == true) {
                getDataList(path)
            } else {
                arrayListOf(
                    DataFile(path, "No data", false, "0")
                )
            }
        } catch (e: Exception) {
            arrayListOf(
                DataFile("Exception: ${e.message}", "", false, "0")
            )
        }
    }

    override suspend fun getDataList(path: String): List<DataFile> {
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

        lines.forEachIndexed { index, line ->
            val parts = line.split(",")
            if (parts.size == 3) {
                val name = parts[0].trim()
                val size = parts[2].trim()
                val isGame = index != 0
                gameList.add(DataFile(path, name, isGame, size))
            }
        }
        return gameList
    }

    private fun encodeForUrl(input: String): String {
        return input.replace(" ", "%20")
    }
}