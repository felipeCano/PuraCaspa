package com.pura.caspa.domain.usecase

import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.repository.PuraCaspaRepository

class JoinPartyUseCase(private val puraCaspaRepository: PuraCaspaRepository) {
    suspend operator fun invoke(roomId: String): Resource<Unit> {
        return puraCaspaRepository.joinParty(roomId)
    }
}