package com.pura.caspa.data.remote.ads

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.pura.caspa.BuildConfig
import com.pura.caspa.domain.repository.AdProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleAdProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AdProvider {
    private var rewardedAd: RewardedAd? = null

    override fun loadAd() {
        val adRequest = AdRequest.Builder().build()
        val testAdUnitId = BuildConfig.ADMOB_REWARDED_UNIT_ID

        RewardedAd.load(context, testAdUnitId, adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d("ADS_DEBUG", "¡Anuncio de PRUEBA cargado!")
                    rewardedAd = ad
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("ADS_DEBUG", "Fallo al cargar prueba: ${error.message}. Código: ${error.code}")
                    rewardedAd = null
                }
            })
    }

    override fun showRewardedAd(onAdDismissed: (Boolean) -> Unit) {
        val activity = context.findActivity()

        if (rewardedAd == null || activity == null) {
            Log.e("ADS_DEBUG", "No hay anuncio listo o no se encontró la Activity")
            loadAd()
            onAdDismissed(false)
            return
        }

        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d("ADS_DEBUG", "El usuario cerró el anuncio")
                rewardedAd = null
                loadAd() // Cargamos el siguiente
                onAdDismissed(true)
            }
            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e("ADS_DEBUG", "Error al mostrar: ${error.message}")
                rewardedAd = null
                loadAd()
                onAdDismissed(false)
            }
        }
        rewardedAd?.show(activity) { rewardItem ->
            val amount = rewardItem.amount
            val type = rewardItem.type
            Log.d("ADS_DEBUG", "Recompensa ganada: $amount $type")
        }
    }

    private fun Context.findActivity(): Activity? {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        return null
    }
}