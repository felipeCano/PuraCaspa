package com.pura.caspa.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

@Composable
fun AdmobBanner(adUnitId: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                this.adUnitId = adUnitId
                adListener = object : com.google.android.gms.ads.AdListener() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        println("AdMob Error: ${error.message}, Code: ${error.code}")
                    }
                }
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}