package com.yandex.android.mishanga.ui.ad

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequestError
import androidx.compose.foundation.BorderStroke
import com.yandex.android.mishanga.ui.theme.Black
import com.yandex.android.mishanga.ui.theme.BrandPrimary
import androidx.compose.foundation.shape.RoundedCornerShape
import com.yandex.android.mishanga.ui.components.CustomBackground
import com.yandex.android.mishanga.ui.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BannerScreen(
    onBack: () -> Unit,
    topBar: @Composable (title: String, onBack: () -> Unit) -> Unit
) {
    val context = LocalContext.current
    val settings = remember { SettingsStore.get(context).read() }
    val uuid = settings.uuid
    val regionId = settings.regionId
    val adUnitId = settings.adUnitId.ifBlank { AdConstants.TEST_BANNER }
    val aimBannerId = settings.aimBannerId.ifBlank { AdConstants.TEST_BANNER }

    var statusTop by remember { mutableStateOf("idle") }
    var statusBottom by remember { mutableStateOf("idle") }
    var reloadTop by remember { mutableStateOf(false) }
    var reloadBottom by remember { mutableStateOf(false) }

    fun request(): AdRequest = AdRequest.Builder()
        .setParameters(mapOf(
            "uuid" to uuid,
            "region_id" to regionId,
            "aim_banner_id" to aimBannerId
        ))
        .build()

    CustomBackground {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            topBar("banner", onBack)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top action buttons
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    PrimaryButton(
                        text = "Load banner 300x250",
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) { statusTop = "loading…"; reloadTop = true }
                    Spacer(Modifier.height(8.dp))
                    PrimaryButton(
                        text = "Load banner 320x50",
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) { statusBottom = "loading…"; reloadBottom = true }
                    Spacer(Modifier.height(12.dp))
                }

                // Center 300x250
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AndroidView(factory = { ctx ->
                            BannerAdView(ctx).apply {
                                layoutParams = FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                                setAdUnitId(adUnitId)
                                setBannerAdEventListener(object : BannerAdEventListener {
                                    override fun onAdLoaded() {
                                        statusTop = "loaded"
                                        Log.d("YangoAds", "Banner 300x250 loaded")
                                    }

                                    override fun onAdFailedToLoad(error: AdRequestError) {
                                        statusTop = "error: ${error.description}"
                                        Log.d("YangoAds", "Banner 300x250 error $error")
                                    }

                                    override fun onAdClicked() {}
                                    override fun onLeftApplication() {}
                                    override fun onReturnedToApplication() {}
                                    override fun onImpression(data: com.yandex.mobile.ads.common.ImpressionData?) {}
                                })
                                // 300x250 placement
                                setAdSize(BannerAdSize.fixedSize(ctx, 300, 250))
                            }
                        }, update = { view ->
                            if (reloadTop) {
                                view.setAdSize(BannerAdSize.fixedSize(context, 300, 250))
                                view.loadAd(request())
                                reloadTop = false
                            }
                        })
                        Spacer(Modifier.height(8.dp))
                    }
                }

                // Bottom 320x50 pinned
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    AndroidView(factory = { ctx ->
                        BannerAdView(ctx).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            setAdUnitId(adUnitId)
                            setBannerAdEventListener(object : BannerAdEventListener {
                                override fun onAdLoaded() {
                                    statusBottom = "loaded"
                                    Log.d("YangoAds", "Banner 320x50 loaded")
                                }

                                override fun onAdFailedToLoad(error: AdRequestError) {
                                    statusBottom = "error: ${error.description}"
                                    Log.d("YangoAds", "Banner 320x50 error $error")
                                }

                                override fun onAdClicked() {}
                                override fun onLeftApplication() {}
                                override fun onReturnedToApplication() {}
                                override fun onImpression(data: com.yandex.mobile.ads.common.ImpressionData?) {}
                            })
                            // 320x50 bottom placement
                            setAdSize(BannerAdSize.fixedSize(ctx, 320, 50))
                        }
                    }, update = { view ->
                        if (reloadBottom) {
                            view.setAdSize(BannerAdSize.fixedSize(context, 320, 50))
                            view.loadAd(request())
                            reloadBottom = false
                        }
                    })
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}


