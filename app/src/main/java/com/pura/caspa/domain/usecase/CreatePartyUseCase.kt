package com.pura.caspa.domain.usecase

import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.repository.PuraCaspaRepository

class CreatePartyUseCase(private val puraCaspaRepository: PuraCaspaRepository) {
    suspend operator fun invoke(customId: String): Resource<Unit> {
        return puraCaspaRepository.createParty(customId)
    }
}