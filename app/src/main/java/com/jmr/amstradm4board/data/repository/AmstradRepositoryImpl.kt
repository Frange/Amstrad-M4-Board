package com.jmr.amstradm4board.data.repository

import android.app.Application
import android.util.Log
import com.jmr.amstradm4board.data.repository.UtilsRepository.encodeForUrl
import com.jmr.amstradm4board.data.repository.UtilsRepository.getTypeOfFile
import com.jmr.amstradm4board.data.service.AmstradApiService
import com.jmr.amstradm4board.domain.model.DataFile
import com.jmr.amstradm4board.domain.model.DataFileType
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject


class AmstradRepositoryImpl @Inject constructor(
    private val context: Application,
    private val apiService: AmstradApiService,
    private val client: OkHttpClient
) : AmstradRepository {

    override suspend fun navigate(ip: String, path: String): List<DataFile> {
        val isMock = true

        if (isMock) {
            if (path.contains(".dsk")) {
                return getMockDsk()
            } else {
                return getMockDataList()
            }
        } else {
            val url = "http://$ip/config.cgi?ls=//${encodeForUrl(path)}"

            val request = Request.Builder()
                .url(url)
                .build()

            return withContext(Dispatchers.IO) {
                client.newCall(request).execute()
                getDataList(path)
            }
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

    private suspend fun getDataList(path: String): List<DataFile> {
        val response = apiService.downloadFolderFile()

        if (response.isSuccessful) {
            Log.v("MY_LOG", "")
            response.body()?.let { body ->
                val fileContent = body.string()
                return UtilsRepository.parseFileContent(path, fileContent)
            }
        }

        throw Exception("Error downloading file")
    }

    private fun getMockDsk(): List<DataFile> {
        val gameList = mutableListOf<DataFile>()

        val inputStream = context.assets.open("mock_dsk.txt")
        inputStream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                val parts = line.split(",")
                if (parts.size == 3) {
                    val name = "MOCK ${parts[0].trim()}"
                    val size = parts[2].trim()
                    val type = getTypeOfFile(name)
                    gameList.add(DataFile(path, name, type, size))
                }
            }
        }
        return gameList
    }

    private fun getMockDataList(): List<DataFile> {
        val gameList = mutableListOf<DataFile>()

        val inputStream = context.assets.open("mock_list.txt")
        inputStream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                val parts = line.split(",")
                if (parts.size == 3) {
                    val name = "MOCK ${parts[0].trim()}"
                    val size = parts[2].trim()
                    val type = getTypeOfFile(name)
                    gameList.add(DataFile("", name, type, size))
                } else {
                    val name = parts[0].trim()
                    gameList.add(DataFile("", name, DataFileType.FOLDER, "0"))
                }
            }
        }
        return gameList
    }
}