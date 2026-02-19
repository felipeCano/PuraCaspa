package com.pura.caspa.domain.usecase

import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.repository.PuraCaspaRepository

class ResetPlayersVotesUseCase (
    private val repository: PuraCaspaRepository
) {
    suspend operator fun invoke(roomId: String) = repository.resetPlayersVotes(roomId)
}