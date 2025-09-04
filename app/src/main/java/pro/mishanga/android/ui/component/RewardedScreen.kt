package pro.mishanga.android.ui.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader
import pro.mishanga.android.ui.theme.CustomBackground
import pro.mishanga.android.ui.utils.AdConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardedScreen(
    onBack: () -> Unit,
    topBar: @Composable (title: String, onBack: () -> Unit) -> Unit
) {
    val context = LocalContext.current
    val settings = remember { SettingsStore.get(context).read() }
    val adUnitId = settings.adUnitId.ifBlank { AdConstants.TEST_REWARDED }
    val uuid = settings.uuid
    val regionId = settings.regionId
    val aimBannerId = settings.aimBannerId

    var status by remember { mutableStateOf("idle") }
    var load by remember { mutableStateOf(true) }
    var loader by remember { mutableStateOf(RewardedAdLoader(context)) }


    fun loadAndShow() {
        status = "loading…"
        loader.setAdLoadListener(object : RewardedAdLoadListener {
            override fun onAdLoaded(rewardedAd: RewardedAd) {
                status = "loaded"
                rewardedAd.setAdEventListener(object : RewardedAdEventListener {
                    override fun onAdShown() {
                        Log.d("YangoAds", "Rewarded shown")
                    }

                    override fun onAdDismissed() {
                        Log.d("YangoAds", "Rewarded closed")
                    }

                    override fun onAdClicked() {}
                    override fun onAdImpression(impressionData: ImpressionData?) {
                        Log.d("YangoAds", "Rewarded impression: $impressionData")
                    }

                    override fun onRewarded(reward: Reward) {
                        Log.d(
                            "YangoAds",
                            "Rewarded: type=${'$'}{reward.type}, amount=${'$'}{reward.amount}"
                        )
                        status = "reward: ${'$'}{reward.type} x${'$'}{reward.amount}"
                    }

                    override fun onAdFailedToShow(error: AdError) {
                        status = "error: $error"
                        Log.d("YangoAds", "Rewarded failed to show $error")
                    }
                })
                rewardedAd.show(context as android.app.Activity)
                load = false
            }

            override fun onAdFailedToLoad(error: AdRequestError) {
                status = "error: ${'$'}{error.description}"
                Log.d("YangoAds", "Rewarded load error ${'$'}error")
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
            topBar("Rewarded", onBack)

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


