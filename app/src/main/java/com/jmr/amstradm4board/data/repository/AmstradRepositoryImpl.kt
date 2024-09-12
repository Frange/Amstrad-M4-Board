package com.jmr.amstradm4board.data.repository

import android.util.Log
import com.jmr.amstradm4board.data.repository.UtilsRepository.encodeForUrl
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
}