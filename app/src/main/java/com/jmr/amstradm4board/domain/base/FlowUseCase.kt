package com.jmr.amstradm4board.domain.base

import kotlinx.coroutines.flow.*

abstract class FlowUseCase<T> {

    operator fun invoke(): Flow<T> = execute()

    protected abstract fun execute(): Flow<T>

    private val _trigger = MutableStateFlow(true)

    suspend fun launch() {
        _trigger.emit(!(_trigger.value))
    }
}
