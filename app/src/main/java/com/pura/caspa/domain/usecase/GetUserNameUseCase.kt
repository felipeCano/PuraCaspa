package com.pura.caspa.domain.usecase

import com.pura.caspa.domain.repository.PuraCaspaRepository

class GetUserNameUseCase(
    private val repository: PuraCaspaRepository
) {
    suspend operator fun invoke(): String {
        return repository.getUserName()
    }
}