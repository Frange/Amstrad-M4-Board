package com.jmr.amstradm4board.domain.model


data class DataFile(
    val path: String,
    val name: String,
    val isGame: Boolean,
    val fileSize: String
)