package com.pura.caspa.domain.usecase

import com.pura.caspa.domain.repository.PuraCaspaRepository

class SharePartyIDUseCase(val puraCaspaRepository: PuraCaspaRepository) {
    operator fun invoke(partyId: String): String {
        return "¡Únete a mi sala de Pura Caspa! El ID es: $partyId"
    }
}