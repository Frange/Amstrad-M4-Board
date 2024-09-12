package com.jmr.amstradm4board.domain.model


data class DataFile(
    val path: String,
    val name: String,
    val type: DataFileType,
    val fileSize: String
)