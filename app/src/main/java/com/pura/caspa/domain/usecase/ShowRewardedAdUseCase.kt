package com.pura.caspa.domain.usecase

import com.pura.caspa.domain.repository.AdProvider

class ShowRewardedAdUseCase (
    private val adProvider: AdProvider
) {
    operator fun invoke(activity: android.app.Activity, onComplete: (Boolean) -> Unit) {
        adProvider.showRewardedAd(activity) { isEarned ->
            onComplete(isEarned)
        }
    }
}