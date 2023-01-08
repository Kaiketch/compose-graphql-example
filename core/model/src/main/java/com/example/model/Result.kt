package com.example.model

data class Result<T>(
    val data: T?,
    val isLoading: Boolean,
    val errors: List<Throwable>?
) {
    companion object {
        fun <T> startLoading(): Result<T> {
            return Result(
                data = null,
                isLoading = true,
                errors = null,
            )
        }

        fun <T> success(
            response: T
        ): Result<T> {
            return Result(
                data = response,
                isLoading = false,
                errors = null,
            )
        }

        fun <T> error(
            cause: Throwable,
        ): Result<T> {
            return Result(
                data = null,
                isLoading = false,
                errors = listOf(cause),
            )
        }
    }
}
