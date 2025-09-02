package com.yandex.android.mishanga.ui.ad

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.android.mishanga.AdConstants
import com.yandex.android.mishanga.data.SettingsStore
import com.yandex.android.mishanga.ui.components.CustomBackground
import com.yandex.android.mishanga.ui.components.PrimaryButton
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BannerDetailScreen(
    onBack: () -> Unit,
    topBar: @Composable (title: String, onBack: () -> Unit) -> Unit,
    name: String,
    sizeBanner: Pair<Int, Int>
) {
    val context = LocalContext.current
    val settings = remember { SettingsStore.get(context).read() }
    val uuid = settings.uuid
    val regionId = settings.regionId
    val adUnitId = settings.adUnitId.ifBlank { AdConstants.TEST_BANNER }
    val aimBannerId = settings.aimBannerId.ifBlank { AdConstants.TEST_BANNER }

    var status by remember { mutableStateOf("Wait >>>") }
    var reload by remember { mutableStateOf(false) }

    fun request(): AdRequest = AdRequest.Builder()
        .setParameters(
            mapOf(
                "uuid" to uuid,
                "region_id" to regionId,
                "aim_banner_id" to aimBannerId
            )
        )
        .build()

    CustomBackground {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp)) {
            topBar(name, onBack)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryButton(
                    text = "Load Ad",
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    status = "loading…"
                    reload = true
                }

                AndroidView(
                    factory = { ctx ->
                        BannerAdView(ctx).apply {
                            layoutParams = FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            setAdUnitId(adUnitId)
                            setBannerAdEventListener(object : BannerAdEventListener {
                                override fun onAdLoaded() {
                                    status = "loaded"
                                    Log.d("YangoAds", "Banner $name loaded")
                                }

                                override fun onAdFailedToLoad(error: AdRequestError) {
                                    status = "error: ${error.description}"
                                    Log.d("YangoAds", "Banner $name error $error")
                                }

                                override fun onAdClicked() {}
                                override fun onLeftApplication() {}
                                override fun onReturnedToApplication() {}
                                override fun onImpression(data: com.yandex.mobile.ads.common.ImpressionData?) {}
                            })
                            setAdSize(
                                BannerAdSize.fixedSize(
                                    ctx,
                                    sizeBanner.first,
                                    sizeBanner.second
                                )
                            )
                        }
                    },
                    update = { view ->
                        if (reload) {
                            view.setAdSize(
                                BannerAdSize.fixedSize(
                                    context,
                                    sizeBanner.first,
                                    sizeBanner.second
                                )
                            )
                            view.loadAd(request())
                            reload = false
                        }
                    }
                )

                Text("Status: $status")
            }
        }
    }
}