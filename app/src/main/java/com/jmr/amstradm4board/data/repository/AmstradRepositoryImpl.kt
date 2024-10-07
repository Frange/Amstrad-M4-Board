package com.jmr.amstradm4board.data.repository

import android.app.Application
import android.util.Log
import com.jmr.amstradm4board.data.repository.UtilsRepository.encodeForUrl
import com.jmr.amstradm4board.data.repository.UtilsRepository.getTypeOfFile
import com.jmr.amstradm4board.data.service.AmstradApiService
import com.jmr.amstradm4board.domain.model.Command
import com.jmr.amstradm4board.domain.model.DataFile
import com.jmr.amstradm4board.domain.model.DataFileType
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.isMock
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.initPath
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
        if (isMock) {
            return if (path.lowercase().contains(".dsk")) {
                getMockDsk()
            } else {
                getMockDataList()
            }
        } else {
            val url = "http://$ip/config.cgi?${Command.NAVIGATE.value}=//${encodeForUrl(path)}"

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
        val url = "http://$ip/config.cgi?${Command.RUN.value}=//${encodeForUrl(path)}"
        val request = Request.Builder()
            .url(url)
            .build()

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }

    override suspend fun resetM4(ip: String) {
        val url = "http://$ip/config.cgi?${Command.RESET_M4.value}}"
        val request = Request.Builder()
            .url(url)
            .build()

        withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }

    override suspend fun resetCPC(ip: String) {
        val url = "http://$ip/config.cgi?${Command.RESET_CPC.value}"
        val request = Request.Builder()
            .url(url)
            .build()

        withContext(Dispatchers.IO) {
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

    private suspend fun createFolder(ip: String, pathWithFolder: String) {
//        http://192.168.1.39/config.cgi?mkdir=%2F%2F%2F%2Fgames%2Ftest
        val url =
            "http://$ip/config.cgi?${Command.CREATE_FOLDER.value}=$pathWithFolder"
        val request = Request.Builder()
            .url(url)
            .build()

        withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }

    private suspend fun startCart(ip: String, localIp: String, password: String, gateway: String) {
//        http://192.168.1.39/config.cgi?cctr=Start+Cart // NO IDEA WHATS THAT
        val url =
            "http://$ip/config.cgi?${Command.START_CART.value}=Start+Cart"
        val request = Request.Builder()
            .url(url)
            .build()

        withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }

    private suspend fun changeIP(ip: String, localIp: String, password: String, gateway: String) {
//    http://192.168.1.39/config.cgi?ip=192.168.1.39&nm=255.255.255.0&gw=192.168.1.1
        val url =
            "http://$ip/config.cgi?${Command.CHANGE_LOCAL_IP.value}=$localIp&${Command.CHANGE_SUBNET.value}=$password&${Command.CHANGE_GATEWAY.value}=$gateway"
        val request = Request.Builder()
            .url(url)
            .build()

        withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }

    private suspend fun changeWifi(ip: String, ssid: String, password: String) {
//        http://192.168.1.39/config.cgi?ssid=TP-LINK_E7364C&pw=aaaaa
        val url =
            "http://$ip/config.cgi?${Command.CHANGE_SSID.value}=$ssid&${Command.CHANGE_PASSWORD.value}=$password"
        val request = Request.Builder()
            .url(url)
            .build()

        withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }

    private suspend fun renameCPC(ip: String, name: String) {
//        http://192.168.1.39/config.cgi?navn=CPC464
        val url = "http://$ip/config.cgi?${Command.RENAME_CPC.value}=$name"
        val request = Request.Builder()
            .url(url)
            .build()

        withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
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
                    gameList.add(DataFile(initPath, name, type, size))
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