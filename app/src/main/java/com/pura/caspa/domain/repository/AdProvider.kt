package com.pura.caspa.domain.repository

interface AdProvider {
    fun loadAd()
    fun showRewardedAd(onAdDismissed: (Boolean) -> Unit)
}