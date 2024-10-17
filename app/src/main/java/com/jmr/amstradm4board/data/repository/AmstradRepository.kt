package com.jmr.amstradm4board.data.repository

import com.jmr.amstradm4board.data.service.AmstradApiService
import com.jmr.amstradm4board.domain.model.DataFile
import okhttp3.Response

interface AmstradRepository {

    fun updateApiService(amstradApiService: AmstradApiService)

    suspend fun navigate(ip: String, path: String): List<DataFile>

    suspend fun runGame(ip: String, path: String): Response?

    suspend fun resetM4(ip: String)

    suspend fun resetCPC(ip: String)

}