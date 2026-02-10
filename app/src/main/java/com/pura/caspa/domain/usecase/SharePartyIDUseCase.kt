package com.pura.caspa.domain.usecase

import com.pura.caspa.domain.repository.PuraCaspaRepository

class SharePartyIDUseCase(val puraCaspaRepository: PuraCaspaRepository) {
    operator fun invoke(partyId: String): String {
        val deepLink = "https://puracaspa.com/join/$partyId"
        return "¡Únete a mi partida de Pura Caspa! Toca aquí: $deepLink"
    }
}