package com.jmr.amstradm4board.data.repository

import android.util.Log
import com.jmr.amstradm4board.data.service.AmstradApiService
import com.jmr.amstradm4board.domain.base.AppResult
import com.jmr.amstradm4board.domain.model.DataFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import javax.inject.Inject


class AmstradRepositoryImpl @Inject constructor(
    private val apiService: AmstradApiService
) : AmstradRepository {

    override suspend fun performHttpRequest(): Response? {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("http://192.168.1.39/config.cgi?ls=//games/aaa%20JM")
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
            val response = performHttpRequest()
//            val response = apiService.updateList("//$path")
            if (response?.isSuccessful == true) {
                getDataList()
            } else {
                arrayListOf(
                    DataFile("No data", false, "0")
                )
            }
        } catch (e: Exception) {
            arrayListOf(
                DataFile("Exception: ${e.message}", false, "0")
            )
        }
    }

    override suspend fun getDataList(): List<DataFile> {
        val response = apiService.downloadFolderFile()

        if (response.isSuccessful) {
            Log.v("MY_LOG", "")
            response.body()?.let { body ->
                val fileContent = body.string()
                return parseFileContent(fileContent)
            }
        }

        throw Exception("Error downloading file")
    }

    private fun parseFileContent(content: String): List<DataFile> {
        val gameList = mutableListOf<DataFile>()
        val lines = content.lines()

        lines.forEachIndexed { index, line ->
            val parts = line.split(",")
            if (parts.size == 3) {
                val name = parts[0].trim()
                val size = parts[2].trim()
                val isGame = index != 0
                gameList.add(DataFile(name, isGame, size))
            }
        }
        return gameList
    }

}