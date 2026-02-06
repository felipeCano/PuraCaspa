package com.pura.caspa.domain.usecase

import com.pura.caspa.domain.repository.PuraCaspaRepository

class GetInstallationIdUseCase(
    private val repository: PuraCaspaRepository
) {
    suspend operator fun invoke(): String = repository.getInstallationId()
}