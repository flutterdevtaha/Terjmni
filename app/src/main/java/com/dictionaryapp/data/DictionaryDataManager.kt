package com.dictionaryapp.data

import com.dictionaryapp.data.models.DictionaryAPI
import com.dictionaryapp.data.services.DictionaryService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DictionaryDataManager {
    fun getDictionary(word: String): Flow<NetworkResult<List<DictionaryAPI>>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                emit(DictionaryService.getDictionary(word))

            } catch (e: Exception) {
                emit(NetworkResult.Fail(e.message.toString()))
            }

        }.flowOn(Dispatchers.IO)
    }
}