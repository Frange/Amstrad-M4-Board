package com.jmr.amstradm4board.domain.base


data class AppResult<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val e: Throwable?
) {
    companion object {
        fun <T> loading(): AppResult<T> {
            return AppResult(Status.LOADING, null, null, null)
        }

        fun <T> success(data: T?): AppResult<T> {
            return AppResult(Status.SUCCESS, data, null, null)
        }

        fun <T> error(msg: String, data: T?): AppResult<T> {
            return AppResult(Status.ERROR, data, msg, null)
        }

        fun <T> exception(e: Throwable): AppResult<T> {
            return AppResult(Status.EXCEPTION, null, null, null)
        }
    }

}