package com.jmr.amstradm4board.domain.base

import kotlinx.coroutines.flow.*

abstract class FlowUseCaseWithParams<in P, T> {

    operator fun invoke(parameters: P): Flow<T> = execute(parameters)

    protected abstract fun execute(parameters: P): Flow<T>

    private val _trigger = MutableStateFlow(true)

    suspend fun launch() {
        _trigger.emit(!(_trigger.value))
    }
}