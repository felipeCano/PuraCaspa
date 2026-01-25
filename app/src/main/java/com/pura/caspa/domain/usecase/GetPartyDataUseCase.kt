package com.pura.caspa.domain.usecase

import com.pura.caspa.data.model.PartyData
import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.repository.PuraCaspaRepository
import kotlinx.coroutines.flow.Flow

class GetPartyDataUseCase(private val puraCaspaRepository: PuraCaspaRepository) {
    operator fun invoke(roomId: String): Flow<Resource<PartyData>> {
        return puraCaspaRepository.getPartyData(roomId)
    }
}