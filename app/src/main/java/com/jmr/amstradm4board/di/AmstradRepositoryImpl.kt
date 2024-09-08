package com.jmr.amstradm4board.di

import com.google.gson.Gson
import com.jmr.amstradm4board.XferApi
import com.jmr.amstradm4board.domain.base.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class AmstradRepositoryImpl @Inject constructor(
    private val gson: Gson,
    private val service: XferApi
) : AmstradRepository {

    private var fileList: List<String> = arrayListOf()

    override fun requestList(folder: String) = flow {
//        emit(AppResult.loading())

        val response = service.listFiles(folder)
        fileList =
            if (response.body() != null) response.body()!!
            else arrayListOf("Hola", "Caracola")
//        val formatedResponse = gson.fromJson("{list:$response}", ResponseParkList::class.java)
//        companyList = searchAndSortCompany(formatedResponse.list?.map { it -> it.toCompany() }!!)

        emit(AppResult.success(fileList))
    }.catch {
        emit(
            AppResult.exception(it)
        )
    }.flowOn(Dispatchers.IO)

    override fun getFileList(): List<String> {
        return fileList
    }

}