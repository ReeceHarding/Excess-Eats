package com.exceseats.repository

import com.exceseats.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

abstract class BaseRepository {
    protected fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Flow<Resource<T>> = flow {
        try {
            emit(Resource.Loading())
            val result = apiCall()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            Timber.e(e)
            emit(Resource.Error(e.message ?: "An error occurred"))
        }
    }.catch { e ->
        Timber.e(e)
        emit(Resource.Error(e.message ?: "An error occurred"))
    }
}
