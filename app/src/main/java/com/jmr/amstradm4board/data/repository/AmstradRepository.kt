package com.jmr.amstradm4board.data.repository

import com.jmr.amstradm4board.domain.model.DataFile
import okhttp3.Response

interface AmstradRepository {

    suspend fun performHttpRequest(): Response?

    suspend fun updateList(path: String): List<DataFile>

    suspend fun getDataList(): List<DataFile>

}