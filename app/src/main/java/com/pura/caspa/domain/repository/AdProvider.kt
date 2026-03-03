package com.pura.caspa.domain.repository

import android.app.Activity

interface AdProvider {
    fun loadAd()
    fun showRewardedAd(activity: Activity, onAdDismissed: (Boolean) -> Unit)
}