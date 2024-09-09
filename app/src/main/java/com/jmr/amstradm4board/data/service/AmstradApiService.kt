package com.jmr.amstradm4board.data.service

import com.jmr.amstradm4board.domain.model.DataFile
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface AmstradApiService {

    // http://192.168.1.39/config.cgi?ls=//games/aaa%20JM
    // http://192.168.1.39/config.cgi?ls=%2F%2Fgames%2Faaa%20JM%2FAbadia%20(1987).dsk

    @GET("config.cgi")
    suspend fun updateList(
        @Query("ls") path: String
    ): Response<List<DataFile>>

    @GET("config.cgi")
    suspend fun resetCPC(): Response<Unit>

    @GET("config.cgi")
    suspend fun resetM4(): Response<Unit>

    @GET("config.cgi")
    suspend fun listFiles(
        @Query("ls") folder: String
    ): Response<List<String>>

    @GET("sd/m4/dir.txt")
    suspend fun downloadFolderFile(): Response<ResponseBody>
}