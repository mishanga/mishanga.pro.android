package pro.mishanga.android.ui.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.yandex.android.mishanga.data.SettingsStore
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import pro.mishanga.android.ui.theme.CustomBackground
import pro.mishanga.android.ui.utils.AdConstants

@Composable
fun InterstitialScreen(
    onBack: () -> Unit,
    topBar: @Composable (title: String, onBack: () -> Unit) -> Unit
) {
    val context = LocalContext.current
    val settings = remember { SettingsStore.get(context).read() }
    val adUnitId = settings.adUnitId.ifBlank { AdConstants.TEST_INTERSTITIAL }
    val uuid = settings.uuid
    val regionId = settings.regionId
    val aimBannerId = settings.aimBannerId

    var status by remember { mutableStateOf("idle") }
    var loader by remember { mutableStateOf(InterstitialAdLoader(context)) }
    var currentAd by remember { mutableStateOf<InterstitialAd?>(null) }

    var load by remember { mutableStateOf(true) }

    fun loadAndShow() {
        status = "loading…"
        loader.setAdLoadListener(object : InterstitialAdLoadListener {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                status = "loaded"
                currentAd = interstitialAd
                interstitialAd.setAdEventListener(object : InterstitialAdEventListener {
                    override fun onAdShown() {
                        Log.d("YangoAds", "Interstitial shown")
                    }

                    override fun onAdDismissed() {
                        Log.d("YangoAds", "Interstitial closed")
                    }

                    override fun onAdClicked() {}
                    override fun onAdImpression(impressionData: ImpressionData?) {
                        Log.d("YangoAds", "Interstitial impression: $impressionData")
                    }

                    override fun onAdFailedToShow(error: AdError) {
                        status = "error: ${error.description}"
                        Log.d("YangoAds", "Interstitial failed to show $error")
                    }
                })
                interstitialAd.show(context as android.app.Activity)
                load = false
            }

            override fun onAdFailedToLoad(error: AdRequestError) {
                status = "error: ${'$'}{error.description}"
                Log.d("YangoAds", "Interstitial load error ${'$'}error")
            }
        })
        loader.loadAd(AdRequestConfiguration.Builder(adUnitId).build())
    }

    CustomBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            topBar("Interstitial", onBack)
            PrimaryButton(
                text = "Try again",
                modifier = Modifier.fillMaxWidth(0.9f)
            ) { loadAndShow() }
            Text(
                text = if (status.contains("error")) status else ""
            )
        }
    }

    LaunchedEffect(load) {
        loadAndShow()
    }
}
