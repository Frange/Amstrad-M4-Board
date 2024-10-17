package com.jmr.amstradm4board.data.repository

import android.app.Application
import android.util.Log
import com.jmr.amstradm4board.data.repository.UtilsRepository.encodeForUrl
import com.jmr.amstradm4board.data.repository.UtilsRepository.getTypeOfFile
import com.jmr.amstradm4board.data.service.AmstradApiService
import com.jmr.amstradm4board.domain.model.Command
import com.jmr.amstradm4board.domain.model.DataFile
import com.jmr.amstradm4board.domain.model.DataFileType
import com.jmr.amstradm4board.ui.Utils.capitalizeFirstLetter
import com.jmr.amstradm4board.ui.Utils.logs
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.initPath
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.isMock
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

    private fun getUrl(
        ip: String,
        command: Command,
        param1: String = "",
        param2: String = "",
        param3: String = ""
    ): String {
        val url = when (command) {
            Command.NAVIGATE -> "http://$ip/config.cgi?${command.value}=//${encodeForUrl(param1)}"
            Command.RUN -> "http://$ip/config.cgi?${command.value}=//${encodeForUrl(param1)}"
            Command.RESET_M4 -> "http://$ip/config.cgi?${command.value}"
            Command.RESET_CPC -> "http://$ip/config.cgi?${command.value}"
            Command.HACK_MENU -> "http://$ip/config.cgi?${command.value}"
            Command.RENAME_CPC -> "http://$ip/config.cgi?${command.value}=$param1"

            Command.CHANGE_SSID,
            Command.CHANGE_PASSWORD -> "http://$ip/config.cgi?${Command.CHANGE_SSID.value}=$param1&${Command.CHANGE_PASSWORD.value}=$param2"

            Command.CHANGE_SUBNET,
            Command.CHANGE_GATEWAY,
            Command.CHANGE_LOCAL_IP -> "http://$ip/config.cgi?${Command.CHANGE_LOCAL_IP.value}=$param1&${Command.CHANGE_SUBNET.value}=$param2&${Command.CHANGE_GATEWAY.value}=$param3"

            Command.CREATE_FOLDER -> "http://$ip/config.cgi?${command.value}=$param1"
            Command.REMOVE -> "http://$ip/config.cgi?${command.value}=$param1"
            Command.START_CART -> "http://$ip/config.cgi?${command.value}=$param1"
        }

        logs("URL: $url")

        return url
    }

    override suspend fun navigate(ip: String, path: String): List<DataFile> {
        if (isMock) {
            return if (path.lowercase().contains(".dsk")) {
                getMockDsk(path)
            } else {
                getMockDataList()
            }
        } else {
//            val url = "http://192.168.1.39/config.cgi?ls=%2F%2F%2Fgames%2Faaa%20JM%2FBatman%203%20The%20Movie.dsk"
            val url = getUrl(ip = ip, command = Command.NAVIGATE, param1 = path)

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
        val url = getUrl(ip = ip, command = Command.RUN, param1 = path)
        val request = Request.Builder()
            .url(url)
            .build()

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }

    override suspend fun resetM4(ip: String) {
        val url = getUrl(ip = ip, command = Command.RESET_M4)
        val request = Request.Builder()
            .url(url)
            .build()

        withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }

    override suspend fun resetCPC(ip: String) {
        val url = getUrl(ip = ip, command = Command.RESET_CPC)
        val request = Request.Builder()
            .url(url)
            .build()

        withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }

    private suspend fun getDataList(path: String): List<DataFile> {
        val response = apiService.downloadFolderFile()

        logs("getDataList() -> Response: $response")

        if (response.isSuccessful) {
            response.body()?.let { body ->
                val fileContent = body.string()

                logs("getDataList() -> fileContent: $fileContent")

                return UtilsRepository.parseFileContent(path, fileContent)
            }
        }

        throw Exception("Error downloading file")
    }

    private suspend fun createFolder(ip: String, pathWithFolder: String) {
//        http://192.168.1.39/config.cgi?mkdir=%2F%2F%2F%2Fgames%2Ftest
        val url = getUrl(ip = ip, command = Command.CREATE_FOLDER, param1 = pathWithFolder)
        val request = Request.Builder()
            .url(url)
            .build()

        withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }

    private suspend fun startCart(ip: String) {
//        http://192.168.1.39/config.cgi?cctr=Start+Cart // NO IDEA WHATS THAT
        val url = getUrl(ip = ip, command = Command.START_CART, param1 = "Start+Cart")
        val request = Request.Builder()
            .url(url)
            .build()

        withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }

    private suspend fun changeIP(ip: String, localIp: String, subnet: String, gateway: String) {
//    http://192.168.1.39/config.cgi?ip=192.168.1.39&nm=255.255.255.0&gw=192.168.1.1
        val url = getUrl(
            ip = ip,
            command = Command.CHANGE_LOCAL_IP,
            param1 = localIp,
            param2 = subnet,
            param3 = gateway
        )

        val request = Request.Builder()
            .url(url)
            .build()

        withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }

    private suspend fun changeWifi(ip: String, ssid: String, password: String) {
//        http://192.168.1.39/config.cgi?ssid=TP-LINK_E7364C&pw=aaaaa
        val url = getUrl(ip = ip, command = Command.CHANGE_SSID, param1 = ssid, param2 = password)

        val request = Request.Builder()
            .url(url)
            .build()

        withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }

    private suspend fun renameCPC(ip: String, name: String) {
//        http://192.168.1.39/config.cgi?navn=CPC464
        val url = getUrl(ip = ip, command = Command.RENAME_CPC, param1 = name)

        val request = Request.Builder()
            .url(url)
            .build()

        withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }

    private fun getMockDsk(path: String): List<DataFile> {
        val gameList = mutableListOf<DataFile>()

        val game = path.lowercase().replace("/", "").replace(".dsk", "").capitalizeFirstLetter()
        val inputStream = context.assets.open("mock_dsk.txt")

        var lineNumber = 0
        inputStream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                val parts = line.split(",")
                if (parts.size == 3) {
                    val extension = if (lineNumber == 1) ".BIN" else ".BAS"
                    val name = "${game.trim()}${extension}"
                    val size = parts[2].trim()
                    val type = DataFileType.GAME
                    gameList.add(DataFile(initPath, name, type, size))
                }
                lineNumber += 1
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