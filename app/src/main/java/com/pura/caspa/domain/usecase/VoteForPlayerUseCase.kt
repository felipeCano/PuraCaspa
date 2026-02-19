package com.pura.caspa.domain.usecase

import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.repository.PuraCaspaRepository

class VoteForPlayerUseCase (
    private val repository: PuraCaspaRepository
) {
    suspend operator fun invoke(roomId: String, playerVotedId: String): Resource<Unit> {
        return repository.voteForPlayer(roomId, playerVotedId)
    }
}