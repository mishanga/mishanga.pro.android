package com.yandex.android.mishanga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yandex.android.mishanga.ui.SettingsScreen
import com.yandex.android.mishanga.ui.ad.BannerDetailScreen
import com.yandex.android.mishanga.ui.ad.BannerScreen
import com.yandex.android.mishanga.ui.ad.InterstitialScreen
import com.yandex.android.mishanga.ui.ad.RewardedScreen
import com.yandex.android.mishanga.ui.components.AppTopBar
import com.yandex.android.mishanga.ui.components.CustomBackground
import com.yandex.android.mishanga.ui.components.PrimaryButton
import com.yandex.android.mishanga.ui.theme.SamplemishangaTheme

object Routes {
    const val Main = "main"
    const val Banner = "banner"
    const val BannerDetail = "banner_detail"
    const val Interstitial = "interstitial"
    const val Rewarded = "rewarded"
    const val Settings = "settings"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { AppNav() }
    }
}

@Composable
fun AppNav() {
    SamplemishangaTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Routes.Main) {
            composable(Routes.Main) { MainScreen(navController) }
            composable(Routes.Banner) {
                BannerScreen(
                    onBack = { navController.popBackStack() },
                    topBar = { title, onBack ->
                        AppTopBar(
                            title = title,
                            canNavigateBack = true,
                            onBack = onBack,
                            onSettings = null
                        )
                    },
                    onNavigateToDetail = { name, width, height ->
                        navController.navigate("$Routes.BannerDetail/$name/$width/$height")
                    }
                )
            }
            composable("$Routes.BannerDetail/{name}/{width}/{height}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: "banner"
                val width = backStackEntry.arguments?.getString("width")?.toIntOrNull() ?: 300
                val height = backStackEntry.arguments?.getString("height")?.toIntOrNull() ?: 250

                BannerDetailScreen(
                    onBack = { navController.popBackStack() },
                    topBar = { title, onBack ->
                        AppTopBar(
                            title = title,
                            canNavigateBack = true,
                            onBack = onBack,
                            onSettings = null
                        )
                    },
                    name = name,
                    sizeBanner = Pair(width, height),
                )
            }
            composable(Routes.Interstitial) {
                InterstitialScreen(onBack = { navController.popBackStack() }) { title, onBack ->
                    AppTopBar(
                        title = title,
                        canNavigateBack = true,
                        onBack = onBack,
                        onSettings = null
                    )
                }
            }
            composable(Routes.Rewarded) {
                RewardedScreen(onBack = { navController.popBackStack() }) { title, onBack ->
                    AppTopBar(
                        title = title,
                        canNavigateBack = true,
                        onBack = onBack,
                        onSettings = null
                    )
                }
            }
            composable(Routes.Settings) {
                SettingsScreen(onBack = { navController.popBackStack() }) { title, onBack ->
                    AppTopBar(
                        title = title,
                        canNavigateBack = true,
                        onBack = onBack,
                        onSettings = null
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(nav: NavHostController) {
    CustomBackground {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AppTopBar(
                title = "Yango Ads",
                canNavigateBack = false,
                onBack = null,
                onSettings = { nav.navigate(Routes.Settings) })
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
                        text = "banner",
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) { nav.navigate(Routes.Banner) }
                    Spacer(Modifier.height(8.dp))
                    PrimaryButton(
                        text = "interstitial",
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) { nav.navigate(Routes.Interstitial) }
                    Spacer(Modifier.height(8.dp))
                    PrimaryButton(
                        text = "rewarded",
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) { nav.navigate(Routes.Rewarded) }
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun MainPreview() {
    AppNav()
}
