package pro.mishanga.android.ui.component

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.yandex.android.mishanga.data.SettingsStore
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import pro.mishanga.android.ui.theme.CustomBackground
import pro.mishanga.android.ui.utils.AdConstants

@Composable
fun BannerDetailScreen(
    onBack: () -> Unit,
    topBar: @Composable (title: String, onBack: () -> Unit) -> Unit,
    name: String,
    firstSizeBanner: Pair<Int, Int>,
    secondSizeBanner: Pair<Int, Int>
) {
    val context = LocalContext.current
    val settings = remember { SettingsStore.get(context).read() }
    val uuid = settings.uuid
    val regionId = settings.regionId
    val adUnitId = settings.adUnitId.ifBlank { AdConstants.TEST_BANNER }
    val adUnitId2 = settings.adUnitId.ifBlank { AdConstants.TEST_BANNER_2 }

    var status by remember { mutableStateOf("Wait >>>") }
    var reload by remember { mutableStateOf(true) }
    var reload2 by remember { mutableStateOf(true) }

    fun request(aimBannerId: String): AdRequest =
        AdRequest.Builder()
            .setParameters(
                mapOf(
                    "uuid" to uuid,
                    "region_id" to regionId,
                    "aim_banner_id" to aimBannerId
                )
            )
            .build()

    CustomBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
        ) {
            topBar(name, onBack)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

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
                                override fun onImpression(data: ImpressionData?) {}
                            })
                            setAdSize(
                                BannerAdSize.fixedSize(
                                    ctx,
                                    firstSizeBanner.first,
                                    firstSizeBanner.second
                                )
                            )
                        }
                    },
                    update = { view ->
                        if (reload) {
                            view.setAdSize(
                                BannerAdSize.fixedSize(
                                    context,
                                    firstSizeBanner.first,
                                    firstSizeBanner.second
                                )
                            )
                            view.loadAd(request(adUnitId))
                            reload = false
                        }
                    }
                )
                Spacer(Modifier)
                AndroidView(
                    factory = { ctx ->
                        BannerAdView(ctx).apply {
                            layoutParams = FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            setAdUnitId(adUnitId2)
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
                                override fun onImpression(data: ImpressionData?) {}
                            })
                            setAdSize(
                                BannerAdSize.fixedSize(
                                    ctx,
                                    secondSizeBanner.first,
                                    secondSizeBanner.second
                                )
                            )
                        }
                    },
                    update = { view ->
                        if (reload2) {
                            view.setAdSize(
                                BannerAdSize.fixedSize(
                                    context,
                                    secondSizeBanner.first,
                                    secondSizeBanner.second
                                )
                            )
                            view.loadAd(request(adUnitId2))
                            reload = false
                        }
                    }
                )
                Text(
                    text = if (status.contains("error")) status else ""
                )
            }
        }
    }
}