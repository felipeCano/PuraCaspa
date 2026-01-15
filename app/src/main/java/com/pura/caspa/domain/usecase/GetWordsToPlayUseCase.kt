package com.pura.caspa.domain.usecase

import com.pura.caspa.data.model.APIResponse
import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.repository.PuraCaspaRepository

class GetWordsToPlayUseCase(private val puraCaspaRepository: PuraCaspaRepository) {
    suspend fun execute(): Resource<APIResponse> {
        return puraCaspaRepository.getWordstoPlay()
    }
}