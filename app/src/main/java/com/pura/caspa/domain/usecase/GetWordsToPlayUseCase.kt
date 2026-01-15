package com.pura.caspa.domain.usecase

import com.pura.caspa.data.model.Words
import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.repository.PuraCaspaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class GetWordsToPlayUseCase(private val puraCaspaRepository: PuraCaspaRepository) {
    fun execute(hasInternet: Boolean): Flow<Resource<Words>> = flow {
        if (hasInternet) {
            emitAll(puraCaspaRepository.getWordstoPlay())
        } else {
            emit(Resource.Error(message = "Internet is not available"))
        }
    }
}