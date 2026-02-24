package com.pura.caspa.domain.usecase

import com.pura.caspa.domain.repository.AdProvider

class ShowRewardedAdUseCase (
    private val adProvider: AdProvider
) {
    operator fun invoke(onComplete: (Boolean) -> Unit) {
        adProvider.showRewardedAd { isEarned ->
            onComplete(isEarned)
        }
    }
}