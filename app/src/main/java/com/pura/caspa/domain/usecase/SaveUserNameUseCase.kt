package com.pura.caspa.domain.usecase

import com.pura.caspa.domain.repository.PuraCaspaRepository

class SaveUserNameUseCase(private val puraCaspaRepository: PuraCaspaRepository) {
    operator fun invoke(name: String) {
        if (name.isNotBlank()) {
            puraCaspaRepository.saveUserName(name)
        }
    }
}