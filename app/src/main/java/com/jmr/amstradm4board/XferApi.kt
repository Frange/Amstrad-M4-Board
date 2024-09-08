package com.jmr.amstradm4board

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface XferApi {
    @GET("config.cgi")
    suspend fun resetCPC(): Response<Unit>

    @GET("config.cgi")
    suspend fun resetM4(): Response<Unit>

    @GET("config.cgi")
    suspend fun listFiles(@Query("ls") folder: String): Response<List<String>>
}