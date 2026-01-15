package com.pura.caspa.domain.usecase

import com.pura.caspa.data.model.Words
import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.repository.PuraCaspaRepository
import kotlinx.coroutines.flow.Flow

class GetWordsToPlayUseCase(private val puraCaspaRepository: PuraCaspaRepository) {
    fun execute(): Flow<Resource<Words>> {
        return puraCaspaRepository.getWordstoPlay()
    }
}