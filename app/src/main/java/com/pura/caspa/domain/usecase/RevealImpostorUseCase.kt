package com.pura.caspa.domain.usecase

import com.pura.caspa.domain.repository.PuraCaspaRepository

class RevealImpostorUseCase (
    private val repository: PuraCaspaRepository
) {
    suspend operator fun invoke(roomId: String, reveal: Boolean) =
        repository.revealImpostor(roomId, reveal)
}