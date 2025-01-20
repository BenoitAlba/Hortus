package org.alba.hortus.domain.model

import androidx.compose.runtime.Composable
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.unknown
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed class RequestState<out T> {
    data object Loading : RequestState<Nothing>()
    data class Success<out T>(val data: T) : RequestState<T>()
    data class Error(val messageResource: StringResource? = null, val message: String? = null) :
        RequestState<Nothing>()

    fun isLoading(): Boolean = this is Loading
    fun isError(): Boolean = this is Error
    fun isSuccess(): Boolean = this is Success

    fun getSuccessData() = (this as Success).data

    @Composable
    fun getErrorMessage(): String = (this as Error).messageResource?.let {
        stringResource(it)
    } ?: this.message ?: stringResource(Res.string.unknown)
}