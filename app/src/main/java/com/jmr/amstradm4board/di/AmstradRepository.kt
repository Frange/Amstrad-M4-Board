package com.jmr.amstradm4board.di

import com.jmr.amstradm4board.domain.base.AppResult
import kotlinx.coroutines.flow.Flow

interface AmstradRepository {

    fun requestList(folder: String): Flow<AppResult<List<String>>>

    fun getFileList(): List<String>

}