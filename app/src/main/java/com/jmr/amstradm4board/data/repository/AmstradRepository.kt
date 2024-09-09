package com.jmr.amstradm4board.data.repository

import com.jmr.amstradm4board.domain.model.DataFile
import okhttp3.Response

interface AmstradRepository {

    suspend fun runGame(path: String): Response?

    suspend fun navigate(path: String): Response?

    suspend fun updateList(path: String): List<DataFile>

    suspend fun getDataList(path: String): List<DataFile>

}